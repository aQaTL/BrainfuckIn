package aqatl;

import java.util.Scanner;
import java.util.Stack;

public class BrainfuckTranslator 
{
	private char[] memory;
	private int pointer;
	private int memorySize;
	private Stack<Integer> loopStack;
	private Scanner in;
	
	//Execution info
	private int instructions;
	private int moves;
	private long time;
	
    public BrainfuckTranslator()
	{
		memorySize = 30000;
		memory = new char[memorySize];
		pointer = 0;
		loopStack = new Stack<>();
		in = new Scanner(System.in);
	}
	
	public void execute(String bfCode) throws BrainfuckException
	{
		loopStack.clear();
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
					pointer = pointer == memorySize - 1 ? 0 : pointer + 1;
					break;
				case '<':
					pointer = pointer == 0 ? memorySize - 1 : pointer - 1;
					break;
				case '.':
					System.out.print(memory[pointer]);
					break;
				case ',':
					memory[pointer] = (char) in.nextInt();
					break;
				case '[':
					loopStack.push(j);
					break;
				case ']':
					if(memory[pointer] == 0)
					{
						loopStack.pop();
						break;
					}
					j = loopStack.peek();
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
		System.out.println("\nTime: " + time + " miliseconds");
		System.out.println("Instructions: " + instructions);
		System.out.println("Moves: " + moves);
	}

	public int getInstructions()
	{
		return instructions;
	}

	public int getMoves()
	{
		return moves;
	}
	
	public long getTime()
	{
		return time;
	}
}
