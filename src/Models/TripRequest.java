package Models;

import java.util.Date;

import Enums.TripRequestStatus;
import Services.AdminService;

public class TripRequest extends Thread{
    private Student student;
    private Date date;
    private String startLocation;
    private String endLocation;
    //trip requests 
    //reject :- remove it from the array () 
    private TripRequestStatus tripRequestFeedback; //accepted | rejected | no response 

    
    public TripRequest(Student student, Date date, String startLocation, String endLocation){
        this.student = student;
        this.date = date ;
        this.startLocation = startLocation;
        this.endLocation = endLocation;

        //enum 
        this.tripRequestFeedback = TripRequestStatus.NO_RESPONSE; 
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
    public TripRequestStatus getTripRequestFeedback() {
        return tripRequestFeedback;
    }
    public void setTripRequestFeedback(TripRequestStatus tripRequestFeedback) {
        this.tripRequestFeedback = tripRequestFeedback;
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
                + ", endLocation=" + endLocation + ", tripRequestFeedback=" + tripRequestFeedback + "]";
    } 
    
}
