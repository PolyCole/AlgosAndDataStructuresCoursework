import java.util.ArrayList;

/*
 * Author: Cole Polyak
 */

class sortResults
{
	private int numberOfItems;

	private ArrayList<Double> timesInsertion = null;
	private ArrayList<Double> timesMerge = null;

	private double averageInsertionTimes = -1;
	private double averageMergeTimes = -1;

	public sortResults(int numberOfItems)
	{
		this.numberOfItems = numberOfItems;

		timesInsertion = new ArrayList<>();
		timesMerge = new ArrayList<>();
	}

	public void calculateAverageInsertionTimes()
	{
		double average = 0;
		for(double f : timesInsertion)
		{
			average += f;
		}
		averageInsertionTimes = average/timesInsertion.size();
	}

	public void calculateAverageMergeTimes()
	{
		double average = 0;
		for(double f : timesMerge)
		{
			average += f;
		}
		averageMergeTimes = average/timesMerge.size();
	}

	public double getAverageInsertionTimes()
	{
		return averageInsertionTimes;
	}
	
	public void addTimeInsertion(double f)
	{
		timesInsertion.add(f);
	}
	
	public void addTimeMerge(double f)
	{
		timesMerge.add(f);
	}

	public double getAverageMergeTimes()
	{
		return averageMergeTimes;
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Avg. times for n = " + numberOfItems + ": ");
		sb.append("Insertion Sort " + averageInsertionTimes + " sec., ");
		sb.append("Merge Sort " + averageMergeTimes + " sec.");

		return sb.toString();
	}
}