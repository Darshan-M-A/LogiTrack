package com.example.logitrack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.logitrack.entity.Shipment;
import com.example.logitrack.entity.TrackingEvent;

@Repository
public interface TrackingEventRepository
        extends JpaRepository<TrackingEvent, Long> {

    List<TrackingEvent> findByShipment(Shipment shipment);
}
