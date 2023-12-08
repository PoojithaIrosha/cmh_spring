package lk.cmh.web.cmh.product.reviews;

import lk.cmh.web.cmh.product.ProductRepository;
import lk.cmh.web.cmh.user.UserDetailsImpl;
import lk.cmh.web.cmh.user.UserDetailsServiceImpl;
import lk.cmh.web.cmh.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReviewsService {
    private final ReviewsRepository reviewsRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public Reviews saveReview(UserDetailsImpl user, ReviewsDto reviews) {
        Reviews build = Reviews.builder()
                .review(reviews.review())
                .rating(reviews.rating())
                .product(productRepository.findById(reviews.productId()).orElseThrow(() -> new RuntimeException("Product not found")))
                .user(userRepository.findByEmail(user.getUsername()).orElseThrow(() -> new RuntimeException("User not found")))
                .build();
        return reviewsRepository.save(build);
    }

    public Map<String, String> deleteReview(Long reviews) {
        Reviews rv = reviewsRepository.findById(reviews).orElseThrow(() -> new RuntimeException("Review not found"));
        reviewsRepository.delete(rv);
        return Map.of("message", "Review deleted");
    }
}
