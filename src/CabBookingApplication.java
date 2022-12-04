import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import Constants.Constants;
import Display.AdminPanel;
import Display.Home;
import Display.StudentPanel;

/**
 * CabBookingApplication
 */
public class CabBookingApplication {

        public static void main(String[] args) {
                AdminPanel adminP = new AdminPanel();
                StudentPanel StudentP = new StudentPanel();
                Home h = new Home(StudentP, adminP);
                h.setVisible(true);

        }
}
