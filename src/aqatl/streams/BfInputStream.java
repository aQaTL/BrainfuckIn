package aqatl.streams;

import aqatl.BrainfuckInterpreterController;
import java.io.IOException;
import java.io.InputStream;

public class BfInputStream extends InputStream
{
	private BrainfuckInterpreterController bfController;
	private int inputCharPointer;

	public BfInputStream(BrainfuckInterpreterController bfController)
	{
		this.bfController = bfController;
		inputCharPointer = 0;
	}
	
	public void resetStream()
	{
		inputCharPointer = 0;
	}

	@Override
	public int read() throws IOException
	{
		try
		{
			int c = bfController.getUserInputField().getText().codePointAt(inputCharPointer);
			inputCharPointer ++;
			return c;
		}
		catch(StringIndexOutOfBoundsException e)
		{
			bfController.getOutputArea().appendText("\n");
			bfController.getOutputArea().appendText(bfController.inputErrMsg);
			return 0;
		}
	}

}
