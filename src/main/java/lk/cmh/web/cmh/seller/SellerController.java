package lk.cmh.web.cmh.seller;

import lk.cmh.web.cmh.orders.OrderHistoryRepository;
import lk.cmh.web.cmh.orders.OrderStatus;
import lk.cmh.web.cmh.orders.items.OrderHistoryItems;
import lk.cmh.web.cmh.orders.items.OrderHistoryItemsRepository;
import lk.cmh.web.cmh.product.Product;
import lk.cmh.web.cmh.user.User;
import lk.cmh.web.cmh.user.UserDetailsImpl;
import lk.cmh.web.cmh.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/seller")
@Slf4j
public class SellerController {

    private final SellerService sellerService;
    @GetMapping("/orders")
    public List<SellerOrderDto> getOrders(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return sellerService.getOrders(userDetails);
    }

    @PutMapping("/orders/{id}")
    @Secured("ROLE_SELLER")
    public void confirmOrder(@PathVariable long id, @RequestParam OrderStatus status, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        sellerService.confirmOrder(id, status, userDetails);
    }

}
