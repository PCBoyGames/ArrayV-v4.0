package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;


final public class VacuumSort extends Sort {
    public VacuumSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Vacuum");
        this.setRunAllSortsName("Vacuum Sort");
        this.setRunSortName("Vacuum Sort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    private boolean comp_pull(int[] array, int a, int b, double sleep, boolean rev) {
        if(a!=b&&Reads.compareIndices(array, a, b, sleep/2d, true) == 1) {
            Writes.multiSwap(array,rev?b:a,rev?a:b,sleep/10d,true,false);
            return true;
        }
        return false;
    }
    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        for(int i=0; i<sortLength; i++) {
            int m = i, l=0;
            do {
                l=0;
                m=i;
                for(int j=i; j<sortLength; j++) {
                    if(this.comp_pull(array, i, j, 1, false))
                        m=j;
                    else {
                       this.comp_pull(array, m, j, 1, true);
                       l++;
                    }
                }
            } while(l < sortLength-i);
        }
    }
}