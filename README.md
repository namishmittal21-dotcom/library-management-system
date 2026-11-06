# Library Management System

A web application for managing books, members, and borrowing records. It uses Java 17, Spring Boot, Thymeleaf, Spring Security, Spring Data JPA, and MySQL. Features include role-based access, barcode-based book issuing, borrowing history, and due-date reminders.

## Requirements

- Java 17 or later
- MySQL 8 or later

## Run locally

1. Create a MySQL database named `library_db`.
2. Set the database variables in your shell. In PowerShell, for the current terminal:

   ```powershell
   $env:DB_USERNAME = "root"
   $env:DB_PASSWORD = "your-local-mysql-password"
   $env:BOOTSTRAP_ADMIN_USERNAME = "admin"
   $env:BOOTSTRAP_ADMIN_EMAIL = "admin@example.com"
   $env:BOOTSTRAP_ADMIN_PASSWORD = "choose-a-strong-password"
   ```

   Set `DB_URL` too if your MySQL server uses a different address or database name. The first administrator is created only when all three `BOOTSTRAP_ADMIN_*` values are set, and only if that username does not already exist.

3. Start the application from the project folder:

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

4. Open [http://localhost:8080](http://localhost:8080) and sign in with the administrator username and password you configured.

The environment variables above apply only to the current PowerShell window. Use a local, untracked environment file or your operating system's secret manager for persistent development settings. Do not commit credentials.

## Email reminders (optional)

Set `MAIL_USERNAME` and `MAIL_PASSWORD` to SMTP credentials supplied for your own mail account. For Gmail, use an app password. Mail credentials are not required to run the application, but email reminders will not be sent without valid SMTP settings.

## Upload to GitHub

1. Sign in at [github.com](https://github.com) and create a new **empty** repository. Choose a generic repository name such as `library-management-system`. Do not initialize it with a README or license because this folder already has a README.
2. Open PowerShell in this project folder.
3. Run the following commands, replacing `YOUR-USERNAME` and `YOUR-REPOSITORY` with your GitHub account and repository names:

   ```powershell
   git init
   git add .
   git status
   git commit -m "Initial library management system"
   git branch -M main
   git remote add origin https://github.com/YOUR-USERNAME/YOUR-REPOSITORY.git
   git push -u origin main
   ```

4. Check the repository page on GitHub and confirm the files appear. Keep the repository private if it contains any data you do not intend to share.

Before the first push, inspect `git status` and confirm there are no local config files, database exports, screenshots, or real member records staged. If a secret was committed in an earlier version, deleting it in a later commit does not remove it from Git history; revoke or rotate it and clean the history before making the repository public.

## Configuration reference

| Variable | Purpose |
| --- | --- |
| `SERVER_PORT` | Web server port (default `8080`) |
| `DB_URL` | JDBC URL (default points to local `library_db`) |
| `DB_USERNAME`, `DB_PASSWORD` | MySQL credentials |
| `BOOTSTRAP_ADMIN_USERNAME`, `BOOTSTRAP_ADMIN_EMAIL`, `BOOTSTRAP_ADMIN_PASSWORD` | Create the first administrator on startup |
| `MAIL_HOST`, `MAIL_PORT`, `MAIL_USERNAME`, `MAIL_PASSWORD` | Optional SMTP connection |

## License

No license has been selected. Add a license file before inviting others to reuse or redistribute this project.
