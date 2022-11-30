package Services;

import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import Interfaces.AdminInterface;
import Models.*;

public class AdminService implements AdminInterface {
    private static List<Student> registeredStudents = new ArrayList<Student>();

    // to handle all the incoming requests
    private static Map<String, ArrayList<TripRequest>> requestsMap = new HashMap<String, ArrayList<TripRequest>>();

    private static Comparator<TripRequest> cmTripReq = (TripRequest tr1, TripRequest tr2) -> tr1.getDate()
            .compareTo(tr2.getDate());

    private static ArrayList<ArrayList<TripRequest>> groupedTravellers = new ArrayList<ArrayList<TripRequest>>();
    private static Lock requestLock = new ReentrantLock(true);

    public static void registerStudent(Student student) {
        if (registeredStudents.contains(student)) {
            // already registered
            System.out.println("Student Already Registered");
        } else {
            registeredStudents.add(student);
        }
    }

    public static List<Student> getRegisteredStudents() {
        return registeredStudents;
    }
    
    private void toggleIncomingRequestsLock(Boolean lock){
      if(lock){
        requestLock.lock();
      }else{
        requestLock.unlock();
      }
    }

    public static synchronized void handleRequests(TripRequest tripRequest) {

        //need an implementation to lock this method when grouping is taking place 

        // request
        try {
            if (requestsMap.containsKey(tripRequest.getTripId())) {
                // add in the array list
                requestsMap.get(tripRequest.getTripId()).add(tripRequest);
            } else {
                ArrayList<TripRequest> newRequestArray = new ArrayList<TripRequest>();
                newRequestArray.add(tripRequest);
                requestsMap.put(tripRequest.getTripId(), newRequestArray);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public static void debugRequests() {
        // System.out.println(requestsMap);

        for (String key : requestsMap.keySet()) {
            System.out.println(key);
            for (TripRequest tr : requestsMap.get(key)) {
                System.out.println(tr.toString());
            }
            System.out.println();
            System.out.println();
            System.out.println();
        }
    }

    public static void debugGroups() {
        // System.out.println(requestsMap);

        System.out.println(groupedTravellers.size());
        System.out.println();

        System.out.println();

        System.out.println(groupedTravellers);
    }

    // method to group the people
    public static void groupTravellers() {
        for (String tripDateDestKey : requestsMap.keySet()) {
            // create a new thread which executes this method
            Thread groupingThread = new Thread() {
                public void run() {
                    System.out.println("grouping Thread Started for " + tripDateDestKey);
                    groupTravellersInArrayList(tripDateDestKey);
                }
            };

            // start the new thread
            groupingThread.start();
        }
    }

    // each thread is going to execute this method
    // searchKey :- pilani_jaipur_30 ()
    // not making it synchronized as it is immutable for this stage and many threads
    // are now working on it
    public static void groupTravellersInArrayList(String searchKey) {
        ArrayList<TripRequest> travelRequestArray = requestsMap.get(searchKey);
        Collections.sort(travelRequestArray, cmTripReq); // sorted according to time

        System.out.println();
        System.out.println();

        for (int i = 0; i < travelRequestArray.size(); i++) {

            // new subarray of the trip requests (later create a new thread to send invites
            // to each of the students //think about it )
            ArrayList<TripRequest> tripGroup = new ArrayList<TripRequest>();

            while (tripGroup.size() < Trip.MAX_CO_PASSENGERS) {
                // check for time +- 30 mins to each student
                // find the period between
                // the start and end date

                // check for ensuring next element exists
                if (!(i + 1 < requestsMap.size())) {
                    TripRequest currentRequest = travelRequestArray.get(i);
                    TripRequest nextRequest = travelRequestArray.get(i + 1);
                    long duration = currentRequest.getDate().getTime() - nextRequest.getDate().getTime();

                    // long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
                    long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
                    // long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
                    // long diffInDays = TimeUnit.MILLISECONDS.toDays(duration);

                    if (diffInMinutes < 30) {
                        if (!tripGroup.contains(currentRequest)) {
                            tripGroup.add(currentRequest);
                        }
                        if (tripGroup.size() < Trip.MAX_CO_PASSENGERS) {
                            tripGroup.add(nextRequest);
                        } else {
                            // seats full
                            break;
                        }
                        i++;
                    } else {
                        // time diff more group the current request and then check in the nxt iteration
                        // whether the next request can be grouped
                        if (!tripGroup.contains(currentRequest)) {
                            tripGroup.add(currentRequest);

                        }

                        break;
                    }

                } else {
                    // group that element seperately as no other request there
                    tripGroup.add(travelRequestArray.get(i));
                    break;
                }

            }

            // then handle the trip sizes
            System.out.println();
            System.out.println();
            System.out.println(tripGroup);
            groupedTravellers.add(tripGroup);

        }

    }
}
