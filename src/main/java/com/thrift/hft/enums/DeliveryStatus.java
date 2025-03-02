package com.thrift.hft.enums;

public enum DeliveryStatus {

    PROCESSING("Processing"),      // Order is being prepared for shipment
    SHIPPED("Shipped"),         // Order has been shipped by the seller or warehouse
    IN_TRANSIT("In Transit"),      // Order is on the way to the delivery address
    OUT_FOR_DELIVERY("Out For Delivery"),// Order is out for delivery with the courier
    DELIVERED("Delivered"),       // Order has been successfully delivered to the customer
    CANCELLED("Cancelled"),       // Order delivery was cancelled
    RETURN_REQUESTED("Return Requested"),// Customer has requested a return
    RETURNED("Returned"),        // Order has been returned successfully
    FAILED_DELIVERY("Failed Delivery"), // Delivery attempt failed (e.g., incorrect address, customer unavailable)
    REFUNDED ("Refunded") ;    // Refund has been processed for the order



    private String value;

    DeliveryStatus(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
