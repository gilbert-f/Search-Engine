package datastructures.concrete;

import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;

/**
 * Project 3
 * Written by : Dennis Muljadi and Gilbert Febrianto
 * 
 * See IPriorityQueue for details on what each method must do.
 * 
 * Represents a queue where the elements are ordered such that the
 * front element is always the "smallest", as defined by the
 * element's compareTo method.
 */
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a implement a 4-heap.
    private static final int NUM_CHILDREN = 4;

    // You MUST use this field to store the contents of your heap.
    // You may NOT rename this field: we will be inspecting it within
    // our private tests.
    private T[] heap;

    // Extra Fields and Constants
    private static final int INITIAL_SIZE = 6;
    private int height;
    private int heapSize;
    
    public ArrayHeap() {
    		heap = makeArrayOfT(INITIAL_SIZE);
    		height = 1; 
    		heapSize = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain elements of type T.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int size) {
        // This helper method is basically the same one we gave you
        // in ArrayDictionary and ChainedHashDictionary.
        //
        // As before, you do not need to understand how this method
        // works, and should not modify it in any way.
        return (T[]) (new Comparable[size]);
    }
    
    /**
     * Removes and return the smallest element in the queue.
     *
     * If two elements within the queue are considered "equal"
     * according to their compareTo method, this method may break
     * the tie arbitrarily and return either one.
     *
     * @throws EmptyContainerException  if the queue is empty
     */
    @Override
    public T removeMin() {
    		if (this.isEmpty()) {
    			throw new EmptyContainerException();
    		}
    		
	    	T temp = peekMin();
	    	
	    	heap[1] = heap[heapSize]; 
	    	heap[heapSize] = null;
	    	heapSize--;
	    	
	    	percolateDown();
	    	
	    	return temp;
    }
    
    /**
     * Returns, but does not remove, the smallest element in the queue.
     *
     * This method must break ties in the same way the removeMin
     * method breaks ties.
     *
     * @throws EmptyContainerException  if the queue is empty
     */
    @Override
    public T peekMin() {
    		if (this.isEmpty()) {
			throw new EmptyContainerException();
		}
        
        return heap[1];
    }

    /**
     * Inserts the given item into the queue.
     *
     * @throws IllegalArgumentException  if the item is null
     */
    @Override
    public void insert(T item) {
    		if (item == null) {
    			throw new IllegalArgumentException();
    		}
    		
    		this.checkResize();        
        
        heapSize++;
        int index = heapSize;
        heap[index] = item;
        
        percolateUp();
    }

    /**
     * Returns the number of elements contained within this queue.
     */
    @Override
    public int size() {
    		return this.heapSize;
    }
    
    @Override
    public String toString() {
        String output = "[";
        for (int i = 1; i <= heapSize; i++) {
        		if (i >= heapSize) {
        			output += heap[i];
        		} else {
        			output += heap[i] + ", ";
        		}
        }
        return output + "]";
    }
    
    private void checkResize() {
        if (heapSize >= heap.length - 1) {
	        	height++;
		    	T[] temp = makeArrayOfT(heap.length + (int) Math.pow(NUM_CHILDREN, height));
		    	for (int i = 0; i < heap.length; i++) {
		    		temp[i] = heap[i];
		    	}
		    	heap = temp;
        }
    }
    
    private void percolateUp() {
        int index = this.size();
        
        while (hasParent(index)
                && (heap[parentIndex(index)].compareTo(heap[index]) > 0)) {
            swap(index, parentIndex(index));
            index = parentIndex(index);
        }        
    }
    
    private void percolateDown() {
    		int index = 1;
    		
    		while (hasLeftChild(index)) {
    			int smallestChild = leftIndex(index);
    			
    			int i = -1;
    			while (i <= 1) {
    				int childIndex = (NUM_CHILDREN * index) + i;
    				if (childIndex <= heapSize) {
    						if (heap[childIndex].compareTo(heap[smallestChild]) < 0) {
    							smallestChild = childIndex;
    						}
                	} else {
                		break;
                	}
    				i++;
    			}
            
            if (heap[smallestChild].compareTo(heap[index]) < 0) {
                swap(smallestChild, index);
            } else {
                break;
            }
            
            index = smallestChild;
        }  
    }
    
    private void swap(int i1, int i2) {
        T temp = heap[i2];
        heap[i2] = heap[i1];
        heap[i1] = temp;        
    }
    
    private boolean hasParent(int i) {
        return (i > 1);
    }
    
    private int parentIndex(int i) {
		return ((i+2)/NUM_CHILDREN);
    }
    
    private boolean hasLeftChild(int i) {
        return (leftIndex(i) <= this.size());
    }
    
    private int leftIndex(int i) {
        return ((i*NUM_CHILDREN)-2);
    }
}