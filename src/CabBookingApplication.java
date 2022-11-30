import java.text.SimpleDateFormat;
import java.util.Date;

import Constants.Constants;
import Models.Student;
import Models.TripRequest;
import Services.AdminService;

/**
 * CabBookingApplication
 */
public class CabBookingApplication {

    public static void main(String[] args) throws Exception{
        System.out.println("Cab Booking Application");

        //register some students 
        Student s1 = new Student(
            "Harsh",9,"2020"
        );
        Student s2 = new Student(
            "YS",9,"2020"
        );
        Student s3 = new Student(
            "uTKARS",9,"2020"
        );
        
        //dates 
        String d1 = "31-Dec-1998 23:37:50";  
        String d2 = "31-Dec-1998 23:40:50";  
        String d3 = "31-Dec-1998 23:58:50";  
        SimpleDateFormat formatter=new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        Date date1=formatter.parse(d1);  
        Date date2=formatter.parse(d1);  
        Date date3=formatter.parse(d1);  

        
        
        TripRequest t1 = new TripRequest(s1,date1,Constants.Pilani, Constants.Delhi);
        TripRequest t2 = new TripRequest(s2,date2,Constants.Pilani, Constants.Jaipur);
        TripRequest t3 = new TripRequest(s3,date3,Constants.Pilani, Constants.Delhi);

        t1.start();
        t2.start();
        t3.start();
        
        t1.join();
        t2.join();
        t3.join();

        AdminService.debugRequests();

    }
}