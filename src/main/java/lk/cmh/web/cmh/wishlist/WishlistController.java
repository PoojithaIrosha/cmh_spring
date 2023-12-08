package lk.cmh.web.cmh.wishlist;

import lk.cmh.web.cmh.cart.Cart;
import lk.cmh.web.cmh.cart.cart_item.CartItemDto;
import lk.cmh.web.cmh.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/v1/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @GetMapping
    public Wishlist getWishlist(@AuthenticationPrincipal UserDetailsImpl user) {
        return wishlistService.getWishlist(user);
    }

    @PostMapping("/{productId}")
    public ResponseEntity<Map<String, String>> addToWishlist(@AuthenticationPrincipal UserDetailsImpl user, @PathVariable("productId") Long productId) {
        return ResponseEntity.ok(wishlistService.addToWishlist(user, productId));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Map<String, String>> deleteFromCart(@AuthenticationPrincipal UserDetailsImpl user, @PathVariable("productId") Long productId) {
        return ResponseEntity.ok(wishlistService.deleteFromWishlist(user, productId));
    }

}
