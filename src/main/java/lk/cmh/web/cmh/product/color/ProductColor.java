package lk.cmh.web.cmh.product.color;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lk.cmh.web.cmh.cart.cart_item.CartItem;
import lk.cmh.web.cmh.orders.OrderHistory;
import lk.cmh.web.cmh.orders.items.OrderHistoryItems;
import lk.cmh.web.cmh.product.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "product_colors")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductColor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "productColors", cascade = CascadeType.ALL)
    @JsonIgnore
    List<Product> products;

    @OneToMany(mappedBy = "productColor")
    @JsonIgnore
    private List<CartItem> cartItems;

    @OneToMany(mappedBy = "productColor")
    @JsonIgnore
    private List<OrderHistoryItems> orderHistoryItems;
}
