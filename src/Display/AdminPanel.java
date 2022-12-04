package Display;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import Services.AdminService;
import Models.Student;
import Models.admin;

public class AdminPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel name;
	private JLabel password;
	private JTextField nameField;
	private JTextField passField;
	private JButton regButton;

	public AdminPanel() {

		this.setLayout(new BorderLayout());
		this.name = new JLabel("USER ID: ");
		this.name.setFont( new Font("Tahoma", Font.PLAIN, 15));
		this.password = new JLabel("Password: ");
		this.password.setFont( new Font("Tahoma", Font.PLAIN, 15));
		this.nameField = new JTextField(20);
		this.passField = new JPasswordField(20);
		this.regButton = new JButton("LOGIN");
		this.regButton.setFont( new Font("Tahoma", Font.PLAIN, 15));
		this.regButton.setBounds(20, 20, 10, 10);

		regButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				admin a = new admin();
				a.admin_login(nameField.getText(), passField.getText());
				if(a.logincheck==true) {
					new AdminPanel2().setVisible(true);	
				}
				else {
					JOptionPane.showMessageDialog(null, "Enter Correct Username/Password");
				}
			}
		});

		this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.darkGray), "Login"));
		
		JPanel panel = new JPanel();
		panel.add(name);
		panel.add(nameField);
		panel.add(password);
		panel.add(passField);
		panel.add(regButton);
		panel.setVisible(true);
		this.add(panel);
		this.setVisible(true);


	}

}
