package datastructures.sorting;

import static org.junit.Assert.fail;

import java.util.Arrays;

import misc.BaseTest;
import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;
import org.junit.Test;

/**
 * Project 3
 * Written by : Dennis Muljadi and Gilbert Febrianto
 * 
 * See spec for details on what kinds of tests this class should include.
 */
public class TestArrayHeapFunctionality extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<T>();
    }

    @Test(timeout=SECOND)
    public void testBasicSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        assertEquals(0, heap.size());
        heap.insert(3);
        assertEquals(1, heap.size());
        heap.insert(3);
        assertEquals(2, heap.size());
        heap.insert(3);
        assertEquals(3, heap.size());
    }
    
    @Test(timeout=SECOND)
    public void testBasicPeekMin() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        assertEquals(3, heap.peekMin());
    }
    
    @Test(timeout=SECOND)
    public void testExceptionPeekMin() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        try {
        		heap.peekMin();
            // We didn't throw an exception? Fail now.
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException ex) {
            // Do nothing: this is ok
        }
        
        heap.insert(3);
        heap.removeMin();
        
        try {
	    		heap.peekMin();
	        // We didn't throw an exception? Fail now.
	        fail("Expected EmptyContainerException");
	    } catch (EmptyContainerException ex) {
	        // Do nothing: this is ok
	    }
    }
    
    @Test(timeout=SECOND)
    public void testBasicInsert() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        Integer[] arr = new Integer[5];
        for (int i = 1; i < 6; i++) {
        		heap.insert(i);
        		arr[i-1] = i;
        }
        assertEquals(Arrays.toString(arr), heap.toString());
    }
    
    @Test(timeout=SECOND)
    public void testReverseInsert() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        Integer[] arr = {1, 2, 8, 7, 6, 9, 5, 4, 3};
        for (int i = 9; i > 0; i--) {
        		heap.insert(i);
        }
        assertEquals(Arrays.toString(arr), heap.toString());
    }
    
    @Test(timeout=SECOND)
    public void testManyInsert() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        Integer[] arr = new Integer[9999];
        for (int i = 1; i < 10000; i++) {
        		heap.insert(i);
        		arr[i-1] = i;
        }
        assertEquals(Arrays.toString(arr), heap.toString());
    }
    
    @Test(timeout=SECOND)
    public void testRepeatedInsertAndRemoveMin() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        Integer[] arr = {0, 0, 1, 1, 1, 1, 1, 0, 0};
        for (int i = 0; i < 5; i++) {
        		heap.insert(1);
        }
        for (int i = 0; i < 4; i++) {
	    		heap.insert(0);
	    }
        assertEquals(Arrays.toString(arr), heap.toString());
        
        for (int i = 0; i < 9; i++) {
        		assertEquals(heap.peekMin(), heap.removeMin());
	    }
    }
    
    @Test(timeout=SECOND)
    public void testNegativeInsertAndPeekMin() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = -1; i >= -1000; i--) {
        		heap.insert(i);
        		assertEquals(i, heap.peekMin());
        }
        assertEquals(1000, heap.size());
    }
    
    @Test(timeout=SECOND)
    public void testExceptionInsert() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        try {
        		heap.insert(null);
            // We didn't throw an exception? Fail now.
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Do nothing: this is ok
        }
        
        heap.insert(-1);
        
        try {
	    		heap.insert(null);
	        // We didn't throw an exception? Fail now.
	        fail("Expected IllegalArgumentException");
	    } catch (IllegalArgumentException ex) {
	        // Do nothing: this is ok
	    }
    }
    
    @Test(timeout=SECOND)
    public void testBasicResize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        Integer[] arr = new Integer[5];
        for (int i = 1; i < 6; i++) {
        		heap.insert(i);
        		arr[i-1] = i;
        }
        assertEquals(Arrays.toString(arr), heap.toString());
        
	    arr = Arrays.copyOf(arr, arr.length + (int) Math.pow(4, 2));
        for (int i = 6; i < arr.length; i++) {
	    		heap.insert(i);
	    		arr[i-1] = i;
	    }
        
        arr = Arrays.copyOf(arr, arr.length + (int) Math.pow(4, 2));
        for (int i = 22; i < arr.length; i++) {
	    		heap.insert(i);
	    		arr[i-1] = i;
	    }
    }
    
    @Test(timeout=SECOND)
    public void testBasicRemoveMin() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        Integer[] arr = new Integer[0];
        for (int i = 9; i > 0; i--) {
        		heap.insert(i);
        }
        for (int i = 1; i < 10; i++) {
        		heap.removeMin();
        }
        assertEquals(Arrays.toString(arr), heap.toString());
    }
    
    @Test(timeout=SECOND)
    public void testExceptionRemoveMin() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        try {
        		heap.removeMin();
            // We didn't throw an exception? Fail now.
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException ex) {
            // Do nothing: this is ok
        }
        
        heap.insert(1);
        assertEquals(heap.peekMin(), heap.removeMin());
        
        try {
	    		heap.removeMin();
	        // We didn't throw an exception? Fail now.
	        fail("Expected EmptyContainerException");
	    } catch (EmptyContainerException ex) {
	        // Do nothing: this is ok
	    }
    }
    
    @Test(timeout=SECOND)
    public void stressTest() {
        int limit = 1000000;
        	
        IPriorityQueue<Integer> heap = this.makeInstance();

        for (int i = 0; i < limit; i++) {
        		heap.insert(i);
            assertEquals(i+1, heap.size());
        }
        
        for (int i = 0; i < limit; i++) {
	        assertEquals(heap.peekMin(), heap.removeMin());
	    }
    }
    
    @Test(timeout=SECOND)
    public void testAlternatingInsertAndRemove() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < 1000000; i++) {
        		heap.insert(i);
        		assertEquals(1, heap.size());
        		assertEquals(heap.peekMin(), heap.removeMin());
        		assertEquals(0, heap.size());
        }
    }
    
    @Test(timeout=SECOND)
    public void testInsertAndPeekMinEfficiency() {
    		int limit = 1000000;
    		
    		IPriorityQueue<Integer> heap = this.makeInstance();
    		
    		for (int i = limit; i > 0; i--) {
    	        heap.insert(i);	
    	        assertEquals(i, heap.peekMin());
    		}
    		assertEquals(limit, heap.size());
    }
}
