package com.tasnim.portfolio;
// serves the portfolio page and feeds real project data into the Thymeleaf template
// (mail/contact-form handling removed for this test build)

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PortfolioController {

    @GetMapping("/")
    public String indexPage(Model model) {
        model.addAttribute("projects", buildProjects());
        return "index";
    }

    private List<Project> buildProjects() {
        return List.of(
            new Project(
                "PawPal",
                "Desktop App",
                "/assets/img/projects/pawpal.jpg",
                "A JavaFX desktop marketplace for buying and selling pets, with dedicated "
                    + "dashboards for buyers, sellers, and admins — pet listings, orders, "
                    + "ratings, comments, and store management backed by MySQL.",
                List.of("Java", "JavaFX", "MySQL", "FXML"),
                "https://github.com/tasnim0000000/PawPal"
            ),
            new Project(
                "StudyFlow",
                "Full-Stack Web App",
                "/assets/img/projects/studyflow.jpg",
                "A student productivity platform bringing tasks, study planning, notes, "
                    + "assignments, goals, and calendar into one dashboard, with Kanban "
                    + "boards, analytics charts, and global search across everything.",
                List.of("React", "TypeScript", "Tailwind CSS", "Supabase", "Recharts"),
                "https://github.com/tasnim0000000/StudyFlow"
            ),
            new Project(
                "Cox's Canvas",
                "Computer Graphics",
                "/assets/img/projects/coxs-canvas.jpg",
                "A real-time OpenGL & GLUT simulation of Laboni Point, Cox's Bazar — "
                    + "animated ocean waves, a sunset-to-night lighting transition, a "
                    + "rotating Ferris wheel, and free-roam camera controls.",
                List.of("C++", "OpenGL", "GLUT", "Computer Graphics"),
                "https://github.com/tasnim0000000/Cox-s-Canvas"
            )
        );
    }

}
