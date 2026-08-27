#!/usr/bin/env node

const { spawn } = require('child_process');
const path = require('path');
const fs = require('fs');

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
