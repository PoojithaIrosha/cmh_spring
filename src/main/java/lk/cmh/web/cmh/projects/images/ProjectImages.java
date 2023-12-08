package lk.cmh.web.cmh.projects.images;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lk.cmh.web.cmh.product.Product;
import lk.cmh.web.cmh.projects.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "project_images")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProjectImages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String url;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "project_id")
    @JsonIgnore
    private Project project;
}
