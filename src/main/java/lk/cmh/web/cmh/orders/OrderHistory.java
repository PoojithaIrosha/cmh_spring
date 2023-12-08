package lk.cmh.web.cmh.orders;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lk.cmh.web.cmh.orders.items.OrderHistoryItems;
import lk.cmh.web.cmh.product.Product;
import lk.cmh.web.cmh.product.color.ProductColor;
import lk.cmh.web.cmh.product.size.ProductSize;
import lk.cmh.web.cmh.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "order_history")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double total;

    @OneToMany(mappedBy = "orderHistory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderHistoryItems> orderHistoryItems;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private User user;

    @CreationTimestamp
    private Timestamp purchased_at;

}


