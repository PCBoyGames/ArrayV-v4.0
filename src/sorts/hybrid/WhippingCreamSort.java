package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import utils.IndexedRotations;


final public class WhippingCreamSort extends Sort {

    public WhippingCreamSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Whipping Cream");
        this.setRunAllSortsName("Whipping Cream Sort");
        this.setRunSortName("Whipping Cream Sort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    public void M8FW(int[] array, int start, int mid, int end) {
    	if(start>=mid || mid>=end)
    		return;
    	int l = start, r = mid;
    	while(r < end && l < mid) {
    		while(l < mid && Reads.compareValues(array[l], array[r]) <= 0) {
    			l++;
    		}
    		if(l >= mid)
    			return;
    		int z = l;
    		while(l < mid && r < end && Reads.compareValues(array[l], array[r]) > 0) {
    			l++; r++;
    		}
    		IndexedRotations.juggling(array, z, mid, r, 1, true, false);
    		M8(array, z, l, l+(r-mid));
    		mid=r;
    	}
    }
    
    public void M8BW(int[] array, int start, int mid, int end) {
    	if(start>=mid || mid>=end)
    		return;
    	int l = mid-1, r = end-1;
    	while(r >= mid && l >= start) {
    		while(r >= mid && Reads.compareValues(array[l], array[r]) <= 0) {
    			r--;
    		}
    		if(r < mid)
    			return;
    		int z = r;
    		while(l >= start && r >= mid && Reads.compareValues(array[l], array[r]) >= 0) {
    			l--; r--;
    		}
    		IndexedRotations.juggling(array, l+1, mid, z+1, 1, true, false);
    		M8(array, r-(mid-l)+1, r+1, z+1);
    		mid=l+1;
    	}
    }
    
    public void M8(int[] array, int start, int mid, int end) {
    	if(mid-start < end-mid) {
    		M8FW(array, start, mid, end);
    	} else {
    		M8BW(array, start, mid, end);
    	}
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
    	for(int i=1; i<currentLength; i*=2) {
    		for(int j=0; j<currentLength; j+=2*i) {
    			if(j+i>currentLength)
    				break;
    			if(j+2*i<currentLength)
    				M8(array,j,j+i,j+2*i);
    			else
    				M8(array,j,j+i,currentLength);
    		}
    	}
    }
}
