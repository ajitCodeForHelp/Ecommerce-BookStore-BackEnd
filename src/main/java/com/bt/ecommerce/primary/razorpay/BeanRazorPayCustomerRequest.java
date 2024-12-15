package com.bt.ecommerce.primary.razorpay;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BeanRazorPayCustomerRequest {
    private String orderId;
    private int amount;


}


