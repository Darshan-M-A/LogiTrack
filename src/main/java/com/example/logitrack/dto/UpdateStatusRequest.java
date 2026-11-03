package com.example.logitrack.dto;

import com.example.logitrack.entity.ShipmentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UpdateStatusRequest {
    @NotNull(message = "Status is required")
    private ShipmentStatus status;

    @NotBlank(message = "Location is required")
    @Size(max = 200)
    private String location;

    @NotBlank(message = "Description is required")
    @Size(max = 1000)
    private String description;

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
}
