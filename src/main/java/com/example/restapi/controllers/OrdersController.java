package com.example.restapi.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.SessionScope;

import com.example.restapi.data.Orders;
import com.example.restapi.exception.BadRequestException;
import com.example.restapi.exception.ResourceNotFoundException;
import com.example.restapi.data.OrderDetail;
import com.example.restapi.models.Constants;
import com.example.restapi.models.OrderSummaryEntity;
import com.example.restapi.models.UserCartEntity;
import com.example.restapi.services.PlaceOrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/order")
public class OrdersController {

    @Autowired
    private PlaceOrderService orderService;

    @GetMapping()
    public ResponseEntity<List<OrderSummaryEntity>> getOrder(@RequestParam(name = "user_id") String userId) {
        List<OrderSummaryEntity> orderSummary = orderService.getOrderDataByUserId(Integer.parseInt(userId));
        return ResponseEntity.ok(orderSummary);
    }

    @SessionScope
    @PostMapping("/place/{user_id}")
    public ResponseEntity<OrderSummaryEntity> palaceOrders(@PathVariable(name = "user_id") String userId,
            HttpServletRequest request) {
        HttpSession session = request.getSession();
        List<UserCartEntity> listOfCartItems = getCartItemsFromSession(session);

        List<OrderDetail> listOfOrder = new ArrayList<OrderDetail>();

        if (!listOfCartItems.isEmpty()) {

            Optional<Orders> placedOrder = orderService.processSaveOrder(userId, listOfCartItems, listOfOrder);
            if (placedOrder.isPresent()) {
                // clear session data
                session.invalidate();

                return ResponseEntity.ok(new OrderSummaryEntity(placedOrder.get(), listOfOrder));
            } else
                throw new BadRequestException("Quanity is out of stock for product, try again");
        } else
            throw new ResourceNotFoundException("Cart is empty");
    }

    private List<UserCartEntity> getCartItemsFromSession(HttpSession session) {
        List<UserCartEntity> listOfCartItems = (List<UserCartEntity>) session
                .getAttribute(Constants.SESSION_CART_ATTRIBUTE);
        if (listOfCartItems == null) {
            listOfCartItems = new ArrayList<>();
        }
        return listOfCartItems;
    }
}
