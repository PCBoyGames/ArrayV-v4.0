package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 *
 */
public class NitroCircleSortRecursive extends Sort {

    public NitroCircleSortRecursive(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Nitro Circle (Recursive)");
        this.setRunAllSortsName("Recursive Nitro Circle Sort");
        this.setRunSortName("Nitro Circlesort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    void shellPass(int[] array, int a, int b, int gap) {
        for (int i = a + gap; i < b; i++) {
            int tmp = array[i];
            int j = i;
            while (j >= a + gap && Reads.compareValues(array[j - gap], tmp) > 0) {
                Highlights.markArray(2, j - gap);
                Writes.write(array, j, array[j - gap], 0.7, true, false);
                j -= gap;
            }
            if (j - gap >= a) Highlights.markArray(2, j - gap);
            else Highlights.clearMark(2);
            if (j != i) Writes.write(array, j, tmp, 0.7, true, false);
        }
    }

    public void shellSort(int[] array, int a, int b) {
        for (int gap = (int) Math.sqrt(b - a); gap >= 2; gap /= 2.3601) shellPass(array, a, b, gap);
        shellPass(array, a, b, 1);
    }

    protected int circlePass(int[] array, int a, int b, int bnd) {
        if (a >= b) return 0;
        int swapCnt = 0;
        int l = a, r = b, m = (b - a) / 2;
        while (l < r) {
            if (r < bnd && Reads.compareIndices(array, l, r, 0.5, true) > 0) {
                Writes.swap(array, l, r, 1, true, false);
                swapCnt++;
            }
            l++;
            r--;
        }
        swapCnt += circlePass(array, a, a + m, bnd);
        if (a + m + 1 < bnd) swapCnt += circlePass(array, a + m + 1, b, bnd);
        return swapCnt;
    }

    public void sort(int[] array, int a, int b) {
        int length = b - a;
        int threshold = 0, n = 1;
        for (; n < length; n*=2, threshold++);
        threshold /= 2;
        int iterations = 0;
        do {
            iterations++;
            if (iterations >= threshold) {
                shellSort(array, a, b);
                break;
            }
        } while (circlePass(array, a, a + n - 1, b) != 0);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);

    }

}
