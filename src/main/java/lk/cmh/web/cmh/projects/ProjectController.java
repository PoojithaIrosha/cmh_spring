package lk.cmh.web.cmh.projects;

import lk.cmh.web.cmh.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public List<Project> getProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    public Project getProject(@PathVariable Long id) {
        return projectService.getProject(id);
    }

    @GetMapping("/enabled")
    public List<Project> getEnabledProjects() {
        return projectService.getEnabledProjects();
    }

    @GetMapping("/search")
    public List<Project> searchProjects(@RequestParam String keyword) {
        return projectService.searchProjects(keyword);
    }

    @GetMapping("/seller")
    @Secured({"ROLE_SELLER"})
    public List<Project> getProjectsBySeller(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return projectService.getProjectsBySeller(userDetails);
    }

    @PostMapping("/create")
    @Secured("ROLE_SELLER")
    public Project createProject(@RequestBody ProjectDto projectDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return projectService.createProject(projectDto, userDetails);
    }

    @PutMapping("/update/{id}")
    @Secured("ROLE_SELLER")
    public Project updateProject(@PathVariable Long id, @RequestBody ProjectDto projectDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return projectService.updateProject(id, projectDto, userDetails);
    }

    @PutMapping("/{id}/enable")
    @Secured({"ROLE_ADMIN", "ROLE_SELLER"})
    public Map<String, String> enableProject(@PathVariable Long id) {
        return projectService.enableProject(id);
    }

}
