package lk.cmh.web.cmh.projects;

import lombok.Builder;

import java.util.Set;

public record ProjectDto(
        String name,
        String description,
        String category,
        String skills,
        Double budget,
        String duration,
        Set<ProjectImageDto> projectImages
) {
}

@Builder
record ProjectImageDto(String url)  {

}
