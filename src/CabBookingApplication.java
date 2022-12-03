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
        // AdminService.groupTravellers();
        
        //ITERATE OVER EVERY SEARCH KEY AND USE THE SORTING ALGORITHM TO GET REQUESTS GROUPED TOGETHER 
        // ONE SINGLE OPERATION FOR ALL THE SEARCH KEYS 
        String searchKey = "Pilani_Delhi_31";
        ArrayList<TripRequest> requestsGroupedTogether = AdminService.getRequestsMap().get(searchKey);
        Trip trip = AdminService.generateTripFromTripRequests(requestsGroupedTogether);
        // System.out.print(trip.toString()); //trip has been created

        AdminService.addTripToMap(searchKey, trip);
        // now send invites
        AdminService.sendTripRequests(requestsGroupedTogether);

        // System.out.println(AdminService.getMapOftripRequestFromAdminsForIndvStudent());


        //...................USE THESE FUNCTIONS FOR CLI 
        // prints the pending requests for each student
        for (int i = 0; i < AdminService.getRegisteredStudents().size(); i++) {
            System.out.println();
            Student student = AdminService.getRegisteredStudents().get(i);

            ArrayList<TripRequestFromAdmin> pendingRequestForStudent = AdminService
                    .getPendingRequestsForAStudent(student);
            System.out.println(pendingRequestForStudent.size() + " requests for student " + student.getName());

            for (TripRequestFromAdmin requestFromAdmin : pendingRequestForStudent) {
                System.out.println(requestFromAdmin.toString());
            }
        }
        
        // now try reponding to each of the requests
        for (int i = 0; i < AdminService.getRegisteredStudents().size(); i++) {
            System.out.println();
            Student student = AdminService.getRegisteredStudents().get(i);

            ArrayList<TripRequestFromAdmin> pendingRequestForStudent = AdminService
                    .getPendingRequestsForAStudent(student);
            System.out.println("responding to requests:-  " + pendingRequestForStudent.size() + " requests for student "
                    + student.getName());

            for (TripRequestFromAdmin requestFromAdmin : pendingRequestForStudent) {
                // respond with accepted
                // if (requestFromAdmin.getStudent()==s1) {
                //     // AdminService.respondToARequest(requestFromAdmin, TripRequestStatus.REJECTED);
                // }
                //USE THIS FUNCTION IN CLI ARGUMENT 
                AdminService.respondToARequest(requestFromAdmin, TripRequestStatus.REJECTED);
            }
        }

        // then try printing the trip map
        System.out.println(AdminService.getMapOfTrips());

        System.out.println("\n");

        

        System.out.println("\n");

        System.out.println("Parsing trips after students have responded");
        System.out.println("\n");

        //THIS FUNCTION HAS SOME BUG:- code hangs here
        AdminService.parseTripGroupsForStartingTrip();
        
        //PRINTS FINAL TRIPS THAT HAVE BEEN SCHEDULED AFTER PARSING 
        System.out.println(AdminService.getMapOfTrips());

        // try {
        // TimeUnit.SECONDS.sleep(4);
        // } catch (InterruptedException ie) {
        // Thread.currentThread().interrupt();
        // }

        // AdminService.debugGroups();

    }
}


