package com.example.logitrack.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateShipmentRequest {
    @NotBlank(message = "Sender name is required")
    @Size(max = 100)
    private String senderName;

    @NotBlank(message = "Sender address is required")
    @Size(max = 500)
    private String senderAddress;

    @NotBlank(message = "Receiver name is required")
    @Size(max = 100)
    private String receiverName;

    @NotBlank(message = "Receiver address is required")
    @Size(max = 500)
    private String receiverAddress;

    @NotBlank(message = "Delivery agent is required")
    @Size(max = 100)
    private String deliveryAgent;

    public String getSenderName() {
    	return senderName; 
    	}
    public void setSenderName(String senderName) {
    	this.senderName = senderName; }
    public String getSenderAddress() {
    	return senderAddress; 
    	}
    public void setSenderAddress(String senderAddress) {
    	this.senderAddress = senderAddress; 
    	}
    public String getReceiverName() {
    	return receiverName; 
    	}
    public void setReceiverName(String receiverName) {
    	this.receiverName = receiverName; 
    	}
    public String getReceiverAddress() {
    	return receiverAddress; 
    	}
    public void setReceiverAddress(String receiverAddress) {
    	this.receiverAddress = receiverAddress; 
    	}
    public String getDeliveryAgent() {
    	return deliveryAgent; 
    	}
    public void setDeliveryAgent(String deliveryAgent) {
    	this.deliveryAgent = deliveryAgent; 
    	}
}
