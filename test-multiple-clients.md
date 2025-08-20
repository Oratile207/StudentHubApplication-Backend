# Testing Multiple Clients Locally

## Method 1: Multiple Browser Windows/Tabs
1. Start your backend: `.\mvnw.cmd spring-boot:run`
2. Start your React frontend: `npm start` (runs on http://localhost:3000)
3. Open multiple browser windows/tabs:
   - Chrome: http://localhost:3000
   - Firefox: http://localhost:3000
   - Chrome Incognito: http://localhost:3000
   - Edge: http://localhost:3000

Each browser/incognito session will have separate localStorage for JWT tokens.

## Method 2: Multiple Frontend Instances on Different Ports
1. Create multiple frontend instances:

```bash
# Terminal 1 - Main instance
cd your-react-app
npm start # Port 3000

# Terminal 2 - Second instance  
cd your-react-app
PORT=3001 npm start

# Terminal 3 - Third instance
cd your-react-app  
PORT=3002 npm start

# Terminal 4 - Fourth instance
cd your-react-app
PORT=3003 npm start
```

2. Update backend CORS to allow multiple origins:
```java
// In SecurityConfig.java
corsConfig.addAllowedOrigin("http://localhost:3000");
corsConfig.addAllowedOrigin("http://localhost:3001");
corsConfig.addAllowedOrigin("http://localhost:3002");
corsConfig.addAllowedOrigin("http://localhost:3003");
```

## Method 3: Network Testing (Other Devices)
1. Find your computer's IP address:
   - Windows: `ipconfig` (look for IPv4 Address)
   - Example: 192.168.1.100

2. Update application.properties:
```properties
# Allow connections from network
server.address=0.0.0.0
spring.web.cors.allowed-origins=http://localhost:3000,http://192.168.1.100:3000
```

3. Start backend and frontend
4. Access from other devices: http://192.168.1.100:3000

## Testing Scenarios
✅ **User Registration**: Create multiple test accounts
✅ **Real-time Messaging**: Send messages between users  
✅ **Online Status**: Test user online/offline indicators
✅ **Typing Indicators**: Test real-time typing notifications
✅ **Channel Operations**: Join/leave channels with different users
✅ **WebSocket Connections**: Verify real-time updates work
