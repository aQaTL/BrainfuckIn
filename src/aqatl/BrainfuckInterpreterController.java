package aqatl;

import aqatl.streams.BfInputStream;
import aqatl.streams.BfOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
	
	private Stage stage;

	private BrainfuckTranslator bfTrn;
	private BrainfuckInterpreterService bfIntServ;

	private BfInputStream bfIn;
	private BfOutputStream bfOut;
	
	private FileChooser fileChooser;
	private File currentFile;

	public void init(Stage stage)
	{
		this.stage = stage;
		
		bfIn = new BfInputStream(this);
		bfOut = new BfOutputStream(this);
		bfTrn = new BrainfuckTranslator(bfIn, bfOut);

		fileChooser = new FileChooser();

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
		bfIntServ.reset();
		System.out.println(bfIntServ.getState().name());
	}
	
	@FXML
	private void open() throws FileNotFoundException
	{
		currentFile = fileChooser.showOpenDialog(stage);
		Scanner fileScn = new Scanner(currentFile);
		
		StringBuilder fileContentBuilder = new StringBuilder();
		while(fileScn.hasNextLine())
		{
			fileContentBuilder.append(fileScn.nextLine());
			fileContentBuilder.append("\n");
		}
		inputArea.setText(fileContentBuilder.toString());
	}
	
	@FXML
	private void save() throws IOException
	{
		currentFile = fileChooser.showSaveDialog(stage);
		if(!currentFile.exists())
			currentFile.createNewFile();
		
		try(PrintWriter fileWriter = new PrintWriter(currentFile))
		{
			fileWriter.print(inputArea.getText());
			fileWriter.flush();
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	
	@FXML
	private void clearAll()
	{
		inputArea.setText("");
		outputArea.setText("");
		userInputField.setText("");
		resetProgram();
	}
	
	@FXML
	private void exit()
	{
		//TODO unsaved work warning
		System.exit(0);
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
