package lk.cmh.web.cmh.cart;

import lk.cmh.web.cmh.cart.cart_item.CartItem;
import lk.cmh.web.cmh.cart.cart_item.CartItemDto;
import lk.cmh.web.cmh.cart.cart_item.CartItemUpdateDto;
import lk.cmh.web.cmh.user.User;
import lk.cmh.web.cmh.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<Cart> getCart(@AuthenticationPrincipal UserDetailsImpl user) {
        return ResponseEntity.ok(cartService.getCart(user));
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> addToCart(@AuthenticationPrincipal UserDetailsImpl user, @RequestBody CartItemDto cartItem) {
        return ResponseEntity.ok(cartService.addToCart(user, cartItem));
    }

    @PutMapping
    public ResponseEntity<Map<String, String>> updateCart(@AuthenticationPrincipal UserDetailsImpl user, @RequestBody CartItemUpdateDto cartItem) {
        return ResponseEntity.ok(cartService.updateCart(user, cartItem));
    }

    @DeleteMapping("/{cartItem}")
    public ResponseEntity<Map<String, String>> deleteFromCart(@AuthenticationPrincipal UserDetailsImpl user, @PathVariable("cartItem") Long cartItem) {
        return ResponseEntity.ok(cartService.deleteFromCart(user, cartItem));
    }

}
