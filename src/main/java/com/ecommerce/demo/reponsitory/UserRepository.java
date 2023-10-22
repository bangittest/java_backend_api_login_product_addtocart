package com.ecommerce.demo.reponsitory;

import com.ecommerce.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {


        User findByEmail(String email);

        User findUserByEmail(String email);
}
