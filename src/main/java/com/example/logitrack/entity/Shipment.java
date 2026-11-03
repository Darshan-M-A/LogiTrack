package com.example.logitrack.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "shipments")
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tracking_number", nullable = false, unique = true, length = 20)
    private String trackingNumber;

    @Column(nullable = false, length = 100)
    private String senderName;

    @Column(nullable = false, length = 500)
    private String senderAddress;

    @Column(nullable = false, length = 100)
    private String receiverName;

    @Column(nullable = false, length = 500)
    private String receiverAddress;

    @Column(nullable = false, length = 100)
    private String deliveryAgent;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ShipmentStatus status = ShipmentStatus.CREATED;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Shipment() {}

    @PrePersist
    void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (status == null) status = ShipmentStatus.CREATED;
    }

    public Long getId() { 
    	return id; 
    	}
    public String getTrackingNumber() {
    	return trackingNumber; 
    	}
    public void setTrackingNumber(String trackingNumber) {
    	this.trackingNumber = trackingNumber; 
    	}
    public String getSenderName() {
    	return senderName; 
    	}
    public void setSenderName(String senderName) {
    	this.senderName = senderName; 
    	}
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
    public ShipmentStatus getStatus() {
    	return status; 
    	}
    public void setStatus(ShipmentStatus status) {
    	this.status = status; 
    	}
    public LocalDateTime getCreatedAt() {
    	return createdAt; 
    	}
}
