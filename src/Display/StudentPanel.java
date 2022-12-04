package Display;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import Services.AdminService;
import Models.TripRequest;
import Models.Trip;
import Models.Student;

public class StudentPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel name;
	private JLabel phoneNumber;
	private JLabel ID;
	private JTextField nameField;
	private JTextField phoneField;
	private JTextField IDField;
	private JButton regButton;
	private JComboBox comboBox;
	private JComboBox comboBox_1;
	private JLabel lblNewLabel;

	public StudentPanel() {

		this.setLayout(new BorderLayout());
		
		
		this.name = new JLabel("NAME: ");
		this.name.setFont(new Font("Ariel", Font.PLAIN, 15));
		this.phoneNumber = new JLabel("Phone Number: ");
		this.phoneNumber.setFont(new Font("Ariel", Font.PLAIN, 15));
		this.ID = new JLabel("ID ");
		this.ID.setFont(new Font("Ariel", Font.PLAIN, 15));
		this.nameField = new JTextField(20);
		this.phoneField = new JTextField(20);
		this.IDField = new JTextField(20);
		this.regButton = new JButton("REGISTER");
		this.regButton.setFont(new Font("Ariel", Font.PLAIN, 15));
		this.regButton.setBounds(20, 20, 10, 10);
		this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.lightGray), "Register"));
		
		JPanel panel = new JPanel();
		panel.setSize(1000, 500);
		panel.add(name);
		panel.add(nameField);
		panel.add(ID);
		panel.add(IDField);
		panel.add(phoneNumber);
		panel.add(phoneField);
		panel.add(regButton);
		panel.setVisible(true);
		this.add(panel, BorderLayout.NORTH);
		
		JPanel panel2 = new JPanel();
		this.add(panel2);
		
		
		panel2.add(new JLabel("ID NO :"));
		JTextField Id2 = new JTextField(20);
		panel2.add(Id2);
		JButton login = new JButton("LOGIN"); 
		panel2.add(login);
		
		login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean there = false;
				for(Student s :AdminService.getRegisteredStudents()) {
					if(s.getId().equals(Id2.getText()))
					{
						there = true;
						new AddTrips(s).setVisible(true);
						setVisible(false);
					}
			}
				if(there==(false)) {
					JOptionPane.showMessageDialog(null, "Student Not Registered");
				}
			}
		});

		regButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Student s = new Student(nameField.getText(), Integer.parseInt(phoneField.getText()), IDField.getText());
				try {
					if (AdminService.registerStudent(s)) {
						JOptionPane.showMessageDialog(null, "Student Already Registered , LOGIN Instead");
					} else {
						new AddTrips(s).setVisible(true);
						setVisible(false);
					}
				} catch (Exception exception) {
					// TODO: handle exception
					System.out.println(exception.getMessage());
				}
				
			}
		});

	}

}
