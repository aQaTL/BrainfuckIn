package aqatl.streams;

import aqatl.BrainfuckInterpreterController;
import java.io.IOException;
import java.io.OutputStream;

public class BfOutputStream extends OutputStream
{
	private BrainfuckInterpreterController bfController;

	public BfOutputStream(BrainfuckInterpreterController bfController)
	{
		this.bfController = bfController;
	}

	@Override
	public void write(int b) throws IOException
	{
		char c = (char) b;
		bfController.getOutputArea().appendText(Character.toString(c));
	}
}
