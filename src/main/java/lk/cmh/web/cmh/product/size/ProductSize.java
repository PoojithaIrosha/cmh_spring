package lk.cmh.web.cmh.product.size;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "product_sizes")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductSize {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "productSizes", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Product> product;

    @OneToMany(mappedBy = "productSize")
    @JsonIgnore
    private List<CartItem> cartItems;

    @OneToMany(mappedBy = "productSize")
    @JsonIgnore
    private List<OrderHistoryItems> orderHistoryItems;

}
