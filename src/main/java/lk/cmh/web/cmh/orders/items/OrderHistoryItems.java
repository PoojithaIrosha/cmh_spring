package lk.cmh.web.cmh.orders.items;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lk.cmh.web.cmh.orders.OrderHistory;
import lk.cmh.web.cmh.orders.OrderStatus;
import lk.cmh.web.cmh.product.Product;
import lk.cmh.web.cmh.product.color.ProductColor;
import lk.cmh.web.cmh.product.size.ProductSize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_history_items")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderHistoryItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private double price;

    private int quantity;

    @ManyToOne
    @JoinColumn(name = "product_size_id")
    private ProductSize productSize;

    @ManyToOne
    @JoinColumn(name = "product_color_id")
    private ProductColor productColor;

    @ManyToOne
    @JoinColumn(name = "order_history_id")
    @JsonIgnore
    private OrderHistory orderHistory;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;
}
