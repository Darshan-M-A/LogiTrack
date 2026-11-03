package com.example.logitrack.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.logitrack.dto.CreateShipmentRequest;
import com.example.logitrack.dto.UpdateStatusRequest;
import com.example.logitrack.entity.Shipment;
import com.example.logitrack.entity.ShipmentStatus;
import com.example.logitrack.entity.TrackingEvent;
import com.example.logitrack.exception.ResourceNotFoundException;
import com.example.logitrack.repository.ShipmentRepository;
import com.example.logitrack.repository.TrackingEventRepository;

@Service
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final TrackingEventRepository trackingEventRepository;

    public ShipmentService(
            ShipmentRepository shipmentRepository,
            TrackingEventRepository trackingEventRepository) {

        this.shipmentRepository = shipmentRepository;
        this.trackingEventRepository = trackingEventRepository;
    }

    // Get all shipments
    public List<Shipment> findAll() {
        return shipmentRepository.findAll();
    }

    // Find shipment using tracking number
    public Shipment findByTrackingNumber(String trackingNumber) {

        return shipmentRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Shipment not found: " + trackingNumber));
    }

    // Get tracking history
    public List<TrackingEvent> getTrackingHistory(String trackingNumber) {

        Shipment shipment = findByTrackingNumber(trackingNumber);

        return trackingEventRepository.findByShipment(shipment);
    }

    // Create a new shipment
    @Transactional
    public Shipment createShipment(CreateShipmentRequest request) {

        Shipment shipment = new Shipment();

        // Generate tracking number
        shipment.setTrackingNumber(
                "LT-" + UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase());

        // Set shipment details
        shipment.setSenderName(request.getSenderName());
        shipment.setSenderAddress(request.getSenderAddress());
        shipment.setReceiverName(request.getReceiverName());
        shipment.setReceiverAddress(request.getReceiverAddress());

        // Initial status
        shipment.setStatus(ShipmentStatus.CREATED);

        // Save shipment
        Shipment savedShipment = shipmentRepository.save(shipment);

        // Create initial tracking event
        TrackingEvent event = new TrackingEvent();

        event.setShipment(savedShipment);
        event.setStatus(ShipmentStatus.CREATED);
        event.setLocation(request.getSenderAddress());
        event.setDescription("Shipment created");

        trackingEventRepository.save(event);

        return savedShipment;
    }

    // Update shipment status
    @Transactional
    public Shipment updateStatus(
            String trackingNumber,
            UpdateStatusRequest request,
            String updatedBy) {

        // Find existing shipment
        Shipment shipment = findByTrackingNumber(trackingNumber);

        ShipmentStatus newStatus = request.getStatus();
        ShipmentStatus currentStatus = shipment.getStatus();

        if (newStatus == null) {
            throw new IllegalArgumentException(
                    "Please select a shipment status.");
        }

        // Prevent changes after delivery
        if (currentStatus == ShipmentStatus.DELIVERED) {
            throw new IllegalArgumentException(
                    "A delivered shipment cannot be updated.");
        }

        // Check that the status moves to the next stage only
        ShipmentStatus[] statuses = ShipmentStatus.values();

        int currentIndex = currentStatus.ordinal();

        if (currentIndex + 1 >= statuses.length
                || newStatus != statuses[currentIndex + 1]) {

            throw new IllegalArgumentException(
                    "Invalid status transition from "
                            + currentStatus + " to " + newStatus);
        }

        // Update and save shipment status
        shipment.setStatus(newStatus);
        shipmentRepository.save(shipment);

        // Record the status change in tracking history
        TrackingEvent event = new TrackingEvent();

        event.setShipment(shipment);
        event.setStatus(newStatus);
        event.setLocation(request.getLocation());
        event.setDescription(request.getDescription());

        trackingEventRepository.save(event);

        return shipment;
    }
}