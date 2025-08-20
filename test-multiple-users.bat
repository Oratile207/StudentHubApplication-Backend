@echo off
echo Starting StudentHub Backend...
start "Backend" cmd /c "mvnw.cmd spring-boot:run"

echo Waiting for backend to start...
timeout /t 10

echo Backend should be running on http://localhost:8080
echo.
echo To test multiple clients:
echo 1. Open multiple browser windows/tabs to http://localhost:3000
echo 2. Register different users in each browser
echo 3. Test real-time messaging between users
echo.
echo Test scenarios:
echo - User registration/login
echo - Real-time messaging 
echo - Typing indicators
echo - Online status updates
echo - Channel join/leave
echo.
echo Press any key to stop backend...
pause
taskkill /f /im "java.exe" 2>nul
