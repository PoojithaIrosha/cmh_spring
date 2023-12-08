package lk.cmh.web.cmh.orders;

import lk.cmh.web.cmh.orders.items.OrderItemDto;
import lombok.Builder;

import java.util.List;

@Builder
public record OrderDto(double total, List<OrderItemDto> orderItems) {

}
