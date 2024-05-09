package sorts.bogo;

import sorts.templates.BogoSorting;

import main.ArrayVisualizer;

final public class ClotSort extends BogoSorting {
    public ClotSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Clot");
        this.setRunAllSortsName("Clot Sort");
        this.setRunSortName("Clotsort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(128);
        this.setBogoSort(true);
        this.setQuestion("Enter the luck for this sort:", 99);
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 1 || answer > 100) return 50;
        return answer;
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
    	while(!isArraySorted(array, length)) {
    		for(int i=1, j=0; i<length; i++) {
    			if(randInt(1, 101) > bucketCount) {
    				Writes.multiSwap(array, i, j, 0.1, true, false);
    				for(int k=j++; j>1&&k>=0; k--) {
    	    			if(randInt(0,bucketCount+2)==0) break;
    					if(Reads.compareIndices(array, k, k+1, 0.1, true) < 0) {
    						Writes.swap(array, k, k+1, 0.1, true, false);
    					}
    				}
    			} else {
    				for(int k=i; k>=j; k--) {
    	    			if(randInt(0,bucketCount+2)==0) break;
    					if(Reads.compareIndices(array, k, k+1, 0.1, true) > 0) {
    						Writes.swap(array, k, k+1, 0.1, true, false);
    					}
    				}
    			}
    		}
    	}
    }
}