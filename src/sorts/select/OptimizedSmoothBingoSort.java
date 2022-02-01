package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class OptimizedSmoothBingoSort extends Sort {
    public OptimizedSmoothBingoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Optimized Smooth Bingo");
        this.setRunAllSortsName("Optimized Smooth Bingo Sort");
        this.setRunSortName("Optimized Smooth Bingo Sort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int seek = 0, equal = 0;
        for(int i=length-1; i>seek;) {
            for(int j=seek; j>0 && Reads.compareValues(array[j-1], array[j]) == 0; j--, equal++);
            for(int j=seek+1; j<=i; j++) {
                int c = Reads.compareIndices(array, j, seek, 0.1, true);
                if(c >= 0) {
                    if(j != ++seek)
                        Writes.swap(array, j, seek, 0.5, true, false);
                    if(c == 0)
                        equal++;
                    else
                        equal = 0;
                }
            }
            if(seek >= i)
                return;
            do {
                Writes.swap(array, seek--, i--, 1, true, false);
            } while(equal-- > 0);
            if(equal < 0) equal = 0;
            if(seek < 0) seek = 0;
        }
    }
}