package io.github.cappycot.jarcmdr;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class CommandFrame extends JFrame {
	// Default Settings //
	private static final long serialVersionUID = -5000732405037534401L;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CommandFrame frame = new CommandFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public CommandFrame() {
		setBounds(100, 100, 400, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
