package lk.cmh.web.cmh.orders;

import lk.cmh.web.cmh.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderHistoryController {

    private final OrderHistoryService orderHistoryService;

    @GetMapping
    public Iterable<OrderHistory> getAllOrders(@AuthenticationPrincipal UserDetailsImpl user) {
        return orderHistoryService.getAllOrders(user);
    }

    @GetMapping("/{id}")
    public OrderHistory getOrder(@PathVariable Long id) {
        return orderHistoryService.getOrder(id);
    }

    @PostMapping
    public OrderHistory addOrder(@RequestBody OrderDto order, @AuthenticationPrincipal UserDetailsImpl user) {
        return orderHistoryService.addOrder(order, user);
    }

}
