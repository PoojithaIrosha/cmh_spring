package lk.cmh.web.cmh.cart;

import lk.cmh.web.cmh.cart.cart_item.CartItem;
import lk.cmh.web.cmh.cart.cart_item.CartItemDto;
import lk.cmh.web.cmh.cart.cart_item.CartItemUpdateDto;
import lk.cmh.web.cmh.product.Product;
import lk.cmh.web.cmh.product.ProductRepository;
import lk.cmh.web.cmh.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public Cart getCart(UserDetailsImpl user) {
        return cartRepository.findCartByUser_Email(user.getUsername()).orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    public Map<String, String> addToCart(UserDetailsImpl user, CartItemDto cartItem) {
        Cart cart = cartRepository.findCartByUser_Email(user.getUsername()).orElseThrow(() -> new RuntimeException("Cart not found"));
        Product product = productRepository.findByIdAndIsDeletedFalse(cartItem.productId()).orElseThrow(() -> new RuntimeException("Product not found"));

        cart.getCartItems().stream().filter(cartItem1 -> cartItem.productId().equals(cartItem1.getProduct().getId())
                && (cartItem.sizeId() == 0 || cartItem.sizeId() == cartItem1.getProductSize().getId())
                && (cartItem.colorId() == 0 || cartItem.colorId() == cartItem1.getProductColor().getId())).findFirst().ifPresentOrElse(cartItem1 -> {
            cartItem1.setQuantity(cartItem1.getQuantity() + cartItem.quantity());
        }, () -> {
            CartItem build = CartItem.builder()
                    .product(product)
                    .quantity(cartItem.quantity())
                    .price(product.getPrice())
                    .cart(cart)
                    .build();

            if (cartItem.sizeId() != 0) {
                build.setProductSize(product.getProductSizes().stream().filter(size -> size.getId() == cartItem.sizeId()).findFirst().orElseThrow(() -> new RuntimeException("Size not found")));
            }

            if (cartItem.colorId() != 0) {
                build.setProductColor(product.getProductColors().stream().filter(color -> color.getId() == cartItem.colorId()).findFirst().orElseThrow(() -> new RuntimeException("Color not found")));
            }

            cart.getCartItems().add(build);
        });

        cartRepository.save(cart);
        return Map.of("message", "Added to cart");
    }

    public Map<String, String> deleteFromCart(UserDetailsImpl user, Long cartItemId) {
        Cart cart = cartRepository.findCartByUser_Email(user.getUsername()).orElseThrow(() -> new RuntimeException("Cart not found"));
        CartItem cartItem = cart.getCartItems().stream().filter(item -> item.getId() == cartItemId).findFirst().orElseThrow(() -> new RuntimeException("Cart item not found"));
        cart.getCartItems().remove(cartItem);
        cartRepository.save(cart);
        return Map.of("message", "Deleted from cart");
    }

    public Map<String, String> updateCart(UserDetailsImpl user, CartItemUpdateDto cartItem) {
        Cart cart = cartRepository.findCartByUser_Email(user.getUsername()).orElseThrow(() -> new RuntimeException("Cart not found"));
        CartItem cartItemToUpdate = cart.getCartItems().stream().filter(item -> item.getId() == cartItem.cartItemId()).findFirst().orElseThrow(() -> new RuntimeException("Cart item not found"));
        cartItemToUpdate.setQuantity(cartItem.quantity());
        cartRepository.save(cart);
        return Map.of("message", "Cart Updated");
    }
}
