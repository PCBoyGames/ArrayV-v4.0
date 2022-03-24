package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class TinyPullSort extends Sort {
    public TinyPullSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Tinypull");
        this.setRunAllSortsName("Tinypull Sort");
        this.setRunSortName("Tinypullsort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    @Override
    public void runSort(int[]A,int l,int f){
        for(int j=l; j>0; j--)
        	for(int i=1; i<j; i++) {
	        	Writes.swap(A, i-1, i, 1, true, false);
	        	if(Reads.compareValues(A[i-1], A[i]) > 0){
	        		i=0;
	        	}
	        }
    }
}