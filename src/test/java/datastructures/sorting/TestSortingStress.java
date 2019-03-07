package datastructures.sorting;

import misc.BaseTest;
import misc.Searcher;

import org.junit.Test;

import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;
import static org.junit.Assert.fail;

/**
 * Project 3
 * Written by : Dennis Muljadi and Gilbert Febrianto
 * 
 * See spec for details on what kinds of tests this class should include.
 */
public class TestSortingStress extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<T>();
    }
    
    @Test(timeout=10*SECOND)
    public void stressTestTopKSort() {
    		int limit = 999999;
        IList<Integer> list1 = new DoubleLinkedList<Integer>();
        
        for (int i = 0; i < limit; i++) {
	        	list1.add(i);
        }
        
        IList<Integer> top = Searcher.topKSort(limit+1, list1);
        assertEquals(limit, top.size());
        
        int index = 0;
        for (Integer item : top) {
        		assertEquals(index, item);
        		index++;
        }
        
        top = Searcher.topKSort(limit, list1);
        assertEquals(limit, top.size());
        
        index = 0;
        for (Integer item : top) {
        		assertEquals(index, item);
        		index++;
        }
        
        list1 = new DoubleLinkedList<Integer>();
        
        for (int i = limit - 1; i >= 0; i--) {
        		list1.add(i);
	    }
	    
	    top = Searcher.topKSort(limit/100, list1);
	    assertEquals(limit/100, top.size());
	    
	    index = limit-(limit/100);
	    for (Integer item : top) {
	    		assertEquals(index, item);
	    		index++;
	    }
	    
	    try {
	        	for (int i = 0; i < limit; i++) {
	        		top = Searcher.topKSort(-1, list1);
		        fail("Expected IllegalArgumentException");
	        	}
	    } catch (IllegalArgumentException ex) {
	    		// do nothing
	    }
    }
	
    @Test(timeout=10*SECOND)
    public void stressTestArrayHeap() {
        int limit = 999999;
    	
        IPriorityQueue<Integer> heap = this.makeInstance();
        
        int counter = 0;
        
        for (int i = limit; i >= 0; i--) {
        		counter++;
        		heap.insert(i);
            assertEquals(i, heap.peekMin());
            assertEquals(counter, heap.size());
        }
        
        for (int i = limit; i >= 0; i--) {
        		counter--;
	        assertEquals(heap.peekMin(), heap.removeMin());
	        assertEquals(counter, heap.size());
	    }
        
        for (int i = limit; i >= 0; i--) {
	    		counter++;
	    		heap.insert(i);
	        assertEquals(i, heap.peekMin());
	        assertEquals(counter, heap.size());
	    }
	    
	    for (int i = limit; i >= 0; i--) {
	    		counter--;
	        assertEquals(heap.peekMin(), heap.removeMin());
	        assertEquals(counter, heap.size());
	    }
    }
}
