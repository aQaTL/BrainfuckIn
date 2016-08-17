package aqatl;

import java.io.IOException;
import java.util.Scanner;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application
{

	@Override
	public void start(Stage primaryStage) throws IOException
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("BrainfuckInterpreterView.fxml"));
		Scene scene = new Scene(loader.load());
		loader.<BrainfuckInterpreterController>getController().init(primaryStage);
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("Brainfuck");
		primaryStage.show();
	}

	public static void main(String[] args) throws IOException
	{
		BrainfuckTranslator bfTrn = new BrainfuckTranslator(System.in, System.out);

		if(args.length != 0)
			executeInCMD(bfTrn);
		
		launch(args);
	}

	public static void executeInCMD(BrainfuckTranslator bfTrn)
	{
		StringBuilder sb = new StringBuilder();

		try(Scanner in = new Scanner(System.in);)
		{
			while(in.hasNextLine())
			{
				sb.append(in.nextLine());
			}
			bfTrn.execute(sb.toString().trim());

			bfTrn.printExecutionInfo();
		}
		catch(BrainfuckException | IOException ex)
		{
			System.err.println(ex.getLocalizedMessage());
		}
	}

}

