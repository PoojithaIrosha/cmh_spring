package lk.cmh.web.cmh.projects;

import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.StorageClient;
import lk.cmh.web.cmh.projects.images.ProjectImages;
import lk.cmh.web.cmh.user.User;
import lk.cmh.web.cmh.user.UserDetailsImpl;
import lk.cmh.web.cmh.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final FirebaseApp firebaseApp;

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public List<Project> getEnabledProjects() {
        return projectRepository.findAllByEnabledTrue();
    }

    public Project createProject(ProjectDto projectDto, UserDetailsImpl userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Project project = Project.builder()
                .name(projectDto.name())
                .description(projectDto.description())
                .category(projectDto.category())
                .skills(projectDto.skills())
                .budget(projectDto.budget())
                .duration(projectDto.duration())
                .seller(user)
                .enabled(true)
                .build();
        project.setProjectImages(projectDto.projectImages().stream().map(projectImageDto -> ProjectImages.builder()
                .url(projectImageDto.url())
                .project(project)
                .build()).toList());

        return projectRepository.save(project);
    }

    public Project updateProject(Long id, ProjectDto projectDto, UserDetailsImpl userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Project project = projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found"));

        if (!project.getSeller().getId().equals(user.getId())) {
            throw new RuntimeException("You are not allowed to update this project");
        }

        project.setName(projectDto.name());
        project.setDescription(projectDto.description());
        project.setCategory(projectDto.category());
        project.setSkills(projectDto.skills());
        project.setBudget(projectDto.budget());
        project.setDuration(projectDto.duration());

        if(projectDto.projectImages() != null) {
            project.getProjectImages().removeIf(productImage -> {
                String imageUrl = productImage.getUrl();
                boolean shouldRemove = !projectDto.projectImages().stream().map(ProjectImageDto::url)
                        .toList().contains(imageUrl);

                if (shouldRemove) {
                    StorageClient.getInstance(firebaseApp).bucket().get("projects/" + imageUrl).delete();
                }

                return shouldRemove;
            });

            List<ProjectImages> list = projectDto.projectImages().stream().map(dto -> ProjectImages.builder().url(dto.url()).project(project).build()).toList();
            project.getProjectImages().addAll(list);
        }

        return projectRepository.save(project);
    }

    public Project getProject(Long id) {
        return projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found"));
    }

    public List<Project> getProjectsBySeller(UserDetailsImpl userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return projectRepository.findAllBySeller_Id(user.getId());
    }

    public Map<String, String> enableProject(Long id) {
        projectRepository.findById(id).ifPresent(project -> {
            project.setEnabled(!project.isEnabled());
            projectRepository.save(project);
        });
        return Map.of("message", "Project status changed");
    }

    public List<Project> searchProjects(String keyword) {
        return projectRepository.findAllByNameContainingIgnoreCase(keyword);
    }
}
