package com.bt.ecommerce.primary.razorpay;
import lombok.Data;
public class BeanRazorPayRequest {
    @Data
    public static class RazorPayRequest {
        public int amount;
        public String currency;
        public boolean accept_partial;
        public long expire_by;
        public String reference_id;
        public String description;
        public Customer customer;
        public Notify notify;
        public boolean reminder_enable;
        public String callback_url;
        public String callback_method;
    }


    @Data
    public static class Customer {
        public String name;
        public String contact;
        public String email;
    }

    @Data
    public static class Notify {
        public boolean sms;
        public boolean email;
    }


}


