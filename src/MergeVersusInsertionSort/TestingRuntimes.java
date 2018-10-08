import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * Author: Cole Polyak
 * 23 September 2018
 * Project 2
 * 
 * Class tests the difference in run times between Insertion and Merge sort. 
 * Tests for list sizes of 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192.
 * Calculates the average run time for the input size for each algorithm. 
 */

public class TestingRuntimes {

	private static Scanner fileIn = null;
	
	// Number of elements in list.
	private static int itemCount = 8;
	
	private static ArrayList<String> words = new ArrayList<>();

	public static void main(String[] args) 
	{
		// Main loop testing algorithm.
		for(int i = 8; i <= 8192; i*=2)
		{
			itemCount = i;
			initializeFile(args);
			timeAlgorithms();
		}
	}
	
	// Times the algorithms.
	private static void timeAlgorithms()
	{
		SortResult newResult = new SortResult(itemCount);
		
		// Sublist of larger file to be used for sorting.
		ArrayList<String> arr = new ArrayList<>(words.subList(0, itemCount));
		
		timeInsertionSort(arr, newResult);
		timeMergeSort(arr, newResult);
		
		// Outputting results for n-count.
		System.out.println(newResult.toString());
	}
	
	// Times insertion sort.
	private static void timeInsertionSort(ArrayList<String> arr, SortResult result)
	{
		// Important to avoid pointer mishaps.
		ArrayList<String> arrayToBeUsedForSort;
		CpuTimer c1;
		
		// Conducts several timing runs.
		for(int i = 0; i < 48192 / itemCount; ++i)
		{
			// Copies array
			arrayToBeUsedForSort = (ArrayList<String>) arr.clone();
			
			c1 = new CpuTimer();
			InsertionSort(arrayToBeUsedForSort);
			
			// Adds run time to result.
			result.addTimeInsertion(c1.getElapsedCpuTime());
			
			// Checks everything is in order.
			doChecks(arr, arrayToBeUsedForSort);
		}
		
		result.calculateAverageInsertionTimes();
	}
	
	// Times merge sort.
	private static void timeMergeSort(ArrayList<String> arr, SortResult result)
	{
		// Avoids pointer mishaps.
		ArrayList<String> arrayToBeUsedForSort;
		CpuTimer c2;
		
		// Conducts several timing runs.
		for(int i = 0; i < 48192 / itemCount; ++i)
		{
			// Copies array
			arrayToBeUsedForSort = (ArrayList<String>) arr.clone();
			
			c2 = new CpuTimer();
			MergeSortSetup(arrayToBeUsedForSort);
			
			// Adds run time to result.
			result.addTimeMerge(c2.getElapsedCpuTime());
			
			// Checks that everything is in order.
			doChecks(arr, arrayToBeUsedForSort);
		}
		
		result.calculateAverageMergeTimes();
	}
	
	// Method ensures that the array was sorted correctly.
	private static void doChecks(ArrayList<String> input, ArrayList<String> output)
	{
		// We expect the array not to be sorted before and to be sorted after.
		boolean alphaSortedCheck = !(AlphaSorted(input)) && AlphaSorted(output);
		
		// We expect the hash to be the same.
		boolean hashCheck = CheckHash(input, itemCount) == CheckHash(output,itemCount);
		
		// One of the two above statements failed.
		if(!(alphaSortedCheck && hashCheck))
		{
			throw new IllegalStateException("Issue with Hashes or Alpha Sort.");
		}
	}
	 
	// Returns true if the array is sorted in ascending order. False otherwise.
	private static boolean AlphaSorted(ArrayList<String> arr)
	{
		for(int i = 0; i < arr.size()-2; ++i)
		{
			boolean proceedsCheck = arr.get(i).compareTo(arr.get(i+1)) <= 0;
			if(!(proceedsCheck)) return false;
		}
		return true;
	}
	
	// Generates the hash value of the array by summing the string hash values.
	private static int CheckHash(ArrayList<String> arr, int n)
	{
		int sum = 0;
		for(int i = 0; i < n; ++i)
		{
			sum += arr.get(i).hashCode();
		}
		
		return sum;
	}
	
	// Initializes file that is read in.
	private static void initializeFile(String[] args)
	{
		try
		{
			fileIn = new Scanner(new FileInputStream(args[0]));
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		
		while(fileIn.hasNext())
		{
			words.add(fileIn.next());
		}
	}
	
	// Provides the initial call to begin merge sorting.
	private static void MergeSortSetup(ArrayList<String> arr)
	{
		MergeSort(arr, 0, arr.size());
	}
	
	// THe recursive portion of merge sort.
	public static void MergeSort(ArrayList<String> arr, int p, int r) {
		if (p < r) 
		{
			int q = (p + r) / 2;
			MergeSort(arr, p, q);
			MergeSort(arr, q + 1, r);
			Merge(arr, p, q, r);
		}
	}

	// Merges the two halves of the array back together.
	public static void Merge(ArrayList<String> arr, int p, int q, int r) {
		int nOne = q - p + 1;
		int nTwo = r - q;

		ArrayList<String> left = new ArrayList<>();
		ArrayList<String> right = new ArrayList<>();

		// Populating Left
		for (int i = 0; i < nOne; i++) 
		{
			if(p + i - 1 == -1)continue;
			left.add(arr.get(p + i - 1));
		}

		// Populating Right
		for (int j = 0; j < nTwo; j++) 
		{
			right.add(arr.get(q + j));
		}

		// Left "pointer"
		int i = 0;
		
		// Right "pointer"
		int j = 0;

		for (int k = p-1; k < r; k++) 
		{
			if(k == -1) continue;
				
			// Left pointer is out of bounds.
			if(i == left.size())
			{
				arr.set(k, right.get(j));
				++j;
				continue;
			}
			
			// Right pointer is out of pounds.
			if(j == right.size())
			{
				arr.set(k, left.get(i));
				++i;
				continue;
			}
			
			// Left value proceeds right value.
			if(left.get(i).compareTo(right.get(j)) <= 0)
			{
				arr.set(k, left.get(i));
				i++;
			}
			
			// Right value proceeds left value.
			else
			{
				arr.set(k, right.get(j));
				j++;
			}
		}
	}

	// Insertion Sort.
	private static void InsertionSort(ArrayList<String> list)
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
	}
}

// Class stores all the run-time data from the sorting process.
class SortResult
{
	private int numberOfItems;

	private ArrayList<Double> timesInsertion = null;
	private ArrayList<Double> timesMerge = null;

	private double averageInsertionTimes = 0;
	private double averageMergeTimes = 0;

	public SortResult(int numberOfItems)
	{
		this.numberOfItems = numberOfItems;

		timesInsertion = new ArrayList<>();
		timesMerge = new ArrayList<>();
	}

	// Average Insertion time.
	public void calculateAverageInsertionTimes()
	{
		double average = 0;
		for(double f : timesInsertion)
		{
			average += f;
		}
		averageInsertionTimes = average/timesInsertion.size();
	}

	// Average merge time.
	public void calculateAverageMergeTimes()
	{
		double average = 0;
		for(double f : timesMerge)
		{
			average += f;
		}
		averageMergeTimes = average/timesMerge.size();
	}

	// Getters
	public double getAverageInsertionTimes() {return averageInsertionTimes;}
	public double getAverageMergeTimes(){return averageMergeTimes;}
	
	// Adding times.
	public void addTimeInsertion(double f)
	{
		timesInsertion.add(f);
	}
	
	public void addTimeMerge(double f)
	{
		timesMerge.add(f);
	}

	// Converting to string output.
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Avg. times for n = " + numberOfItems + ": ");
		sb.append("Insertion Sort " + averageInsertionTimes + " sec., ");
		sb.append("Merge Sort " + averageMergeTimes + " sec.");

		return sb.toString();
	}
}
