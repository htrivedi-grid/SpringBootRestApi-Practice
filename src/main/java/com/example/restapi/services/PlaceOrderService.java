package com.example.restapi.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.restapi.data.OrderDetail;
import com.example.restapi.data.Orders;
import com.example.restapi.models.OrderSummaryEntity;
import com.example.restapi.models.UserCartEntity;
import com.example.restapi.repository.OrderDetailsRepository;
import com.example.restapi.repository.OrdersRepository;

@Service
public class PlaceOrderService {

    @Autowired
    private UserCartService cartService;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    public Orders saveOrder(Orders order) {
        return ordersRepository.save(order);
    }

    public List<OrderDetail> saveOrderDetails(List<OrderDetail> orderDetail) {
        return orderDetailsRepository.saveAll(orderDetail);
    }

    public List<OrderSummaryEntity> getOrderDataByUserId(Integer userId) {
        List<OrderSummaryEntity> data = new ArrayList<OrderSummaryEntity>();
        List<Orders> listOfOrders = ordersRepository.findByUserId(userId);

        for (Orders order : listOfOrders) {
            List<OrderDetail> listOfOrderDetails = orderDetailsRepository.findByOrderId(order.getId());
            data.add(new OrderSummaryEntity(order, listOfOrderDetails));
        }
        return data;
    }

    public Optional<Orders> processSaveOrder(String userId, List<UserCartEntity> listOfCartItems,
            List<OrderDetail> listOfOrder) {

        Orders newOrder = new Orders();
        newOrder.setUserId(Integer.parseInt(userId));

        // Check for product quantity
        for (UserCartEntity cartItem : listOfCartItems) {
            if (!cartService.hasRequiredQuantity(cartItem.getProductId(), cartItem.getQuantity())) {
                return null;
            } else {
                OrderDetail orderDetail = new OrderDetail(0, cartItem.getProductId(), cartItem.getTotalAmount());
                listOfOrder.add(orderDetail);
                newOrder.setTotalAmount(newOrder.getTotalAmount() + orderDetail.getTotalAmount());
            }
        }

        // Place new order
        Orders placedOrder = saveOrder(newOrder);
        listOfOrder.stream().forEach(data -> data.setOrderId(placedOrder.getId()));
        saveOrderDetails(listOfOrder);

        // update inventory
        cartService.updateInventoryQuantity(listOfCartItems);

        return Optional.of(placedOrder);
    }
}
