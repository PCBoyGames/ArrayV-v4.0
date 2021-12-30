package sorts.merge;

import java.util.PriorityQueue;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class BottomDownMergeSort extends Sort {

    public BottomDownMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Bottom-Down Merge");
        this.setRunAllSortsName("Bottom-Down Sort");
        this.setRunSortName("Bottom-Down Mergesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    private int[] main, scratch;
    private class ArrayCopy implements Comparable<ArrayCopy> {
    	private int start, end, balance;
    	public ArrayCopy(int start, int end, int balance) {
    		this.start = start; this.end = end; this.balance = balance;
    	}
    	public int length() {
    		return end - start;
    	}
    	public void perform() {
    		Writes.arraycopy(scratch, start, main, start, this.length(), 1, true, false);
    	}
    	private int branchlessComp(int a, int b) {
    		return -((a - b) >>> 31) | ((b - a) >>> 31);
    	}
    	public int compare(ArrayCopy other) {
    		return branchlessComp(Math.abs(this.balance), Math.abs(other.balance));
    	}
    	
    	@Override
    	public int compareTo(ArrayCopy other) {
    		return this.compare(other);
    	}
    }
    private void merge(PriorityQueue<ArrayCopy> q, int start, int mid, int end) {
    	int l = start, r = mid, t = start, b = 0;
    	while(l < mid && r < end) {
    		Highlights.markArray(1, l);
    		Highlights.markArray(2, r);
    		if(Reads.compareValues(main[l], main[r]) <= 0) {
    			Writes.write(scratch, t++, main[l++], 1, true, true);
    			b = (b << 1) + 1;
    		} else {
    			Writes.write(scratch, t++, main[r++], 1, true, true);
    			b = (b >>> 1) - 1;
    		}
    	}
    	while(l < mid) {
    		Highlights.markArray(1, l);
			Writes.write(scratch, t++, main[l++], 1, true, true);
			b = (b >>> 1) + b;
    	}
    	while(r < end) {
    		Highlights.markArray(2, r);
			Writes.write(scratch, t++, main[r++], 1, true, false);
			b = (b >>> 1) - 1;
    	}
    	Highlights.clearAllMarks();
    	q.offer(new ArrayCopy(start, end, b));
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
    	PriorityQueue<ArrayCopy> q = new PriorityQueue<>();
        main = array;
        scratch = Writes.createExternalArray(length);
        int i = 2;
        for(; i<=length; i*=2) {
        	for(int j=0; j<length; j+=i) {
        		if(j+i < length)
        		 	this.merge(q, j, j+i/2, j+i);
        		else
        		 	this.merge(q, j, j+i/2, length);
        	}
        	while(q.size() > 0) {
        	 	q.poll().perform();
        	}
        }
        if(i / 2 != length) {
        	this.merge(q, 0, i/2, length);
    		q.poll().perform();
        }
    }
}
