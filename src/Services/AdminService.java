package Services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import Enums.TripRequestStatus;
import Enums.TripStatus;
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

    //each key is the student id as it is unique 
    private static Map<String,ArrayList<TripRequestFromAdmin>> mapOftripRequestFromAdmins = new HashMap<String,ArrayList<TripRequestFromAdmin>>();

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

    // private void toggleIncomingRequestsLock(Boolean lock) {
    // if (lock) {
    // requestLock.lock();
    // } else {
    // requestLock.unlock();
    // }
    // }

    public static synchronized void handleRequests(TripRequest tripRequest) {

        // need an implementation to lock this method when grouping is taking place

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

    // method to group the people
    public static void groupTravellers() {
        for (String tripDateDestKey : requestsMap.keySet()) {
            // create a new thread which executes this method
            // ThreadGroup groThreadGroup = new ThreadGroup("groupingThreadGroup");
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
    // NOT WORKING PROPERLY !!!1
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

    // after the grouping send out requests to each user
    public static void sendTripRequests(ArrayList<TripRequest> tripRequests) {
        Trip generatedTrip = generateTripFromTripRequests(tripRequests);

        // create a TripRequestFromAdmin instance for each user
        for (TripRequest tripRequest : tripRequests) {

            Student indvStudent = tripRequest.getStudent();
            TripRequestFromAdmin tripRequestFromAdmin = new TripRequestFromAdmin(indvStudent, generatedTrip,
                    TripRequestStatus.NO_RESPONSE); // no response as the user has yet to submit accepted and rejected
                   

                    //add it in the map of requests 
                    if (mapOftripRequestFromAdmins.containsKey(indvStudent.getId())) {
                        // add in the array list
                        mapOftripRequestFromAdmins.get(indvStudent.getId()).add(tripRequestFromAdmin);
                    } else {
                        ArrayList<TripRequestFromAdmin> newRequestArray = new ArrayList<TripRequestFromAdmin>();
                        newRequestArray.add(tripRequestFromAdmin);
                        mapOftripRequestFromAdmins.put(tripRequest.getTripId(), newRequestArray);
                    }
        }
    }

    //get the pendng list of trip requests for an individual 
    public static ArrayList<TripRequestFromAdmin> getPendingRequestsForAStudent(Student student){
        ArrayList<TripRequestFromAdmin> tripRequestForStudent = new ArrayList<TripRequestFromAdmin>();

        if (mapOftripRequestFromAdmins.containsKey(student.getId())) {
            // add in the array list
            return mapOftripRequestFromAdmins.get(student.getId());
        } else {
            ArrayList<TripRequestFromAdmin> emptyRequestsFromAdmin = new ArrayList<TripRequestFromAdmin>();
            return emptyRequestsFromAdmin;
        }
        
    }

    //respond acccepted or rejected to a particular trip request 
    public static void respondToARequest(TripRequestFromAdmin tripRequestFromAdmin , TripRequestStatus tripRequestResponse){
        //find the student and then change the map of vals also 
        Student student = tripRequestFromAdmin.getStudent();

        if (mapOftripRequestFromAdmins.containsKey(student.getId())) {
            // find the trip request and then append it 
            ArrayList<TripRequestFromAdmin> tripRequestFromAdminsForStudent = mapOftripRequestFromAdmins.get(student.getId());
            if(tripRequestFromAdminsForStudent.contains(tripRequestFromAdmin)){
                //append it 
                int i = tripRequestFromAdminsForStudent.indexOf(tripRequestFromAdmin);
                //change it in the map 
                tripRequestFromAdminsForStudent.get(i).setTripRequestStatus(tripRequestResponse);
            }

           
        } else {
            //this student is not registered 
            //throw exception for this
        }

    }


    // make the Trip class from the grouped travellers
    public static Trip generateTripFromTripRequests(ArrayList<TripRequest> tripRequests) {
        ArrayList<Student> coPassengers = new ArrayList<Student>();
        // add the students
        for (TripRequest tripRequest : tripRequests) {
            coPassengers.add(tripRequest.getStudent());
        }
        // should be the max of all the preferred dates
        Date dateTimeOfTravel = tripRequests.stream().map(t -> t.getDate()).max(Date::compareTo).get();

        double totalTripCost = generateTripCost(tripRequests.get(0).getStartLocation(),
                tripRequests.get(0).getEndLocation());

        String tripId = tripRequests.get(0).getTripId() + tripRequests.get(0).getDate().toString();

        // trip status :- check every request (and see if it is accepted)
        TripStatus tripStatus = TripStatus.PROPOSED;

        Trip generatedTrip = new Trip(coPassengers, tripId, tripRequests.get(0).getStartLocation(),
                tripRequests.get(0).getEndLocation(), dateTimeOfTravel, tripStatus, totalTripCost);

        return generatedTrip;
    }

    public static double generateTripCost(String startLocation, String endLocation) {
        // complete this method @utkarsh (hardcode vals) :- Jaipur to Delhi etc
        return 0.0;
    }

    // method: for sending out requests for each student (create a new class named
    // Request
    // indv status ,)
    // then according to the responses modify the trip instance

    // if the user has rejected the request sent to him just remove him from the
    // list of copassengers

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
}
