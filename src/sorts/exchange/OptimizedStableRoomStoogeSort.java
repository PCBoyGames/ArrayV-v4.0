package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Harumi
in collaboration with thatsOven and PCBoy

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Harumi
 * @author thatsOven
 * @author PCBoy
 *
 */
public class OptimizedStableRoomStoogeSort extends Sort {

    public OptimizedStableRoomStoogeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Optimized Stable Room Stooge");
        this.setRunAllSortsName("Optimized Stable Room Stooge Sort");
        this.setRunSortName("Optimized Stable Room Stoogesort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    public int maxSorted(int[] array, int start, int end, double delay, boolean mark) {
        int a = end - 1;
        int b = end - 1;
        boolean segment = true;
        while (segment) {
            if (b - 1 < start) return start;
            if (Reads.compareIndices(array, b - 1, b, delay, mark) > 0) segment = false;
            else b--;
        }
        int sel = b - 1;
        for (int s = b - 2; s >= start; s--) if (Reads.compareIndices(array, sel, s, delay, mark) < 0) sel = s;
        while (Reads.compareIndices(array, sel, a, delay, mark) <= 0) {
            a--;
            if (a < start) break;
        }
        return a + 1;
    }
    
    public void stoogeBubble(int[] array, int start, int end) {
        if (end - start + 1 == 2) {
            if (Reads.compareIndices(array, start, end, 0.0025, true) > 0) {
                Writes.swap(array, start, end, 0.005, true, false);
            }
        } else if (end - start + 1 > 2) {
            int third = (end - start + 1) / 3;
            stoogeBubble(array, start, end - third);
            stoogeBubble(array, start + third, end);
        }
    }
    
    public void sort(int[] array, int a, int b) {
        b = maxSorted(array, a, b, 0.5, true);
        while (b - a > 1) {
            stoogeBubble(array, a, b - 1);
            Highlights.clearMark(3);
            Highlights.clearMark(4);
            b = maxSorted(array, a, b, 0.5, true);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);

    }

}
