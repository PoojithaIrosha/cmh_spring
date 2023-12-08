package lk.cmh.web.cmh.projects;

import lk.cmh.web.cmh.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findAllByEnabledTrue();

    List<Project> findAllBySeller_Id(Long sellerId);

    List<Project> findAllByNameContainingIgnoreCase(String keyword);
}
