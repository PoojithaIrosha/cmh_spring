package lk.cmh.web.cmh.product;

import lk.cmh.web.cmh.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public Page<Product> getAll(@RequestParam int page, @RequestParam int size) {
        return productService.getAll(page, size);
    }

    @GetMapping("/top")
    public ResponseEntity<List<Product>> getTopProducts() {
        return ResponseEntity.ok(productService.getTopProduct());
    }

    @GetMapping("{id}")
    public ResponseEntity<Product> getProduct(@PathVariable long id) {
        return ResponseEntity.ok(productService.getProduct(id));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Product>> searchProducts(@RequestParam int page, @RequestParam int size, @RequestParam String text, @RequestParam String priceMin, @RequestParam String priceMax) {
        return ResponseEntity.ok(productService.searchProducts(page, size, text, priceMin, priceMax));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<Product>> getProductsByCategory(@PathVariable long categoryId, @RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(productService.getProductsByCategory(categoryId, page, size));
    }

    @GetMapping("/seller")
    @Secured({"ROLE_SELLER"})
    public ResponseEntity<List<Product>> getProductsBySeller(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(productService.getProductsBySeller(userDetails));
    }

    @GetMapping("/seller/enabled/{id}")
    public ResponseEntity<List<Product>> getEnabledProductsBySeller(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getEnabledProductsBySeller(id));
    }

    @PostMapping
    @Secured({"ROLE_SELLER"})
    public ResponseEntity<Product> saveProduct(@RequestBody ProductReqDto product, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(productService.saveProduct(product, userDetails));
    }

    @PutMapping("{id}")
    @Secured({"ROLE_SELLER"})
    public ResponseEntity<Product> updateProduct(@PathVariable long id, @RequestBody ProductReqDto product) {
        return ResponseEntity.ok(productService.updateProduct(id, product));
    }

    @PutMapping("enable/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_SELLER"})
    public ResponseEntity<Map<String, String>> enableProduct(@PathVariable long id) {
        return ResponseEntity.ok(productService.enableProduct(id));
    }

    @DeleteMapping("{id}")
    @Secured({"ROLE_ADMIN", "ROLE_SELLER"})
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable long id) {
        return ResponseEntity.ok(productService.deleteProduct(id));
    }
}