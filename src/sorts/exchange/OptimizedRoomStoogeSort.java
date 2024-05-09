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
public class OptimizedRoomStoogeSort extends Sort {

    public OptimizedRoomStoogeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Optimized Room Stooge");
        this.setRunAllSortsName("Optimized Room Stooge Sort");
        this.setRunSortName("Optimized Room Stoogesort");
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
    
    public void stoogeBubble(int[] arr, int i, int j) {
        if (Reads.compareIndices(arr, i, j, 0.0025, true) > 0) {
            Writes.swap(arr, i, j, 0.005, true, false);
        }    
        if (j - i + 1 >= 3) {
            int t = (j - i + 1) / 3;
            
            Highlights.markArray(3, j - t);
            Highlights.markArray(4, i + t);
    
            this.stoogeBubble(arr, i, j-t);
            this.stoogeBubble(arr, i+t, j);
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
