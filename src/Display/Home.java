package Display;
import javax.swing.*;

public class Home extends JFrame{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTabbedPane tabbedPane;
	private JFrame newFrame;

    public Home( StudentPanel studentsP, AdminPanel adminP) {
    	
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.add("Student", studentsP);
        tabbedPane.add("Admin",adminP);

        this.getContentPane().add(tabbedPane);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Car Rental Office");
        this.setSize(1140, 600);
        this.setResizable(false);

    }
    
    public static void main(String[] args) {
    	AdminPanel adminP = new AdminPanel();
    	StudentPanel StudentP = new StudentPanel();
    	Home h = new Home(StudentP , adminP);
    	h.setVisible(true);
    	
    	
    }
}