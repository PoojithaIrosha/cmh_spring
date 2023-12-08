package lk.cmh.web.cmh.product.reviews;


import lk.cmh.web.cmh.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/v1/products/reviews")
@RequiredArgsConstructor
public class ReviewsController {

    private final ReviewsService reviewsService;

    @PostMapping
    public ResponseEntity<Reviews> saveReview(@AuthenticationPrincipal UserDetailsImpl user, @RequestBody ReviewsDto reviews) {
        return ResponseEntity.ok(reviewsService.saveReview(user, reviews));
    }

    @DeleteMapping("{reviewId}")
    public ResponseEntity<Map<String, String>> deleteReview(@PathVariable("reviewId") Long reviews) {
        return ResponseEntity.ok(reviewsService.deleteReview(reviews));
    }

}
