import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * Author: Cole Polyak
 * 7 October 2018
 * Main.java
 * 
 * This class compares the run times of Kadane's 
 * algorithm and another divide and conquer algorithm
 * that also finds the max sub array.
 */

public class Main
{
	public static PrintWriter fileOut = null;
	public static Scanner fileIn = null;
	public static long[] nums;
	
	// The number of times we will find the max array.
	public static int iterations;
	
	public static void main(String[] args)
	{
		initializeFileInput(args);
		
		if(fileIn != null)
		{
			nums = initializeArray();
			fileIn.close();
			
			// Where we'll store the times.
			ArrayList<Float> divideTimes = new ArrayList<>();
			ArrayList<Float> kadaneTimes = new ArrayList<>();
			
			long divideConquerSum = 0;
			long kadaneSum = 0;
			
			for(int i = 0; i < iterations; ++i)
			{
				// Timing Divde and Conquer
				CpuTimer cp = new CpuTimer();
				divideConquerSum = divideAndConquerMaximum();
				divideTimes.add((float) cp.getElapsedCpuTime());
				
				// Timing Kadanes.
				cp = new CpuTimer();
				kadaneSum = kadaneMaximum();
				kadaneTimes.add((float)cp.getElapsedCpuTime());
			}
			
			float avgDivideTime = averageArrList(divideTimes);
			float avgKadaneTime = averageArrList(kadaneTimes);
			
			// Writing to file.
			outputData(avgDivideTime, divideConquerSum, avgKadaneTime, kadaneSum);
		}
	}
	
	/**
	 * 
	 * @param avgDivideTime : Average time for divde and conquer
	 * @param divideSum : The max subarray sum returned.
	 * @param avgKadaneTime : Average time for kadane's.
	 * @param kadaneSum : The max subarray sum returned.
	 */
	private static void outputData(float avgDivideTime, long divideSum, float avgKadaneTime, long kadaneSum)
	{
		try 
		{
			// Creating our output file.
			fileOut = new PrintWriter(new File("TimingOutput_" + nums.length + ".txt"));
			
			// Writing
			fileOut.println(nums.length +
							",\"R\","  + 
							avgDivideTime + 
							"," + 
							divideSum);
			
			fileOut.println(nums.length +
							",\"K\","  + 
							avgKadaneTime + 
							"," + 
							kadaneSum);
			
			fileOut.close();
		} 
		catch (FileNotFoundException e) 
		{
			System.out.println("Output file not found. Exiting");
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param args : The argument array for our input file initialization.
	 */
	private static void initializeFileInput(String[] args)
	{
		try
		{
			fileIn = new Scanner(new FileInputStream(args[0]), "UTF-8");
		}
		catch(FileNotFoundException e)
		{
			System.out.println("File not found. Exiting.");
			System.exit(1);
		}
	}
	
	/**
	 * 
	 * @param arr : The array list to be averaged.
	 * @return : The average value in the array.
	 */
	private static float averageArrList(ArrayList<Float> arr)
	{
		float sum = 0;
		for(int i = 0; i < arr.size(); ++i)
		{
			sum += arr.get(i);
		}
		
		return sum / arr.size();
	}
	
	/**
	 * 
	 * @return : The array after each value has been read in.
	 */
	public static long[] initializeArray()
	{
		// Since we don't know the size initially, using an AL will be easier.
		ArrayList<Long> tempArrList = new ArrayList<>();
		while(fileIn.hasNextLong()) tempArrList.add(fileIn.nextLong());
		
		// Transcribing the ArrayList to an array.
		long[] resultArray = new long[tempArrList.size()];
		for(int i = 0; i < tempArrList.size(); ++i)
		{
			resultArray[i] = tempArrList.get(i);
		}
		
		// Determining how many iterations.
		iterations = 625000/resultArray.length;
		
		return resultArray;
	}
	
	/**
	 * 
	 * @return : The maximum subarray sum using the divide and conquer method.
	 */
	public static long divideAndConquerMaximum()
	{
		// Sum returned by divide and conquer.
		long divideConquerSum = FindMaxSubarray(nums, 0, nums.length-1);
		
		// The sum returned by assuming the array is circular.
		long wrapSum = completeSumCalculator(nums, false);
		
		if(wrapSum > divideConquerSum) return wrapSum;
		
		return divideConquerSum;
	}
	
	/**
	 * 
	 * @return : The maximum subarray sum using Kadane's algorithm.
	 */
	public static long kadaneMaximum()
	{
		// Sum returned by kadane's algorithm.
		long kadaneSum = kadane(nums);
		
		// The sum returned by assuming the array is circular.
		long wrapSum = completeSumCalculator(nums, true);
		
		if(wrapSum > kadaneSum) return wrapSum;
		
		return kadaneSum;
	}
	
	/**
	 * 
	 * @param arr : Array to be used.
	 * @param isKadane : Which method we will be using to find the maximum.
	 * @return : The maximum value of the subarray assuming the array is circular.
	 */
	private static long completeSumCalculator(long[] arr, boolean isKadane)
	{
		// Cloning array to avoid accidentally altering original.
		long[] arrClone = arr.clone();
		
		// This value will be the sum of the regular array.
		long completeSum = 0;
		
		for(int i = 0; i < arrClone.length; ++i)
		{
			completeSum += arr[i];
			
			/*
			 * By inverting the array we can use our existing methods to find
			 * the "largest" (most negative) section of the array. 
			 */
			arrClone[i] = -arrClone[i];
		}

		if(isKadane)
		{
			completeSum += kadane(arrClone);
			return completeSum;
		}
		
		/*
		 * Adding the sum of the smallest subarray to the sum of the total array
		 * leaves behind the sum of the maximum array. 
		 */
		completeSum += FindMaxSubarray(arrClone, 0, arrClone.length-1);
		return completeSum;
	}
	
	/**
	 * 
	 * @param arr : Array to be used.
	 * @return : The sum of the maximum sub array.
	 */
	public static long kadane(long[] arr)
	{
		long maxSumSoFar = 0;
		long maxSumTok = 0;
		
		for(int k = 0; k < arr.length; ++k)
		{
			maxSumTok = maxSumTok + arr[k];
			
			// If it's negative we know we're too low.
			if(maxSumTok < 0) maxSumTok = 0;
			
			// Replacing our observed maxsum with our current, larger, max sum.
			if(maxSumSoFar < maxSumTok) maxSumSoFar = maxSumTok;
		}
		
		return maxSumSoFar;
	}
	
	/**
	 * 
	 * @param arr : Array to be used. 
	 * @param low : Index of left side.
	 * @param high : Index of right side.
	 * @return : A Result object, containing the sum of the max subarray on that side.
	 */
	public static long FindMaxSubarray(long[] arr, int low, int high)
	{
		long returned = 0;
		
		int mid;
		
		// Base case. Array has no substance.
		if(high == low)
		{
			return returned;
		}
		else
		{
			// Midpoint of array.
			mid = (low + high) /2;
			
			// Recursive splits.
			long leftSum = FindMaxSubarray(arr, low, mid);
			long rightSum = FindMaxSubarray(arr, mid + 1, high);
			long crossSum = FindMaxCrossingSubarray(arr, low, mid, high);
			
			// Whichever sum is the biggest, return.
			if(leftIsBiggest(leftSum, rightSum, crossSum))
			{
				return leftSum;
			}
			else if(rightIsBiggest(leftSum, rightSum, crossSum))
			{
				return rightSum;
			}
			else
			{
				return crossSum;
			}
			
		}
	}
	
	// Checking if left subarray contains largest sum.
	private static boolean leftIsBiggest(long leftSum, long rightSum, long crossSum)
	{
		boolean toReturn = (leftSum >= rightSum) &&
				   (leftSum >= crossSum);
		return toReturn;
	}
	
	// Checking if right subarray contains largest sum.
	private static boolean rightIsBiggest(long leftSum, long rightSum, long crossSum)
	{
		boolean toReturn = (rightSum >= leftSum) &&
				   (rightSum >= crossSum);
		return toReturn;
	}
	
	/**
	 * 
	 * @param arr : Array to be used.
	 * @param low : Left most point.
	 * @param mid : Middle point.
	 * @param high: Right most point.
	 * @return : Result object containing sum and indicies.
	 */
	public static long FindMaxCrossingSubarray(long[] arr, int low, int mid, int high)
	{
		
		long leftSum = arr[mid];
		long sum = 0;
		
		// Going from middle to beginning.
		for(int i = mid; i >= low; --i)
		{
			if(i == -1) continue;
			sum += arr[i];
			if(sum > leftSum)
			{
				leftSum = sum;
			}
		}
		
		long rightSum = arr[mid + 1];
		sum = 0;
		
		// Going from middle to end.
		for(int j = mid+1; j <= high; ++j)
		{
			sum += arr[j];
			if(sum > rightSum)
			{
				rightSum = sum;
			}
		}
		return leftSum + rightSum;
	}
}
