package com.example.restapi.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.restapi.data.OrderDetail;
import com.example.restapi.data.Orders;
import com.example.restapi.models.OrderSummaryEntity;
import com.example.restapi.repository.OrderDetailsRepository;
import com.example.restapi.repository.OrdersRepository;

@Service
public class PlaceOrderService {

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
}
