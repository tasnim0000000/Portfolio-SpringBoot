package com.tasnim.portfolio;

import java.util.List;

/**
 * Plain data holder passed from the controller into the Thymeleaf template.
 * This is what makes the projects section a real server-rendered template
 * (th:each over this list) instead of static, hand-written HTML cards.
 */
public class Project {

    private final String name;
    private final String statusChip;
    private final String coverImage;
    private final String description;
    private final List<String> tags;
    private final String githubUrl;

    public Project(String name, String statusChip, String coverImage,
                    String description, List<String> tags, String githubUrl) {
        this.name = name;
        this.statusChip = statusChip;
        this.coverImage = coverImage;
        this.description = description;
        this.tags = tags;
        this.githubUrl = githubUrl;
    }

    public String getName() { return name; }
    public String getStatusChip() { return statusChip; }
    public String getCoverImage() { return coverImage; }
    public String getDescription() { return description; }
    public List<String> getTags() { return tags; }
    public String getGithubUrl() { return githubUrl; }
}
