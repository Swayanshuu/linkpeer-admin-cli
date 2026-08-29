#!/usr/bin/env node

const { spawn } = require('child_process');
const path = require('path');
const fs = require('fs');
const https = require('https');
const packageJson = require('../package.json');

function checkUpdateNotification() {
  try {
    const req = https.get('https://registry.npmjs.org/@linkpeer/admin/latest', { timeout: 1200 }, (res) => {
      if (res.statusCode !== 200) return;
      let data = '';
      res.on('data', chunk => data += chunk);
      res.on('end', () => {
        try {
          const latestVersion = JSON.parse(data).version;
          if (latestVersion && isNewerVersion(packageJson.version, latestVersion)) {
            console.log('\n\u001B[1;33m💡 A new version of LinkPeer Admin CLI is available: ' 
              + packageJson.version + ' -> \u001B[1;32m' + latestVersion + '\u001B[1;33m\u001B[0m');
            console.log('\u001B[1;33m   Run "npm install -g @linkpeer/admin" to update.\u001B[0m\n');
          }
        } catch (e) {}
      });
    });
    req.on('error', () => {});
    req.on('timeout', () => req.destroy());
  } catch (e) {}
}

function isNewerVersion(current, latest) {
  const cParts = (current || '').split('.').map(n => parseInt(n, 10) || 0);
  const lParts = (latest || '').split('.').map(n => parseInt(n, 10) || 0);
  const len = Math.max(cParts.length, lParts.length);
  for (let i = 0; i < len; i++) {
    const cVal = cParts[i] || 0;
    const lVal = lParts[i] || 0;
    if (lVal > cVal) return true;
    if (lVal < cVal) return false;
  }
  return false;
}

// Trigger async update check
checkUpdateNotification();

const jarPath = path.join(__dirname, '..', 'lib', 'linkpeer-admin-cli.jar');

if (!fs.existsSync(jarPath)) {
  console.error(`Error: LinkPeer Admin CLI JAR file not found at:\n  ${jarPath}`);
  console.error(`Please run "npm run build" first to build and place the JAR file in lib/.`);
  process.exit(1);
}

const args = ['-jar', jarPath, ...process.argv.slice(2)];

const child = spawn('java', args, {
  stdio: 'inherit',
  env: process.env
});

child.on('error', (err) => {
  if (err.code === 'ENOENT') {
    console.error('Error: "java" command not found in your system PATH.');
    console.error('Please ensure Java Runtime Environment (JRE/JDK 21+) is installed and added to system PATH.');
  } else {
    console.error('Error starting Java CLI process:', err.message);
  }
  process.exit(1);
});

child.on('exit', (code, signal) => {
  if (code !== null) {
    process.exit(code);
  } else {
    process.exit(1);
  }
});
