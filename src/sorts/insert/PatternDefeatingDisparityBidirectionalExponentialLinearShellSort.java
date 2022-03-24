package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class PatternDefeatingDisparityBidirectionalExponentialLinearShellSort extends Sort {
    
    public PatternDefeatingDisparityBidirectionalExponentialLinearShellSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Pattern-Defeating Disparity Bidirectional Exponential Linear Shell");
        this.setRunAllSortsName("Pattern-Defeating Disparity Bidirectional Exponential Linear Shell Sort");
        this.setRunSortName("Pattern-Defeating Disparity Bidirectional Exponential Linear Shellsort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    private int disparity(int[] array, int start, int end) {
    	if(end-start < 3)
    		return end-1;
    	int min = start, max = start;
    	for(int i=start+1; i<end; i++) {
    		if(Reads.compareIndices(array, i, max, 0.125, true) == 1) {
    			min = i - 1;
    			while(min > start && Reads.compareIndices(array, min, i, 1, true) > 0) {
    				min=(min-i)*2+i;
    			}
    			max = i;
    		} else if(Reads.compareIndices(array, i, min, 0.125, true) == -1) {
    			min = i;
    		}
    	}
    	return Math.abs(max-min);
    }
    
    private boolean isSorted(int[] array, int end) {
    	int comp = Reads.compareIndices(array, 0, 1, 0.1, true);
    	if(end == 2) {
    		if(comp == 1)
    			Writes.swap(array, 0, 1, 0.1, true, false);
    		return true;
    	}
    	if(comp == 0)
    		comp = -1;
    	for(int i=0; i<end-1; i++) {
    		if(Reads.compareIndices(array, i, i+1, 0.1, true) != comp) {
    			if(comp == 1) {
    				Writes.reversal(array, 0, i, 0.1, true, false);
    			}
    			return false;
    		}
    	}
		if(comp == 1) {
			Writes.reversal(array, 0, end-1, 0.1, true, false);
		}
    	return true;
    }
    
    private void gapReverse(int[] array, int start, int end, int gap) {
    	Writes.changeReversals(1);
    	while(start <= end-gap) {
    		Writes.swap(array, start, end, 1, true, false);
    		start+=gap;
    		end-=gap;
    	}
    }
    
    private int exp(int[] array, int start, int end, int v, boolean invert, int gap) {
    	int n=1;
    	while(n*gap<end-start && (invert ^ (Reads.compareValues(array[end-(n*gap)], v) > 0))) {
    		n*=2;
    	}
    	if(n*gap>end-start) {
    		n=(end-start)/gap;
    	}
    	return end-((n/2)*gap);
    }
    
    private void shell(int[] array, int start, int end, int gap) {
    	if(end-start <= gap)
    		return;
    	if(gap < 1) gap = 1;
    	int len=end-start;
    	boolean[] gaps = new boolean[gap];
    	for(int i=start+gap; i<end; i++) {
    		int t = array[i],
    			g = (i-start)%gap;
    		Highlights.markArray(1, i);
    		Delays.sleep(0.1);
    		if(gaps[g] ^ (Reads.compareValues(array[start+g], t) >= 0)) {
    			gapReverse(array, start+g, i-gap, gap);
    			gaps[g]=!gaps[g];
    			continue;
    		}
    		int j=exp(array, start+g, i, t, gaps[g], gap);
    		while(j >= start) {
    			if(gaps[g] ^ Reads.compareValues(array[j], t) < 0) {
    				break;
    			}
    			j-=gap;
    		}
    		int l=i-gap;
    		while(l>j) {
    			Writes.write(array, l+gap, array[l], 0.1, true, false);
    			l -= gap;
    		}
			Writes.write(array, l+gap, t, 0.1, true, false);
    	}
    	int eg = end-(len%gap);
    	for(int i=0; i<gap; i++) {
    		int b=i+eg;
    		if(b >= end) {
    			b-=gap;
    		}
    		if(gaps[i]) {
    			gapReverse(array, start+i, b, gap);
    		}
    	}
    }
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
    	int size = currentLength, t = (int) Math.sqrt(currentLength),
    		t2 = (int) Math.cbrt(currentLength);
    	
    	while(size > 1) {
    		int disparity = this.disparity(array, 0, size);
    		if(disparity <= Math.sqrt(size)) {
    			disparity = (int) Math.sqrt(size);
    		}
    		
    		if(disparity == size - 1 && isSorted(array, currentLength))
    			return;
    		
    		this.shell(array, 0, currentLength, disparity);
    		
    		size = disparity;
    	}
    }
}