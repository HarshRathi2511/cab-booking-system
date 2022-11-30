package Models;

import java.util.Date;

public class TripRequest {
    private Student student;
    private Date date;
    private String startLocation;
    private String endLocation;

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
    public String generateTripId(){
        return this.startLocation+"_"+ this.endLocation;
    }
    
}
