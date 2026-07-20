# Portfolio (Vercel diagnostic build — mail/contact form removed)

This is a stripped-down copy of the full Spring Boot portfolio, with **every
trace of the mail/contact-form feature removed**, for one purpose only:
isolating whether the 500 errors on Vercel are caused by the mail setup or
by something else entirely.

## What's different from the full project

Removed completely:
- `spring-boot-starter-mail` dependency (`pom.xml`)
- `ContactMailService.java` (deleted)
- The `/contact` POST endpoint and mail service wiring in `PortfolioController.java`
- All `spring.mail.*` and `portfolio.contact.recipient` lines in `application.properties`
- The contact form HTML, and its success/error banners, in `templates/index.html`

Everything else — the Thymeleaf `th:each` project cards, the CSS, the JS,
`Dockerfile.vercel`, the `server.port=${PORT:80}` fix, the `--platform=linux/amd64`
pin — is unchanged from the full project.

## How to use this

**Don't replace your real project with this.** Deploy this as a **separate,
throwaway Vercel project** just to get a clean answer, then come back to the
real one once we know what's happening:

1. Push this folder to a **new**, separate GitHub repo (don't overwrite your
   real `Portfolio-SpringBoot` repo)
2. Import that new repo into Vercel as a **new** project
3. No environment variables needed this time — there's nothing left that
   reads any
4. Deploy, then visit the URL

## Reading the result

- **Loads fine** → the mail setup was the problem in the full project. Next
  step: figure out why `MAIL_USERNAME`/`MAIL_APP_PASSWORD` aren't reaching the
  container the way they do locally.
- **Still 500s** → mail was never the issue. The problem is something about
  the container/Vercel project itself, independent of any app code — worth
  taking straight to Vercel's support/community with this exact repro, since
  it means even a "vanilla" Spring Boot page fails the same way.

## Running it locally first (recommended before deploying)

```bash
docker build -f Dockerfile.vercel -t portfolio-nomail .
docker run -p 9090:9090 -e PORT=9090 portfolio-nomail
```

Visit `http://localhost:9090` — should look identical to the full site,
minus the contact form section at the bottom.
