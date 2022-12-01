package Models;

import Enums.TripRequestStatus;

//If this instance has been created then the request has been sent to the user (this is after grouping the members )
public class TripRequestFromAdmin {
    private Student student;
    private Trip trip; //for which trip the admin has sent the request 
    private TripRequestStatus tripRequestStatus;
    
    public TripRequestFromAdmin(Student student, Trip trip, TripRequestStatus tripRequestStatus) {
        this.student = student;
        this.trip = trip;
        this.tripRequestStatus = tripRequestStatus;
    }
    public Student getStudent() {
        return student;
    }
    public void setStudent(Student student) {
        this.student = student;
    }
    public Trip getTrip() {
        return trip;
    }
    public void setTrip(Trip trip) {
        this.trip = trip;
    }
    public TripRequestStatus getTripRequestStatus() {
        return tripRequestStatus;
    }
    public void setTripRequestStatus(TripRequestStatus tripRequestStatus) {
        this.tripRequestStatus = tripRequestStatus;
    }
    
}
