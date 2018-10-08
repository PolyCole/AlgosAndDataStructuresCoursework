import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * Author: Cole Polyak
 * 16 September 2018
 * Programming Assignment 1 : CS 2370
 * 
 * Class sorts input into output file using Insertion sort.
 * Best called from command line. 
 * 
 */

public class InsertionSort 
{
	private Scanner fileIn = null;
	private PrintWriter fileOut = null;
	
	public static void  main(String[] args)
	{
		// Reading command line arguments.
		@SuppressWarnings(value = { "unused" })
		InsertionSort s = new InsertionSort(args[0], args[1]);
	}
	
	public InsertionSort(String inputFile, String outputFile)
	{
		try 
		{
			fileIn = new Scanner(new FileInputStream(inputFile), "UTF-8");
			fileOut = new PrintWriter(new FileOutputStream(outputFile));
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
			System.exit(1);
		}
		
		sort(fileIn);
	}
	
	private void sort(Scanner file)
	{
		ArrayList<String> sortedStrings = new ArrayList<>();
		
		while(file.hasNext()) 
		{
			sortedStrings.add(file.next());
		}
		
		// Passes to method that actually sorts.
		sort(sortedStrings);
		
		file.close();
	}
	
	// Insertion Sort.
	private void sort(ArrayList<String> list)
	{
		// Starts at second element.
		for(int j = 1; j < list.size(); ++j)
		{
			int i = j - 1;
			String key = list.get(j);
			
			// Moves backwards
			while(i >= 0)
			{
				// Comparing to current string.
				if(key.compareTo(list.get(i)) > 0) break;
				
				list.set(i+1, list.get(i));
				i--;
			}
			list.set(i+1, key);
		}
		
		output(list);
	}
	
	// Prints to file
	private void output(ArrayList<String> arr)
	{		
			for(String s : arr)
			{
				fileOut.println(s);
			}
			
			fileOut.close();
	}
}
