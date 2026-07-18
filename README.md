# Tasnim Ahmed — Portfolio (Spring Boot version)

The same portfolio site, repackaged as a Spring Boot app, following the exact
project layout from the `demo` starter (Maven + Spring Boot 4.1.0 + Thymeleaf +
Spring Web MVC).


## Structure

```
portfolio-springboot/
├── pom.xml
├── Dockerfile.vercel                          ← used by Vercel's container deploy (see below)
├── .dockerignore
├── mvnw / mvnw.cmd / .mvn/                    ← Maven wrapper (same as demo)
├── src/
│   ├── main/
│   │   ├── java/com/tasnim/portfolio/
│   │   │   ├── PortfolioApplication.java      ← main class (was DemoApplication)
│   │   │   ├── PortfolioController.java       ← maps "/" → index template, feeds project data, handles /contact
│   │   │   ├── Project.java                   ← plain data class for the model
│   │   │   └── ContactMailService.java        ← sends the contact form email
│   │   └── resources/
│   │       ├── application.properties         ← port 9090, address 0.0.0.0
│   │       ├── static/
│   │       │   ├── css/style.css
│   │       │   ├── js/main.js
│   │       │   └── assets/img/
│   │       │       ├── profile.jpg             ← ADD PHOTO HERE
│   │       │       └── projects/
│   │       │           ├── pawpal.jpg          ← ADD SCREENSHOTS HERE
│   │       │           ├── studyflow.jpg
│   │       │           └── coxs-canvas.jpg
│   │       └── templates/
│   │           └── index.html                 ← the whole portfolio page, th:each over ${projects}
│   └── test/java/com/tasnim/portfolio/
│       └── PortfolioApplicationTests.java
```

This mirrors your `demo` ZIP one-to-one: same Spring Boot version (4.1.0), same
dependency set (Thymeleaf + Web MVC + devtools), same `static/` + `templates/`
split, same `application.properties` settings (port `9090`, bound to `0.0.0.0`).

## Other Thymeleaf wiring

The rest of the page (hero, about, skills, experience, research, contact) is
static content — no per-item repetition needed there, so it stays as plain
HTML. But every asset reference still goes through Thymeleaf's URL resolution
using **natural templating**: both a plain `href`/`src` (so the file still
opens correctly if double-clicked directly) and a `th:href`/`th:src` (what
Thymeleaf actually uses when the app serves the page):

```html
<link rel="stylesheet" href="css/style.css" th:href="@{/css/style.css}" />
```

## Running it

```bash
./mvnw spring-boot:run
```

Then visit **http://localhost:9090**

(Or build a jar: `./mvnw clean package` → `java -jar target/portfolio-0.0.1-SNAPSHOT.jar`)

> Note: this was scaffolded in a sandboxed environment without access to Maven
> Central, so the build itself hasn't been run here — but the structure,
> dependencies, and Thymeleaf wiring exactly match your working `demo` project,
> so `./mvnw spring-boot:run` should work the same way it does for that one.

## Contact form (sends email directly to you)

The Contact section now has a real form (Name / Email / Message) at the bottom
of the page. Submitting it does a `POST /contact`, which sends an email
straight to `tasnimtbh123@gmail.com` — the visitor's email is set as the
**Reply-To**, so hitting "Reply" in your inbox replies directly to them.

**How it's wired:**
- `ContactMailService.java` — builds and sends the email via Spring's `JavaMailSender`
- `PortfolioController.java` — `@PostMapping("/contact")` receives the form fields,
  calls the mail service, and redirects back to `/#contact` with a success/error flag
- `templates/index.html` — the `<form>` itself, plus a success/error banner that
  appears after submitting (`th:if="${contactSuccess}"` / `th:if="${contactError}"`)

### 1. Generate a Gmail App Password

Gmail no longer allows apps to sign in with your normal account password — you
need an **App Password** instead:

1. Go to your Google Account → **Security**
2. Turn on **2-Step Verification** if it isn't already on (required for App Passwords)
3. Go to **Security → 2-Step Verification → App passwords**
   (direct link: https://myaccount.google.com/apppasswords)
4. Create one named something like "Portfolio contact form"
5. Google gives you a 16-character password (e.g. `abcd efgh ijkl mnop`) — copy it,
   spaces don't matter either way

This app password is what goes into `MAIL_APP_PASSWORD` below — **not** your real
Gmail password.

### 2. Set the two environment variables

`application.properties` reads credentials from environment variables, so
nothing sensitive is ever committed to git:

```properties
spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_APP_PASSWORD}
```

**Option A — set them in your terminal before running:**

```bash
export MAIL_USERNAME=tasnimtbh123@gmail.com
export MAIL_APP_PASSWORD=abcdefghijklmnop
./mvnw spring-boot:run
```

(Windows PowerShell: `$env:MAIL_USERNAME="..."`, `$env:MAIL_APP_PASSWORD="..."`)

**Option B — set them in your IDE's run configuration** (IntelliJ: Run →
Edit Configurations → Environment variables) so you don't have to `export`
them every time.

**Option C — a gitignored local properties file**, if you find that easier:
create `src/main/resources/application-local.properties` (already excluded
in `.gitignore`) with:
```properties
spring.mail.username=tasnimtbh123@gmail.com
spring.mail.password=abcdefghijklmnop
```
then run with `SPRING_PROFILES_ACTIVE=local ./mvnw spring-boot:run`.

Whichever option you pick, **never commit the real app password to git.**

### 3. Test it

Run the app, scroll to the Contact section, fill out the form, submit — you
should get an email at `tasnimtbh123@gmail.com` within a few seconds, and the
page will show a green "message sent" banner. If something's misconfigured
(wrong password, env vars not set, etc.) you'll see a red error banner instead
— check the console logs for the underlying `MailException`.

### Deploying this somewhere later

If you eventually host this (not just run it locally), set `MAIL_USERNAME` and
`MAIL_APP_PASSWORD` as environment variables / secrets in whatever platform you
deploy to (Render, Railway, a VPS, etc.) — the same way you would for any other
secret. Don't put the real values in `application.properties` itself.



Same as the static version — drop files into:
- `src/main/resources/static/assets/img/profile.jpg`
- `src/main/resources/static/assets/img/projects/pawpal.jpg`
- `src/main/resources/static/assets/img/projects/studyflow.jpg`
- `src/main/resources/static/assets/img/projects/coxs-canvas.jpg`

See the `PLACE_PHOTOS_HERE.md` files in those folders for sizing details.

## Deploying to Vercel

Vercel added Dockerfile-based deployments on **June 30, 2026** (`Vercel Functions` +
`Fluid compute`), and Spring Boot is explicitly one of their supported examples —
so this now works, which wasn't true before that date. Reference:
[vercel.com/blog/dockerfile-on-vercel](https://vercel.com/blog/dockerfile-on-vercel)

**How it works here:**
- `Dockerfile.vercel` at the project root — a two-stage build (Maven build → slim JRE runtime)
- Vercel's contract: the server must listen on `$PORT`, which Vercel injects at
  runtime. `application.properties` already does this: `server.port=${PORT:9090}`
  (falls back to 9090 only if `PORT` is unset, which only happens when running
  locally without Docker)
- The container is stateless (no persistent storage between requests) — that's
  fine here, since the contact form doesn't need to remember anything, it just
  sends an email and returns

### Steps

1. Push this project to a GitHub repo (Vercel deploys from git, or via `vercel deploy` from the CLI)
2. In the Vercel dashboard: **Add New → Project → Import** your repo. Vercel will
   detect `Dockerfile.vercel` and build/deploy it as a container-backed Function.
3. **Set the same two environment variables** as local dev, but in
   **Project Settings → Environment Variables** on Vercel:
   - `MAIL_USERNAME` → your Gmail address
   - `MAIL_APP_PASSWORD` → the Gmail App Password (see "Contact form" section above)
4. Redeploy (or it'll deploy automatically after you add the env vars, depending
   on your project settings). Every subsequent `git push` rebuilds and redeploys
   automatically, with its own preview URL.

### Testing the container locally first (recommended before deploying)

```bash
docker build -f Dockerfile.vercel -t portfolio .
docker run -p 9090:9090 \
  -e PORT=9090 \
  -e MAIL_USERNAME=tasnimtbh123@gmail.com \
  -e MAIL_APP_PASSWORD=abcdefghijklmnop \
  portfolio
```

Then visit `http://localhost:9090` — if the page loads and the contact form
sends mail, the same image will work on Vercel.

> Note: this Dockerfile was written by hand against Vercel's published contract
> (listen on `$PORT`, default 80) rather than test-deployed here, since this
> sandbox has no access to Vercel or Docker Hub. Do a local `docker build` test
> (above) before pushing to Vercel to confirm the base image tags
> (`maven:3.9-eclipse-temurin-25`, `eclipse-temurin:25-jre`) resolve correctly
> in your environment — swap the Java version tag if your registry doesn't have
> a `25` variant yet.



## Extending it later

The contact form currently sends a plain-text email with name/email/message.
Natural next steps if you want to build on it: basic spam protection (a honeypot
field, or rate-limiting by IP), saving submissions to a database as well as
emailing them, or switching to a transactional email API (SendGrid, Resend,
Postmark) instead of Gmail SMTP if you outgrow Gmail's sending limits.
