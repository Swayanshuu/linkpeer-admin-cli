# LinkPeer Admin CLI - Commands Reference

This document provides a comprehensive list of all commands available in the LinkPeer Admin CLI.

---

## Authentication Commands
These commands handle your session. You must be logged in as an admin (exists in the `admin_users` table) to execute privileged commands.

| Command | Description | Example |
|---|---|---|
| `login` | Login to the CLI. It will interactively prompt for your email and securely prompt for your password, masking it with asterisks. | `login` |
| `logout` | Ends your current session and clears credentials. | `logout` |
| `whoami` | Shows the currently logged in admin user. | `whoami` |

---

## User Commands (`users`)
Manage platform users, view their details, and handle verifications.

| Command | Description | Example |
|---|---|---|
| `users list` | Lists all users in a tabular format. | `users list` |
| `users view <userId>` | View detailed profile information for a specific user. | `users view 123e4567-e89b...` |
| `users search <keyword>` | Search for a user by name, email, department, or college. | `users search John` |
| `users verify <userId>` | Manually mark a user as verified. | `users verify 123e4567...` |
| `users unverify <userId>`| Revoke a user's verified status. | `users unverify 123e4567...` |

---

## Faculty Commands (`faculty`)
Manage faculty verification requests.

| Command | Description | Example |
|---|---|---|
| `faculty pending` | View all users claiming to be faculty but who are not yet verified. Shows their uploaded proof. | `faculty pending` |
| `faculty approve <userId>`| Approve a pending faculty request (marks them as verified). | `faculty approve 123e4567...` |
| `faculty reject <userId>` | Reject a faculty request (clears their proof and sets verified to false). | `faculty reject 123e4567...` |

---

## Post Commands (`posts`)
Manage content created by users on the platform.

| Command | Description | Example |
|---|---|---|
| `posts list` | View a high-level list of all posts. | `posts list` |
| `posts view <postId>` | View full details of a specific post including content and links. | `posts view 123e4567...` |
| `posts user <userId>` | See all posts authored by a specific user. | `posts user 123e4567...` |
| `posts delete <postId>` | Safely delete a post and all its cascade dependencies (comments, likes, saved references). | `posts delete 123e4567...` |

---

## Comment Commands (`comments`)
Manage comments on posts.

| Command | Description | Example |
|---|---|---|
| `comments list` | View all comments across the platform. | `comments list` |
| `comments post <postId>`| View all comments made on a specific post. | `comments post 123e4567...` |
| `comments view <commentId>`| View a single comment in detail. | `comments view 123e4567...` |
| `comments delete <commentId>`| Delete a specific comment. | `comments delete 123e4567...` |

---

## Subscription Commands (`subs`)
Manage user subscription plans.

| Command | Description | Example |
|---|---|---|
| `subs active` | List all currently active subscriptions. | `subs active` |
| `subs user <userId>` | View the subscription history and status for a specific user. | `subs user 123e4567...` |
| `subs cancel <userId>` | Cancel a user's active subscription (sets status to 'cancelled'). | `subs cancel 123e4567...` |
| `subs extend <userId> <days>`| Extend an active subscription by a given number of days. | `subs extend 123e4567... 30` |

---

## Payment Commands (`payments`)
View transaction and payment details.

| Command | Description | Example |
|---|---|---|
| `payments recent` | List the 50 most recent payments. | `payments recent` |
| `payments pending` | List all payments currently stuck in a 'pending' state. | `payments pending` |
| `payments view <id>` | View details of a specific transaction using its Payment ID. | `payments view 123e4567...` |

---

## Analytics Commands (`analytics` or `dashboard`)
View platform statistics and activity.

| Command | Description | Example |
|---|---|---|
| `dashboard` | View high-level metrics (total users, breakdown by type, total revenue this month, new users today, etc.). | `dashboard` |
| `analytics top-users` | View the top 10 users ranked by their engagement/ranking score. | `analytics top-users` |
| `analytics activity <userId>`| View the chronological activity log for a specific user. | `analytics activity 123e4567...` |
| `analytics views <userId>` | See the total number of profile views a specific user has received. | `analytics views 123e4567...` |

---

## Export Commands (`export`)
Export data from the platform to CSV files for external analysis. Note: The generated CSV files will be saved in the root directory of the CLI application.

| Command | Description | Example |
|---|---|---|
| `export users` | Export all user data to `users.csv`. | `export users` |
| `export posts` | Export all posts to `posts.csv`. | `export posts` |
| `export subscriptions` | Export all subscription data to `subscriptions.csv`. | `export subscriptions` |
