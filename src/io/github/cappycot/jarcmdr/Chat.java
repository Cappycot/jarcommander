/*
 * Chat.java
 *
 * Date: 15/08/31 (YY/MM/DD)
 * 
 */

package io.github.cappycot.jarcmdr;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * A representation of either a text or image chat.
 * 
 * @author Chris Wang
 */
public class Chat implements Serializable {
	/* Database Variables */
	public static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static BufferedImage test = null;
	private static int chats = 0;
	private transient int chatID;

	/* Picture Variables */
	/**
	 * Determines whether the chat is a picture or text.
	 */
	private boolean isPicture;
	/**
	 * Database picture.<br>
	 * Transient because the bytes,<br>
	 * are socket-transferred.
	 */
	private transient ImageIcon picture;
	/* String/Data Variables */
	/**
	 * The bulk of the data transferred between client and server.
	 */
	private byte[] bytes;
	/**
	 * Text derived from bytes or error message.
	 */
	private String chat = "";

	public Chat(byte[] data, boolean pic) {
		bytes = data;
		isPicture = pic;
		if (isPicture)
			chat = "Picture:";
		init();
	}

	public Chat(byte[] data, String desc) {
		bytes = data;
		isPicture = true;
		chat = desc;
		init();
	}

	public int getChatID() {
		return chatID;
	}

	private transient boolean init = false;

	/**
	 * Translates sent bytes into a picture or string text.
	 */
	public void init() {
		if (init)
			return;
		init = true;
		chatID = logChat();
		if (isPicture) {
			InputStream is = null;
			try {
				/* Initiate Image from bytes. */
				is = new ByteArrayInputStream(bytes);
				test = ImageIO.read(is); // Test Image for Corruption...
				picture = new ImageIcon(bytes);
			} catch (Exception e) {
				isPicture = false;
				chat += "#770000[Image failed to load. (" + e.getMessage()
						+ ")]";
			} finally {
				try {
					is.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			chat = new String(bytes);
		}
	}

	public boolean isPicture() {
		return isPicture;
	}

	public String getChat() {
		return chat;
	}

	public ImageIcon getPicture() {
		return picture;
	}
	
	private synchronized static int logChat() {
		chats++;
		return chats - 1;
	}
}
