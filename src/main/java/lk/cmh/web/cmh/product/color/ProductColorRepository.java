package lk.cmh.web.cmh.product.color;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductColorRepository extends JpaRepository<ProductColor, Long> {
    Optional<ProductColor> findByName(String name);
}
