package com.ecommerce.demo.reponsitory;

import com.ecommerce.demo.model.AuthenticationToken;
import com.ecommerce.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<AuthenticationToken,Integer> {

    AuthenticationToken findByUser(User user);
    AuthenticationToken findByToken(String user);
}
