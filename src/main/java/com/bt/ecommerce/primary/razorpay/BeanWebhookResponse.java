package com.bt.ecommerce.primary.razorpay;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
public class BeanWebhookResponse {
    @Data

    public class AcquirerData {
        public String bank_transaction_id;
    }

    @Data

    public class Customer {
        public String contact;
        public String email;
    }

    @Data

    public class Entity {
        public int amount;
        public int amount_due;
        public int amount_paid;
        public int attempts;
        public int created_at;
        public String currency;
        public String entity;
        public String id;
        public Notes notes;
        public Object offer_id;
        public ArrayList<String> offers;
        public String receipt;
        public String status;
        public AcquirerData acquirer_data;
        public int amount_refunded;
        public int amount_transferred;
        public String bank;
        public int base_amount;
        public boolean captured;
        public Object card_id;
        public String contact;
        public String description;
        public String email;
        public Object error_code;
        public Object error_description;
        public Object error_reason;
        public Object error_source;
        public Object error_step;
        public int fee;
        public String fee_bearer;
        public boolean international;
        public Object invoice_id;
        public String method;
        public String order_id;
        public Object refund_status;
        public int tax;
        public Object vpa;
        public Object wallet;
        public boolean accept_partial;
        public String callback_method;
        public String callback_url;
        public int cancelled_at;
        public Customer customer;
        public int expire_by;
        public int expired_at;
        public int first_min_partial_amount;
        public Notify notify;
        public Object payments;
        public String reference_id;
        public boolean reminder_enable;
        public Reminders reminders;
        public String short_url;
        public String source;
        public String source_id;
        public int updated_at;
        public String user_id;
    }

    @Data

    public class Notes {
        @JsonProperty("Policy Name")
        public String policyName;
    }

    @Data

    public class Notify {
        public boolean email;
        public boolean sms;
    }

    @Data

    public class Order {
        public Entity entity;
    }

    @Data

    public class Payload {
        public Order order;
        public Payment payment;
        public PaymentLink payment_link;
    }

    @Data

    public class Payment {
        public Entity entity;
    }

    @Data
    public class PaymentLink {
        public Entity entity;
    }

    @Data
    public class Reminders {
    }

    @Data
    public class Transaction {
        public String account_id;
        public ArrayList<String> contains;
        public int created_at;
        public String entity;
        public String event;
        public Payload payload;
    }


}
