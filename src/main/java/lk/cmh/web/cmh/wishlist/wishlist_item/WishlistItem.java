package lk.cmh.web.cmh.wishlist.wishlist_item;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lk.cmh.web.cmh.cart.Cart;
import lk.cmh.web.cmh.product.Product;
import lk.cmh.web.cmh.wishlist.Wishlist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "wishlist_item")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class WishlistItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonManagedReference
    private Product product;

    @ManyToOne
    @JoinColumn(name = "wishlist_id", nullable = false)
    @JsonBackReference
    private Wishlist wishlist;

}
