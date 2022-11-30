package Services;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import Interfaces.AdminInterface;
import Models.*;

public class AdminService implements AdminInterface{
    private static List<Student> registeredStudents = new ArrayList<Student>();

    //to handle all the incoming requests 
    private static Map<String,ArrayList<TripRequest>> requestsMap = new HashMap<String,ArrayList<TripRequest>>();

    private Comparator<TripRequest> cmTripReq = (TripRequest tr1, TripRequest tr2)->tr1.getDate().compareTo(tr2.getDate());
    
    public static void registerStudent( Student student){
        if(registeredStudents.contains(student)){
            //already registered 
            System.out.println("Student Already Registered");
        }else {
           registeredStudents.add(student);
        }
    }

    public static List<Student> getRegisteredStudents() {
        return registeredStudents;
    }

    public static synchronized void handleRequests(TripRequest tripRequest){
        if(requestsMap.containsKey(tripRequest.getTripId())){
           //add in the array list 
           requestsMap.get(tripRequest.getTripId()).add(tripRequest);
        }else {
            ArrayList<TripRequest> newRequestArray = new ArrayList<TripRequest>();
            newRequestArray.add(tripRequest);
            requestsMap.put(tripRequest.getTripId(), newRequestArray);
        }
    } 

    public static void debugRequests(){
        // System.out.println(requestsMap);

        for(String key : requestsMap.keySet()){
            System.out.println(key);
            for(TripRequest tr : requestsMap.get(key)){
                System.out.println(tr.toString());
            }
        }
    }

    



}
