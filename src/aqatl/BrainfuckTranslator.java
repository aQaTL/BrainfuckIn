package aqatl;

import java.util.Scanner;

public class BrainfuckTranslator 
{
	private char[] memory;
	private int pointer;
	private int loopStart;
	private Scanner in;
	
	//Execution info
	private int instructions;
	private int moves;
	private long time;
	
    public BrainfuckTranslator()
	{
		this.memory = new char[30000];
		pointer = 0;
		in = new Scanner(System.in);
	}
	
	public void execute(String bfCode) throws BrainfuckException
	{
		if(bfCode.length() > memory.length)
			throw new BrainfuckException("Memory overflow");
		
		moves = 0;
		time = System.currentTimeMillis();
		instructions = bfCode.length();
		for(int j = 0; j < instructions; j++)
		{
			moves++;
			switch(bfCode.charAt(j))
			{
				case '+':
					memory[pointer]++;
					break;
				case '-':
					memory[pointer]--;
					break;
				case '>':
					pointer++;
					break;
				case '<':
					pointer--;
					break;
				case '.':
					System.out.print(memory[pointer]);
					break;
				case ',':
					memory[pointer] = (char) in.nextInt();
					break;
				case '[':
					loopStart = j;
					break;
				case ']':
					if(memory[pointer] == 0)
						break;
					j = loopStart;
					break;
					
				default:
					moves--;
					break;
			}
		}
		System.out.println();
		time = System.currentTimeMillis() - time;
	}
	
	public void printExecutionInfo()
	{
		System.out.println("Time: " + time + " miliseconds");
		System.out.println("Instructions: " + instructions);
		System.out.println("Moves: " + moves);
	}
}
