package Display;

import javax.swing.*;

import Enums.TripRequestStatus;
import Models.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Services.AdminService;

public class AdminPanel2 extends JFrame {

	private static final long serialVersionUID = 1L;

	public AdminPanel2() {

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Student Details");
		this.setSize(1140, 600);
		this.setResizable(true);

		JPanel panel2 = new JPanel();
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));

		panel2.setName("Admin Interfcae");

		JButton backButton = new JButton("LOGOUT");
		backButton.setAlignmentX(0.75f);
		backButton.setBounds(20, 20, 10, 10);
		panel2.add(backButton);
		panel2.add(Box.createVerticalStrut(20));
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				AdminPanel adminP = new AdminPanel();
				StudentPanel StudentP = new StudentPanel();
				Home h = new Home(StudentP, adminP);
				h.setVisible(true);
			}

		});

		JLabel title = new JLabel("List of Registered Students");
		panel2.add(title);
		for (Student s : AdminService.getRegisteredStudents()) {

			panel2.add(new JLabel(s.getName()));
			panel2.add(new JLabel(s.getId()));
			panel2.add(new JLabel(String.valueOf((s.getPhoneNumber()))));
			panel2.add(Box.createVerticalStrut(10));

		}

		JButton groupButton = new JButton("Group Students");
		groupButton.setAlignmentX(0.5f);
		groupButton.setBounds(20, 20, 10, 10);
		panel2.add(groupButton);
		panel2.add(Box.createVerticalStrut(20));
		panel2.setVisible(true);
		this.getContentPane().add(panel2, BorderLayout.NORTH);

		groupButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// USE THIS TO START GROUPING USING MULTITHREADING
				AdminService.groupTravellers();
				for (int i = 0; i < AdminService.getGroupedTravellers().size(); i++) {

					AdminService.sendTripRequests(AdminService.getGroupedTravellers().get(i));

				}
				AdminService.setRequestsMap(new HashMap<String, ArrayList<TripRequest>>());
				AdminService.setGroupedTravellers(new ArrayList<ArrayList<TripRequest>>());
				AdminService.debugTrips();
				dispose();
				new AdminPanel2().setVisible(true);

			}
		});
		panel2.add(Box.createVerticalStrut(10));
		JLabel trips = new JLabel("ALL PROPOSED and SCHEDULED TRIPS");
		panel2.add(trips);
		for (Trip t : AdminService.getMapOfTrips().values()) {
			panel2.add(Box.createVerticalStrut(15));
			for (Student s1 : t.getCoPassengers()) {
				JLabel s2 = new JLabel("Name- "+s1.getName());
				JLabel id = new JLabel("ID No- "+ s1.getId());
				panel2.add(s2);
				panel2.add(id);
				panel2.add(Box.createVerticalStrut(3));
			}
			JLabel start = new JLabel("Start Location- "+t.getStartLocation());
			JLabel end = new JLabel("End Location-"+t.getEndLocation());
			panel2.add(start);
			panel2.add(end);

			JLabel dateTime = new JLabel("Departure Time- "+t.getDepartureDate().toString());
			JLabel status = new JLabel("Trip Status -"+ t.getTripStatus().toString());
			panel2.add(dateTime);
			panel2.add(status);

		}
		panel2.add(Box.createVerticalStrut(20));
		JButton scheduleButton = new JButton("Schedule Trips");
		scheduleButton.setAlignmentX(0.5f);
		groupButton.setBounds(20, 20, 10, 10);
		panel2.add(scheduleButton);
		panel2.add(Box.createVerticalStrut(20));
		panel2.setVisible(true);

		scheduleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AdminService.finalSchedulingAndChangingTripStatus();
				new AdminPanel2().setVisible(true);

			}
		});

		JLabel title2 = new JLabel("Spendings of All Registered Students");
		panel2.add(title2);

		for (Student s : AdminService.getRegisteredStudents()) {

			panel2.add(new JLabel(s.getName()));
			panel2.add(new JLabel(s.getId()));
			if (s.getTotalCost() > 0.0) {
				panel2.add(new JLabel(String.valueOf((s.getTotalCost()))));
			} else {
				panel2.add(new JLabel("The Student hasnt taken any trips yet"));
			}

			panel2.add(Box.createVerticalStrut(10));

		}

	}

}
