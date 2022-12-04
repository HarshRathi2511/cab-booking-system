package Models;

public class admin {
    private String username = "Admin";
    private String password = "Password";
    public boolean logincheck = false;

    public void admin_login(String admin, String password) {
        if (this.username.compareTo(admin) == 0 && this.password.compareTo(password) == 0) {
            logincheck = true;
            System.out.println("Welcome");


        } else {
            System.out.println("Error");
        }
    }

    public void admin_logout() {
        if (logincheck) {
            logincheck = false;
            System.out.println("admin logged out");
        } else
            System.out.println("Error");


    }
}
