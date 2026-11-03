package com.example.logitrack.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.logitrack.dto.CreateShipmentRequest;
import com.example.logitrack.dto.UpdateStatusRequest;
import com.example.logitrack.entity.Shipment;
import com.example.logitrack.entity.ShipmentStatus;
import com.example.logitrack.entity.TrackingEvent;
import com.example.logitrack.exception.ResourceNotFoundException;
import com.example.logitrack.service.ShipmentService;

import jakarta.validation.Valid;

@Controller
public class ShipmentController {

    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    // Dashboard
    @GetMapping("/")
    public String dashboard(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) ShipmentStatus status,
            Model model) {

        List<Shipment> shipments = shipmentService.findAll();

        if (q != null && !q.isBlank()) {
            String search = q.trim().toLowerCase();

            shipments = shipments.stream()
                    .filter(shipment ->
                            contains(shipment.getTrackingNumber(), search)
                            || contains(shipment.getSenderName(), search)
                            || contains(shipment.getReceiverName(), search))
                    .toList();
        }

        if (status != null) {
            shipments = shipments.stream()
                    .filter(shipment -> shipment.getStatus() == status)
                    .toList();
        }

        model.addAttribute("shipments", shipments);
        model.addAttribute("q", q);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("statuses", ShipmentStatus.values());

        model.addAttribute("totalShipments",
                shipmentService.findAll().size());

        model.addAttribute("deliveredCount",
                countByStatus(ShipmentStatus.DELIVERED));

        model.addAttribute("inTransitCount",
                countByStatus(ShipmentStatus.IN_TRANSIT));

        model.addAttribute("createdCount",
                countByStatus(ShipmentStatus.CREATED));

        return "dashboard";
    }

    // Show create shipment form
    @GetMapping("/shipments/new")
    public String showCreateForm(Model model) {
        model.addAttribute("shipmentForm", new CreateShipmentRequest());
        return "create-shipment";
    }

    // Create shipment
    @PostMapping("/shipments")
    public String createShipment(
            @Valid @ModelAttribute("shipmentForm")
            CreateShipmentRequest request,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "create-shipment";
        }

        Shipment shipment = shipmentService.createShipment(request);

        redirectAttributes.addFlashAttribute(
                "successMessage", "Shipment created successfully!");

        return "redirect:/shipments/" + shipment.getTrackingNumber();
    }

    // View shipment details
    @GetMapping("/shipments/{trackingNumber}")
    public String shipmentDetails(
            @PathVariable String trackingNumber,
            Model model) {

        Shipment shipment =
                shipmentService.findByTrackingNumber(trackingNumber);

        List<TrackingEvent> history =
                shipmentService.getTrackingHistory(trackingNumber);

        model.addAttribute("shipment", shipment);
        model.addAttribute("history", history);
        model.addAttribute("statusForm", new UpdateStatusRequest());

        ShipmentStatus nextStatus = getNextStatus(shipment.getStatus());

        model.addAttribute("nextStatus", nextStatus);

        return "shipment-details";
    }

    // Update shipment status
    @PostMapping("/shipments/{trackingNumber}/status")
    public String updateStatus(
            @PathVariable String trackingNumber,
            @Valid @ModelAttribute("statusForm")
            UpdateStatusRequest statusForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        Shipment shipment =
                shipmentService.findByTrackingNumber(trackingNumber);

        if (bindingResult.hasErrors()) {
            model.addAttribute("shipment", shipment);
            model.addAttribute(
                    "history",
                    shipmentService.getTrackingHistory(trackingNumber));
            model.addAttribute(
                    "nextStatus",
                    getNextStatus(shipment.getStatus()));

            return "shipment-details";
        }

        shipmentService.updateStatus(
                trackingNumber,
                statusForm,
                null);

        redirectAttributes.addFlashAttribute(
                "successMessage", "Shipment status updated successfully!");

        return "redirect:/shipments/" + trackingNumber;
    }

    // Handle missing shipments
    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleNotFound(
            ResourceNotFoundException exception,
            Model model) {

        model.addAttribute("message", exception.getMessage());
        return "not-found";
    }

    // Get the next status in the workflow
    private ShipmentStatus getNextStatus(ShipmentStatus currentStatus) {

        List<ShipmentStatus> statuses =
                Arrays.asList(ShipmentStatus.values());

        int currentIndex = statuses.indexOf(currentStatus);

        if (currentIndex >= 0 && currentIndex < statuses.size() - 1) {
            return statuses.get(currentIndex + 1);
        }

        return null;
    }

    // Count shipments by status
    private long countByStatus(ShipmentStatus status) {
        return shipmentService.findAll().stream()
                .filter(shipment -> shipment.getStatus() == status)
                .count();
    }

    // Search helper
    private boolean contains(String value, String search) {
        return value != null
                && value.toLowerCase().contains(search);
    }
}
