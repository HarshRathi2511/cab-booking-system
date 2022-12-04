import Display.AdminPanel;
import Display.Home;
import Display.StudentPanel;
import Services.AdminService;

/**
 * CabBookingApplication
 */
public class CabBookingApplication {

        public static void main(String[] args) {
                AdminPanel adminP = new AdminPanel();
                StudentPanel StudentP = new StudentPanel();
                Home h = new Home(StudentP, adminP);
                h.setVisible(true);

                Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                        public void run() {
                            // This block is run on shutdown
                            AdminService.fileWriter();
            
                        }
                    }, "Shutdown-thread"));

        }
}
