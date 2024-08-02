package com.example.restapi.models;

import java.util.List;

import com.example.restapi.data.OrderDetail;
import com.example.restapi.data.Orders;

public class OrderSummaryEntity {

    private Orders order;
    private List<OrderDetail> orderDetails;

    public OrderSummaryEntity(Orders order, List<OrderDetail> orderDetails) {
        this.order = order;
        this.orderDetails = orderDetails;
    }

    public Orders getOrder() {
        return order;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

}
