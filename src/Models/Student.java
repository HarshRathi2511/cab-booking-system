package Models;

import Interfaces.StudentInterface;

public class Student implements StudentInterface {
    private String name;
    private int phoneNumber;
    private String id;

    public Student(String name, int phoneNumber, String id) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
}
