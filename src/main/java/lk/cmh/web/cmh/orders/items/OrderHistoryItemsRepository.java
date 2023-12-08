package lk.cmh.web.cmh.orders.items;

import lk.cmh.web.cmh.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface OrderHistoryItemsRepository extends JpaRepository<OrderHistoryItems, Long> {

    List<OrderHistoryItems> findAllByProductInOrderByIdDesc(Collection<Product> products);

}
