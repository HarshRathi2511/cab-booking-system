import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import Constants.Constants;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import Constants.Constants;
import Enums.TripRequestStatus;
import Models.*;
import Services.AdminService;

/**
 * CabBookingApplication
 */
public class CabBookingApplication {

    public static void main(String[] args) throws Exception {
        System.out.println("Cab Booking Application");

        // register some students
        Student s1 = new Student(
                "Harsh", 9, "1");
        Student s2 = new Student(
                "YS", 9, "2");
        Student s3 = new Student(
                "Utkarsh", 9, "3");
        Student s4 = new Student(
                "Devayush", 9, "4");
        Student s5 = new Student(
                "haha", 9, "5");
        AdminService.registerStudent(s1);
        AdminService.registerStudent(s2);
        AdminService.registerStudent(s3);
        AdminService.registerStudent(s4);
        AdminService.registerStudent(s5);

        System.out.println(AdminService.getRegisteredStudents().size() + " students registered");

        // dates
        String d1 = "31-Dec-1998 20:37:50";
        String d2 = "31-Dec-1998 2:40:50";
        String d3 = "31-Dec-1998 23:58:50";
        String d4 = "31-Dec-1998 23:58:50";
        String d5 = "31-Dec-1998 23:59:50";

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        Date date1 = formatter.parse(d1);
        Date date2 = formatter.parse(d2);
        Date date3 = formatter.parse(d3);
        Date date4 = formatter.parse(d4);
        Date date5 = formatter.parse(d5);

        TripRequest t1 = new TripRequest(s1, date1, Constants.Pilani, Constants.Delhi);
        TripRequest t2 = new TripRequest(s2, date2, Constants.Pilani, Constants.Delhi);
        TripRequest t3 = new TripRequest(s3, date3, Constants.Pilani, Constants.Delhi);
        TripRequest t4 = new TripRequest(s4, date4, Constants.Pilani, Constants.Delhi);
        TripRequest t5 = new TripRequest(s5, date5, Constants.Pilani, Constants.Delhi);

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();

        t1.join();
        t2.join();
        t3.join();
        t4.join();
        t5.join();

        AdminService.debugRequests();

        //USE THIS TO START GROUPING USING MULTITHREADING
        AdminService.groupTravellers();
        AdminService.debugGroups();
    }
}


