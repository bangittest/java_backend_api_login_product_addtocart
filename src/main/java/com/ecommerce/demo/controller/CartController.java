package com.ecommerce.demo.controller;

import com.ecommerce.demo.common.ApiResponse;
import com.ecommerce.demo.dto.cart.AddToCartDto;
import com.ecommerce.demo.dto.cart.CartDto;
import com.ecommerce.demo.model.User;
import com.ecommerce.demo.service.AuthenticationService;
import com.ecommerce.demo.service.CartService;
import com.ecommerce.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private AuthenticationService authenticationService;

    //post cart api
    //them vao gio hang
    @PostMapping("/add")
    public ResponseEntity<ApiResponse>addtoCart(@RequestBody AddToCartDto addToCartDto, @RequestParam("token") String token){
        //authenticate the token
        authenticationService.authenticate(token);

        //find the user
        User user=authenticationService.getUser(token);

        cartService.addToCart(addToCartDto,user);

        return new ResponseEntity<>(new ApiResponse(true,"added to cart"), HttpStatus.CREATED);
    }

    //get all cart items for a user

    @GetMapping("/")
    public ResponseEntity<CartDto>getCartItems(@RequestParam("token")String token){
        //authenticate the token
        authenticationService.authenticate(token);

        //find the user
        User user=authenticationService.getUser(token);


        //get cart items
        //xem gio hang
        CartDto cartDto=cartService.listCartItems(user);
        return new ResponseEntity<>(cartDto,HttpStatus.OK);
    }
    //delete
    //delete a cart item for a user
    @GetMapping("/delete/{cartItemId}")
    private ResponseEntity<ApiResponse>deleteCartItem(@PathVariable("cartItemId")Integer itemId,
                                                      @RequestParam("token")String token){
        //authenticate the token
        authenticationService.authenticate(token);

        //find the user
        User user=authenticationService.getUser(token);

        cartService.deleteCartItem(itemId,user);
        return new ResponseEntity<>(new ApiResponse(true,"Item has been removed"), HttpStatus.OK);
    }
}
