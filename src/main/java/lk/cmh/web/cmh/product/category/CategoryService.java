package lk.cmh.web.cmh.product.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, Category category) {
        Category c = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        c.setName(category.getName());
        c.setImage(category.getImage());
        return categoryRepository.save(c);
    }

    public Category getCategory(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public Map<String, String> deleteCategory(Long id) {
        boolean b = categoryRepository.existsById(id);
        if (b) {
            categoryRepository.deleteById(id);
            return Map.of("message", "Category deleted successfully");
        } else {
            throw new RuntimeException("Category not found");
        }
    }
}
