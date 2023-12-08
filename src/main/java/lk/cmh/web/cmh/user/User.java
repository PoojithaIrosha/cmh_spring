package lk.cmh.web.cmh.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lk.cmh.web.cmh.cart.Cart;
import lk.cmh.web.cmh.product.Product;
import lk.cmh.web.cmh.projects.Project;
import lk.cmh.web.cmh.wishlist.Wishlist;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString(exclude = {"products", "cart", "wishlist", "projects"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(unique = true, columnDefinition = "TEXT")
    private String sub;

    private String picture;

    @Column(columnDefinition = "TEXT", nullable = true)
    private String fcmToken;

    @OneToMany(mappedBy = "seller", fetch = FetchType.EAGER)
    @JsonManagedReference
    public Set<Product> products;

    @OneToMany(mappedBy = "seller", fetch = FetchType.EAGER)
    @JsonManagedReference
    public Set<Project> projects;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Cart cart;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Wishlist wishlist;

    @Embedded
    private UserAddress address;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    public User(String email, String password, UserRole role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(String firstName, String lastName, String email, String password, UserRole role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
