package com.ecommerce.demo.service;

import com.ecommerce.demo.dto.order.CheckoutItemDto;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class OrderService {

    @Value("${BASE_URL}")
    private String baseUrl;

    @Value("${STRIPE_SECRET_KEY}")
    private String apiKey;

    public Session createSession(List<CheckoutItemDto> checkoutItemDtoList) throws StripeException {
        // Tạo URL thành công và thất bại
        String successUrl = baseUrl + "payment/success";
        String failureUrl = baseUrl + "payment/failure";

        // Đặt khóa bí mật của Stripe
        Stripe.apiKey = apiKey;

        List<SessionCreateParams.LineItem> sessionItemList = new ArrayList<>();

        for (CheckoutItemDto checkoutItemDto : checkoutItemDtoList) {
            sessionItemList.add(createSessionLineItem(checkoutItemDto));
        }

        // Xây dựng tham số phiên
        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setCancelUrl(failureUrl)
                .addAllLineItem(sessionItemList)
                .setSuccessUrl(successUrl)
                .build();

        return Session.create(params);
    }

    private SessionCreateParams.LineItem createSessionLineItem(CheckoutItemDto checkoutItemDto) {
        return SessionCreateParams.LineItem.builder()
                .setPriceData(createPriceData(checkoutItemDto))
                .setQuantity(Long.parseLong(String.valueOf(checkoutItemDto.getQuantity())))
                .build();
    }

    private SessionCreateParams.LineItem.PriceData createPriceData(CheckoutItemDto checkoutItemDto) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("vnd")
                .setUnitAmount((long) (checkoutItemDto.getPrice() * 100))
                .setProductData(
                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName(checkoutItemDto.getProductName())
                                .build()
                ).build();
    }
}



//    @Autowired
//    private CartService cartService;

//    @Autowired
//    OrderRepository orderRepository;

//    @Autowired
//    OrderItemsRepository orderItemsRepository;

//    @Value("${BASE_URL}")
//    private String baseURL;
//
//    @Value("${STRIPE_SECRET_KEY}")
//    private String apiKey;
//
//    // create total price
//    SessionCreateParams.LineItem.PriceData createPriceData(CheckoutItemDto checkoutItemDto) {
//        return SessionCreateParams.LineItem.PriceData.builder()
//                .setCurrency("usd")
//                .setUnitAmount((long)(checkoutItemDto.getPrice()*100))
//                .setProductData(
//                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
//                                .setName(checkoutItemDto.getProductName())
//                                .build())
//                .build();
//    }
//
//    // build each product in the stripe checkout page
//    SessionCreateParams.LineItem createSessionLineItem(CheckoutItemDto checkoutItemDto) {
//        return SessionCreateParams.LineItem.builder()
//                // set price for each product
//                .setPriceData(createPriceData(checkoutItemDto))
//                // set quantity for each product
//                .setQuantity(Long.parseLong(String.valueOf(checkoutItemDto.getQuantity())))
//                .build();
//    }
//
//    // create session from list of checkout items
//    public Session createSession(List<CheckoutItemDto> checkoutItemDtoList) throws StripeException {
//
//        // supply success and failure url for stripe
//        String successURL = baseURL + "payment/success";
//        String failedURL = baseURL + "payment/failed";
//
//
//        // set the private key
//        Stripe.apiKey = apiKey;
//
//        List<SessionCreateParams.LineItem> sessionItemsList = new ArrayList<>();
//
//        // for each product compute SessionCreateParams.LineItem
//        for (CheckoutItemDto checkoutItemDto : checkoutItemDtoList) {
//            sessionItemsList.add(createSessionLineItem(checkoutItemDto));
//        }
//
//        // build the session param
//        SessionCreateParams params = SessionCreateParams.builder()
//                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
//                .setMode(SessionCreateParams.Mode.PAYMENT)
//                .setCancelUrl(failedURL)
//                .addAllLineItem(sessionItemsList)
//                .setSuccessUrl(successURL)
//                .build();
//        return Session.create(params);
//    }


