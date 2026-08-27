# LinkPeer Admin CLI - Commands Reference

This document provides a comprehensive list of all commands available in the LinkPeer Admin CLI.

---

## Authentication Commands
These commands handle your session. You must be logged in as an admin (exists in the `admin_users` table) to execute privileged commands. Passwords are password-masked with `*` when entered interactively.

| Command | Description | Example |
|---|---|---|
| `login` | Login to the CLI. Interactively prompts for email and securely prompts for password (masked with `*`). | `login` |
| `logout` | Ends your current session and clears credentials. | `logout` |
| `whoami` | Shows the currently logged in admin user. | `whoami` |

---

## User Commands (`users`)
Manage platform users, view their details, and handle verifications.

| Command | Description | Example |
|---|---|---|
| `users list` | Lists all users in a tabular format (ID, Name, Email, Type, Department, Grad Year, Verified, Plan, Status). | `users list` |
| `users view <userId>` | View detailed profile information for a specific user (including faculty verification fields, subscription status, and FCM tokens). | `users view 123e4567-e89b...` |
| `users search <keyword>` | Search for a user by name, email, department, or college. | `users search John` |
| `users verify <userId>` | Manually mark a user as verified (approves faculty status if requested). | `users verify 123e4567...` |
| `users unverify <userId>`| Revoke a user's verified status. | `users unverify 123e4567...` |

---

## Faculty Commands (`faculty`)
Manage faculty verification requests.

| Command | Description | Example |
|---|---|---|
| `faculty pending` | View all users claiming to be faculty but who are not yet verified. Shows uploaded proof image URL. | `faculty pending` |
| `faculty approve <userId>`| Approve a pending faculty request (marks them as faculty verified). | `faculty approve 123e4567...` |
| `faculty reject <userId> [reason]` | Reject a faculty request with an optional rejection reason. | `faculty reject 123e4567... "Invalid ID card"` |

---

## Post Commands (`posts`)
Manage content created by users on the platform.

| Command | Description | Example |
|---|---|---|
| `posts list` | View a high-level list of all posts. | `posts list` |
| `posts view <postId>` | View full details of a specific post including author details, image URLs, file attachments, and links. | `posts view 42` |
| `posts user <userId>` | See all posts authored by a specific user. | `posts user 123e4567...` |
| `posts delete <postId>` | Delete a post and all its cascade dependencies (comments, likes, saved posts, milestones, notifications). | `posts delete 42` |

---

## Comment Commands (`comments`)
Manage comments on posts.

| Command | Description | Example |
|---|---|---|
| `comments list` | View all comments across the platform with like counts. | `comments list` |
| `comments post <postId>`| View all comments made on a specific post. | `comments post 42` |
| `comments view <commentId>`| View a single comment in detail (including liked_by users and timestamp). | `comments view 101` |
| `comments delete <commentId>`| Delete a specific comment. | `comments delete 101` |

---

## Notice Commands (`notices`)
Manage official notices and notice publishers.

| Command | Description | Example |
|---|---|---|
| `notices list` | List all platform notices ordered by creation date. | `notices list` |
| `notices view <id>` | View details of a specific notice. | `notices view 123e4567-e89b...` |
| `notices publishers` | View all authorized notice publishers. | `notices publishers` |
| `notices add-publisher <userId>` | Grant notice publisher authority to a user. | `notices add-publisher 123e4567-e89b...` |
| `notices remove-publisher <userId>` | Revoke notice publisher authority from a user. | `notices remove-publisher 123e4567-e89b...` |
| `notices delete <id>` | Delete a notice and its attachments. | `notices delete 123e4567-e89b...` |

---

## Broadcast Commands (`broadcasts`)
Create and manage platform-wide broadcasts.

| Command | Description | Example |
|---|---|---|
| `broadcasts list` | List all broadcasts and open/click metrics. | `broadcasts list` |
| `broadcasts view <id>` | View detailed broadcast statistics. | `broadcasts view 123e4567-e89b...` |
| `broadcasts send <title> <message>` | Send a new broadcast to users. Supports `-a/--audience`, `-i/--image`, and `-l/--link` options. | `broadcasts send "Downtime Notice" "Server maintenance at 2 AM" -a all` |

---

## Notification Commands (`notifications`)
Inspect system notifications sent to users.

| Command | Description | Example |
|---|---|---|
| `notifications list` | List all recent notifications across the platform. | `notifications list` |
| `notifications user <userId>` | List notifications received by a specific user. | `notifications user 123e4567-e89b...` |

---

## Subscription Commands (`subs`)
Manage user subscription plans.

| Command | Description | Example |
|---|---|---|
| `subs active` | List all currently active subscriptions. | `subs active` |
| `subs user <userId>` | View the subscription history and status for a specific user. | `subs user 123e4567-e89b...` |
| `subs cancel <userId>` | Cancel a user's active subscription (sets status to 'cancelled'). | `subs cancel 123e4567-e89b...` |
| `subs extend <userId> <days>`| Extend an active subscription by a given number of days. | `subs extend 123e4567-e89b... 30` |

---

## Payment Commands (`payments`)
View transaction and payment details.

| Command | Description | Example |
|---|---|---|
| `payments recent` | List the 50 most recent payments with plan type, amount, provider, and status details. | `payments recent` |
| `payments pending` | List all payments currently stuck in a 'pending' state. | `payments pending` |
| `payments view <id>` | View details of a specific payment transaction. | `payments view 123e4567-e89b...` |

---

## Analytics Commands (`analytics` or `dashboard`)
View platform statistics and activity.

| Command | Description | Example |
|---|---|---|
| `dashboard` | View high-level metrics (total users, breakdown by type, total revenue, new users today, active subs, total posts/comments). | `dashboard` |
| `analytics top-users` | View the top 10 users ranked by their engagement/ranking score. | `analytics top-users` |
| `analytics activity <userId>`| View the chronological activity log for a specific user. | `analytics activity 123e4567-e89b...` |
| `analytics views <userId>` | See the total number of profile views a specific user has received. | `analytics views 123e4567-e89b...` |

---

## Export Commands (`export`)
Export data from the platform to CSV files for external analysis. Note: The generated CSV files will be saved in the root directory of the CLI application.

| Command | Description | Example |
|---|---|---|
| `export users` | Export all user data to `users.csv`. | `export users` |
| `export posts` | Export all posts to `posts.csv`. | `export posts` |
| `export subscriptions` | Export all subscription data to `subscriptions.csv`. | `export subscriptions` |
| `export notices` | Export all notice data to `notices.csv`. | `export notices` |
| `export broadcasts` | Export all broadcast data to `broadcasts.csv`. | `export broadcasts` |

---

## Automatic Features & Environment Configuration

### Automatic NPM Update Notifications
The CLI automatically performs a non-blocking check against the NPM registry on startup. If a newer version of `@linkpeer/admin` is published, an update notice will be displayed with instructions to update via `npm install -g @linkpeer/admin`.

### Database Connection & PgBouncer Compatibility
The CLI includes built-in support for PostgreSQL connection poolers (such as Supabase's PgBouncer on port `6543`). It automatically forces `prepareThreshold=0` and `preferQueryMode=simple` to ensure prepared statement collisions (`ERROR: prepared statement "S_..." already exists`) are prevented.

To override the default database connection in local environments or `.env`:
```env
SUPABASE_DB_URL=jdbc:postgresql://custom-host:6543/postgres?sslmode=require&prepareThreshold=0&preferQueryMode=simple
SUPABASE_DB_USERNAME=custom_user
SUPABASE_DB_PASSWORD=custom_password
```

