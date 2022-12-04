package Interfaces;

import java.util.*;

import Enums.*;
import Models.*;

public interface AdminServiceInterface {
    Boolean registerStudent(Student student);

    List<Student> getRegisteredStudents();

    void addRequestFromStudent(Student student, Date date, String startLocation, String endLocation);

    void groupTravellers();

    void sendTripRequests(ArrayList<TripRequest> tripRequests);

    ArrayList<TripRequestFromAdmin> getPendingRequestsForAStudent(Student student);

    void respondToARequest(TripRequestFromAdmin tripRequestFromAdmin,
            TripRequestStatus tripRequestResponse);

    void finalSchedulingAndChangingTripStatus();
}
