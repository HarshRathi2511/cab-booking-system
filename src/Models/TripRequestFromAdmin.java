package Models;

import Enums.TripRequestStatus;

//If this instance has been created then the request has been sent to the user (this is after grouping the members )
public class TripRequestFromAdmin {
    @Override
    public String toString() {
        return "TripRequestFromAdmin [student=" + student + ", trip=" + trip + ", tripRequestStatus="
                + tripRequestStatus + "]";
    }
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
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((student == null) ? 0 : student.hashCode());
        result = prime * result + ((trip == null) ? 0 : trip.hashCode());
        result = prime * result + ((tripRequestStatus == null) ? 0 : tripRequestStatus.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TripRequestFromAdmin other = (TripRequestFromAdmin) obj;
        if (student == null) {
            if (other.student != null)
                return false;
        } else if (!student.equals(other.student))
            return false;
        if (trip == null) {
            if (other.trip != null)
                return false;
        } else if (!trip.equals(other.trip))
            return false;
        if (tripRequestStatus != other.tripRequestStatus)
            return false;
        return true;
    }

}
    

