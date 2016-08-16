package aqatl;

import aqatl.streams.BfInputStream;
import aqatl.streams.BfOutputStream;
import java.io.IOException;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class BrainfuckInterpreterController
{

	@FXML
	private TextArea inputArea, outputArea;
	@FXML
	private Button startButton, stopButton;
	@FXML
	private TextField instructionsField, movesField, timeField, userInputField;

	public final String errorMsg = "Program execution failed";
	public final String inputErrMsg = "Missing input";

	private BrainfuckTranslator bfTrn;
	private BrainfuckInterpreterService bfIntServ;

	private BfInputStream bfIn;
	private BfOutputStream bfOut;

	public void init()
	{
		bfIn = new BfInputStream(this);
		bfOut = new BfOutputStream(this);
		bfTrn = new BrainfuckTranslator(bfIn, bfOut);

		bfIntServ = new BrainfuckInterpreterService();
		bfIntServ.setOnRunning(workerStateEvent -> resetProgram());
		bfIntServ.setOnSucceeded(workerStateEvent ->
		{
			instructionsField.setText(Integer.toString(bfTrn.getInstructions()));
			movesField.setText(Integer.toString(bfTrn.getMoves()));
			timeField.setText(Long.toString(bfTrn.getTime()) + " miliseconds");
		});
		bfIntServ.setOnFailed(workerStateEvent -> bfIntServ.getException().printStackTrace());
	}

	private void resetProgram()
	{
		instructionsField.setText("");
		movesField.setText("");
		timeField.setText("");
		bfIn.resetStream();
	}

	@FXML
	private void executeProgram()
	{
		bfIntServ.restart();
	}

	@FXML
	private void stopProgram()
	{
		bfIntServ.cancel();
		System.out.println(bfIntServ.getState().name());
	}

	public TextArea getOutputArea()
	{
		return outputArea;
	}

	public TextField getUserInputField()
	{
		return userInputField;
	}

	private class BrainfuckInterpreterService extends Service<Void>
	{

		@Override
		protected Task<Void> createTask()
		{
			return new Task<Void>()
			{
				@Override
				protected Void call() throws Exception
				{
					try
					{
						outputArea.setText("");
						bfTrn.execute(inputArea.getText());
					}
					catch(BrainfuckException | IOException ex)
					{
						outputArea.setText(errorMsg);
					}
					catch(ArrayIndexOutOfBoundsException e)
					{
						System.err.println("NONONONONONONONONO!");
					}

					return null;
				}
			};
		}
	}
}
