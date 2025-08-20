# StudentHub Backend Overview

## Tech Stack
- **Framework**: Spring Boot 3.3.4 (Java 17)
- **Database**: MySQL 
- **Security**: JWT Authentication
- **Build**: Maven

## Database Configuration
```properties
# MySQL Connection
spring.datasource.url=jdbc:mysql://localhost:3306/StudentHubDatabase
spring.datasource.username=root
spring.datasource.password=admin

# JPA Settings
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

## REST API Endpoints

### Authentication (`/auth`)
- `POST /auth/register` - User registration
- `POST /auth/login` - User login
- `GET /auth/get/{id}` - Get user
- `PUT /auth/update` - Update user
- `DELETE /auth/delete/{id}` - Delete user
- `GET /auth/getAll` - List all users

### Products (`/products`)
- `POST /products/create` - Create product
- `GET /products/read/{id}` - Get product
- `PUT /products/update` - Update product
- `DELETE /products/delete/{id}` - Delete product
- `GET /products/getAll` - List products

### Channels (`/channel`)
- `POST /channel/create` - Create channel
- `GET /channel/read/{id}` - Get channel
- `PUT /channel/update` - Update channel
- `DELETE /channel/delete/{id}` - Delete channel
- `GET /channel/getAll` - List channels

## Database Entities

### User
- **Table**: `users`
- **Key**: `userId`
- **Fields**: firstName, lastName, email, password, role, studentNumber, staffNumber
- **Relationship**: One-to-One with EntrepreneurUserProfile

### Products
- **Table**: `products`
- **Key**: `productId`
- **Fields**: productName, productDescription, productReturnType
- **Relationship**: Many-to-One with UserProduct

### Channel
- **Table**: `channel`
- **Key**: `channelId`
- **Fields**: channelName, channelType, permissions
- **Relationships**: 
  - Many-to-One with User (admin)
  - One-to-Many with UserPost

### UserPost
- **Table**: `user_posts`
- **Key**: `userPostId`
- **Fields**: content, postTimestamp, moderation flags
- **Relationships**: Many-to-One with User and Channel

## User Roles
- `ADMIN` - System administrator
- `STUDENT` - Regular student
- `FACULTY_MEMBER` - Teaching staff
- `IT_SUPPORT_STAFF` - Technical support
- `GUEST` - Visitor access
- `ENTREPRENEUR` - Business-focused student

## Security
- JWT tokens for authentication
- BCrypt password encoding
- CORS enabled for `localhost:3000`
- Public endpoints: `/auth/**`
- All other endpoints require authentication

## Service Layer
- Generic `IService<T, id>` interface with CRUD operations
- Organized by domain:
  - `user_services/` - User management
  - `channel_services/` - Channels and products
  - `post_services/` - Post management

## Development
- **Server Port**: 8080
- **Database**: MySQL on localhost:3306
- **Frontend**: CORS configured for localhost:3000
- **Run**: `mvn spring-boot:run`