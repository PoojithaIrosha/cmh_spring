package lk.cmh.web.cmh.seller;

import lk.cmh.web.cmh.mail.MailServiceProvider;
import lk.cmh.web.cmh.mail.OrderStatusUpdateEmail;
import lk.cmh.web.cmh.orders.OrderStatus;
import lk.cmh.web.cmh.orders.items.OrderHistoryItems;
import lk.cmh.web.cmh.orders.items.OrderHistoryItemsRepository;
import lk.cmh.web.cmh.user.User;
import lk.cmh.web.cmh.user.UserDetailsImpl;
import lk.cmh.web.cmh.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SellerService {

    private final UserRepository userRepository;
    private final OrderHistoryItemsRepository itemsRepository;
    private final JavaMailSender javaMailSender;

    public List<SellerOrderDto> getOrders(UserDetailsImpl userDetails) {
        User seller = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));

        List<SellerOrderDto> orderDtos = new ArrayList<>();

        List<OrderHistoryItems> orderHistoryItems = itemsRepository.findAllByProductInOrderByIdDesc(seller.products);
        orderHistoryItems.forEach(ohi -> {

            SellerOrderDto sellerOrderDto = SellerOrderDto.builder()
                    .id(ohi.getOrderHistory().getId())
                    .total(ohi.getOrderHistory().getTotal())
                    .purchased_at(ohi.getOrderHistory().getPurchased_at())
                    .orderItems(new ArrayList<>())
                    .build();

            SellerOrderItemDto itemDto = SellerOrderItemDto.builder()
                    .id(ohi.getId())
                    .product(ohi.getProduct())
                    .quantity(ohi.getQuantity())
                    .price(ohi.getPrice())
                    .status(ohi.getStatus())
                    .productColor(ohi.getProductColor())
                    .productSize(ohi.getProductSize())
                    .build();

            sellerOrderDto.orderItems().add(itemDto);

            if (ohi.getOrderHistory().getOrderHistoryItems().size() > 1) {

                List<OrderHistoryItems> collect = ohi.getOrderHistory().getOrderHistoryItems().stream().filter(ohi1 -> (ohi1.getProduct().getId() != ohi.getProduct().getId() && ohi1.getProduct().getSeller().getId() == ohi.getProduct().getSeller().getId())).toList();

                collect.forEach(orderHistoryItems1 -> {
                    SellerOrderItemDto idto = SellerOrderItemDto.builder()
                            .id(orderHistoryItems1.getId())
                            .product(orderHistoryItems1.getProduct())
                            .quantity(orderHistoryItems1.getQuantity())
                            .price(orderHistoryItems1.getPrice())
                            .status(orderHistoryItems1.getStatus())
                            .productColor(orderHistoryItems1.getProductColor())
                            .productSize(orderHistoryItems1.getProductSize())
                            .build();

                    sellerOrderDto.orderItems().add(idto);
                });
            }

            orderDtos.add(sellerOrderDto);
        });

        return orderDtos;
    }

    public void confirmOrder(long orderId, OrderStatus orderStatus, UserDetailsImpl userDetails) {
        User seller = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));

        itemsRepository.findById(orderId).ifPresent(orderHistoryItems -> {

            if (seller.getId() == orderHistoryItems.getProduct().getSeller().getId()) {

                orderHistoryItems.setStatus(orderStatus);
                itemsRepository.save(orderHistoryItems);

                OrderStatusUpdateEmail email = new OrderStatusUpdateEmail(javaMailSender, orderHistoryItems.getOrderHistory().getUser().getEmail(), orderHistoryItems.getOrderHistory().getUser().getFirstName(), orderHistoryItems.getOrderHistory().getId(), orderHistoryItems.getProduct().getName(), orderStatus.toString());
                MailServiceProvider.getInstance().sendMail(email);
            } else {
                throw new RuntimeException("You are not the seller of this product");
            }
        });
    }
}
