package lk.cmh.web.cmh.product;

import lk.cmh.web.cmh.product.product_image.ProductImage;
import lombok.Builder;
import lombok.ToString;

import java.util.Set;

@Builder
public record ProductReqDto(
        String name,
        String description,
        Double price,
        Condition productCondition,
        int quantity,
        String category,
        Set<ProductImageDto> productImages,
        String colors,
        String sizes
) {
}

@Builder
record ProductImageDto(String url)  {

}