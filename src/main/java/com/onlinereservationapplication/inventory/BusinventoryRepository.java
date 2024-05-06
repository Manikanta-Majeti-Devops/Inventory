package com.onlinereservationapplication.inventory;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinventoryRepository extends JpaRepository<Businventory, String>
{
    // You can add custom query methods here if needed
    Businventory findByBusNumber(String busNumber);

}