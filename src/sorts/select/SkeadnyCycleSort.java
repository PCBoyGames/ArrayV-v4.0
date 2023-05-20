package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan
in collaboration with PCBoy

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 * @author PCBoy
 *
 */
public final class SkeadnyCycleSort extends Sort {

    public SkeadnyCycleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Skeadny Cycle");
        this.setRunAllSortsName("Skeadny Cycle Sort");
        this.setRunSortName("Skeadny Cyclesort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(2048);
        this.setBogoSort(false);
    }
    
    int countLesser(int[] array, int a, int b, int idx) {
        int r = a;
        
        for(int i = a+1; i < b; i++) {
            Highlights.markArray(1, r);
            Highlights.markArray(2, i);
            Delays.sleep(0.01);
            
            r += Reads.compareValues(array[i], array[idx]) < 0 ? 1 : 0;
        }
        Highlights.clearMark(2);
        return r;
    }
    
    // Easy patch to avoid self-reversals and the "reversals can be done in a single
    // swap" notes.
    protected void reverse(int[] array, int a, int b, double times) {
        if (b <= a) return;
        if (b - a >= 3)
            Writes.reversal(array, a, b, 0.25 / times, true, false);
        else
            Writes.swap(array, a, b, 0.25 / times, true, false);
    }
    
    public void sort(int[] array, int a, int b) {
        for (int i = a; i < b - 1; i++) {
            double times = 1;
            int r = countLesser(array, i, b, i);
            if (r != i) {
                do {
                    while(Reads.compareIndices(array, r, i, 0.125, true) == 0) r++;
                    reverse(array, i, r, times);
                    times += 0.1;
                    r = countLesser(array, i, b, i);
                } while (r != i);
            }
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);

    }

}
