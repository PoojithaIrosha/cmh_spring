package lk.cmh.web.cmh.wishlist;

import lk.cmh.web.cmh.cart.Cart;
import lk.cmh.web.cmh.cart.cart_item.CartItem;
import lk.cmh.web.cmh.product.Product;
import lk.cmh.web.cmh.product.ProductRepository;
import lk.cmh.web.cmh.user.User;
import lk.cmh.web.cmh.user.UserDetailsImpl;
import lk.cmh.web.cmh.user.UserRepository;
import lk.cmh.web.cmh.wishlist.wishlist_item.WishlistItem;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;


    public Wishlist getWishlist(UserDetailsImpl userDetails) {
        return wishlistRepository.findWishlistByUser_Email(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("Wishlist not found"));
    }

    public Map<String, String> addToWishlist(UserDetailsImpl userDetails, Long productId) {
        Wishlist wishlist = wishlistRepository.findWishlistByUser_Email(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("Wishlist not found"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

        wishlist.getWishlistItems().stream().filter(item -> item.getProduct().getId() == productId).findFirst().ifPresentOrElse(
                wishlistItem -> {
                    throw new RuntimeException("Product already in wishlist");
                },
                () -> {
                    wishlist.getWishlistItems().add(WishlistItem.builder()
                            .product(product)
                            .wishlist(wishlist)
                            .build());
                }
        );
        wishlistRepository.save(wishlist);
        return Map.of("message", "Added to wishlist");
    }

    public Map<String, String> deleteFromWishlist(UserDetailsImpl userDetails, Long productId) {
        Wishlist wishlist = wishlistRepository.findWishlistByUser_Email(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("Wishlist not found"));
        WishlistItem wishlist1Item = wishlist.getWishlistItems().stream().filter(item -> item.getProduct().getId() == productId).findFirst().orElseThrow(() -> new RuntimeException("Wishlist item not found"));
        wishlist.getWishlistItems().remove(wishlist1Item);
        wishlistRepository.save(wishlist);
        return Map.of("message", "Deleted from wishlist");
    }
}
