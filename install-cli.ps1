$ErrorActionPreference = "Stop"

Write-Host "========================================="
Write-Host "   Installing LinkPeer Admin CLI..."
Write-Host "========================================="

$INSTALL_DIR = "$env:USERPROFILE\linkpeer-cli"
$CONFIG_DIR = "$env:USERPROFILE\.linkpeer"

# 1. Create Directories
if (-not (Test-Path $INSTALL_DIR)) {
    New-Item -ItemType Directory -Force -Path $INSTALL_DIR | Out-Null
}
if (-not (Test-Path $CONFIG_DIR)) {
    New-Item -ItemType Directory -Force -Path $CONFIG_DIR | Out-Null
}

# 2. Build the Fat Jar
Write-Host "Building application..."
.\mvnw.cmd clean package -DskipTests
if ($LASTEXITCODE -ne 0) {
    Write-Error "Build failed!"
    exit 1
}

# 3. Copy files to install directory
Write-Host "Copying files to $INSTALL_DIR..."
Copy-Item "target\linkpeer-admin-cli-1.0.0.jar" -Destination "$INSTALL_DIR\linkpeer-admin-cli.jar" -Force

# 4. Copy .env to config directory
if (Test-Path ".env") {
    Write-Host "Copying .env to $CONFIG_DIR..."
    Copy-Item ".env" -Destination "$CONFIG_DIR\.env" -Force
} else {
    Write-Warning ".env file not found in current directory. Database connection may fail until configured in $CONFIG_DIR\.env"
}

# 5. Create the wrapper batch script
Write-Host "Creating wrapper script..."
$WRAPPER_PATH = "$INSTALL_DIR\linkpeer-admin.bat"
"@echo off`r`njava -jar `"%USERPROFILE%\linkpeer-cli\linkpeer-admin-cli.jar`" %*" | Out-File -FilePath $WRAPPER_PATH -Encoding ASCII

# 6. Instructions for PATH setup
Write-Host ""
Write-Host "================================================================" -ForegroundColor Green
Write-Host "Installation Successful!" -ForegroundColor Green
Write-Host "================================================================" -ForegroundColor Green
Write-Host "To use the 'linkpeer-admin' command from ANY terminal, you must"
Write-Host "add the installation directory to your System PATH."
Write-Host ""
Write-Host "Directory to add: $INSTALL_DIR" -ForegroundColor Cyan
Write-Host ""
Write-Host "How to add it:"
Write-Host "1. Press Windows Key, search for 'Environment Variables'"
Write-Host "2. Click 'Edit the system environment variables'"
Write-Host "3. Click 'Environment Variables...' button"
Write-Host "4. Under 'User variables', select 'Path' and click 'Edit'"
Write-Host "5. Click 'New' and paste: $INSTALL_DIR"
Write-Host "6. Click OK on all windows, and restart your terminal."
Write-Host "================================================================"
