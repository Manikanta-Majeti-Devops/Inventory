package com.onlinereservationapplication.inventory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("api/v1")

public class AppController
{
    private final BusinventoryRepository businventoryRepository;
    private static final Logger logger = LoggerFactory.getLogger(AppController.class);

    AppController(BusinventoryRepository businventoryRepository)
    {
        this.businventoryRepository = businventoryRepository;
    }

    @GetMapping("fetch/all/busInventory")
    public ResponseEntity<List<Businventory>> fetchAllBookings()
    {
        return ResponseEntity.ok(businventoryRepository.findAll());
    }

    @PostMapping("add/busInventory")
    public ResponseEntity<String> addBusInventory(@RequestBody Businventory businventory)
    {
        businventory.setBusNumber(businventory.getBusNumber());
        businventory.setAvailableSeats(businventory.getAvailableSeats());
        businventory.setLastUpdatedDate(String.valueOf(LocalDateTime.now()));
        Businventory savedInventory = businventoryRepository.save(businventory);
        if (savedInventory != null && savedInventory.getBusNumber() != null)
        {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("BusInventory with bus number \"" + businventory.getBusNumber() + "\" added successfully");
        }
        else
        {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("BusInventory with bus number \"" + businventory.getBusNumber() + "\" NOT added successfully");
        }
    }

    @PostMapping("edit/busInventory/{busNumber}/{editAvailableSeats}")
    public ResponseEntity<String> editBusInventory(@PathVariable String busNumber,
                                                   @PathVariable int editAvailableSeats)
    {
        Businventory businventory = businventoryRepository.findByBusNumber(busNumber);
        businventory.setAvailableSeats(editAvailableSeats);
        businventory.setLastUpdatedDate(String.valueOf(LocalDateTime.now()));
        Businventory savedInventory = businventoryRepository.save(businventory);
        if (savedInventory != null && savedInventory.getBusNumber() != null)
        {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("BusInventory with bus number \"" + busNumber + "\" updated successfully");
        }
        else
        {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("BusInventory with bus number \"" + busNumber + "\" NOT updated successfully");
        }
    }

    @GetMapping("get/seatsAvailable/{busNumber}")
    public int fetchAvailableSeats(@PathVariable String busNumber)
    {
        Businventory businventory = businventoryRepository.findByBusNumber(busNumber);
        assert businventory != null;
        return businventory.getAvailableSeats();
    }

    @PostMapping("delete/busInventory/{busNumber}")
    public ResponseEntity<String> deleteBusInventory(@PathVariable String busNumber)
    {
        // Find BusInventory by bus number
        Businventory busInventory = businventoryRepository.findByBusNumber(busNumber);

        // Check if BusInventory exists
        if (busInventory != null) {
            // Delete the BusInventory
            businventoryRepository.delete(busInventory);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("BusInventory with bus number \"" + busNumber + "\" deleted successfully");
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("BusInventory with bus number \"" + busNumber + "\" NOT deleted successfully");
        }
    }

    // this URI is in specific to use by booking service to
    // update the available seats by subtracting the booked seats
    @PostMapping("update/busInventory/{busNumber}/{bookedSeats}")
    public ResponseEntity<String> updateBusInventory(@PathVariable String busNumber,
                                                     @PathVariable int bookedSeats)
    {

        Businventory businventory = businventoryRepository.findByBusNumber(busNumber);
        if (businventory != null) {
            // update available seats
            int currentAvailableSeats = businventory.getAvailableSeats();
            int updatedAvailableSeats = currentAvailableSeats - bookedSeats;
            businventory.setAvailableSeats(updatedAvailableSeats);
            businventory.setLastUpdatedDate(String.valueOf(LocalDateTime.now()));
            // save the updated BusInventory
            businventoryRepository.save(businventory);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("BusInventory with bus number \"" + busNumber + "\" updated successfully");
        }
        else
        {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("BusInventory with bus number \"" + busNumber + "\"NOT updated successfully");
        }
    }
}
