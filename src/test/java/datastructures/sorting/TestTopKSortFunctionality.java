package datastructures.sorting;

import misc.BaseTest;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import misc.Searcher;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

/**
 * Project 3
 * Written by : Dennis Muljadi and Gilbert Febrianto
 * 
 * See spec for details on what kinds of tests this class should include.
 */
public class TestTopKSortFunctionality extends BaseTest {
    @Test(timeout=SECOND)
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<Integer>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(15 + i, top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testReverseUsage() {
        IList<Integer> list = new DoubleLinkedList<Integer>();
        int limit = 50;
        
        for (int i = limit-1; i >= 0; i--) {
            list.add(i*2);
        }
        
        int k = 5;
        IList<Integer> top = Searcher.topKSort(k, list);
        assertEquals(k, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(2*(limit - k + i), top.get(i));
        }
        
        k = 50;
        top = Searcher.topKSort(k, list);
        assertEquals(k, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(i*2, top.get(i));
        }
        
        k = 50;
        top = Searcher.topKSort(k+5, list);
        assertEquals(k, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(i*2, top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testSingleUsage() {
        IList<Integer> list = new DoubleLinkedList<Integer>();
        list.add(1);
        
        IList<Integer> top = Searcher.topKSort(1, list);
        assertEquals(1, top.size());
        assertEquals(1, top.get(0));
    }
    
    @Test(timeout=SECOND)
    public void testSmallUsage() {
        IList<Integer> list = new DoubleLinkedList<Integer>();
        list.add(1);
        list.add(2);
        
        IList<Integer> top = Searcher.topKSort(5, list);
        assertEquals(2, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(1 + i, top.get(i));
        }
        
        top = Searcher.topKSort(1, list);
        assertEquals(1, top.size());
        assertEquals(2, top.get(0));
    }
    
    @Test(timeout=SECOND)
    public void testLargeUsage() {
        IList<Integer> list = new DoubleLinkedList<Integer>();
        int limit = 100000;
        for (int i = 0; i < limit; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(1000, list);
        assertEquals(1000, top.size());
        
        int index = limit-1000;
        for (Integer item : top) {
        		assertEquals(index, item);
        		index++;
        }
        
        top = Searcher.topKSort(limit+1, list);
        assertEquals(limit, top.size());
        
        index = 0;
        for (Integer item : top) {
        		assertEquals(index, item);
        		index++;
        }
    }
    
    @Test(timeout=SECOND)
    public void testNegativeKException() {
        IList<Integer> list = new DoubleLinkedList<Integer>();
        
        try {
        		IList<Integer> top = Searcher.topKSort(-1, list);
	        // We didn't throw an exception? Fail now.
	        fail("Expected IllegalArgumentException");
	    } catch (IllegalArgumentException ex) {
	        // Do nothing: this is ok
	    }
    }

    @Test(timeout=SECOND)
    public void testKSort() {
    		int limit = 999999;
        IList<Integer> list1 = new DoubleLinkedList<Integer>();
        List<Integer> list2 = new ArrayList<Integer>();
        
        for (int i = limit - 1; i >= 0; i--) {
        		list1.add(i);
        		list2.add(i);
        }
        
        Collections.sort(list2);
        
        int k = 10000;
        IList<Integer> top = Searcher.topKSort(k, list1);
        assertEquals(k, top.size());
        
        for (int i = 0; i < k; i++) {
            assertEquals(list2.get(list2.size() - (k - i)), top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testKLargerThanSize() {
        IList<Integer> list = new DoubleLinkedList<Integer>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(30, list);
        assertEquals(20, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(i, top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testDuplicateEntriesAndKEqualSize() {
        IList<Integer> list = new DoubleLinkedList<Integer>();
        for (int i = 0; i < 20; i++) {
            list.add(0);
        }
        
        for (int i = 0; i < 20; i++) {
            list.add(1);
        }

        IList<Integer> top = Searcher.topKSort(40, list);
        assertEquals(40, top.size());
        for (int i = 0; i < top.size()/2; i++) {
            assertEquals(0, top.get(i));
        }
        for (int i = 20; i < top.size(); i++) {
            assertEquals(1, top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testKZero() {
        IList<Integer> list = new DoubleLinkedList<Integer>();
        for (int i = 0; i < 10000; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(0, list);
        assertEquals(0, top.size());
    }
    
    @Test(timeout=SECOND)
    public void testOnEmptyList() {
    		IList<Integer> list = new DoubleLinkedList<Integer>();
    		IList<Integer> top = Searcher.topKSort(40, list);
    		assertEquals(0, top.size());
    }
    
    @Test(timeout=SECOND)
    public void testNullListException() {
        IList<Integer> list = null;
        
        try {
        		IList<Integer> top = Searcher.topKSort(10, list);
	        // We didn't throw an exception? Fail now.
	        fail("Expected IllegalArgumentException");
	    } catch (IllegalArgumentException ex) {
	        // Do nothing: this is ok
	    }
    }
}
