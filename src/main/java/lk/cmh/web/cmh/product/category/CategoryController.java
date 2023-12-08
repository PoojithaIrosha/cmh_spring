package lk.cmh.web.cmh.product.category;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("{id}")
    public ResponseEntity<Category> getCategory(@PathVariable("id") Long id) {
        return ResponseEntity.ok(categoryService.getCategory(id));
    }

    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        return ResponseEntity.ok(categoryService.createCategory(category));
    }

    @PutMapping("{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Category> updateCategory(@PathVariable("id") Long id, @RequestBody Category category) {
        return ResponseEntity.ok(categoryService.updateCategory(id, category));
    }

    @DeleteMapping("{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Map<String, String>> deleteCategory(@PathVariable("id") Long id) {
        return ResponseEntity.ok(categoryService.deleteCategory(id));
    }

}
