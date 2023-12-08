package lk.cmh.web.cmh.orders.items;

import lombok.Builder;

@Builder
public record OrderItemDto(
        long productId,
        int quantity,
        long sizeId,
        long colorId
) {
}
