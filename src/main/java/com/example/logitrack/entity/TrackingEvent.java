package com.example.logitrack.entity;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "tracking_events")
public class TrackingEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shipment_id", nullable = false)
    private Shipment shipment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ShipmentStatus status;

    @Column(nullable = false, length = 200)
    private String location;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false, updatable = false)
    private LocalDateTime eventTime;

    public TrackingEvent() {}

    @PrePersist
    void onCreate() {
        if (eventTime == null) eventTime = LocalDateTime.now();
    }

    public Long getId() {
    	return id; 
    	}
    public Shipment getShipment() {
    	return shipment; 
    	}
    public void setShipment(Shipment shipment) {
    	this.shipment = shipment; 
    	}
    public ShipmentStatus getStatus() {
    	return status; 
    	}
    public void setStatus(ShipmentStatus status) {
    	this.status = status; 
    	}
    public String getLocation() {
    	return location; 
    	}
    public void setLocation(String location) {
    	this.location = location; 
    	}
    public String getDescription() {
    	return description; 
    	}
    public void setDescription(String description) {
    	this.description = description; 
    	}
    public LocalDateTime getEventTime() {
    	return eventTime; 
    	}
}
