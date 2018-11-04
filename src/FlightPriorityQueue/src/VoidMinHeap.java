import java.util.ArrayList;

public class VoidMinHeap 
{
	ArrayList<Flight> flights;
	
	public VoidMinHeap()
	{
		flights = new ArrayList<Flight>();
	}
	
	private int getLeftChildIndex(int parentIndex) { return 2 * parentIndex + 1; }
	private int getRightChildIndex(int parentIndex) { return 2 * parentIndex + 2; }
	private int getParentIndex(int childIndex) { return (childIndex -1) /2; }
	
	private boolean hasLeftChild(int index) { return getLeftChildIndex(index) < flights.size();}
	private boolean hasRightChild(int index) {return getRightChildIndex(index) < flights.size();}
	private boolean hasParent(int index) { return getParentIndex(index) >= 0;}
	
	private Flight leftChild(int index) { return flights.get(getLeftChildIndex(index));}
	private Flight rightChild(int index) { return flights.get(getRightChildIndex(index));}
	private Flight parent(int index) { return flights.get(getParentIndex(index)); }
	
	private void swap(int indexOne, int indexTwo) 
	{
		Flight temp = flights.get(indexOne);
		flights.set(indexOne, flights.get(indexTwo));
		flights.set(indexTwo, temp);
	}
	
	public Flight peek()
	{
		if(flights.size() ==0) throw new IllegalStateException();
		return flights.get(0);
	}
	
	public Flight poll()
	{
		if(flights.size() == 0) throw new IllegalStateException();
		Flight top = flights.get(0);
		flights.remove(0);
		if(flights.size() > 0) 
		{
			flights.set(0, new Flight("NULL", -1, Integer.MAX_VALUE));
		}
		
		heapifyDown();
		return top;
	}
	
	public void add(Flight newFlight)
	{
		flights.add(newFlight);
		heapifyUp();
	}
	
	public void heapifyUp()
	{
		int index = flights.size() -1;
		while(hasParent(index) && parent(index).getLandingPriority() > flights.get(index).getLandingPriority())
		{
			swap(getParentIndex(index), index);
			index = getParentIndex(index);
		}
	}
	
	public void heapifyDown()
	{
		int index = 0;
		while(hasRightChild(index))
		{
			int smallerChildIndex = getLeftChildIndex(index);
			
			if(hasRightChild(index) && rightChild(index).getLandingPriority() < leftChild(index).getLandingPriority())
			{
				smallerChildIndex = getRightChildIndex(index);
			}

			if(flights.get(index).getLandingPriority() < flights.get(smallerChildIndex).getLandingPriority())
			{
				break;
			}
			else
			{
				swap(index, smallerChildIndex);
				index = smallerChildIndex;
			}
		}
		removePlaceHolder();
	}
	
	private void removePlaceHolder()
	{
		for(int i = 0; i < flights.size(); ++i)
		{
			if(flights.get(i).getLandingPriority() == Integer.MAX_VALUE)
			{
				flights.remove(i);
			}
		}
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		for(Flight f : flights)
		{
			sb.append(f + "**");
		}
		
		return sb.toString();
	}
	
	public int size()
	{
		return flights.size();
	}
}
