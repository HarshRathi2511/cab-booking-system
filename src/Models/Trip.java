package Models;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Enums.*;
import Exceptions.StudentNotFoundException;

public class Trip {
    private List<Student> coPassengers;
    private String tripId; // start_end_time
    private String startLocation; // start_end (eg pilani_jaipur)
    private String endLocation;

    // departure for the trip //max of all the times in the set
    private Date departureDate;
    private TripStatus tripStatus;
    private double totalTripCost;

    // map of indv responses
    private HashMap<String, TripRequestStatus> everyPassengerStatusMap;

    // co passenger limit
    public static int MAX_CO_PASSENGERS = 4;

    public Trip(List<Student> coPassengers, String tripId, String startLocation, String endLocation, Date departureDate,
                TripStatus tripStatus, double totalTripCost) {
        this.coPassengers = coPassengers;
        this.tripId = tripId;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.departureDate = departureDate;
        this.tripStatus = tripStatus;
        this.totalTripCost = totalTripCost;

        this.everyPassengerStatusMap = generateEveryPassengerStatusMap();

    }

    private HashMap<String, TripRequestStatus> generateEveryPassengerStatusMap() {
        HashMap<String, TripRequestStatus> everyPassengerStatusMap = new HashMap<String, TripRequestStatus>();

        // fill it with default NO_RESPONSE
        for (Student coPassengers : this.coPassengers) {
            everyPassengerStatusMap.put(coPassengers.getId(), TripRequestStatus.NO_RESPONSE);
        }

        return everyPassengerStatusMap;
    }

    public void removeCoPassengers(List<Student> students) throws StudentNotFoundException {
        try {
            this.coPassengers.removeAll(students);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            //custom exception here 
            // System.out.println(student.getName() + " is not a copassenger:ERR cant remove");
            throw new StudentNotFoundException("cant remove this student");

        }

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

    public  String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((coPassengers == null) ? 0 : coPassengers.hashCode());
        result = prime * result + ((tripId == null) ? 0 : tripId.hashCode());
        result = prime * result + ((startLocation == null) ? 0 : startLocation.hashCode());
        result = prime * result + ((endLocation == null) ? 0 : endLocation.hashCode());
        result = prime * result + ((departureDate == null) ? 0 : departureDate.hashCode());
        result = prime * result + ((tripStatus == null) ? 0 : tripStatus.hashCode());
        long temp;
        temp = Double.doubleToLongBits(totalTripCost);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((everyPassengerStatusMap == null) ? 0 : everyPassengerStatusMap.hashCode());
        return result;
    }

    public HashMap<String, TripRequestStatus> getEveryPassengerStatusMap() {
        return everyPassengerStatusMap;
    }

    public void setEveryPassengerStatusMap(HashMap<String, TripRequestStatus> everyPassengerStatusMap) {
        this.everyPassengerStatusMap = everyPassengerStatusMap;
    }

    public static int getMAX_CO_PASSENGERS() {
        return MAX_CO_PASSENGERS;
    }

    public static void setMAX_CO_PASSENGERS(int mAX_CO_PASSENGERS) {
        MAX_CO_PASSENGERS = mAX_CO_PASSENGERS;
    }

    @Override
    public String toString() {
        return "Trip [coPassengers=" + coPassengers + ", tripId=" + tripId + ", startLocation=" + startLocation
                + ", endLocation=" + endLocation + ", departureDate=" + departureDate + ", tripStatus=" + tripStatus
                + ", totalTripCost=" + totalTripCost + ", everyPassengerStatusMap=" + everyPassengerStatusMap + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Trip other = (Trip) obj;
        if (coPassengers == null) {
            if (other.coPassengers != null)
                return false;
        } else if (!coPassengers.equals(other.coPassengers))
            return false;
        if (tripId == null) {
            if (other.tripId != null)
                return false;
        } else if (!tripId.equals(other.tripId))
            return false;
        if (startLocation == null) {
            if (other.startLocation != null)
                return false;
        } else if (!startLocation.equals(other.startLocation))
            return false;
        if (endLocation == null) {
            if (other.endLocation != null)
                return false;
        } else if (!endLocation.equals(other.endLocation))
            return false;
        if (departureDate == null) {
            if (other.departureDate != null)
                return false;
        } else if (!departureDate.equals(other.departureDate))
            return false;
        if (tripStatus != other.tripStatus)
            return false;
        if (Double.doubleToLongBits(totalTripCost) != Double.doubleToLongBits(other.totalTripCost))
            return false;
        if (everyPassengerStatusMap == null) {
            if (other.everyPassengerStatusMap != null)
                return false;
        } else if (!everyPassengerStatusMap.equals(other.everyPassengerStatusMap))
            return false;
        return true;
    }

}
