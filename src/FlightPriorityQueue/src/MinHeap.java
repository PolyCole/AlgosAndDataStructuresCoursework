import java.util.Arrays;

/*
 * Author: Cole Polyak
 * 23 October 2018
 * MinHeap.java
 * 
 * This class provides functionality for a minimum heap.
 */

public class MinHeap 
{
	/*
	 * I used a dynamically scaling array as opposed to an ArrayList because
	 * I didn't want the List to shift when I removed the first element. 
	 */
	private int capacity = 10;
	private int size = 0;
	Flight[] flights;
	
	// Constructor
	public MinHeap()
	{
		flights = new Flight[capacity];
	}
	
	/**
	 * This method swaps two indicies.
	 * @param indexOne : First index to be used in swap.
	 * @param indexTwo : Second index to be used in swap.
	 */
	private void swap(int indexOne, int indexTwo) 
	{
		Flight temp = flights[indexOne];
		flights[indexOne] = flights[indexTwo];
		flights[indexTwo] = temp;
	}
	
	/**
	 * Ensures that the array scales correctly and that we have
	 * enough space to insert the flights we need to.
	 */
	private void checkSize()
	{
		if(size == capacity)
		{
			// Copies the array into a larger arary.
			flights = Arrays.copyOf(flights, capacity * 2);
			capacity *= 2;
		}
	}
	
	/**
	 * This method looks at the item on the top of the heap.
	 * @return : The flight with the minimum landing priority.
	 */
	public Flight peek()
	{
		if(size ==0) throw new IllegalStateException();
		return flights[0];
	}
	
	/**
	 * Pulls the top item off the heap and reconstructs the heap.
	 * @return : The item on the top of the heap.
	 */
	public Flight poll()
	{
		if(size == 0) throw new IllegalStateException();
		Flight item = flights[0];
		
		// Pulling the bottom-most leaf up.
		flights[0] = flights[size -1];
		
		// Removing the leaf to avoid duplicates.
		flights[size - 1] = null;
		size--;
		
		// Ensures validity of heap.
		heapifyDown();
		
		return item;
	}
	
	/**
	 * Adds a new item to the heap.
	 * @param newFlight : Flight to be adde to the heap.
	 */
	public void add(Flight newFlight)
	{
		// Makes sure there's space.
		checkSize();
		flights[size] = newFlight;
		size++;
		
		// Adds flight in a leaf position, have to ensure heap is valid above leaf.
		heapifyUp();
	}
	
	/**
	 * Ensures the validity of the heap from a leaf up to the root.
	 */
	public void heapifyUp()
	{
		// Always inserting at size -1.
		int index = size -1;
		while(hasParent(index) && parent(index).getLandingPriorityAdj() > flights[index].getLandingPriorityAdj())
		{
			// Swaps new node into proper place. 
			swap(getParentIndex(index), index);
			index = getParentIndex(index);
		}
	}
	
	/**
	 * Ensures validity of the heap moving from the root to the leaves. 
	 */
	public void heapifyDown()
	{
		int index = 0;
		while(hasLeftChild(index))
		{
			int smallerChildIndex = getLeftChildIndex(index);
			
			// Checks which of the children are smaller.
			if(hasRightChild(index) && rightChild(index).getLandingPriorityAdj() < leftChild(index).getLandingPriorityAdj())
			{
				smallerChildIndex = getRightChildIndex(index);
			}
			
			// We're all good. 
			if(flights[index].getLandingPriorityAdj() < flights[smallerChildIndex].getLandingPriorityAdj())
			{
				break;
			}
			// Need to switch node down. 
			else
			{
				swap(index, smallerChildIndex);
				index = smallerChildIndex;
			}
		}
	}
	
	// Index getters.
	private int getLeftChildIndex(int parentIndex) {return (2*parentIndex) + 1; }
	private int getRightChildIndex(int parentIndex) { return (2*parentIndex) + 2; }
	private int getParentIndex(int childIndex) { return (childIndex -1) /2; }
	
	// Children / Parent checks.
	private boolean hasLeftChild(int index) { return getLeftChildIndex(index) < size;}
	private boolean hasRightChild(int index) {return getRightChildIndex(index) < size;}
	private boolean hasParent(int index) { return getParentIndex(index) >= 0;}
	
	// Gets the parent or child.
	private Flight leftChild(int index) { return flights[getLeftChildIndex(index)];}
	private Flight rightChild(int index) { return flights[getRightChildIndex(index)];}
	private Flight parent(int index) { return flights[getParentIndex(index)]; }
	
	// Gets size. 
	public int getSize()
	{
		return size;
	}
}
