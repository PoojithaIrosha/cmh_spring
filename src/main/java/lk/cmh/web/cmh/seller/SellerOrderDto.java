package lk.cmh.web.cmh.seller;

import lombok.Builder;

import java.sql.Timestamp;
import java.util.List;

@Builder
public record SellerOrderDto(long id, double total, Timestamp purchased_at, List<SellerOrderItemDto> orderItems) {
}
