# Discord Clone - CPUT StudentHub Frontend

## Table of Contents
1. [Project Overview](#project-overview)
2. [Architecture & Technology Stack](#architecture--technology-stack)
3. [API Integration & Backend Communication](#api-integration--backend-communication)
4. [File Structure & Organization](#file-structure--organization)
5. [State Management with Redux](#state-management-with-redux)
6. [Component Architecture](#component-architecture)
7. [Routing System](#routing-system)
8. [Key Features & Enhancements](#key-features--enhancements)
9. [Fixes & Improvements](#fixes--improvements)
10. [Development Workflow](#development-workflow)
11. [Testing & Debugging](#testing--debugging)
12. [Best Practices & Conventions](#best-practices--conventions)
13. [Security Considerations](#security-considerations)
14. [Future Enhancements](#future-enhancements)

---

## Project Overview

**CPUT StudentHub** is a Discord-inspired communication platform built as a modern React application. The system provides real-time communication features including user authentication, channel management, user profiles, global search, channel discovery, and marketplace elements. This frontend application is designed to work seamlessly with a Spring Boot backend API, supporting features like friend systems, enhanced memberships, and WebSocket-based real-time updates.

### Key Objectives
- Provide a student-focused social platform for academic collaboration, networking, and resource sharing.
- Emulate Discord's intuitive interface while tailoring to university needs (e.g., role-based access for students/faculty/entrepreneurs).

**Last Updated**: August 27, 2025  
**Version**: 3.0.0  
**Status**: Production Ready

---

## Architecture & Technology Stack

### Frontend Technologies
- **React 19**: Component-based UI library with hooks and functional components.
- **TypeScript 4.9.5**: Static type checking for enhanced developer experience.
- **React Router DOM 7**: Client-side routing and navigation.
- **Redux Toolkit 2.2.8**: Predictable state container with modern Redux patterns (slices: auth, channel, profile).
- **Axios 1.7.7**: Promise-based HTTP client with interceptors.
- **Tailwind CSS 4 + CSS Modules**: Utility-first CSS framework for styling.
- **FontAwesome 6.6.0**: Icon library for UI components.
- **@stomp/stompjs & sockjs-client**: For STOMP over WebSocket real-time communication.
- **CRACO**: Create React App Configuration Override for custom webpack setup.

### Development Tools
- **React Scripts 5.0.1**: Build toolchain and development server.
- **Jest 29.7.0 & React Testing Library**: Testing utilities for React components.
- **TypeScript**: Enhanced IDE support and compile-time error checking.

### Configuration
- **Environment Variables**: `.env` file with `REACT_APP_API_URL=http://localhost:8080`.
- **CORS**: Configured for backend at `http://localhost:8080`.
- **Build Optimization**: Gzipped bundles (~97 kB JS, ~7.5 kB CSS).

---

## API Integration & Backend Communication

The frontend integrates with a Spring Boot backend via REST APIs and WebSocket for real-time features.

### Base Configuration
- **API Base URL**: `http://localhost:8080` (configurable via env).
- **Authentication**: JWT Bearer tokens stored in localStorage, with validation and expiration handling.
- **Interceptors**: Axios interceptors add Authorization headers and handle 401/403 errors (auto-logout/redirect).

### Key API Endpoints (from `src/services/api.ts`)
- **Auth**: `/api/auth/login` (payload: `{email, password}`), `/api/auth/register` (full user details including role/studentNumber).
- **Channels**: `/channel/create` (with name/type/description), `/channel/getAll`, `/channel/join/{id}`, `/channel/leave/{id}`, `/channel/my-channels`, `/channel/check-membership/{id}`, `/channel/{id}/members`.
- **Messages**: `/messages/{channelId}?page=&size=`, `/messages/send`.
- **Users/Profiles**: `/auth/get/{id}`, `/user_profile/*`, `/entrepreneur_profile/*`.
- **Posts/Products**: `/user_post/*`, `/products/*`, `/user_product/*`.
- **Friends**: `/users/friend-request`, `/users/friends`, etc. (integrated for friend system).
- **Search**: Planned for `/search/global` (channels/users/messages).

### WebSocket Integration (`src/services/websocket.ts`)
- **Endpoint**: `ws://localhost:8080/ws?token={encoded_jwt}`.
- **Protocol**: STOMP over SockJS with fallback.
- **Destinations**: `/app/join-channel`, `/app/typing`, `/topic/channel/{id}`, `/topic/user-status`.
- **Features**: Auto-reconnect with backoff, heartbeat (ping every 30s), subscription management, error handling (403/401 redirect).
- **Message Types**: message, user_joined/left, typing, online_users, ping, join/leave_channel.

### Error Handling
- Specific messages for 401 (invalid creds), 403 (forbidden), network errors.
- Global loading states and toasts for API failures.

---

## File Structure & Organization

```
src/
├── components/          # Reusable UI components
│   ├── features/        # Feature-specific (Channel, User, etc.)
│   ├── layout/          # Layout elements (TopNavBar, etc.)
│   └── modals/          # Modals (Login, Signup, CreateChannel, Search, DiscoverChannels, ChannelManagement)
├── screens/             # Main screens (LoginScreen, SignupScreen, ChannelScreen, ProfileScreen)
├── store/               # Redux store
│   └── slices/          # Slices (authSlice, channelSlice, profileSlice)
├── services/            # Services (api.ts, websocket.ts)
├── utils/               # Utilities (userDisplay.ts, jwt.ts, defaultChannels.ts)
├── hooks/               # Custom hooks (useChannelMembership.ts)
├── config.ts            # Environment config
├── types.d.ts           # Type declarations
└── App.tsx              # Root component
```

- **New Files**: `ChannelManagementModal.tsx`, `SearchModal.tsx`, `DiscoverChannelsModal.tsx`, `useChannelMembership.ts`, `userDisplay.ts`.

---

## State Management with Redux

- **Slices**:
  - **authSlice**: Token, user data ({id, username, displayName, email, avatar, role, etc.}), authentication status.
  - **channelSlice**: Channel list, active channel, memberships.
  - **profileSlice**: User profile details.
- **Actions**: setToken, setUser, logout; addChannel, setActiveChannel; updateProfile.
- **Selectors**: useSelector for user/channel state.
- **Persistence**: Tokens/user data in localStorage for session persistence.

---

## Component Architecture

- **Modular Design**: Components are co-located with styles (`.module.css`).
- **Reusable Components**: UserAvatar (with initials fallback), ChannelItem/List.
- **Modals**: LoginModal, SignupModal, CreateChannelModal, ChannelManagementModal (tabs: info/edit/members), SearchModal (Ctrl+K, channels/users), DiscoverChannelsModal (filters/search/join).
- **Hooks**: useChannelMembership for check/load/refresh memberships.
- **Utilities**: userDisplay.ts for name/role/avatar handling across formats.

---

## Routing System

- **Routes**:
  - `/`: Home/Channels.
  - `/login`, `/signup`: Auth screens.
  - `/channels`: Channel list/screen.
  - `/profile/:id`: Profile view.
- **Protected Routes**: Use isAuthenticated() to guard private pages.
- **Navigation**: React Router for seamless SPA routing.

---

## Key Features & Enhancements

- **Authentication**: Role-based forms (STUDENT/FACULTY/ENTREPRENEUR/ADMIN/GUEST/IT_SUPPORT_STAFF), student/staff number validation, auto-redirect post-login.
- **Channels**: Default helpful channels (#welcome-home, etc.), CRUD modal with tabs, join/leave with membership checks, discover modal with category filters/search.
- **Search**: Global modal (Ctrl+K) for channels/users/messages, real-time filtering.
- **Profiles**: View/edit with bio/contact/avatar; entrepreneur extensions (company/industry).
- **Real-Time**: STOMP WebSocket for messaging, presence, typing indicators.
- **Posts/Products**: Basic integration for content/marketplace.
- **UI/UX**: TopNavBar with search/discover, loading states, error toasts, keyboard shortcuts.

---

## Fixes & Improvements

- **Auth Fixes**: Handled {token, user} response, fixed loops/invalid tokens, MIME errors (.htaccess/SPA routing), 403 on user fetch.
- **WebSocket Fixes**: STOMP alignment, token encoding/validation, reconnect/backoff, error handling.
- **Channel Fixes**: Type consistency (id: number|string), HashSet integration via APIs, member display.
- **User Display Fixes**: Utilities for name/role/avatar across formats, fixed "User #X" in TopNavBar.
- **Console Fixes**: 403/WS failures, JWT parsing.
- **Integration**: Full backend alignment (no changes needed per status report).

---

## Development Workflow

### Commands
- `npm install`: Install dependencies.
- `npm start`: Start dev server (http://localhost:3000).
- `npm test`: Run tests in watch mode.
- `npm build`: Build for production.

### Workflow
1. Clone repo.
2. Install deps.
3. Start backend (mvn spring-boot:run on 8080).
4. Start frontend.
5. Use Postman for API testing.

---

## Testing & Debugging

- **Unit Tests**: Jest/RTL for components (e.g., login form submission).
- **Integration Tests**: E2E flows (auth → channels → messaging).
- **Debugging**: Browser console for logs (API/WS/JWT), Redux DevTools for state.
- **Checklist**: Auth (valid/invalid/expired), WS (connect/subscribe/reconnect), Channels (create/join/leave/discover), Errors (401/403/network).
- **Tools**: Network tab, storage inspection.

---

## Best Practices & Conventions

- **Code Style**: Functional components, hooks over classes, TypeScript strict mode.
- **Naming**: CamelCase for components, kebab-case for files.
- **Error Handling**: Centralized in services, user-friendly messages.
- **Performance**: Memoization, lazy loading where applicable.
- **Accessibility**: ARIA labels, keyboard navigation.

---

## Security Considerations

- **Auth**: Secure token storage (localStorage with validation), auto-logout on expiration.
- **Input**: Client/server validation, XSS sanitization.
- **API**: HTTPS in prod, CORS config.
- **WebSocket**: Token-based auth, error redirects.

---

## Future Enhancements

- **Real-Time Advanced**: File uploads, voice channels, screen sharing.
- **Search**: Backend-enhanced global search with federation.
- **Performance**: Virtual scrolling, caching.
- **UX**: Themes, shortcuts, PWA support.
- **Friends**: Full UI for requests/blocking (backend-integrated).

---

This documentation provides a comprehensive overview of the Discord Clone frontend project architecture, implementation details, and development guidelines. It serves as a reference for team members to understand the codebase structure, make informed development decisions, and maintain consistency across the project.