/*
 * Author: Cole Polyak
 * 23 October 2018
 * Flight.java
 * 
 * This class stores all data associated with a flight.
 */

public class Flight 
{
	private String flightID;
	private int arrivalTime;
	private int landingPriority;
	private int landedTime;
	private double landingPriorityAdj;
	
	// Constructor.
	public Flight(String flightID, int arrivalTime, int landingPriority)
	{
		this.flightID = flightID;
		this.arrivalTime = arrivalTime;
		this.landingPriority = landingPriority;
		
		/*
		 * So, the way I got into this solution was because I was having
		 * issues when two flights had identical priorities. My solution to
		 * that problem is this variable. It adds a miniscule amount to the priority
		 * by dividing the arrival time by 1000 and adding it to the priority.
		 * This way, flights that arrive first are prioritized when two flights have
		 * identical landing priorities.
		 */
		landingPriorityAdj = landingPriority + (arrivalTime/1000.0);
		
		landedTime = 0;
	}
	
	// Setter
	public void setLandedTime(int landedTime)
	{
		this.landedTime = landedTime;
	}

	// Getters
	public String getFlightID()
	{
		return flightID;
	}
	
	public int getArrivalTime()
	{
		return arrivalTime;
	}
	
	public int getLandingPriority()
	{
		return landingPriority;
	}
	
	public double getLandingPriorityAdj()
	{
		return landingPriorityAdj;
	}
	
	// For file output.
	public String toString()
	{
		return flightID + "\t" + arrivalTime + "\t" + landingPriority + "\t" + landedTime;
	}
}
