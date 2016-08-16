package aqatl;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Stack;

public class BrainfuckTranslator 
{
	private char[] memory;
	private int pointer;
	private int memorySize;
	private Stack<Integer> loopStack;
	private InputStream in;
	private BufferedOutputStream out;
	
	//Execution info
	private int instructions;
	private int moves;
	private long time;
	
    public BrainfuckTranslator(InputStream in, OutputStream out)
	{
		memorySize = 30000;
		loopStack = new Stack<>();

		this.in = in;
		this.out = new BufferedOutputStream(out);
	}
	
	public void execute(String bfCode) throws BrainfuckException, IOException, ArrayIndexOutOfBoundsException
	{
		memory = new char[memorySize];
		pointer = 0;
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
					out.write((int) memory[pointer]);
					break;
				case ',':
					memory[pointer] = (char) in.read();
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
		out.write((int) '\n');
		out.flush();
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
