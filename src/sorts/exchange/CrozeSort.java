package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/* _________________________
  /                         \
 | As Seen On PCBoy's Oh God |
 |       and Sort Grid       |
 |   (AKA: C' Restrained,    |
 |  rebranded due to false   |
 |   alarm from profanity    |
 |   filter going offline)   |
  \_________________________/ */
final public class CrozeSort extends BogoSorting {
    public CrozeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Croze");
        this.setRunAllSortsName("Croze Sort");
        this.setRunSortName("Croze Sort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        while(!isArraySorted(array, currentLength))
        	for(int i=0; i<currentLength; i++) {
	        	for(int j=0; j<i; j++) {
	        		if(Reads.compareIndices(array, j, i, 0.01, true) > 0) {
	        			for(int k=j+1; k<currentLength; k++) {
	        				if(Reads.compareIndices(array, j, k, 0.01, true) > 0)
	        					Writes.multiSwap(array, k, j, 0.1, true, false);
	        				else if(k > i)
	        					break;
	        			}
	        		} else {
	        			Writes.multiSwap(array, i, j+1, 0.1, true, false);
	        		}
	        	}
	        }
    }
}
