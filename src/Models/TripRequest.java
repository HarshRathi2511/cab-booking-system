package Models;

import java.util.Date;

import Enums.TripRequestStatus;
import Services.AdminService;

public class TripRequest extends Thread{
    private Student student;
    private Date date;
    private String startLocation;
    private String endLocation;

    
    public TripRequest(Student student, Date date, String startLocation, String endLocation){
        this.student = student;
        this.date = date ;
        this.startLocation = startLocation;
        this.endLocation = endLocation;

        //enum 
        // this.tripRequestFeedback = TripRequestStatus.NO_RESPONSE; 
    }
     

    public Student getStudent() {
        return student;
    }
    public void setStudent(Student student) {
        this.student = student;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public String getStartLocation() {
        return startLocation;
    }
    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }
    public String getEndLocation() {
        return endLocation;
    }
    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }
    public String getTripId(){  //eg pilani_jaipur_30
        return this.startLocation+"_"+ this.endLocation+"_"+this.date.getDate();
    }

    //this method will be executed by the thread 
    public void run(){
        //to add (some checks for this)
        AdminService.handleRequests(this); 
        // AdminService.debugRequests();
    }
    @Override
    public String toString() {
        return "TripRequest [student=" + student + ", date=" + date + ", startLocation=" + startLocation
                + ", endLocation=" + endLocation + ", tripRequestFeedback="  + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((student == null) ? 0 : student.hashCode());
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((startLocation == null) ? 0 : startLocation.hashCode());
        result = prime * result + ((endLocation == null) ? 0 : endLocation.hashCode());
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
        TripRequest other = (TripRequest) obj;
        if (student == null) {
            if (other.student != null)
                return false;
        } else if (!student.equals(other.student))
            return false;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
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
        return true;
    }

}

