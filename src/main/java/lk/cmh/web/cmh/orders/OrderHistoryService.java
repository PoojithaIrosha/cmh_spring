package lk.cmh.web.cmh.orders;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.FirestoreClient;
import lk.cmh.web.cmh.cart.CartRepository;
import lk.cmh.web.cmh.orders.items.OrderHistoryItems;
import lk.cmh.web.cmh.product.Product;
import lk.cmh.web.cmh.product.ProductRepository;
import lk.cmh.web.cmh.user.User;
import lk.cmh.web.cmh.user.UserDetailsImpl;
import lk.cmh.web.cmh.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderHistoryService {

    private final OrderHistoryRepository orderHistoryRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final FirebaseApp firebaseApp;

    public OrderHistory addOrder(OrderDto order, UserDetailsImpl userDetails) {

        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));

        OrderHistory orderHistory = OrderHistory.builder()
                .total(order.total())
                .orderHistoryItems(new ArrayList<>())
                .user(user).build();

        order.orderItems().forEach(orderItem -> {
            Product product = productRepository.findById(orderItem.productId()).orElseThrow(() -> new RuntimeException("Product not found"));
            OrderHistoryItems build = OrderHistoryItems.builder()
                    .product(product)
                    .price(product.getPrice())
                    .quantity(orderItem.quantity())
                    .orderHistory(orderHistory)
                    .status(OrderStatus.PENDING)
                    .build();

            if (orderItem.sizeId() != 0) {
                build.setProductSize(product.getProductSizes().stream().filter(size -> size.getId() == orderItem.sizeId()).findFirst().orElseThrow(() -> new RuntimeException("Size not found")));
            }

            if (orderItem.colorId() != 0) {
                build.setProductColor(product.getProductColors().stream().filter(color -> color.getId() == orderItem.colorId()).findFirst().orElseThrow(() -> new RuntimeException("Color not found")));
            }

            orderHistory.getOrderHistoryItems().add(build);
            product.setQuantity(product.getQuantity() - orderItem.quantity());
            productRepository.save(product);

            cartRepository.findCartByUser_Email(userDetails.getUsername()).ifPresent(cart -> {
                cart.getCartItems().removeIf(cartItem -> cartItem.getProduct().getId() == orderItem.productId() &&  (orderItem.sizeId() == 0 || (cartItem.getProductSize() != null && cartItem.getProductSize().getId().equals(orderItem.sizeId()))) &&
                        (orderItem.colorId() == 0 || (cartItem.getProductColor() != null && cartItem.getProductColor().getId().equals(orderItem.colorId()))));
                cartRepository.save(cart);
            });

            Firestore firestore = FirestoreClient.getFirestore(firebaseApp);
            CollectionReference notificationsCollection = firestore.collection("sellers").document(product.getSeller().getEmail()).collection("notifications");
            Map<String, Object> data = new HashMap<>();
            data.put("title", "New Order");
            data.put("body", "Hey, you just got an order!");
            data.put("time", String.valueOf(System.currentTimeMillis()));
            data.put("seen", false);
            notificationsCollection.add(data);
        });

        OrderHistory save = orderHistoryRepository.save(orderHistory);

        return save;
    }

    public Iterable<OrderHistory> getAllOrders(UserDetailsImpl user) {
        return orderHistoryRepository.findAllByUser_EmailOrderByIdDesc(user.getUsername());
    }

    public OrderHistory getOrder(Long id) {
        return orderHistoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
    }
}
