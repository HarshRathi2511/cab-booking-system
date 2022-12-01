package Models;

import java.util.Date;
import java.util.List;

import Enums.TripStatus;

public class Trip {
    private List<Student> coPassengers;
    private String tripId; // start_end_time
    private String startLocation; // start_end (eg pilani_jaipur)
    private String endLocation;

    // departure for the trip //max of all the times in the set
    private Date departureDate;
    private TripStatus tripStatus;
    private double totalTripCost;

    //co passenger limit 
    public static int MAX_CO_PASSENGERS = 4;

    public Trip(List<Student> coPassengers, String tripId, String startLocation, String endLocation, Date departureDate,
             double totalTripCost) {
        this.coPassengers = coPassengers;
        this.tripId = tripId;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.departureDate = departureDate;
        this.tripStatus = TripStatus.PROPOSED;
        this.totalTripCost = totalTripCost;
    }

    public List<Student> getCoPassengers() {
        return coPassengers;
    }

    public void setCoPassengers(List<Student> coPassengers) {
        this.coPassengers = coPassengers;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public TripStatus getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(TripStatus tripStatus) {
        this.tripStatus = tripStatus;
    }

    public double getTotalTripCost() {
        return totalTripCost;
    }

    public void setTotalTripCost(double totalTripCost) {
        this.totalTripCost = totalTripCost;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

}
