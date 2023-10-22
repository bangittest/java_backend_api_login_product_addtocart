package com.ecommerce.demo.service;

import com.ecommerce.demo.dto.cart.AddToCartDto;
import com.ecommerce.demo.dto.cart.CartDto;
import com.ecommerce.demo.dto.cart.CartItemDto;
import com.ecommerce.demo.exceptions.CustomException;
import com.ecommerce.demo.model.Cart;
import com.ecommerce.demo.model.Product;
import com.ecommerce.demo.model.User;
import com.ecommerce.demo.reponsitory.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    @Autowired
    ProductService productService;

    @Autowired
    CartRepository cartRepository;
    public void addToCart(AddToCartDto addToCartDto, User user) {
        //validate if the product id is valid
        //xác thực xem id sản phẩm có hợp lệ không
        Product product= productService.findById(addToCartDto.getProductId());
        Cart cart=new Cart();
        cart.setProduct(product);
        cart.setUser(user);
        cart.setQuantity(addToCartDto.getQuantity());
        cart.setCreatedDate(new Date());
        //save the cart
        cartRepository.save(cart);
    }

    public CartDto listCartItems(User user) {
        final List<Cart>cartList=cartRepository.findAllByUserOrderByCreatedDateDesc(user);

        List<CartItemDto>cartItems=new ArrayList<>();

        double totalCost=0;
        for (Cart cart:cartList){
            CartItemDto cartItemDto=new CartItemDto(cart);
            totalCost+=cartItemDto.getQuantity()*cart.getProduct().getPrice();
            cartItems.add(cartItemDto);
        }
        CartDto cartDto=new CartDto();
        cartDto.setTotalCost(totalCost);
        cartDto.setCartItems(cartItems);
        return cartDto;

    }

    public void deleteCartItem(Integer cartItemId, User user) {
        //the item id belongs to user
        //id mục thuộc về người dùng
        Optional<Cart>optionalCart=cartRepository.findById(cartItemId);
        if (optionalCart.isEmpty()){
            throw new CustomException("cart item id is invalid(Id mặt hàng trong giỏ hàng không hợp lệ) " +cartItemId);
        }
        Cart cart=optionalCart.get();
        if (cart.getUser()!=user){
            throw new CustomException("cart item does not belong to user(mặt hàng trong giỏ hàng không thuộc về người dùng) : " +cartItemId);
        }
        cartRepository.delete(cart);
    }

}
