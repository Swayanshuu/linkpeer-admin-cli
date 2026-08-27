const fs = require('fs');
const path = require('path');
const { execSync } = require('child_process');

const rootDir = path.resolve(__dirname, '..');
const targetDir = path.join(rootDir, 'target');
const libDir = path.join(rootDir, 'lib');
const targetJarName = 'linkpeer-admin-cli.jar';
const destJarPath = path.join(libDir, targetJarName);

console.log('Building Maven project...');
const isWindows = process.platform === 'win32';
const mvnCmd = fs.existsSync(path.join(rootDir, isWindows ? 'mvnw.cmd' : 'mvnw'))
  ? (isWindows ? '.\\mvnw.cmd' : './mvnw')
  : 'mvn';

try {
  execSync(`${mvnCmd} clean package -DskipTests`, {
    cwd: rootDir,
    stdio: 'inherit'
  });
} catch (err) {
  console.error('Maven build failed:', err.message);
  process.exit(1);
}

if (!fs.existsSync(targetDir)) {
  console.error('Target directory does not exist after build.');
  process.exit(1);
}

const files = fs.readdirSync(targetDir);
const jarFile = files.find(f => f.endsWith('.jar') && !f.endsWith('.original'));

if (!jarFile) {
  console.error('No executable JAR file found in target directory.');
  process.exit(1);
}

if (!fs.existsSync(libDir)) {
  fs.mkdirSync(libDir, { recursive: true });
}

const srcJarPath = path.join(targetDir, jarFile);
console.log(`Copying ${jarFile} -> lib/${targetJarName}...`);
fs.copyFileSync(srcJarPath, destJarPath);
console.log('Build completed successfully!');
