package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

+---------------------------+
| SORTING ALGORITHM SCARLET |
+---------------------------+
|    A sorting algorithm    |
|    studio by Flanlaina    |
|    (a.k.a Ayako-chan)     |
+---------------------------+

 */

/**
 * @author Flanlaina
 * @author PCBoy
 *
 */
public class ReverseShnexCycleSort extends Sort {
    public ReverseShnexCycleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Reverse Shnex Cycle");
        this.setRunAllSortsName("Reverse Shnex Cycle Sort");
        this.setRunSortName("Reverse Shnex Cyclesort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(512);
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
    
    public void cycleSort(int[] array, int a, int b) {
        for (int i = a; i < b - 1; i++) {
            int r = countLesser(array, i, b, i);
            if (r == i) continue;
            double times = 1;
            do {
                while(Reads.compareIndices(array, r, i, 0.01, true) == 0) r++;
                Writes.multiSwap(array, r, i, 0.25 / times, true, false);
                times += 0.1;
                r = countLesser(array, i, b, i);
            } while (r != i);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        cycleSort(array, 0, sortLength);
    }
}
