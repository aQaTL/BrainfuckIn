package aqatl;

import java.util.Scanner;

public class BrainfuckMachine
{

	public static void main(String[] args)
	{
		BrainfuckTranslator bfTrn = new BrainfuckTranslator();

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
		catch(BrainfuckException ex)
		{
			System.err.println(ex.getLocalizedMessage());
		}
	}
}
