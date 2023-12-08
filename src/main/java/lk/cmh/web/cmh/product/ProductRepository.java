package lk.cmh.web.cmh.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByIdAndIsDeletedFalse(Long id);

    Page<Product> findAllByIsDeletedFalse(Pageable pageable);

    Page<Product> findByNameContainingAndIsDeletedFalse(String text, Pageable pageable);

    Page<Product> findByNameContainingAndPriceBetweenAndIsDeletedFalse(String text, Double priceMin, Double priceMax, Pageable pageable);

    Page<Product> findByNameContainingAndPriceGreaterThanEqualAndIsDeletedFalse(String text, Double priceMin, Pageable pageable);

    Page<Product> findByNameContainingAndPriceLessThanEqualAndIsDeletedFalse(String text, Double priceMax, Pageable pageable);

    Page<Product> findAllByCategory_IdAndIsDeletedFalse(long category, Pageable pageable);

    @Query("select p from Product p where p.isDeleted = false order by p.id desc limit 5")
    List<Product> findFive();

    List<Product> findAllBySeller_Id(Long sellerId);
    List<Product> findAllBySeller_IdAndIsDeletedFalse(Long sellerId);
}
