package sorts.hybrid;

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
 * @author gooflang
 *
 */
public class NitroConeSortRecursive extends Sort {

    public NitroConeSortRecursive(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Nitro Cone (Recursive)");
        this.setRunAllSortsName("Recursive Nitro Cone Sort");
        this.setRunSortName("Nitro Conesort");
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

    public boolean conePass(int[] array, int a, int b, int c, int d, int bnd) {
        Writes.recordDepth(d++);
        if (a >= b || a+c >= b-c) return false;
        boolean swaps = false;
        if (b-c < bnd && Reads.compareIndices(array, a+c, b-c, 0.5, true) > 0) {
            Writes.swap(array, a+c, b-c, 0.5, true, false);
            swaps = true;
        }
        int m = (a+b) >> 1;
        Writes.recursion();
        swaps |= conePass(array, a, m, c, d, bnd);
        Writes.recursion();
        swaps |= conePass(array, m+1, b, c, d, bnd);
        return swaps;
    }

    public boolean cone(int[] array, int a, int b, int bnd) {
        if (a >= b) return false;
        boolean swaps = false;
        for (int i = 0; i <= (b-a) >> 1; i++) swaps |= conePass(array, a, b, i, 0, bnd);
        return swaps;
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
        } while (cone(array, a, a + n - 1, b));
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);

    }

}
