package com.ecommerce.demo.controller;

import com.ecommerce.demo.dto.order.CheckoutItemDto;
import com.ecommerce.demo.dto.order.StripeResponse;
import com.ecommerce.demo.service.AuthenticationService;
import com.ecommerce.demo.service.OrderService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private AuthenticationService authenticationService;


    // stripe create session API
    @PostMapping("/create-checkout-session")
    public ResponseEntity<StripeResponse> checkoutList(@RequestBody List<CheckoutItemDto> checkoutItemDtoList) {
        try {
            // create the stripe session
            Session session = orderService.createSession(checkoutItemDtoList);
            StripeResponse stripeResponse = new StripeResponse(session.getId());
            // send the stripe session id in response
            return new ResponseEntity<>(stripeResponse, HttpStatus.OK);
        } catch (StripeException e) {
            // Handle the StripeException
            String errorMessage = e.getMessage(); // Lấy tin nhắn lỗi từ Stripe
            // Có thể gửi errorMessage về cho người dùng hoặc ghi vào bản ghi lỗi tùy thuộc vào yêu cầu của bạn
            return new ResponseEntity<>(new StripeResponse(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
