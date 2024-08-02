package com.example.restapi.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.SessionScope;

import com.example.restapi.data.Orders;
import com.example.restapi.data.OrderDetail;
import com.example.restapi.models.Constants;
import com.example.restapi.models.OrderSummaryEntity;
import com.example.restapi.models.UserCartEntity;
import com.example.restapi.services.PlaceOrderService;
import com.example.restapi.services.UserCartService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/order")
public class OrdersController {

    @Autowired
    private UserCartService cartService;

    @Autowired
    private PlaceOrderService orderService;

    @GetMapping()
    public ResponseEntity<Object> getOrder(@RequestParam(name = "user_id") String userId) {
        List<OrderSummaryEntity> orderSummary = orderService.getOrderDataByUserId(Integer.parseInt(userId));
        return ResponseEntity.ok(orderSummary);
    }

    @SessionScope
    @PostMapping("/place/{user_id}")
    public ResponseEntity<Object> palaceOrders(@PathVariable(name = "user_id") String userId,
            HttpServletRequest request) {
        HttpSession session = request.getSession();
        List<UserCartEntity> listOfCartItems = getCartItemsFromSession(session);

        Orders newOrder = new Orders();
        newOrder.setUserId(Integer.parseInt(userId));

        List<OrderDetail> listOfOrder = new ArrayList<OrderDetail>();

        if (!listOfCartItems.isEmpty()) {
            // Check for product quantity
            for (UserCartEntity cartItem : listOfCartItems) {
                if (!cartService.hasRequiredQuantity(cartItem.getProductId(), cartItem.getQuantity())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Quanity is out of limit for product " + cartItem.getProductId());
                } else {
                    OrderDetail orderDetail = new OrderDetail(0, cartItem.getProductId(), cartItem.getTotalAmount());
                    listOfOrder.add(orderDetail);
                    newOrder.setTotalAmount(newOrder.getTotalAmount() + orderDetail.getTotalAmount());
                }
            }

            // Place new order
            Orders placedOrder = orderService.saveOrder(newOrder);
            listOfOrder.stream().forEach(data -> data.setOrderId(placedOrder.getId()));
            orderService.saveOrderDetails(listOfOrder);

            // update inventory
            cartService.updateInventoryQuantity(listOfCartItems);

            // clear session data
            session.invalidate();

            return ResponseEntity.ok(new OrderSummaryEntity(placedOrder, listOfOrder));
        } else
            return ResponseEntity.notFound().build();
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
