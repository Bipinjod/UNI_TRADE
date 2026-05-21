# ============================================================
#  UniTrade – One-Click Setup Script (Windows PowerShell)
#  Run this ONCE after receiving the project folder.
#  Right-click setup.ps1 → "Run with PowerShell"
# ============================================================

$ErrorActionPreference = "Stop"
$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $projectRoot

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "   UniTrade Setup Wizard" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# ── Step 1: Create database.properties ───────────────────────
Write-Host "[1/4] Setting up database.properties..." -ForegroundColor Yellow

$dbPropsPath = "src\main\resources\database.properties"
$dbTemplatePath = "src\main\resources\database.properties.template"

if (Test-Path $dbPropsPath) {
    Write-Host "      database.properties already exists. Skipping." -ForegroundColor Gray
} else {
    $mysqlPassword = Read-Host "      Enter your MySQL root password (press Enter if no password)"

    $content = @"
db.url=jdbc:mysql://localhost:3306/unitrade_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&characterEncoding=UTF-8
db.username=root
db.password=$mysqlPassword
db.driver=com.mysql.cj.jdbc.Driver
"@
    Set-Content -Path $dbPropsPath -Value $content -Encoding UTF8
    Write-Host "      database.properties created!" -ForegroundColor Green
}

# ── Step 2: Import SQL schema into MySQL ─────────────────────
Write-Host ""
Write-Host "[2/4] Importing database schema into MySQL..." -ForegroundColor Yellow

$mysqlPassword = (Get-Content $dbPropsPath | Where-Object { $_ -match "^db\.password=" }) -replace "db\.password=", ""
$schemaFile = "$projectRoot\database\unitrade_final_schema.sql"

# Find mysql.exe automatically
$mysqlExe = Get-Command mysql -ErrorAction SilentlyContinue | Select-Object -ExpandProperty Source
if (-not $mysqlExe) {
    # Try common XAMPP location
    $candidates = @(
        "C:\xampp\mysql\bin\mysql.exe",
        "C:\wamp64\bin\mysql\mysql8.0.31\bin\mysql.exe",
        "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe",
        "C:\Program Files\MySQL\MySQL Server 8.4\bin\mysql.exe"
    )
    foreach ($c in $candidates) {
        if (Test-Path $c) { $mysqlExe = $c; break }
    }
}

if (-not $mysqlExe) {
    Write-Host "      MySQL not found automatically." -ForegroundColor Yellow
    Write-Host "      Please import manually in phpMyAdmin:" -ForegroundColor Yellow
    Write-Host "        1. Open http://localhost/phpmyadmin" -ForegroundColor White
    Write-Host "        2. Click Import tab" -ForegroundColor White
    Write-Host "        3. Choose file: database\unitrade_final_schema.sql" -ForegroundColor White
    Write-Host "        4. Click Go" -ForegroundColor White
} else {
    try {
        Write-Host "      Using MySQL at: $mysqlExe" -ForegroundColor Gray
        # Pipe the SQL file directly into mysql (works on all platforms)
        if ($mysqlPassword -eq "") {
            Get-Content $schemaFile | & $mysqlExe -u root
        } else {
            Get-Content $schemaFile | & $mysqlExe -u root -p"$mysqlPassword"
        }
        if ($LASTEXITCODE -eq 0) {
            Write-Host "      Database schema imported successfully!" -ForegroundColor Green
        } else {
            throw "mysql exited with code $LASTEXITCODE"
        }
    } catch {
        Write-Host "      Auto-import failed: $_" -ForegroundColor Red
        Write-Host "      Please import manually in phpMyAdmin:" -ForegroundColor Yellow
        Write-Host "        1. Open http://localhost/phpmyadmin" -ForegroundColor White
        Write-Host "        2. Click Import tab" -ForegroundColor White
        Write-Host "        3. Choose file: database\unitrade_final_schema.sql" -ForegroundColor White
        Write-Host "        4. Click Go" -ForegroundColor White
    }
}

# ── Step 3: Maven build ───────────────────────────────────────
Write-Host ""
Write-Host "[3/4] Building project with Maven..." -ForegroundColor Yellow

try {
    & ".\mvnw.cmd" clean package -DskipTests
    if ($LASTEXITCODE -eq 0) {
        Write-Host "      BUILD SUCCESS!" -ForegroundColor Green
    } else {
        Write-Host "      Build failed. Check errors above." -ForegroundColor Red
        Write-Host "      Common fix: Make sure JDK 17+ is installed and JAVA_HOME is set." -ForegroundColor Yellow
        pause
        exit 1
    }
} catch {
    Write-Host "      mvnw.cmd not found or failed: $_" -ForegroundColor Red
    pause
    exit 1
}

# ── Step 4: Done ─────────────────────────────────────────────
Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host "   Setup Complete!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""
Write-Host " Next steps:" -ForegroundColor Cyan
Write-Host "  1. Open IntelliJ IDEA"
Write-Host "  2. File > Open > select this folder"
Write-Host "  3. Run/Edit Configurations > Smart Tomcat:"
Write-Host "     - Context path:  /unitrade"
Write-Host "     - Server port:   8080"
Write-Host "     - Deployment dir: src/main/webapp"
Write-Host "  4. Click Run"
Write-Host "  5. Open browser: http://localhost:8080/unitrade/"
Write-Host ""
Write-Host " Default admin login:" -ForegroundColor Cyan
Write-Host "     Email:    admin@unitrade.com"
Write-Host "     Password: admin123"
Write-Host ""
pause