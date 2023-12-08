package lk.cmh.web.cmh.seller;

import lk.cmh.web.cmh.orders.OrderStatus;
import lk.cmh.web.cmh.product.Product;
import lk.cmh.web.cmh.product.color.ProductColor;
import lk.cmh.web.cmh.product.size.ProductSize;
import lombok.Builder;

@Builder
public record SellerOrderItemDto(
        long id,
        Product product,
        double price,
        int quantity,
        ProductSize productSize,
        ProductColor productColor,
        OrderStatus status
) {
}
