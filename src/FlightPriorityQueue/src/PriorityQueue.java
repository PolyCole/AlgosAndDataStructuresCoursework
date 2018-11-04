/*
 * Author: Cole Polyak
 * 23 October 2018
 * PriorityQueue.java
 * 
 * This class creates a priority queue using
 * a min heap. 
 */

public class PriorityQueue 
{
	// Our heap.
	private MinHeap heap;
	
	public PriorityQueue()
	{
		heap = new MinHeap();
	}
	
	// Remasks add as Enqueue.
	public void enqueue(Flight f)
	{
		heap.add(f);
	}
	
	// Remasks poll as dequeue.
	public Flight dequeue()
	{
		return heap.poll();
	}
	
	public Flight peek()
	{
		return heap.peek();
	}
	
	// Heapsize.
	public int size()
	{
		return heap.getSize();
	}
}
