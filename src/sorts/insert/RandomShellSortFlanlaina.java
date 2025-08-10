package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

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
public class RandomShellSortFlanlaina extends BogoSorting {
    public RandomShellSortFlanlaina(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Randomized Shell (Flanlaina)");
        this.setRunAllSortsName("Flanlaina's Randomized Shell Sort");
        this.setRunSortName("Flanlaina's Random Shellsort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public int gappedBinarySearch(int[] array, int start, int end, int gap, int value) {
        while (start < end) {
            int mid = start + BogoSorting.randInt(0, (end - start) / gap) * gap;
            Highlights.markArray(2, mid);
            Delays.sleep(1);
            if (Reads.compareValues(value, array[mid]) < 0) {
                end = mid;
            } else {
                start = mid + gap;
            }
        }
        Highlights.clearMark(2);
        return start;
    }

    public void shellPass(int[] array, int a, int b, int gap) {
        for (int i = a + gap; i < b; i++) {
            Highlights.markArray(3, i);
            int pos = gappedBinarySearch(array, a + (i - a) % gap, i, gap, array[i]);
            int j = i;
            int tmp = array[i];
            while (j > pos) {
                Highlights.markArray(2, j - gap);
                Writes.write(array, j, array[j - gap], 0.25, true, false);
                j -= gap;
            }
            Highlights.clearMark(2);
            if (j != i) Writes.write(array, j, tmp, 0.25, true, false);
            else {
                Highlights.markArray(1, j);
                Delays.sleep(0.25);
            }
            Highlights.clearMark(1);
        }
        Highlights.clearAllMarks();
    }

    public void shellSort(int[] array, int a, int b) {
        int gap = b - a;
        while(gap != 1) {
            gap = BogoSorting.randInt((int) Math.sqrt(gap), gap > 1 ? gap : gap - 1);
            shellPass(array, a, b, gap);
        }
        // for (int gap = (b - a) / 2; gap >= 2; gap /= 2) shellPass(array, a, b, gap);
        // shellPass(array, a, b, 1);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        shellSort(array, 0, sortLength);
    }
}
