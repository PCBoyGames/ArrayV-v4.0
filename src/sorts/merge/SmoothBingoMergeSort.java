package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan
in collaboration with Distray

-----------------------------
- Sorting Algorithm Scarlet -
-----------------------------

 */

/**
 * @author Ayako-chan
 * @author Distray
 *
 */
public final class SmoothBingoMergeSort extends Sort {

    public SmoothBingoMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        setSortListName("Smooth Bingo Merge");
        setRunAllSortsName("Smooth Bingo Merge Sort");
        setRunSortName("Smooth Bingo Mergesort");
        setCategory("Merge Sorts");
        setComparisonBased(true);
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(false);
        setUnreasonableLimit(0);
        setBogoSort(false);
    }

    public void bingoSort(int[] array, int a, int b) {
        int length = b - a;
        int seek = 0, equal = 0;
        for(int i=length-1; i>seek;) {
            for(int j=seek; j>0 && Reads.compareValues(array[a+j-1], array[a+j]) == 0; j--, equal++);
            for(int j=seek+1; j<=i; j++) {
                int c = Reads.compareIndices(array, a+j, a+seek, 0.125, true);
                if(c >= 0) {
                    if(j != ++seek)
                        Writes.swap(array, a+j, a+seek, 0.125, true, false);
                    if(c == 0)
                        equal++;
                    else
                        equal = 0;
                }
            }
            if(seek >= i)
                return;
            do
                Writes.swap(array, a + seek--, a + i--, 1, true, false);
            while(equal-- > 0);
            if(equal < 0) equal = 0;
            if(seek < 0) seek = 0;
        }
    }
    
    public void mergeSort(int[] array, int a, int b) {
        if(b - a < 2)
            return;
        int m = a + (b - a) / 2;
        mergeSort(array, a, m);
        mergeSort(array, m, b);
        bingoSort(array, a, b);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        mergeSort(array, 0, sortLength);

    }

}
