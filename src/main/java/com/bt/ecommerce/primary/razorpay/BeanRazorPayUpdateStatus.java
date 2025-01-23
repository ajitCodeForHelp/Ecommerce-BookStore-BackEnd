package com.bt.ecommerce.primary.razorpay;

import lombok.Data;

import java.util.ArrayList;

public class BeanRazorPayUpdateStatus{
    @Data
    public class Customer{
        public String contact;
    }
    @Data
    public class Notify{
        public boolean email;
        public boolean sms;
        public boolean whatsapp;
    }
    @Data
    public class Payment{
        public int amount;
        public int created_at;
        public String method;
        public String payment_id;
        public String status;
    }
    @Data
    public class Reminders{
        public String status;
    }
    @Data
    public class Root{
        public boolean accept_partial;
        public int amount;
        public int amount_paid;
        public String callback_method;
        public String callback_url;
        public int cancelled_at;
        public int created_at;
        public String currency;
        public Customer customer;
        public String description;
        public int expire_by;
        public int expired_at;
        public int first_min_partial_amount;
        public String id;
        public Object notes;
        public Notify notify;
        public String order_id;
        public ArrayList<Payment> payments;
        public String reference_id;
        public boolean reminder_enable;
        public Reminders reminders;
        public String short_url;
        public String status;
        public int updated_at;
        public boolean upi_link;
        public String user_id;
        public boolean whatsapp_link;
        private String cartUuid;
    }

    @Data
    public class RefundResponse{
        public String id;
        public int amount;
        public String status;
    }


}
