package lk.cmh.web.cmh.product;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lk.cmh.web.cmh.product.category.Category;
import lk.cmh.web.cmh.product.color.ProductColor;
import lk.cmh.web.cmh.product.product_image.ProductImage;
import lk.cmh.web.cmh.product.reviews.Reviews;
import lk.cmh.web.cmh.product.size.ProductSize;
import lk.cmh.web.cmh.user.User;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode(exclude = {"productImages", "category", "seller", "reviews", "productColors", "productSizes"})
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Double price;

    @Enumerated(EnumType.STRING)
    private Condition productCondition;

    private int quantity;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> productImages = new ArrayList<>();

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "seller_id", nullable = false)
    @JsonIgnoreProperties({"products", "projects"})
    private User seller;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Reviews> reviews = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    private List<ProductColor> productColors;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<ProductSize> productSizes;

    @Column(columnDefinition = "boolean default false")
    private boolean isDeleted;

}
