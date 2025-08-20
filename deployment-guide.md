# Beta Deployment Options

## 🚀 Railway (Recommended - Free Tier Available)
1. Go to [railway.app](https://railway.app)
2. Connect your GitHub repository
3. Add MySQL database service
4. Set environment variables:
   ```
   SPRING_DATASOURCE_URL=<railway_mysql_url>
   SPRING_DATASOURCE_USERNAME=<db_username>
   SPRING_DATASOURCE_PASSWORD=<db_password>
   JWT_SECRET=your-secure-production-secret
   ```
5. Deploy automatically on git push

## 🌊 Heroku (Classic Option)
1. Install Heroku CLI
2. Create app: `heroku create studenthub-app`
3. Add MySQL addon: `heroku addons:create cleardb:ignite`
4. Set environment variables:
   ```bash
   heroku config:set JWT_SECRET=your-secure-production-secret
   ```
5. Deploy: `git push heroku main`

## ☁️ Render (Free Tier)
1. Connect GitHub to [render.com](https://render.com)
2. Create PostgreSQL database
3. Create web service from repository
4. Set environment variables in dashboard
5. Auto-deploy on git push

## 📊 DigitalOcean App Platform
1. Connect to [DigitalOcean App Platform](https://www.digitalocean.com/products/app-platform)
2. Create MySQL database
3. Deploy from GitHub
4. Configure environment variables
5. Custom domain support

## Testing Your Beta Deployment
Once deployed, you can:
- Share the URL with friends/classmates
- Test from different devices/networks
- Validate real-time features work at scale
- Monitor performance and errors

## Pre-Deployment Checklist
- [ ] Update CORS origins to include your domain
- [ ] Use strong JWT secret in production
- [ ] Configure database connection properly
- [ ] Test all endpoints work
- [ ] Verify WebSocket connections
- [ ] Check logs for errors
