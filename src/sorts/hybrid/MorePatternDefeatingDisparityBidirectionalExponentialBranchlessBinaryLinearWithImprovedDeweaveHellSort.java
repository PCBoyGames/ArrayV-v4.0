package sorts.hybrid;

import java.util.Comparator;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class MorePatternDefeatingDisparityBidirectionalExponentialBranchlessBinaryLinearWithImprovedDeweaveHellSort extends Sort {
    
    public MorePatternDefeatingDisparityBidirectionalExponentialBranchlessBinaryLinearWithImprovedDeweaveHellSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("More Pattern-Defeating Disparity Bidirectional Exponential Branchless Binary/Linear Shell With Improved Deweave");
        this.setRunAllSortsName("More Pattern-Defeating Disparity Bidirectional Exponential Branchless Binary/Linear Hell Sort with Improved Deweave");
        this.setRunSortName("More Pattern-Defeating Disparity Bidirectional Exponential Branchless Binary/Linear Shellsort (+ Improved Deweave)");
        this.setCategory("Hybrid Sorts");
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
    
    private boolean binary = false;
    
    private void gapReverse(int[] array, int start, int end, int gap) {
    	Writes.changeReversals(1);
    	while(start <= end-gap) {
    		Writes.swap(array, start, end, 1, true, false);
    		start+=gap;
    		end-=gap;
    	}
    }
    
    private int sign(int v) {
    	return (v>>31) | -(-v>>31);
    }
    
    private int m(int val, int base) {
    	return val-(val%base);
    }
    
    private int gbinsearch(int[] array, int start, int end, int key, int gap, int d, double slp, Comparator<Integer> bcmp) {
    	while(end-start > 0) {
    		int l = end - start,
    			c = d * bcmp.compare(key, array[start+m(l/2, gap)]); // branchless compare here
    		Highlights.markArray(1, start+l/2);
    		Delays.sleep(slp);
    		start += m((l / 2) + gap, gap) * sign(c+1);
    		end -= m((l + gap) / 2, gap) * -sign(c-1);
    	}
    	Highlights.clearAllMarks();
    	return start;
    }
    
    private int exp(int[] array, int start, int end, int v, boolean invert, int gap) {
    	int n=1;
    	while(n*gap<end-start && (invert ^ (Reads.compareValues(array[end-(n*gap)], v) > 0))) {
    		n*=2;
    	}
    	int m=end-(n*gap);
    	if(n*gap>end-start) {
    		n=(end-start)/gap;
    		m=start;
    	}
    	if(binary) {
    		return gbinsearch(array, m, end-((n/2)*gap), v, gap, invert?-1:1, 1.25, Reads::compareValues);
    	}
    	return end-((n/2)*gap);
    }
    
    private void shell(int[] array, int start, int end, int gap) {
    	if(end-start <= gap)
    		return;
    	if(gap < 1) gap = 1;
    	int len=end-start;
    	boolean[] gaps = new boolean[gap];
    	Writes.changeAllocAmount(gap);
    	for(int i=start+gap; i<end; i++) {
    		int t = array[i],
    			g = (i-start)%gap;
    		Highlights.markArray(1, i);
    		Delays.sleep(0.1);
    		if(gaps[g] ^ (Reads.compareValues(array[start+g], t) >= 0)) {
    			gapReverse(array, start+g, i-gap, gap);
    			Writes.changeAuxWrites(1);
    			gaps[g]=!gaps[g];
    			continue;
    		}
    		int j=exp(array, start+g, i, t, gaps[g], gap);
			int l=i-gap;
    		if(binary) {
    			while(l>=j) {
        			Writes.write(array, l+gap, array[l], 0.1, true, false);
        			l -= gap;
    			}
    			if(j<i)
    				Writes.write(array, j, t, 0.1, true, false);
    		} else {
	    		while(j >= start) {
	    			if(gaps[g] ^ Reads.compareValues(array[j], t) < 0) {
	    				break;
	    			}
	    			j-=gap;
	    		}
        		while(l>j) {
        			Writes.write(array, l+gap, array[l], 0.1, true, false);
        			l -= gap;
        		}
    			Writes.write(array, l+gap, t, 0.1, true, false);
    		}
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
    	Writes.changeAllocAmount(-gap);
    }
    
    private void circleRoutine(int[] array, int start, int end, int bound, int dir, int gap) {
    	for(int i=start, j=end; i<j; i+=gap, j-=gap) {
    		if(j < bound) {
    			if(Reads.compareValues(array[i], array[j]) == dir) {
    				Writes.swap(array, i, j, 1, true, false);
    			}
    		}
    	}
    	int mid = start+Math.floorDiv(end-start, 2*gap)*gap;
    	if(mid==start) return;
    	circleRoutine(array, start, mid, bound, dir, gap);
    	circleRoutine(array, mid+gap, end, bound, dir, gap);
    }
    
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
    	int size = currentLength, t = (int) Math.sqrt(currentLength);
    	binary = false;
    	while(size > 1) {
    		if(size == 2) {
    			int e = 1;
    			while(e < currentLength) {
    				e*=2;
    			}
    			circleRoutine(array, 0, e-1, currentLength, 1, 1);
    			return;
    		}
    		int disparity = this.disparity(array, 0, size);
    		if(disparity < Math.cbrt(size)) {
    			binary = true;
    		}
    		if(disparity <= Math.sqrt(size)) {
    			disparity = (int) Math.sqrt(size);
    		}
    		
    		boolean doCirc = true;
    		
    		if(disparity == size - 1 && isSorted(array, currentLength))
    			return;
    		else if(disparity == size - 1) {
    			disparity /= 2;
    			doCirc = false;
    		}
    		
    		if(doCirc && disparity == size / 2 && (size & 3) == 0) {
    			for(int i=0; i<=disparity; i+=size/4) {
    				int e = 1;
        			while(e < currentLength-i) {
        				e*=2;
        			}
        			circleRoutine(array, i, e-disparity+i, currentLength, 1, disparity);
    			}
    		} else
    			this.shell(array, 0, currentLength, disparity);
    		
    		size = disparity;
    	}
    }
}