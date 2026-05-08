# 🎓 UniTrade – Student Marketplace Platform

A peer-to-peer marketplace web app for university students to **buy/sell items**, **offer services**, and **post help requests**.

Built with **Java Servlets + JSP (MVC)**, **MySQL**, deployed on **Apache Tomcat**.

---

## 📋 Prerequisites

Make sure the following are installed before you begin:

| Tool | Version | Download |
|------|---------|----------|
| JDK | 17+ | https://www.oracle.com/java/technologies/downloads/#java17 |
| MySQL | 8.0+ | https://dev.mysql.com/downloads/installer/ |
| Apache Tomcat | 10.1+ | https://tomcat.apache.org/download-10.cgi |
| IntelliJ IDEA | Any | https://www.jetbrains.com/idea/ (Community is fine) |
| Git | Any | https://git-scm.com/ |

> Maven is **not required** separately — the project includes `mvnw` / `mvnw.cmd` (Maven Wrapper).

---

## 🚀 Setup in 5 Steps

### Step 1 — Clone the Repository

```bash
git clone https://github.com/Bipinjod/UNI_TRADE.git
cd UNI_TRADE
```

---

### Step 2 — Set Up the Database

#### 2a. Start MySQL and create the database + tables

```bash
mysql -u root -p < database/unitrade_final_schema.sql
```

This will:
- Create the `unitrade_db` database (if it doesn't exist)
- Create all tables (users, items, services, orders, categories, etc.)
- Insert default categories and an admin account

> **Default admin login** → `admin@unitrade.com` / `admin123`

#### 2b. (Optional) Verify it worked

```sql
mysql -u root -p
USE unitrade_db;
SHOW TABLES;
```

---

### Step 3 — Configure Database Credentials

```bash
# Copy the template
cp src/main/resources/database.properties.template src/main/resources/database.properties
```

> On Windows PowerShell:
> ```powershell
> Copy-Item src\main\resources\database.properties.template src\main\resources\database.properties
> ```

Now open `src/main/resources/database.properties` and fill in **your** MySQL password:

```properties
db.url=jdbc:mysql://localhost:3306/unitrade_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&characterEncoding=UTF-8
db.username=root
db.password=YOUR_MYSQL_PASSWORD_HERE
db.driver=com.mysql.cj.jdbc.Driver
```

> ⚠️ `database.properties` is in `.gitignore` — **never commit it with real credentials**.

---

### Step 4 — Build the Project

```powershell
# Windows
.\mvnw.cmd clean package -DskipTests
```

```bash
# macOS / Linux
./mvnw clean package -DskipTests
```

Expected output:
```
[INFO] BUILD SUCCESS
[INFO] Building war: D:\...\target\unitrade.war
```

---

### Step 5 — Deploy and Run

#### Option A: IntelliJ IDEA (Recommended)

1. **Open project** → `File → Open → select the cloned folder`
2. Wait for Maven to import (bottom progress bar)
3. **Add Tomcat** → `Run → Edit Configurations → + → Tomcat Server → Local`
   - Under **Server** tab: point to your Tomcat installation directory
   - Under **Deployment** tab: click `+` → `Artifact` → select `UniTrade:war exploded`
   - Set **Application context** to `/unitrade`
4. Click ▶ **Run**
5. Browser opens → `http://localhost:8080/unitrade/`

#### Option B: Manual WAR Deploy

```powershell
# Copy WAR to Tomcat webapps
Copy-Item target\unitrade.war "C:\path\to\tomcat\webapps\"

# Start Tomcat
C:\path\to\tomcat\bin\startup.bat
```

Access at: **http://localhost:8080/unitrade/**

---

## 🔑 Default Accounts

| Role | Email | Password |
|------|-------|----------|
| Admin | admin@unitrade.com | admin123 |

> Regular users must register and wait for admin approval before they can log in.

---

## 📁 Project Structure

```
UniTrade/
├── src/main/
│   ├── java/com/unitrade/
│   │   ├── controller/        # Servlets (user/, admin/, auth/, publicweb/)
│   │   ├── dao/               # Database Access Objects
│   │   ├── service/           # Business logic layer
│   │   ├── model/             # POJOs / Entity classes
│   │   ├── filter/            # Servlet filters (auth, role checks)
│   │   └── util/              # Helpers (DBConnection, CookieUtil, etc.)
│   ├── resources/
│   │   ├── database.properties          ← YOU CREATE THIS (gitignored)
│   │   └── database.properties.template ← committed, safe to share
│   └── webapp/
│       ├── assets/css/        # main.css, auth.css, user.css, admin.css
│       ├── assets/js/
│       ├── assets/uploads/    # User-uploaded images (gitignored)
│       ├── user/              # User JSP pages
│       ├── admin/             # Admin JSP pages
│       ├── auth/              # Login / Register pages
│       └── WEB-INF/web.xml    # Servlet mappings
├── database/
│   ├── unitrade_final_schema.sql  ← run this first!
│   └── migrate_v2.sql
└── pom.xml
```

---

## 🐛 Troubleshooting

### ❌ `database.properties` not found / DB connection error

- Did you copy the template and fill in your password? See Step 3.
- Is MySQL running? Try: `mysql -u root -p`
- Check MySQL service: `net start MySQL80` (Windows)

### ❌ BUILD FAILURE – Java version

- Ensure JDK **17** (not JRE, not JDK 11/21) is set as `JAVA_HOME`
- Check: `java -version` and `javac -version` in a new terminal

### ❌ Port 8080 already in use

```powershell
# Find what's using port 8080
netstat -ano | findstr :8080

# Kill it (replace 1234 with actual PID)
taskkill /PID 1234 /F
```

### ❌ 404 on all pages

- Ensure Application Context is set to `/unitrade` in Tomcat run config
- Redeploy the artifact after rebuild

### ❌ Images not showing after upload

- Ensure the `src/main/webapp/assets/uploads/items/` folder exists  
  (it has a `.gitkeep` file so it should be cloned correctly)

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Java 17, Jakarta Servlets 6.0, JSP 3.1 |
| Frontend | JSP, JSTL, vanilla CSS + JS |
| Database | MySQL 8.0 |
| Build | Maven (wrapper included) |
| Server | Apache Tomcat 10.1 |
| Security | jBCrypt password hashing |

---

## 👥 Contributing

1. Create your feature branch: `git checkout -b feature/your-feature`
2. Commit your changes: `git commit -m "Add your feature"`
3. Push to the branch: `git push origin feature/your-feature`
4. Open a Pull Request to `main`

---

*Last updated: May 2026*

