package lk.cmh.web.cmh.orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {

    Iterable<OrderHistory> findAllByUser_EmailOrderByIdDesc(String email);

}
