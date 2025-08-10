package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Haruki
in collaboration with PCBoy

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Haruki (Ayako-chan)
 * @author PCBoy
 *
 */
public class ShnexCycleSort extends Sort {
    public ShnexCycleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Shnex Cycle");
        this.setRunAllSortsName("Shnex Cycle Sort");
        this.setRunSortName("Shnex Cyclesort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(32);
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
                Writes.multiSwap(array, i, r, 1 / times, true, false);
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
