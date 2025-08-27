# JSON Circular Reference Fix

## Problem Explained

The error you saw:
```
Document nesting depth (1001) exceeds the maximum allowed (1000)
HttpMessageNotWritableException: Could not write JSON
```

This happens because of **circular references** between JPA entities during JSON serialization.

## What Causes Circular References?

### The Infinite Loop:
```
User → Friendship → User → Friendship → User → Friendship → ...
User → Channel → User → Channel → User → Channel → ...
Channel → Message → User → Channel → Message → User → ...
```

### Entity Relationships:
1. **User** has collections of **Friendship** entities
2. **Friendship** has references back to **User** entities  
3. **Channel** has collections of **User** entities
4. **User** entities reference back to **Channel**
5. **Message** references both **User** and **Channel**

When Jackson (JSON serializer) tries to serialize these entities, it gets caught in an infinite loop trying to serialize all the nested relationships.

## Solutions Applied

### 1. User Entity - Hide Friendship Collections
**File**: `src/main/java/za/co/studenthub/domain/User.java`

```java
// ✅ FIXED: Ignore friendship collections during JSON serialization
@JsonIgnore
@OneToMany(mappedBy = "fromUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
private List<Friendship> sentFriendRequests = new ArrayList<>();

@JsonIgnore
@OneToMany(mappedBy = "toUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
private List<Friendship> receivedFriendRequests = new ArrayList<>();
```

**Why**: These collections are not needed in JSON responses since we use dedicated endpoints (`/users/friends`, `/users/friend-requests`) to get friendship data.

### 2. Channel Entity - Hide User Collections
**File**: `src/main/java/za/co/studenthub/domain/Channel.java`

```java
// ✅ FIXED: Ignore member collections during JSON serialization
@JsonIgnore
@ManyToMany
@JoinTable(name = "channel_members",
        joinColumns = @JoinColumn(name = "channel_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id"))
private Set<User> channelMembers;

@JsonIgnore
@OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
private List<ChannelMembership> memberships = new java.util.ArrayList<>();
```

**Why**: Channel member information is accessed through dedicated endpoints (`/channel/{channelId}/members`).

### 3. Message Entity - Selective Property Hiding
**File**: `src/main/java/za/co/studenthub/domain/Message.java`

```java
// ✅ FIXED: Only ignore problematic nested properties
@ManyToOne(fetch = FetchType.EAGER)
@JoinColumn(name = "author_id", nullable = false)
@JsonIgnoreProperties({"sentFriendRequests", "receivedFriendRequests", "userPassword"})
private User author;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "channel_id", nullable = false)
@JsonIgnoreProperties({"channelMembers", "memberships"})
private Channel channel;
```

**Why**: We still need basic User and Channel info in messages, but not the nested collections that cause circular references.

## JSON Annotations Used

### `@JsonIgnore`
- **Purpose**: Completely hides the field from JSON serialization
- **Use case**: Collections that are not needed in API responses
- **Example**: User friendship collections, Channel member collections

### `@JsonIgnoreProperties`
- **Purpose**: Hides specific properties of nested objects
- **Use case**: When you need the object but not all its properties
- **Example**: Message needs User info but not User's friendship collections

## Impact on API Responses

### ✅ What Still Works:
- **User objects** in API responses contain: id, name, email, role, status, avatar
- **Channel objects** contain: id, name, type, description, admin info
- **Message objects** contain: id, content, author info, channel info, timestamp

### 🚫 What's Hidden:
- User friendship collections (use dedicated endpoints instead)
- Channel member collections (use dedicated endpoints instead)
- Sensitive data like passwords

## Testing the Fix

### Before (Error):
```bash
curl -X GET "/users/friends" 
# Result: 500 Error - Circular reference exception
```

### After (Success):
```bash
curl -X GET "/users/friends"
# Result: 200 OK with clean JSON:
[
  {
    "id": 123,
    "name": "John Doe",
    "email": "john@example.com",
    "isOnline": true,
    "status": "ONLINE"
  }
]
```

## Best Practices Applied

1. **Use DTOs for API responses** - We already do this for friends/channels
2. **Hide collections with @JsonIgnore** - Applied to prevent circular refs
3. **Selective hiding with @JsonIgnoreProperties** - For partial object serialization
4. **Lazy loading** - Prevents unnecessary data fetching
5. **Dedicated endpoints** - Separate endpoints for complex relationships

## Performance Benefits

- **Faster serialization** - No infinite loops
- **Smaller JSON payloads** - Only necessary data is included
- **Better memory usage** - Lazy loading prevents unnecessary entity loading
- **Cleaner API responses** - More predictable and focused data structure

The fix eliminates circular references while maintaining all necessary functionality and improving API performance.
