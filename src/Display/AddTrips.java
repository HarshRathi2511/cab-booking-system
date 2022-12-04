package Display;

import javax.swing.*;

import Enums.TripRequestStatus;

import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;
import java.awt.event.*;
import Models.Student;
import Models.Trip;
import Models.TripRequest;
import Models.TripRequestFromAdmin;
import Services.AdminService;

public class AddTrips extends JFrame {

	private Student s;
	private TripRequest t;

	private static final long serialVersionUID = 1L;

	public AddTrips(Student s) {

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Add Trip");
		this.setSize(1140, 600);
		this.setResizable(true);

		JPanel panel2 = new JPanel();
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));

		panel2.setName("Add Trips");
		JButton backButton = new JButton("Go Back");
		backButton.setVerticalAlignment(SwingConstants.BOTTOM);
		backButton.setBounds(20, 20, 10, 10);
		panel2.add(backButton);
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				AdminPanel adminP = new AdminPanel();
				StudentPanel StudentP = new StudentPanel();
				Home h = new Home(StudentP, adminP);
				h.setVisible(true);
			}

		});

		JLabel TripID = new JLabel("ID NO: ");
		JLabel Date = new JLabel("Date and Time : ");

		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");

		JLabel Start = new JLabel("Start Location: ");
		JLabel End = new JLabel("End Location: ");

		panel2.add(TripID);
		JLabel IDField = new JLabel(s.getId());
		panel2.add(IDField);
		panel2.add(Date);

		JTextField dateField = new JTextField();
		panel2.add(dateField);
		dateField.setColumns(10);

		panel2.add(Start);
		String locations[] = { "Delhi", "Jaipur", "Pilani" };
		JComboBox comboBox = new JComboBox(locations);

		panel2.add(comboBox);
		panel2.add(End);

		JComboBox comboBox_1 = new JComboBox(locations);
		panel2.add(comboBox_1);

		JButton addButton = new JButton("ADD TRIP");
		addButton.setBounds(20, 20, 10, 10);
		panel2.add(addButton);
		panel2.setVisible(true);
		this.getContentPane().add(panel2, BorderLayout.NORTH);
		JLabel prop = new JLabel("Proposed Trips");
		prop.setFont(new Font("Tahoma", Font.PLAIN, 25));
		panel2.add(prop);

		ArrayList<TripRequestFromAdmin> propTrips = AdminService.getPendingRequestsForAStudent(s);
		for (TripRequestFromAdmin t1 : propTrips) {

			if (t1.getStudent().getId().equals(s.getId())) {
				for (Student s1 : t1.getTrip().getCoPassengers()) {
					JLabel s2 = new JLabel(s1.getName());
					JLabel id = new JLabel(s1.getId());
					panel2.add(s2);
					panel2.add(id);
				}

				JLabel start = new JLabel("Start Location - "+t1.getTrip().getStartLocation());
				JLabel end = new JLabel("End Location - "+t1.getTrip().getEndLocation());
				JLabel cost = new JLabel("Cost - "+String.valueOf(t1.getTrip().getTotalTripCost()));
				JLabel date = new JLabel("Departure Date -"+ t1.getTrip().getDepartureDate().toString());
				panel2.add(start);
				panel2.add(end);
				panel2.add(date);
				JButton acceptButton = new JButton("ACCEPT");
				acceptButton.setBounds(20, 20, 10, 10);
				panel2.add(acceptButton);
				JButton rejectButton = new JButton("REJECT");
				rejectButton.setBounds(20, 20, 10, 10);
				panel2.add(rejectButton);

				acceptButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						AdminService.respondToARequest(t1, TripRequestStatus.ACCEPTED);
						AdminService.removeProposedTripsForAStudent(s) ;
						JOptionPane.showMessageDialog(null, "Trip Accepted");
						
						rejectButton.setEnabled(false);
					}

				});
				panel2.setVisible(true);
				rejectButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						AdminService.respondToARequest(t1, TripRequestStatus.REJECTED);
						JOptionPane.showMessageDialog(null, "Trip Rejected");
						acceptButton.setEnabled(false);
						
					}
				});
			}
		}
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					TripRequest t = new TripRequest(s, formatter.parse(dateField.getText()),
							comboBox.getSelectedItem().toString(), comboBox_1.getSelectedItem().toString());
					AdminService.addRequestFromStudent(s, t.getDate(), t.getStartLocation(), t.getEndLocation());
					JOptionPane.showMessageDialog(null, "Trip Request Sent");
					addButton.setEnabled(false);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}

		});
		
		

	}

}
