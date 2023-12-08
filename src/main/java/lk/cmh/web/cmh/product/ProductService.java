package lk.cmh.web.cmh.product;

import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.StorageClient;
import jakarta.transaction.Transactional;
import lk.cmh.web.cmh.product.category.Category;
import lk.cmh.web.cmh.product.category.CategoryRepository;
import lk.cmh.web.cmh.product.color.ProductColor;
import lk.cmh.web.cmh.product.color.ProductColorRepository;
import lk.cmh.web.cmh.product.product_image.ProductImage;
import lk.cmh.web.cmh.product.size.ProductSize;
import lk.cmh.web.cmh.product.size.ProductSizeRepository;
import lk.cmh.web.cmh.user.UserDetailsImpl;
import lk.cmh.web.cmh.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ProductColorRepository productColorRepository;
    private final ProductSizeRepository productSizeRepository;
    private final FirebaseApp firebaseApp;

    public Page<Product> getAll(int page, int size) {
        return productRepository.findAllByIsDeletedFalse(PageRequest.of(page, size));
    }

    public Page<Product> searchProducts(int page, int size, String text, String priceMin, String priceMax) {
        Pageable pageable = PageRequest.of(page, size);
        if(!priceMin.isBlank() && priceMax.isBlank()) {
            return productRepository.findByNameContainingAndPriceGreaterThanEqualAndIsDeletedFalse(text, Double.parseDouble(priceMin), pageable);
        } else if(priceMin.isBlank() && !priceMax.isBlank()) {
            return productRepository.findByNameContainingAndPriceLessThanEqualAndIsDeletedFalse(text, Double.parseDouble(priceMax), pageable);
        } else if(!priceMin.isBlank()) {
            return productRepository.findByNameContainingAndPriceBetweenAndIsDeletedFalse(text, Double.parseDouble(priceMin), Double.parseDouble(priceMax), pageable);
        }else {
            return productRepository.findByNameContainingAndIsDeletedFalse(text, pageable);
        }
    }

    public Product getProduct(long id) {
        return productRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Transactional
    public Product saveProduct(ProductReqDto product, UserDetailsImpl userDetails) {
        Product p = Product.builder()
                .name(product.name())
                .description(product.description())
                .price(product.price())
                .productCondition(product.productCondition())
                .quantity(product.quantity())
                .category(categoryRepository.findByNameContaining(product.category()).orElse(Category.builder()
                        .name(product.category())
                        .build()))
                .seller(userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("Seller not found")))
                .build();

        if (product.colors() != null) {
            List<String> colors = Arrays.stream(product.colors().split(",")).toList();
            p.setProductColors(colors.stream().map(color -> productColorRepository.findByName(color.trim().toLowerCase()).orElse(ProductColor.builder().name(color.trim().toLowerCase()).build())).toList());
        }

        if (product.sizes() != null) {
            List<String> sizes = Arrays.stream(product.sizes().split(",")).toList();
            p.setProductSizes(sizes.stream().map(size -> productSizeRepository.findByName(size.trim().toLowerCase()).orElse(ProductSize.builder().name(size.trim().toLowerCase()).build())).toList());
        }

        List<ProductImage> list = product.productImages().stream().map(dto -> ProductImage.builder().url(dto.url()).product(p).build())
                .toList();
        p.setProductImages(list);

        return productRepository.save(p);
    }

    public Page<Product> getProductsByCategory(long category, int page, int size) {
        return productRepository.findAllByCategory_IdAndIsDeletedFalse(category, PageRequest.of(page, size));
    }

    @Transactional
    public Product updateProduct(long id, ProductReqDto product) {
        log.info("Updating product {}", product);

        Product p = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        p.setName(product.name());
        p.setDescription(product.description());
        p.setPrice(product.price());
        p.setProductCondition(product.productCondition());
        p.setQuantity(product.quantity());
        p.setCategory(categoryRepository.findByNameContaining(product.category()).orElseThrow(() -> new RuntimeException("Category not found")));

        if (product.colors() != null && !product.colors().isBlank()) {
            List<String> colorNames = Arrays.stream(product.colors().split(","))
                    .map(String::trim)
                    .map(String::toLowerCase)
                    .toList();

            p.getProductColors().removeIf(existingColor -> !colorNames.contains(existingColor.getName()));

            colorNames.stream()
                    .filter(newColorName -> p.getProductColors().stream().noneMatch(existingColor -> existingColor.getName().equals(newColorName)))
                    .map(newColorName -> productColorRepository.findByName(newColorName)
                            .orElse(ProductColor.builder().name(newColorName).build()))
                    .forEach(p.getProductColors()::add);
        }

        if (product.sizes() != null && !product.sizes().isBlank()) {
            List<String> sizeNames = Arrays.stream(product.sizes().split(","))
                    .map(String::trim)
                    .map(String::toLowerCase)
                    .toList();

            p.getProductSizes().removeIf(existingSize -> !sizeNames.contains(existingSize.getName()));

            sizeNames.stream()
                    .filter(newSizeName -> p.getProductSizes().stream().noneMatch(existingSize -> existingSize.getName().equals(newSizeName)))
                    .map(newSizeName -> productSizeRepository.findByName(newSizeName)
                            .orElse(ProductSize.builder().name(newSizeName).build()))
                    .forEach(p.getProductSizes()::add);
        }

        if(product.productImages() != null) {
            p.getProductImages().removeIf(productImage -> {
                String imageUrl = productImage.getUrl();
                boolean shouldRemove = !product.productImages().stream().map(ProductImageDto::url)
                        .toList().contains(imageUrl);

                if (shouldRemove) {
                    StorageClient.getInstance(firebaseApp).bucket().get("products/" + imageUrl).delete();
                }

                return shouldRemove;
            });

            List<ProductImage> list = product.productImages().stream().map(dto -> ProductImage.builder().url(dto.url()).product(p).build()).toList();
            p.getProductImages().addAll(list);
        }

        return productRepository.save(p);
    }

    public Map<String, String> deleteProduct(long id) {
        productRepository.deleteById(id);
        return Map.of("message", "Product deleted successfully");
    }

    public List<Product> getTopProduct() {
        return productRepository.findFive();
    }

    public List<Product> getProductsBySeller(UserDetailsImpl userDetails) {
        return productRepository.findAllBySeller_Id(userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found")).getId());
    }

    public Map<String, String> enableProduct(long id) {
        productRepository.findById(id).ifPresent(product -> {
            product.setDeleted(!product.isDeleted());
            productRepository.save(product);
        });

        return Map.of("message", "Product status changed");
    }

    public List<Product> getEnabledProductsBySeller(Long id) {
        return productRepository.findAllBySeller_IdAndIsDeletedFalse(userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found")).getId());
    }
}
