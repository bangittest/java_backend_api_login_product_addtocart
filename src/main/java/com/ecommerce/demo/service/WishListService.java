package com.ecommerce.demo.service;

import com.ecommerce.demo.dto.ProductDto;
import com.ecommerce.demo.model.User;
import com.ecommerce.demo.model.WishList;
import com.ecommerce.demo.reponsitory.WishListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WishListService {
    @Autowired
    WishListRepository wishListRepository;

    @Autowired
    ProductService productService;
    public void createWishList(WishList wishList) {
    wishListRepository.save(wishList);
    }

    public List<ProductDto> getWishListForUser(User user) {
       final List<WishList>wishLists= wishListRepository.findAllByUserOrderByCreatedDate(user);
        List<ProductDto>productDtos=new ArrayList<>();
        for (WishList wishList:wishLists){
            productDtos.add(productService.getProductDto(wishList.getProduct()));
        }
        return productDtos;
    }
}
