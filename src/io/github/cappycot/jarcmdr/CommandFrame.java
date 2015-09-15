package io.github.cappycot.jarcmdr;

import java.awt.BorderLayout;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class CommandFrame extends JFrame {
	// Default Settings //
	private static final long serialVersionUID = -5000732405037534401L;
	private JTextField inputField;
	private JTextField statusField;
	private JScrollPane scrollPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		CommandFrame frame = new CommandFrame();
		frame.setVisible(true);
	}

	/**
	 * Create the frame.
	 */
	public CommandFrame() {
		// General Frame Setup //
		setIconImage(Toolkit
				.getDefaultToolkit()
				.getImage(
						CommandFrame.class
								.getResource("/com/sun/java/swing/plaf/windows/icons/Computer.gif")));
		setTitle("Jar Commander V0.0.1");
		setBounds(100, 100, 400, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Text Input //
		inputField = new JTextField();
		inputField.setToolTipText("System.in");
		getContentPane().add(inputField, BorderLayout.SOUTH);
		inputField.setColumns(10);

		scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);

		statusField = new JTextField();
		statusField.setHorizontalAlignment(SwingConstants.CENTER);
		statusField.setText("Status Bar");
		statusField.setEditable(false);
		getContentPane().add(statusField, BorderLayout.NORTH);
		statusField.setColumns(10);
	}

}
