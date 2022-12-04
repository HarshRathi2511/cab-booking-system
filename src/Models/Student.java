package Models;

import Interfaces.StudentInterface;
import java.io.*;
import java.util.ArrayList;

public class Student implements StudentInterface, Serializable {
    private String name;
    private long phoneNumber;
    private String id;
    private double totalCost;

    static File file = new File("Student.txt");
    static ObjectOutputStream oos = null;

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

    public long getPhoneNumber() {
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

    public static void register(String name, int phoneNumber, String id) throws java.io.IOException {
        Student st = new Student(name, phoneNumber, id);
        oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(st);
        oos.close();

    }

    public double getTotalCost() {
        return totalCost;
    }

    public void addTotalCost(double totalCost) {
        this.totalCost += totalCost;

    }

    // @Override
    // public long hashCode() {
    //     final int prime = 31;
    //     long result = 1;
    //     result = prime * result + ((name == null) ? 0 : name.hashCode());
    //     result = prime * result + phoneNumber;
    //     result = prime * result + ((id == null) ? 0 : id.hashCode());
    //     return result;
    // }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Student other = (Student) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (phoneNumber != other.phoneNumber)
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Student [name=" + name + ", phoneNumber=" + phoneNumber + ", id=" + id + ", totalCost=" + totalCost
                + "]";
    }

}
