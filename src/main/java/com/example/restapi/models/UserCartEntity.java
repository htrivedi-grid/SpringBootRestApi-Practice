package com.example.restapi.models;

import java.io.Serializable;

public class UserCartEntity implements Serializable {

    private Integer userId;
    private Integer productId;
    private Integer quantity;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void incrementQuantity() {
        this.quantity++;
    }

    public void decrementQuantity() {
        this.quantity--;
    }

    @Override
    public String toString() {
        return "UserCartEntity [userId=" + userId + ", productId=" + productId + "]";
    }

}
