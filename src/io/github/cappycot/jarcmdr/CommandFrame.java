package io.github.cappycot.jarcmd;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class CommandFrame extends JFrame {
	// Default Settings //
	private static final long serialVersionUID = -5000732405037534401L;
	private static final int DEF_WIDTH = 800;
	private static final int DEF_HEIGHT = 800;
	// User Settings //
	private String fontFamily = "Monospaced";
	private int fontSize = 16;
	private Font font = null;
	private int maxPrints = 1000;
	private String inputColor = "00AA00";
	// Window Variables //
	private static CommandFrame mainFrame = null;
	private JPanel contentPane = new JPanel();
	private JTextField topOutput = new JTextField();
	private JTextField textInput = new JTextField();
	private JScrollPane scrollPane = new JScrollPane();
	private JTextPane textPane = new JTextPane();
	private StyledDocument doc = textPane.getStyledDocument();
	private JButton clearButton = new JButton("Clear");
	private ArrayList<Chat> chats = new ArrayList<>();
	// Runtime Settings //
	private File outputFile;
	private static GuiStream stream;
	private static PipedOutputStream pos = new PipedOutputStream();
	private static PipedInputStream piss;

	/**
	 * Test run.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		activate();
		System.out.println("This is the command output zone.");
		if (args.length > 0) {
			System.out.print("The arguments: ");
			for (int i = 0; i < args.length; i++) {
				System.out.print(args[i] + (i < args.length - 1 ? ", " : ""));
			}
			System.out.println(".");
		} else {
			System.out.println("There were no arguments passed to main.");
		}
	}

	/**
	 * Creates a new command frame if one does not exist already.
	 */
	public static void activate() {
		if (mainFrame == null)
			mainFrame = new CommandFrame();
	}

	private CommandFrame() {
		setTitle("JarTextPlayer V0.1.3");
		setIconImage(Toolkit
				.getDefaultToolkit()
				.getImage(
						CommandFrame.class
								.getResource("/javax/swing/plaf/metal/icons/ocean/computer.gif")));

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		int x = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int y = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		setBounds((x - DEF_WIDTH) / 2, (y - DEF_HEIGHT) / 2, DEF_WIDTH,
				DEF_HEIGHT);

		refreshFonts();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 10));
		setContentPane(contentPane);

		topOutput.setHorizontalAlignment(SwingConstants.CENTER);
		topOutput.setText("Output");
		topOutput.setEditable(false);
		contentPane.add(topOutput, BorderLayout.NORTH);
		topOutput.setColumns(10);

		textPane.setEditable(false);
		scrollPane.setColumnHeaderView(clearButton);
		scrollPane.setViewportView(textPane);
		scrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		contentPane.add(scrollPane, BorderLayout.CENTER);

		// TODO: Wire CommandFrame to GuiStream
		try {
			outputFile = File.createTempFile("JTextOutput", "cap");
			System.out.println(outputFile.getPath());
			outputFile.deleteOnExit();
			stream = new GuiStream(this, outputFile);
			System.setOut(stream);
		} catch (Exception e) {
		}

		contentPane.add(textInput, BorderLayout.SOUTH);
		textInput.setColumns(10);
		try {
			piss = new PipedInputStream();
			pos.connect(piss);
			pos.flush();
			System.setIn(piss);
			textInput.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent key) {
					String txt = textInput.getText().trim();
					if (key.getKeyCode() == 10) {
						textInput.setText("");
						if (!txt.isEmpty())
							System.out.println("#" + inputColor + txt);
						if (txt.startsWith("/") && txt.length() > 1) {
							String[] args = txt.substring(1).split(" ");
							/**
							 * 
							 * Commands that can be used.
							 * 
							 */
							if (args[0].equalsIgnoreCase("help")
									|| args[0].equalsIgnoreCase("?")) {
								boolean helped = true;
								if (args.length > 1) {
									if (args[1].equalsIgnoreCase("color")) {
										System.out
												.println("Changes the color of the input text.");
										System.out
												.println("Usage: /color <6-digit hex>");
										System.out
												.println("Example: /color 00AA00");
									} else if (args[1].equalsIgnoreCase("font")) {
										System.out
												.println("Changes the text font to the specified type.");
										System.out
												.println("Usage: /font <fontname>");
										System.out
												.println("Example: /font Monospaced");
									} else if (args[1].equalsIgnoreCase("size")) {
										System.out
												.println("Changes the font size to the specified number.");
										System.out
												.println("Usage: /size <number>");
										System.out.println("Example: /size 16");
									} else if (args[1].equalsIgnoreCase("help")) {
										System.out
												.println("Yo dawg, I heard you needed some help with your help.");
										System.out
												.println("So I helped with your help. Does that help?");
									} else if (args[1].equalsIgnoreCase("?")) {
										System.out.println("1. ???");
										System.out.println("2. ????");
										System.out.println("3. ?????");
										System.out.println("4. Profit!!");
									} else if (args[1]
											.equalsIgnoreCase("restart")) {
										System.out
												.println("If available, attempts to restart the entire program.");
										System.out
												.println("Usage: /restart <arg 1> <arg 2> ... <arg n>");
									} else if (args[1].equalsIgnoreCase("quit")) {
										System.out
												.println("Shuts down the whole program.");
										System.out.println("Usage: /quit");
									} else {
										System.out.println("Unknown command.");
										helped = false;
									}
								} else {
									helped = false;
								}
								if (!helped) {
									System.out.println("Available Commands:");
									System.out.println("/color");
									System.out.println("/font");
									System.out.println("/size");
									System.out.println("/help");
									System.out.println("/restart");
									System.out.println("/quit");
									System.out
											.println("Use /help <command> to learn more about a command.");
								}
							}
							/**
							 * Input Color
							 */
							else if (args[0].equalsIgnoreCase("color")) {
								boolean failed = false;
								String newColor = "00AA00";
								try {
									if (args[1].startsWith("#"))
										args[1] = args[1].substring(1);
									int x = Integer.decode("#" + args[1]);
									failed = !(x >= 0 && x <= 16777215);
									newColor = args[1];
								} catch (Exception e) {
									failed = true;
								}
								if (failed)
									System.out
											.println("Usage: /color <6-digit hex>");
								else
									inputColor = newColor;
							}
							/**
							 * Font Type
							 */
							else if (args[0].equalsIgnoreCase("font")) {
								if (args.length > 1) {
									String fam = "";
									for (int i = 1; i < args.length; i++) {
										fam += args[i] + " ";
									}
									fontFamily = fam.trim();
									refreshFonts();
								} else
									System.out
											.println("Usage: /font <fontname>");
							}
							/**
							 * Font Size
							 */
							else if (args[0].equalsIgnoreCase("size")) {
								boolean failed = false;
								try {
									fontSize = Integer.parseInt(args[1]);
									refreshFonts();
								} catch (Exception e) {
									failed = true;
								}
								if (failed)
									System.out.println("Usage: /size <number>");
							}
							/**
							 * Restart
							 */
							else if (args[0].equalsIgnoreCase("restart")) {
								System.out
										.println("#A10013Restart cannot be used.");
							}
							/**
							 * Quit
							 */
							else if (args[0].equalsIgnoreCase("quit")) {
								System.exit(0);
							} else {
								System.out
										.println("Unknown command. Use /help for a list of commands.");
							}
							/**
							 * 
							 * End of commands section.
							 * 
							 */
						} else {
							try {
								pos.write((txt + "\n").getBytes());
								pos.flush();
							} catch (IOException a) {
							}
						}
					}
				}
			});
		} catch (IOException a) {
			a.printStackTrace();
		}

		clearButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				chats = new ArrayList<>();
				textPane.setText("");
				intro();
			}
		});
		setVisible(true);
		intro();
	}

	/**
	 * Changes fonts.
	 */
	private void refreshFonts() {
		font = new Font(fontFamily, Font.PLAIN, fontSize);
		topOutput.setFont(font);
		textPane.setFont(font);
		clearButton.setFont(font);
		textInput.setFont(font);
	}

	/**
	 * Prints error and stacktrace.
	 * 
	 * @param e
	 */
	public void error(Exception e) {
		Toolkit.getDefaultToolkit().beep();
		System.out.println("#FF0000" + e.getClass().getCanonicalName()
				+ ": #000000" + e.getMessage());
		for (StackTraceElement ste : e.getStackTrace()) {
			int line = ste.getLineNumber();
			System.out.println("-at "
					+ ste.getClassName()
					+ "."
					+ ste.getMethodName()
					+ "("
					+ (line > 0 ? "#FF0000" + line + "#000000"
							: "Unknown Source") + ")");
		}
	}

	public void intro() {
		System.out.println("#6655FFJava Text Display#000000 by Chris Wang");
		System.out
				.println("Changelog (V0.1.0): Set System.in and System.out accordingly.");
		System.out
				.println("Changelog (V0.1.2+): Fixed message overflow problem.");
		System.out.println("Running Java " + System.getProperty("java.version")
				+ " on " + System.getProperty("os.name") + " "
				+ System.getProperty("os.arch") + ".");
		System.out
				.println("Use the text input bar at the bottom as the input.");
		System.out
				.println("----------------------------------------------------------------");
	}

	public synchronized void render(Chat chat) {
		render(chat, false);
	}

	/**
	 * Legacy shit taken from elsewhere. This blows having to modify it.
	 * 
	 * @param chat
	 */
	public void render(Chat chat, boolean bypass) {
		/* If Messages are Over Limit */
		if (chats.size() > maxPrints && !bypass) {
			chats.remove(0);
			textPane.setText("");
			for (Chat c : chats)
				render(c, true);
		}
		if (!bypass)
			chats.add(chat);
		/* Render Variables */
		ArrayList<Color> colors = new ArrayList<>(); // List of rendered
														// colors.
		colors.add(Color.BLACK);
		ArrayList<String> strings = new ArrayList<>(); // List of strings to
														// color.
		String toSplit = chat.getChat(); // Coded text to divide.
		int pointer = 0; // Cursor.
		int pointer2 = 0;
		boolean colorCode = false;
		Style s = doc.addStyle("Line", null); // Style for coloring/drawing.
		StyleConstants.setFontSize(s, 8);

		do {
			/* Append Text before the color change to strings ArrayList. */
			pointer = pointer2; // Advance pointer
			pointer2 = toSplit.indexOf("#", pointer); // Search for color
														// symbol.
			colorCode = pointer2 != -1 && pointer2 + 6 <= toSplit.length();
			// Located color symbol and is there potential for a color code?
			strings.add(toSplit.substring(pointer, colorCode ? pointer2
					: toSplit.length()));
			if (colorCode) {
				/* Test the next 6 digits past the color "#" symbol. */
				String colorTest = toSplit.substring(pointer2, pointer2 + 7);
				try {
					int r = Integer.parseInt(colorTest.substring(1, 3), 16);
					int g = Integer.parseInt(colorTest.substring(3, 5), 16);
					int b = Integer.parseInt(colorTest.substring(5, 7), 16);
					Color color = new Color(r, g, b);
					colors.add(color);
					pointer2 += 7;
				} catch (Exception e) {
					int latest = strings.size() - 1;
					strings.set(latest, strings.get(latest) + "#");
					pointer2++; // Advance pointer to next searchable digit.
				}
			}
		} while (colorCode);

		/* Render Colored Text */

		Color last = null;
		for (int i = 0; i < strings.size(); i++) {
			Color current = i < colors.size() ? colors.get(i) : colors
					.get(colors.size() - 1);
			/* Set color style */
			if (!current.equals(last)) {
				/* Attempt to retrieve color style. */
				s = doc.getStyle("Color" + current.getRGB());
				if (s == null) { // If color found is null, then add color
									// set.
					s = doc.addStyle("Color" + current.getRGB(), null);
					StyleConstants.setForeground(s, current);
				}
			} else if (s == null) {
				s = doc.getStyle("Color" + current.getRGB());
			}
			/* Insert said colored text. */
			try {
				doc.insertString(doc.getLength(), strings.get(i), s);
				if (i == strings.size() - 1)
					doc.insertString(doc.getLength(), "", doc.getStyle("Line"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (chat.isPicture()) {
			/* Attempt Render of Picture */
			s = doc.getStyle("Picture" + chat.getChatID());
			if (s == null) {
				s = doc.addStyle("Picture" + chat.getChatID(), null);
				StyleConstants.setIcon(s, chat.getPicture());
				try {
					doc.insertString(doc.getLength(), "\n", s);
					doc.insertString(doc.getLength(), "\n",
							doc.getStyle("Line"));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (!bypass) {
			JScrollBar x = scrollPane.getVerticalScrollBar();
			StringBuilder sb = new StringBuilder();
			for (String ss : strings) {
				sb.append(ss);
			}
			if (colors.size() > 1)
				topOutput.setForeground(colors.get(colors.size() - 1));
			else
				topOutput.setForeground(Color.BLACK);
				topOutput.setText(sb.toString());
			try {
				x.setValue(x.getMaximum());
				x = scrollPane.getVerticalScrollBar();
				x.setValue(x.getMaximum());
			} catch (Exception e) {
			}
		}
	}
}
