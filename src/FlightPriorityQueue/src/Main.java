import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * Author: Cole Polyak
 * 23 October 2018
 * Main.java
 * 
 */

public class Main 
{
	// File IO
	static Scanner fileIn = null;
	static PrintWriter fileOut = null;

	// Simulation variables
	private static int numRunways = 0;
	private static int currentTime = 1;

	// All of our flights and our subcategory based on time.
	private static ArrayList<Flight> allFlights = new ArrayList<Flight>();
	private static PriorityQueue holdingPattern = new PriorityQueue();

	public static void main(String[] args)
	{
		if(args.length > 0) 
		{
			initializeScanner(args[0]);
			initializeOutput(args[0]);
			numRunways = fileIn.nextInt();

			// Reads in all flights.
			initializeFlightArray();

			while(allFlights.size() != 0 || holdingPattern.size() != 0)
			{
				// Pulls in flights arriving at time.
				populateHoldingPattern();
				landPlanes();
			}
			fileIn.close();
			fileOut.close();
		}
	}

	/**
	 * Lands all planes that can be landed. 
	 */
	private static void landPlanes()
	{
		// Fils all runways.
		for(int i = 0; i < numRunways; ++i)
		{
			try
			{
				// Removing a flight and marking it landed.
				Flight landed = holdingPattern.dequeue();
				landed.setLandedTime(currentTime);
				fileOut.println(landed);
			}
			// In the case there aren't any flights in the time block.
			catch(IllegalStateException e) 
			{
				break;
			}
		}
		currentTime++;
	}

	/**
	 * Adds all flights arriving at a specific time to the holding pattern.
	 */
	private static void populateHoldingPattern()
	{
		while(allFlights.size() != 0 && allFlights.get(0).getArrivalTime() == currentTime)
		{
			Flight current = allFlights.get(0);
			holdingPattern.enqueue(current);
			allFlights.remove(0);
		}
	}

	/**
	 * Adds all flights from the input file to the collection of flights.
	 */
	private static void initializeFlightArray()
	{
		while(fileIn.hasNext())
		{
			// Reading in Flight data.
			String flightID = fileIn.next();
			int arrivalTime = fileIn.nextInt();
			int landingPriority = fileIn.nextInt();

			Flight newFlight = new Flight(flightID, arrivalTime, landingPriority);
			allFlights.add(newFlight);
		}
	}

	/**
	 * Creates the output file and then initializes the outputstream.
	 * @param outputName : The output name of the string.
	 */
	private static void initializeOutput(String outputName)
	{
		File f = new File(outputName+".out");
		try
		{
			fileOut = new PrintWriter(new FileOutputStream(f));
		}
		catch(FileNotFoundException e)
		{
			System.out.println("File not found. Exiting");
			System.exit(1);
		}
	}

	/**
	 * Creates the file input stream. 
	 * @param inputFile : The name of the input file.
	 */
	private static void initializeScanner(String inputFile)
	{
		try
		{
			fileIn = new Scanner(new FileInputStream(inputFile));
		}
		catch(FileNotFoundException e)
		{
			System.err.println("File not found. Exiting");
			System.exit(1);
		}
	}
}
