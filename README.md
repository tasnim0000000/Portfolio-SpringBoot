# Tasnim Ahmed — Portfolio (Spring Boot version)

The same portfolio site, repackaged as a Spring Boot app, following the exact
project layout from the `demo` starter (Maven + Spring Boot 4.1.0 + Thymeleaf +
Spring Web MVC).

## `static/` vs `templates/` — why this matters

Spring Boot treats these two folders completely differently:

- **`static/`** — files served *as-is*, no server-side processing. Spring just
  hands the raw file to the browser. If your whole page lives here, you're not
  really using Spring for the page at all — it's a plain HTML site parked
  inside a Spring Boot folder.
- **`templates/`** — files rendered by **Thymeleaf**, through a `@Controller`
  method, and can receive real data from Java before the HTML reaches the
  browser (loops, variables, conditionals).

This project keeps `index.html` in `templates/`, served by `PortfolioController`,
and — importantly — the **projects section is genuinely data-driven**, not just
hardcoded HTML with `th:` attributes sprinkled on top. `PortfolioController`
builds a `List<Project>` and passes it into the model; the template loops over
it with `th:each`:

```java
@GetMapping("/")
public String indexPage(Model model) {
    model.addAttribute("projects", buildProjects());
    return "index";
}
```

```html
<div class="project-card reveal" th:each="project, iterStat : ${projects}">
  <h3 class="project-name" th:text="${project.name}">Project Name</h3>
  ...
  <span class="skill-tag" th:each="tag : ${project.tags}" th:text="${tag}">Tag</span>
</div>
```

Add a 4th project by adding one more `new Project(...)` entry in
`PortfolioController.buildProjects()` — no HTML editing required. That's the
actual point of using a template engine instead of static files.

Only `css/`, `js/`, and `assets/img/` sit in `static/` — which is correct,
since those are genuinely static assets (no server-side logic needed to serve
a stylesheet or an image).

## Structure

```
portfolio-springboot/
├── pom.xml
├── mvnw / mvnw.cmd / .mvn/                    ← Maven wrapper (same as demo)
├── src/
│   ├── main/
│   │   ├── java/com/tasnim/portfolio/
│   │   │   ├── PortfolioApplication.java      ← main class (was DemoApplication)
│   │   │   ├── PortfolioController.java       ← maps "/" → index template, feeds project data
│   │   │   └── Project.java                   ← plain data class for the model
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

## Adding photos

Same as the static version — drop files into:
- `src/main/resources/static/assets/img/profile.jpg`
- `src/main/resources/static/assets/img/projects/pawpal.jpg`
- `src/main/resources/static/assets/img/projects/studyflow.jpg`
- `src/main/resources/static/assets/img/projects/coxs-canvas.jpg`

See the `PLACE_PHOTOS_HERE.md` files in those folders for sizing details.

## Extending it later

Your `demo` ZIP also had a `/contact` GET+POST route with a form (name, email,
id, password, message). This portfolio doesn't have a contact form yet — the
Contact section is just mailto/GitHub/LinkedIn links — but if you want a real
contact form backed by this controller pattern, that's a natural next step:
add a `ContactController` the same way `IndexController` did in the demo, and
a `contact.html` template.
