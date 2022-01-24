package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.GrailSorting;
import utils.Rotations;

final public class LaziceSort extends GrailSorting {
    public LaziceSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Lazice Stable");
        this.setRunAllSortsName("Lazice Stable Sort");
        this.setRunSortName("Lazice Sort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    private int runs(int len) {
    	int j = 1, l = 0;
    	while(j < len) {
    		j*=10;
    		l++;
    	}
    	return l*l;
    }
    
    // n^0.5 (log n iterations)
    private int sqrt(int len) {
    	int l = 0, h = len;
    	while(l < h) {
    		int m = l+(h-l)/2;
    		if(m*m > len)
    			h = m;
    		else
    			l = m + 1;
    	}
    	return l;
    }

    // n^0.66 (log^2 n? iterations)
    private int fcrt(int len) {
    	int l = 0, h = len;
    	while(l < h) {
    		int m = l+(h-l)/2;
    		if(m*sqrt(m) > len)
    			h = m;
    		else
    			l = m + 1;
    	}
    	return l;
    }
    
    @Override
    protected void grailRotate(int[] array, int pos, int len1, int len2) {
    	Rotations.cycleReverse(array, pos, len1, len2, 0.5, true, false);
    }
    
    // taken from PDIPop
    private void stableSegmentReversal(int[] array, int start, int end) {
        if (end - start < 3) Writes.swap(array, start, end, 0.075, true, false);
        else Writes.reversal(array, start, end, 0.075, true, false);
        int i = start;
        int left;
        int right;
        while (i < end) {
            left = i;
            while (Reads.compareIndices(array, i, i + 1, 0.25, true) == 0 && i < end) i++;
            right = i;
            if (left != right) {
                if (right - left < 3) Writes.swap(array, left, right, 0.75, true, false);
                else Writes.reversal(array, left, right, 0.75, true, false);
            }
            i++;
        }
    }
    
    private int findRun(int[] array, int start, int max) {
    	if(start >= max - 1)
    		return start+1;
    	int c = Reads.compareIndices(array, start, ++start, 0.1, true),
    		s = start-1,
    		d = c;
    	boolean stableRev = false;
    	if(c == 0) {
    		stableRev = true;
    		c = -1;
    	}
    	while(start < max - 1 && (d == c || d == 0)) {
    		d = Reads.compareIndices(array, start, start+1, 0.1, true);
    		if(d == 0)
    			stableRev = true;
    		start++;
    	}
    	if(c == 1) {
    		if(stableRev) {
    			stableSegmentReversal(array, s, start-1);
    		} else {
    			Writes.reversal(array, s, start-1, 1, true, false);
    		}
    	}
    	return start;
    }
    
    private void mergeRuns(int[] array, int start, int end) {
    	int z = runs(end-start),
    		x = fcrt(end-start),
    		s = start,
    		r = start,
    		d = 0;
    	while(r < end) {
    		int y = findRun(array, r, end);
    		if(y > r + x) {
    			s = r = y;
    			d = 0;
    			continue;
    		}
    		if(d > 0) {
    			grailMergeWithoutBuffer(array, s, r-s, y-r);
    		}
    		if(d + 1 == z) {
    			s = y;
    		}
    		r = y;
    		d = (d + 1) % z;
    	}
    }
    private int natMerge(int[] array, int start, int end) {
    	int a = start,
    		b = findRun(array, start, end);
    	if(b == end)
    		return b;
    	boolean did = false;
    	while(a < b && !did) {
    		int c = Reads.compareIndices(array, a, b, 0.1, true);
    		if(c == 0) c = -1;
    		if(c == 1) {
    			int t = b;
    			boolean d;
	    		do {
	    			b++;
	    		} while((d = Reads.compareIndices(array, b-1, b, 0.1, true) <= 0) &&
	    				Reads.compareIndices(array, a, b, 0.1, true) > 0);
	    		if(!d)
	    			did = true;
	    		grailRotate(array, a, t-a, b-t);
	    		a += b - t;
    		} else {
	    		do {
	    			a++;
	    		} while(a < b && Reads.compareIndices(array, a, b, 0.1, true) <= 0);
    		}
    	}
    	if(did)
    		return b;
    	while(b < end && Reads.compareValues(array[b-1], array[b]) <= 0) {
    		b++;
    	}
    	while(b < end-1 && Reads.compareValues(array[b], array[b+1]) <= 0) {
    		b--;
    	}
    	return b+1;
    }
    
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
    	mergeRuns(array, 0, currentLength);
		int iters;
    	do {
    		iters = 0;
    		for(int i=0; i<currentLength; i=natMerge(array, i, currentLength)) {
    			iters++;
    		}
    	} while(iters > 1);
    }
}