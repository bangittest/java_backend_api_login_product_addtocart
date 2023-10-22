package com.ecommerce.demo.reponsitory;

import com.ecommerce.demo.model.User;
import com.ecommerce.demo.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishListRepository extends JpaRepository<WishList,Integer> {
    List<WishList>findAllByUserOrderByCreatedDate(User user);
}
