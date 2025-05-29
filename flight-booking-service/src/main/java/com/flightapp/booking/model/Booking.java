package com.flightapp.booking.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String flightNumber;
    private String flightDate;
    private String passengerName;
    private int numberOfSeats;
    private String bookingReference;

    private boolean checkedIn;       // New field to track check-in status
    private String checkinNumber;    // New field for check-in confirmation number

    // Getters and Setters

    public Long getId() { 
        return id; 
    }

    public void setId(Long id) { 
        this.id = id; 
    }

    public String getFlightNumber() { 
        return flightNumber; 
    }

    public void setFlightNumber(String flightNumber) { 
        this.flightNumber = flightNumber; 
    }

    public String getFlightDate() { 
        return flightDate; 
    }

    public void setFlightDate(String flightDate) { 
        this.flightDate = flightDate; 
    }

    public String getPassengerName() { 
        return passengerName; 
    }

    public void setPassengerName(String passengerName) { 
        this.passengerName = passengerName; 
    }

    public int getNumberOfSeats() { 
        return numberOfSeats; 
    }

    public void setNumberOfSeats(int numberOfSeats) { 
        this.numberOfSeats = numberOfSeats; 
    }

    public String getBookingReference() { 
        return bookingReference; 
    }

    public void setBookingReference(String bookingReference) { 
        this.bookingReference = bookingReference; 
    }

    public boolean isCheckedIn() { 
        return checkedIn; 
    }

    public void setCheckedIn(boolean checkedIn) { 
        this.checkedIn = checkedIn; 
    }

    public String getCheckinNumber() { 
        return checkinNumber; 
    }

    public void setCheckinNumber(String checkinNumber) { 
        this.checkinNumber = checkinNumber; 
    }
}
