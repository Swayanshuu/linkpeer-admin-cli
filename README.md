# LinkPeer Admin CLI (`@linkpeer/admin`)

[![npm version](https://img.shields.io/npm/v/@linkpeer/admin.svg)](https://www.npmjs.com/package/@linkpeer/admin)
[![License: ISC](https://img.shields.io/badge/License-ISC-blue.svg)](https://opensource.org/licenses/ISC)
[![Java 21](https://img.shields.io/badge/Java-21-orange.svg)](https://jdk.java.net/21/)
[![Node.js 18+](https://img.shields.io/badge/Node.js-18%2B-brightgreen.svg)](https://nodejs.org/)

**LinkPeer Admin CLI** is the official command-line administration tool for managing the LinkPeer platform. Built with **Spring Boot 3** and **Picocli** in Java, and wrapped as a global **NPM package**, it provides administrators with powerful, real-time command-line and interactive REPL shell capabilities to manage users, verify faculty credentials, inspect analytics, handle payments, and control platform content.

---

## 🏗️ Architecture

LinkPeer Admin CLI uses a hybrid NPM + Java architecture to combine the simplicity of `npm install -g` with the high-performance backend power of Java & Spring Boot:

```text
User executes:
  linkpeer analytics dashboard
       │
       ▼
NPM Global Command Bin
       │
       ▼
Node.js Launcher Wrapper (bin/linkpeer.js)
       │
       ▼
Executes: java -jar lib/linkpeer-admin-cli.jar analytics dashboard
       │
       ▼
Spring Boot 3 + Picocli Engine (Executes Command & Interacts with PostgreSQL)
```

- **NPM Package Wrapper**: Light Node.js launcher script that forwards terminal input/output (`stdio: inherit`), handles exit codes, and provides a cross-platform command entry point (`linkpeer`).
- **Java Spring Boot Engine**: A self-contained, high-performance executable JAR (`lib/linkpeer-admin-cli.jar`) containing all domain services, repositories, and Picocli command handlers.

---

## ⚡ Prerequisites

To install and run LinkPeer Admin CLI, your environment needs:

1. **Java Runtime Environment (JRE/JDK)**: Java 21 or higher installed and available in your system `PATH`.
   - Verify with: `java -version`
2. **Node.js**: Node.js 18 or higher.
   - Verify with: `node -v`

---

## 🚀 Installation

Install the CLI globally from NPM:

```bash
npm install -g @linkpeer/admin
```

Verify the installation:

```bash
linkpeer --help
```

---

## 🔐 Configuration & Environment Variables

LinkPeer Admin CLI loads database credentials from environment variables or a `.env` file located in the working directory or in `~/.linkpeer/.env`.

### 1. Create your `.env` file
Copy the `.env.example` template to `.env`:

```bash
cp .env.example .env
```

### 2. Configure Database Credentials
Edit your `.env` file with your PostgreSQL / Supabase connection details:

```env
SUPABASE_DB_URL=jdbc:postgresql://your-host:6543/postgres?sslmode=require
SUPABASE_DB_USERNAME=your_db_user
SUPABASE_DB_PASSWORD=your_db_password
```

---

## 💡 Quick Start & Usage

### Interactive REPL Shell Mode
Launch the interactive shell by running `linkpeer` without subcommands:

```bash
linkpeer
```

You will be greeted with the LinkPeer Administration banner and interactive shell prompt:

```text
  _      _       _    _____               
 | |    (_)     | |  |  __ \              
 | |     _ _ __ | | _| |__) |__  ___ _ __ 
 | |    | | '_ \| |/ /  ___/ _ \/ _ \ '__|
 | |____| | | | |   <| |  |  __/  __/ |   
 |______|_|_| |_|_|\_\|_|   \___|\___|_|   
                                          
          ADMINISTRATION CLI              

? Authentication Required. Please log in.
Email: admin@linkpeer.com
Password: *********
```

### Direct Command Execution
You can also run commands directly from your terminal:

```bash
# View main help menu
linkpeer --help

# Login to your admin account
linkpeer login

# View platform analytics dashboard
linkpeer dashboard

# List registered platform users
linkpeer users list
```

---

## 🛠️ Commands Reference Summary

| Command Category | Command | Description | Example |
|---|---|---|---|
| **Authentication** | `login` | Interactive admin authentication | `linkpeer login` |
| | `logout` | End session & clear stored session token | `linkpeer logout` |
| | `whoami` | Show currently authenticated admin details | `linkpeer whoami` |
| **Analytics** | `dashboard` | Display high-level metrics & platform activity | `linkpeer dashboard` |
| | `analytics top-users` | View top users ranked by engagement score | `linkpeer analytics top-users` |
| | `analytics activity <userId>`| View chronological activity logs for a user | `linkpeer analytics activity <id>` |
| **User Management** | `users list` | Tabular list of platform users | `linkpeer users list` |
| | `users view <userId>` | Detailed profile & faculty verification data | `linkpeer users view <id>` |
| | `users search <keyword>` | Search users by name, email, or department | `linkpeer users search John` |
| | `users verify <userId>` | Manually verify user status | `linkpeer users verify <id>` |
| **Faculty Requests** | `faculty pending` | List pending faculty verification claims | `linkpeer faculty pending` |
| | `faculty approve <userId>`| Approve a faculty verification request | `linkpeer faculty approve <id>` |
| | `faculty reject <userId>` | Reject a faculty request with reason | `linkpeer faculty reject <id> "Invalid ID"` |
| **Content Management**| `posts list` | List posts created on the platform | `linkpeer posts list` |
| | `posts delete <postId>` | Delete post and cascading comments/likes | `linkpeer posts delete 42` |
| | `comments list` | View platform comments with like counts | `linkpeer comments list` |
| **Notices & Broadcasts**| `notices list` | List platform notices | `linkpeer notices list` |
| | `broadcasts send` | Send system-wide broadcast notification | `linkpeer broadcasts send "Alert" "Body"` |
| **Subscriptions** | `subs active` | View active user subscriptions | `linkpeer subs active` |
| | `subs extend <userId> <days>`| Extend user subscription duration | `linkpeer subs extend <id> 30` |
| **Payments** | `payments recent` | View 50 most recent payment transactions | `linkpeer payments recent` |
| **Data Exports** | `export users` | Export user table data to CSV file | `linkpeer export users` |

*For complete command options and flags, see [COMMANDS.md](COMMANDS.md).*

---

## 💻 Development & Publishing Guide

### 1. Build locally
To build the Spring Boot Maven project and automatically stage the executable JAR into `lib/linkpeer-admin-cli.jar`:

```bash
npm run build
```

### 2. Test locally
Install the local package globally to test changes before publishing:

```bash
npm install -g .
```

### 3. Release a new version to NPM
To release a new version to NPM:

```bash
# 1. Bump version (patch/minor/major)
npm version patch

# 2. Publish to NPM (automatically builds Maven JAR before publishing)
npm publish --access public
```

---

## 📄 License

ISC License © LinkPeer Team.
