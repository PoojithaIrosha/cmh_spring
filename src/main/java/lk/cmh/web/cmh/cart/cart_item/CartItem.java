package lk.cmh.web.cmh.cart.cart_item;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lk.cmh.web.cmh.cart.Cart;
import lk.cmh.web.cmh.product.Product;
import lk.cmh.web.cmh.product.color.ProductColor;
import lk.cmh.web.cmh.product.size.ProductSize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart_item")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnoreProperties("seller")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "product_color_id")
    @JsonIgnoreProperties({"products", "cartItems"})
    private ProductColor productColor;

    @ManyToOne
    @JoinColumn(name = "product_size_id")
    @JsonIgnoreProperties({"product", "cartItems"})
    private ProductSize productSize;

    private int quantity;

    private double price;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    @JsonBackReference
    private Cart cart;

}
