package sorts.merge;

import main.ArrayVisualizer;
import sorts.insert.BinaryInsertionSort;
import sorts.templates.GrailSorting;


final public class AdaptiveRemoteMergeSort extends GrailSorting {
    public AdaptiveRemoteMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Adaptive Remote Merge");
        this.setRunAllSortsName("Adaptive Remote Merge Sort");
        this.setRunSortName("Adaptive Remote Merge Sort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    private void firstPhase(int[] array, int start, int end) {
    	int pos = end - 1, keyTemp = array[end];
        
        while (pos >= start && Reads.compareValues(array[pos], keyTemp) == 1) {
        	Writes.write(array, pos + 1, array[pos], 1, true, false);
        	pos--;
        } 
        Writes.write(array, pos + 1, keyTemp, 1, true, false);
    }
	
	private void shift(int[] array, int from, int to, double sleep) {
    	if(from == to)
    		return;
		int k = array[from];
    	if(from < to) {
        	Writes.arraycopy(array, from+1, array, from, to-from, sleep/2d, true, false);
    	} else {
        	Writes.reversearraycopy(array, to, array, to+1, from-to, sleep/2d, true, false);
    	}
    	Writes.write(array, to, k, sleep, true, false);
    }
	
    private void lastPhase(int[] array, int start, int end) {
    	int pos = start + 1, keyTemp = array[pos-1];
        
        while (pos < end && Reads.compareValues(array[pos], keyTemp) == -1) {
        	Writes.write(array, pos - 1, array[pos], 1, true, false);
        	pos++;
        } 
        Writes.write(array, pos - 1, keyTemp, 1, true, false);
    }
    
    public void unguardedBinaryInsert(int[] array, int start, int end, double compSleep, double writeSleep) {
        for (int i = start; i < end; i++) {
            int num = array[i];
            int lo = start, hi = i;
            
            if(Reads.compareValues(array[i-1], num) <= 0) {
            	continue;
            } else if(Reads.compareValues(array[start], num) >= 0) {
            	this.shift(array, i, start, writeSleep);
            	continue;
            }
            
            while (lo < hi) {
                int mid = lo + ((hi - lo) / 2); // avoid int overflow!
                Highlights.markArray(1, lo);
                Highlights.markArray(2, mid);
                Highlights.markArray(3, hi);
                
                Delays.sleep(compSleep);
                
                if (Reads.compareValues(num, array[mid]) < 0) { // do NOT move equal elements to right of inserted element; this maintains stability!
                    hi = mid;
                }
                else {
                    lo = mid + 1;
                }
            }

            Highlights.clearMark(3);
            
            // item has to go into position lo

            int j = i - 1;
            
            while (j >= lo)
            {
                Writes.write(array, j + 1, array[j], writeSleep, true, false);
                j--;
            }
            Writes.write(array, lo, num, writeSleep, true, false);
            
            Highlights.clearAllMarks();
        }
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        for(int l=2; l<=length; l++) {
        	int phase = 0;
        	if(l == 2) {
        		for(int i=0; i<length-1; i+=2) {
        			if(Reads.compareValues(array[i], array[i+1]) == 1) {
        				Writes.swap(array, i, i+1, 1, true, false);
        			}
        		}
        	} else {
        		for(int i=0; i<length; i+=l) {
        			int next = Math.min(i+l, length);
        			if(i + l <= length) {
        				if(phase == 0) {
            				this.firstPhase(array, i, next-1);
            			} else if(phase == l-2) {
            				this.lastPhase(array, i, next);
            			} else {
            				this.grailMergeWithoutBuffer(array, i, l - (phase + 1), phase + 1);
            			}
        			}// else { // uncomment this to make it look more like Remote Merge
        			//	    this.unguardedBinaryInsert(array, i, next, 0.125, 0.025);
        		   //   }
        			phase = (phase + 1) % (l - 1);
        		}
        	}
        }
        // halving the iteration length and merging the rest works in most cases, but it still fails
        // with a handful
        // this.grailMergeWithoutBuffer(array, 0, length/2, (length+1)/2);
    }
}