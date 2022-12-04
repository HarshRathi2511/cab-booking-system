
package Services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import Constants.Constants;
import Enums.TripRequestStatus;
import Enums.TripStatus;
import Interfaces.AdminInterface;
import Interfaces.AdminServiceInterface;
import Models.*;

public class AdminService implements Serializable{
    private static List<Student> registeredStudents = new ArrayList<Student>();

    //file handling variables 
    static File file=new File("Student.txt");
     static ObjectOutputStream oos=null;
     static ObjectInputStream ois=null;

    // to handle all the incoming requests
    private static Map<String, ArrayList<TripRequest>> requestsMap = new HashMap<String, ArrayList<TripRequest>>();

    private static Comparator<TripRequest> cmTripReq = (TripRequest tr1, TripRequest tr2) -> tr1.getDate()
            .compareTo(tr2.getDate());

    private static ArrayList<ArrayList<TripRequest>> groupedTravellers = new ArrayList<ArrayList<TripRequest>>();
    private static Lock requestLock = new ReentrantLock(true);

    // each key is the student id as it is unique
    private static Map<String, ArrayList<TripRequestFromAdmin>> mapOftripRequestFromAdminsForIndvStudent = new HashMap<String, ArrayList<TripRequestFromAdmin>>();

    // map which has the list of trips :- trip id as the key and the Trip instance
    // as the value
    private static Map<String, Trip> mapOfTrips = new HashMap<String, Trip>();

    private static ExecutorService addRequestThreadPool = Executors.newFixedThreadPool(10);
    public static ExecutorService groupTravllersThreadPool = Executors
            .newFixedThreadPool(10);

    // public static boolean registerStudent(Student student) {
    //     if (registeredStudents.contains(student)) {
    //         // already registered
    //         System.out.println("Student Already Registered");
    //         return true;
    //     } else {
    //         registeredStudents.add(student);
    //         return false;
    //     }
    // }
    public static boolean registerStudent(Student student) throws java.io.IOException,ClassNotFoundException{
        if(AdminService.file.isFile()){
            ois=new ObjectInputStream(new FileInputStream(file));
            registeredStudents=(ArrayList<Student>)ois.readObject();
            if(registeredStudents.contains(student)){
                System.out.println("Student Already Registered");
                return true;
            }
            else{
                registeredStudents.add(student);
                return false;
            }
        }
        else {


            if (registeredStudents.contains(student)) {
                // already registered
                System.out.println("Student Already Registered");
                return true;
            } else {
                registeredStudents.add(student);
                return false;
            }
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

    // helper methods : to add the trip in the map
    public static void addTripToMap(String tripId, Trip trip) {
        // if(mapOfTrips.containsKey(tripId)){

        // mapOfTrips.put(tripId, trip);
        // }else {
        mapOfTrips.put(tripId, trip);
        // }
    }

    public static void removeTripFromMap(String tripId) {
  
        mapOfTrips.remove(tripId);
    }

    public static void addRequestFromStudent(Student student, Date date, String startLocation, String endLocation) {
        TripRequest t1 = new TripRequest(student, date, startLocation, endLocation);

        // diff implementation using Executor service
        try {
            addRequestThreadPool.execute(t1);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.print(e.getMessage());
        }

    }

    public static void removeProposedTripsForAStudent(Student student) {
        if (mapOftripRequestFromAdminsForIndvStudent.containsKey(student.getId())) {
            // add in the array list
            ArrayList<TripRequestFromAdmin> emptyRequestsFromAdmin = new ArrayList<TripRequestFromAdmin>();
            mapOftripRequestFromAdminsForIndvStudent.put(student.getId(), emptyRequestsFromAdmin);
        } else {
            // nothing here
            
        }
    }

    // DO NOT USE THIS DIRECTLY :- HELPER METHOD FOR THE ABOVE FUNCTION
    public static synchronized void handleRequests(TripRequest tripRequest) {

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

        // while grouping the travellers no new requests should be made hence this process 
        addRequestThreadPool.shutdown();
        try {
            if (addRequestThreadPool.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                addRequestThreadPool.shutdownNow();
            }

        } catch (InterruptedException e) {
            // TODO: handle exception
            addRequestThreadPool.shutdownNow();
        }

        // old code
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
            try {
                groupingThread.join();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            // groupTravllersThreadPool.execute(groupingThread);
        }
    }

    // each thread is going to execute this method
    // searchKey :- pilani_jaipur_30 ()
    // not making it synchronized as it is immutable for this stage and many threads
    // are now working on it
    public static void groupTravellersInArrayList(String searchKey) {
        ArrayList<TripRequest> travelRequestArray = requestsMap.get(searchKey);
        ArrayList<TripRequest> helperRequestArray = new ArrayList<TripRequest>();
        for (int i = 0; i < travelRequestArray.size(); i++) {
            helperRequestArray.add(travelRequestArray.get(i));
        }

        Collections.sort(helperRequestArray, cmTripReq);

        while (helperRequestArray.size() != 0) {

            ArrayList<TripRequest> tripGroup = new ArrayList<TripRequest>();

            TripRequest firstRequest = helperRequestArray.get(0);

            tripGroup.add(firstRequest);

            // for (int i = 0; i < travelRequestArray.size(); i++) {

            // new subarray of the trip requests (later create a new thread to send invites
            // to each of the students //think about it )

            int j = 1;

            while (tripGroup.size() <= Trip.MAX_CO_PASSENGERS) {
                // check for time +- 30 mins to each student
                // find the period between
                // the start and end date
                if (j < helperRequestArray.size()) {
                    TripRequest currentRequest = helperRequestArray.get(j);

                    long duration = currentRequest.getDate().getTime() - firstRequest.getDate().getTime();

                    long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);

                    if (diffInMinutes <= 30 && tripGroup.size() < Trip.MAX_CO_PASSENGERS) {
                        tripGroup.add(currentRequest);
                        System.out.println(tripGroup.size());

                        j++;

                    } else {
                        helperRequestArray.remove(tripGroup);
                        break;
                    }
                } else
                    break;

                // check for ensuring next element exists
            }
            for (TripRequest i : tripGroup) {
                helperRequestArray.remove(i);
            }
            // then handle the trip sizes

            // System.out.println(tripGroup);
            groupedTravellers.add(tripGroup);
        }

    }

    // after the grouping send out requests to each user
    public static void sendTripRequests(ArrayList<TripRequest> tripRequests) {
        Trip generatedTrip = generateTripFromTripRequests(tripRequests);

        // store that trip generated in the map (here first time the trip objects is
        // generated)
        addTripToMap(generatedTrip.getTripId(), generatedTrip);

        // create a TripRequestFromAdmin instance for each user
        for (TripRequest tripRequest : tripRequests) {

            Student indvStudent = tripRequest.getStudent();
            TripRequestFromAdmin tripRequestFromAdmin = new TripRequestFromAdmin(indvStudent, generatedTrip,
                    TripRequestStatus.NO_RESPONSE); // no response as the user has yet to submit accepted and rejected

            // add it in the map of requests
            if (mapOftripRequestFromAdminsForIndvStudent.containsKey(indvStudent.getId())) {
                // add in the array list
                mapOftripRequestFromAdminsForIndvStudent.get(indvStudent.getId()).add(tripRequestFromAdmin);
            } else {
                ArrayList<TripRequestFromAdmin> newRequestArray = new ArrayList<TripRequestFromAdmin>();
                newRequestArray.add(tripRequestFromAdmin);
                mapOftripRequestFromAdminsForIndvStudent.put(indvStudent.getId(), newRequestArray);
            }

            System.out.println("sent trip request to " + indvStudent.getName());
        }
    }

    // get the pendng list of trip requests for an individual
    public static ArrayList<TripRequestFromAdmin> getPendingRequestsForAStudent(Student student) {
        ArrayList<TripRequestFromAdmin> tripRequestForStudent = new ArrayList<TripRequestFromAdmin>();

        if (mapOftripRequestFromAdminsForIndvStudent.containsKey(student.getId())) {
            // add in the array list

            return mapOftripRequestFromAdminsForIndvStudent.get(student.getId());
        } else {
            ArrayList<TripRequestFromAdmin> emptyRequestsFromAdmin = new ArrayList<TripRequestFromAdmin>();
            return emptyRequestsFromAdmin;
        }

    }

    // respond acccepted or rejected to a particular trip request
    public static void respondToARequest(TripRequestFromAdmin tripRequestFromAdmin,
            TripRequestStatus tripRequestResponse) {
        // find the student and then change the map of vals also
        Student student = tripRequestFromAdmin.getStudent();

        // set the trip response in the map for indv students
        if (mapOftripRequestFromAdminsForIndvStudent.containsKey(student.getId())) {
            // find the trip request and then append it
            ArrayList<TripRequestFromAdmin> tripRequestFromAdminsForStudent = mapOftripRequestFromAdminsForIndvStudent
                    .get(student.getId());
            if (tripRequestFromAdminsForStudent.contains(tripRequestFromAdmin)) {
                // append it
                int i = tripRequestFromAdminsForStudent.indexOf(tripRequestFromAdmin);
                // change it in the map
                tripRequestFromAdminsForStudent.get(i).setTripRequestStatus(tripRequestResponse);
            }

        } else {
            System.out.println("Student not registered");
            // this student is not registered
            // throw exception for this
        }

        // set it in the trip map
        // change the trip object
        Trip underlyingTrip = tripRequestFromAdmin.getTrip();
        // change vals in the map
        HashMap<String, TripRequestStatus> everyPassengerStatusMap = underlyingTrip.getEveryPassengerStatusMap();
        //
        for (String studentId : everyPassengerStatusMap.keySet()) {
            if (student.getId() == studentId) {
                everyPassengerStatusMap.put(studentId, tripRequestResponse);
            }

        }

        underlyingTrip.setEveryPassengerStatusMap(everyPassengerStatusMap);
        addTripToMap(underlyingTrip.getTripId(), underlyingTrip);

    }

    // method:- parsing for each trip and then generate which trip is feasible
    // (this method runs after everyone has responded yes or no)
    public static void finalSchedulingAndChangingTripStatus() {
        List<String> listOfTripsToRemove = parseTripGroupsForStartingTrip();
        for (String tripToRemoveId : listOfTripsToRemove) {
            removeTripFromMap(tripToRemoveId);
        }
    }

    private static List<String> parseTripGroupsForStartingTrip() {
        // list of trip ids which are supposed to deleted :- this is used to avoid the
        // concurrent modification exception
        List<String> listOfTripsToRemove = new ArrayList<String>();
        for (String tripId : mapOfTrips.keySet()) {
            // iterate
            Trip trip = mapOfTrips.get(tripId);
            HashMap<String, TripRequestStatus> everyCoPassengerStatusMap = trip.getEveryPassengerStatusMap();

            // copassengers to remove from the array
            List<Student> coPassengersWhoRejectedOrNoRes = new ArrayList<Student>();
            List<Student> passengersInTrip = new ArrayList<Student>();

            for (Student coPassenger : trip.getCoPassengers()) {
                if (everyCoPassengerStatusMap.get(coPassenger.getId()) == TripRequestStatus.REJECTED
                        | everyCoPassengerStatusMap.get(coPassenger.getId()) == TripRequestStatus.NO_RESPONSE) {
                    // remove that person from the map
                    coPassengersWhoRejectedOrNoRes.add(coPassenger);

                } else {
                    // accepted status
                    passengersInTrip.add(coPassenger);
                }
            }
            // set the corresponding diagram
            trip.setCoPassengers(passengersInTrip);

            // if every copassenger rejected this request
            if (trip.getCoPassengers().size() == 0) {
                // delete this trip from the map
                // removeTripFromMap(tripId);
                listOfTripsToRemove.add(tripId);
                System.out.print("entered danger area");

            } else {
                //  update the indv cost for each student according to number of
                // copassengers
                double totalTripCost = generateTripCost(trip.getStartLocation(), trip.getEndLocation());
                changeAmountOwedForStudentsAfterTrip(passengersInTrip, totalTripCost);

                // now the trip is SCHEDULED
                trip.setTripStatus(TripStatus.SCHEDULED);
                // update the trip instance in the map
                addTripToMap(tripId, trip);
            }

        }
        return listOfTripsToRemove;
    }

    private static void changeAmountOwedForStudentsAfterTrip(List<Student> students, double totalCost) {
        double amountOwedForEachStudent = totalCost / students.size();

        for (Student st : students) {
            st.addTotalCost(amountOwedForEachStudent);
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
        // generating a unique trip id for each trip
        String tripId = tripRequests.get(0).getTripId() + tripRequests.get(0).getDate().toString()
                + tripRequests.get(0).getStudent().toString();

        // trip status :- check every request (and see if it is accepted)
        TripStatus tripStatus = TripStatus.PROPOSED;

        Trip generatedTrip = new Trip(coPassengers, tripId, tripRequests.get(0).getStartLocation(),
                tripRequests.get(0).getEndLocation(), dateTimeOfTravel, tripStatus, totalTripCost);

        return generatedTrip;
    }

    private static double generateTripCost(String startLocation, String endLocation) {
        // complete this method @utkarsh (hardcode vals) :- Jaipur to Delhi etc
        double value = 0;
        if ((startLocation == Constants.Delhi && endLocation == Constants.Pilani)
                || (startLocation == Constants.Pilani && endLocation == Constants.Delhi)) {
            value = 1500;

        }
        if ((startLocation == Constants.Jaipur && endLocation == Constants.Pilani)
                || (startLocation == Constants.Pilani && endLocation == Constants.Jaipur)) {

            value = 1000;

        }

        return value;
    }

    public static void fileWriter() {
        try {
            oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(registeredStudents);
            oos.close();
        }
        catch(Exception e){
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

    public static void debugTrips() {
        System.out.println("DEBUGGING TRIPS");

        for (String key : mapOfTrips.keySet()) {
            System.out.println();
            System.out.println(key);
            System.out.println();
            Trip tr = mapOfTrips.get(key);
            System.out.println(tr.toString());

        }
    }

    public static void debugGroups() {
        // System.out.println(requestsMap);

        System.out.println(groupedTravellers.size());
        System.out.println();

        System.out.println();

        for (ArrayList<TripRequest> groupedTravellersArray : groupedTravellers) {
            System.out.println("subgroup size and elements are :- " + groupedTravellersArray.size());
            for (TripRequest tripRequest : groupedTravellersArray) {
                System.out.println(tripRequest.toString());
            }
        }
    }

    public static void debugStudents() {
        for (Student registeredStudents : AdminService.getRegisteredStudents()) {
            System.out.println(registeredStudents.toString());
        }
        ;
    }

    public static void setRegisteredStudents(List<Student> registeredStudents) {
        AdminService.registeredStudents = registeredStudents;
    }

    public static Map<String, ArrayList<TripRequest>> getRequestsMap() {
        return requestsMap;
    }

    public static void setRequestsMap(Map<String, ArrayList<TripRequest>> requestsMap) {
        AdminService.requestsMap = requestsMap;
    }

    public static Comparator<TripRequest> getCmTripReq() {
        return cmTripReq;
    }

    public static void setCmTripReq(Comparator<TripRequest> cmTripReq) {
        AdminService.cmTripReq = cmTripReq;
    }

    public static ArrayList<ArrayList<TripRequest>> getGroupedTravellers() {
        return groupedTravellers;
    }

    public static void setGroupedTravellers(ArrayList<ArrayList<TripRequest>> groupedTravellers) {
        AdminService.groupedTravellers = groupedTravellers;
    }

    public static Lock getRequestLock() {
        return requestLock;
    }

    public static void setRequestLock(Lock requestLock) {
        AdminService.requestLock = requestLock;
    }

    public static Map<String, ArrayList<TripRequestFromAdmin>> getMapOftripRequestFromAdminsForIndvStudent() {
        return mapOftripRequestFromAdminsForIndvStudent;
    }

    public static void setMapOftripRequestFromAdminsForIndvStudent(
            Map<String, ArrayList<TripRequestFromAdmin>> mapOftripRequestFromAdminsForIndvStudent) {
        AdminService.mapOftripRequestFromAdminsForIndvStudent = mapOftripRequestFromAdminsForIndvStudent;
    }

    public static Map<String, Trip> getMapOfTrips() {
        return mapOfTrips;
    }

    public static void setMapOfTrips(Map<String, Trip> mapOfTrips) {
        AdminService.mapOfTrips = mapOfTrips;
    }
}