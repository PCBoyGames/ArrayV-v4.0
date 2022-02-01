package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;


final public class OptimizedZubbleSort extends Sort {
    public OptimizedZubbleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Optimized Zubble");
        this.setRunAllSortsName("Optimized Zubble Sort");
        this.setRunSortName("Optimized Zubblesort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        for(int i = length - 1, cons = 1, first = 1; i >= first; i-=cons) {
            boolean firstset = false;
            for(int j = Math.max(first - 1, 0); j < i; j++) {
                int k = j;
                boolean swap = false;
                while(j < i && Reads.compareValues(array[k], array[j + 1]) == 1){
                    j++;
                    swap = true;
                }
                if(swap) {Writes.swap(array, k, j, 1, true, false); cons=1; if(!firstset) first=k; firstset = true;}
                else {cons++;}
            }
        }
    }
}