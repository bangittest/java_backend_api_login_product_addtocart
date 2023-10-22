package com.ecommerce.demo.controller;

import com.ecommerce.demo.common.ApiResponse;
import com.ecommerce.demo.dto.ProductDto;
import com.ecommerce.demo.model.Product;
import com.ecommerce.demo.model.User;
import com.ecommerce.demo.model.WishList;
import com.ecommerce.demo.service.AuthenticationService;
import com.ecommerce.demo.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wishlist")
public class WishListController {

    @Autowired
    WishListService wishListService;

    @Autowired
    AuthenticationService authenticationService;
    //save product as wishlist item
    // lưu sản phẩm làm mục danh sách mong muốn
    @PostMapping("/add")
    //add to cart
    public ResponseEntity<ApiResponse>addToWishList(@RequestBody Product product, @RequestParam("token")String token){
       //authenticate the token
        //xác thực mã thông báo
        authenticationService.authenticate(token);

       //find the user
        // tìm người dùng
        User user=authenticationService.getUser(token);

       //save the item in wishlist
        // lưu mục vào danh sách yêu thích
        WishList wishList=new WishList(user,product);

        wishListService.createWishList(wishList);
        ApiResponse apiResponse=new ApiResponse(true,"added to wishlist");
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    //lấy tất cả mục danh sách mong muốn cho người dùng
    //getall wishlist item for a user
    @GetMapping("/{token}")
    public ResponseEntity<List<ProductDto>>getWishList(@PathVariable("token")String token){
        //authenticate the token
        authenticationService.authenticate(token);

        //find the user
        User user=authenticationService.getUser(token);

        List<ProductDto> productDtos = wishListService.getWishListForUser(user);
        return new ResponseEntity<>(productDtos,HttpStatus.OK);
    }
}
