package io.github.cappycot.jarcmdr;

import java.io.OutputStream;
import java.io.PrintStream;

public class GuiStream extends PrintStream {

	public GuiStream(CommandFrame cf, OutputStream os) {
		super(os);
	}

	@Override
	public void print(String s) {
		super.print(s);
		// cf.render(new Chat(s.getBytes(),false));
	}

	@Override
	public void println() {
		print("\n");
	}

	@Override
	public void println(String s) {
		print(s + "\n");
	}

	@Override
	public PrintStream printf(String format, Object... args) {
		// cf.render(new Chat(String.format(format, args).getBytes(), false));
		return super.printf(format, args);
	}
}
