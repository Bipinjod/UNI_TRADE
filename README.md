# UniTrade – Student Marketplace Platform

UniTrade is a web application we built as our second-year group project. The idea came from a real problem we noticed — students at our university often want to sell old textbooks, offer tutoring, or ask for help with tasks, but there is no proper platform for it. So we decided to build one ourselves.

The platform lets students buy and sell second-hand items, offer services like tutoring or printing, and post help requests that other students can respond to. Everything goes through an admin panel where new users get approved before they can access the marketplace.

**Tech used:** Java Servlets + JSP (MVC pattern), MySQL, Apache Tomcat 10

---

## Team Members

| Name | Branch | Contribution |
|------|--------|-------------|
| Bipin | `bipin` | Project lead, authentication system, filters, session management, Remember Me |
| Sandesh | `sandesh` | Login and registration page UI, mobile responsiveness |
| Ushudha | `ushudha` | DAO layer, service layer classes, database integration |
| Apshana | `apshana` | User-facing pages, item listings, browsing and search UI |
| Sujal | `Sujal` | UI/UX design, responsisvenss  |

---

## What the App Does

**For Students (Users):**
- Register an account and wait for admin approval
- Post items for sale with images and price
- Browse and search items or services posted by others
- Offer services (tutoring, errands, design work, etc.)
- Post help requests and get responses from other students
- Manage their own orders, wishlist, and profile

**For Admin:**
- Approve or reject new user registrations
- Manage all listings, services, and help requests
- View a dashboard with platform activity overview
- Handle user accounts and reported content

---

## Prerequisites

You will need these installed before running the project:

| Tool | Version |
|------|---------|
| JDK | 17 or above |
| MySQL | 8.0 or above |
| Apache Tomcat | 10.1 |
| IntelliJ IDEA | Any version (Community edition works fine) |

Maven does not need to be installed separately — the project already includes `mvnw.cmd` (Maven Wrapper).

---

## How to Run It

### 1. Clone the repo

```bash
git clone https://github.com/Bipinjod/UNI_TRADE.git
cd UNI_TRADE
```

### 2. Set up the database

Run the SQL file to create the database and all the tables:

```bash
mysql -u root -p < database/unitrade_final_schema.sql
```

This creates the `unitrade_db` database, all tables, default categories, and an admin account.

Default admin login: `admin@unitrade.com` / `admin123`

To verify it worked:
```sql
mysql -u root -p
USE unitrade_db;
SHOW TABLES;
```

### 3. Add your database credentials

Copy the template file:

```powershell
Copy-Item src\main\resources\database.properties.template src\main\resources\database.properties
```

Then open `database.properties` and enter your MySQL password:

```properties
db.url=jdbc:mysql://localhost:3306/unitrade_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&characterEncoding=UTF-8
db.username=root
db.password=YOUR_PASSWORD_HERE
db.driver=com.mysql.cj.jdbc.Driver
```

> This file is listed in `.gitignore` so credentials are never accidentally committed.

### 4. Build the project

```powershell
.\mvnw.cmd clean package -DskipTests
```

You should see `BUILD SUCCESS` at the end with a `unitrade.war` file generated in `/target`.

### 5. Deploy on Tomcat via IntelliJ

1. Open the project folder in IntelliJ IDEA
2. Wait for Maven to finish importing (progress bar at the bottom)
3. Go to `Run → Edit Configurations → + → Tomcat Server → Local`
4. Under **Server** tab: point to your Tomcat installation folder
5. Under **Deployment** tab: click `+` → `Artifact` → select `UniTrade:war exploded`
6. Set **Application context** to `/unitrade`
7. Click Run — your browser should open at `http://localhost:8080/unitrade/`

**Manual deploy (alternative):**
```powershell
Copy-Item target\unitrade.war "C:\path\to\tomcat\webapps\"
C:\path\to\tomcat\bin\startup.bat
```

---

## Project Structure

```
UniTrade/
├── src/main/
│   ├── java/com/unitrade/
│   │   ├── controller/     — Servlets that handle all HTTP requests
│   │   ├── dao/            — Database Access Objects (one per table)
│   │   ├── service/        — Business logic sitting between controller and DAO
│   │   ├── model/          — Plain Java classes representing database entities
│   │   ├── filter/         — Authentication filters and role-based access control
│   │   └── util/           — DBConnection, PasswordUtil, CookieUtil, SessionUtil
│   ├── resources/
│   │   └── database.properties.template
│   └── webapp/
│       ├── user/           — JSP pages for logged-in students
│       ├── admin/          — JSP pages for admin panel
│       ├── auth/           — Login and registration pages
│       └── assets/         — CSS stylesheets, JavaScript, uploaded images
├── database/
│   └── unitrade_final_schema.sql
└── pom.xml
```

---

## Common Issues

**Database connection error** — Make sure MySQL is running and your password in `database.properties` is correct. Also confirm you ran the SQL schema file first.

**Port 8080 already in use:**
```powershell
netstat -ano | findstr :8080
taskkill /PID <PID_NUMBER> /F
```

**404 on all pages** — Check that the application context in your Tomcat config is set to `/unitrade`.

**Build fails on Java version** — The project needs JDK 17. Run `java -version` in terminal to confirm.

**Images not showing after upload** — Make sure `src/main/webapp/assets/uploads/items/` exists (there is a `.gitkeep` file in it so it should be there after cloning).

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Java 17, Jakarta Servlets 6.0, JSP |
| Frontend | JSP, JSTL, CSS, vanilla JavaScript |
| Database | MySQL 8.0 |
| Server | Apache Tomcat 10.1 |
| Build | Maven (wrapper included) |
| Security | jBCrypt password hashing, split-token Remember Me |

---

*Group project — BSc (Hons) Computing, 2025/2026*

