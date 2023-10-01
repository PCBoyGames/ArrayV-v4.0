package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan
in collaboration with Meme Man

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 * @author Meme Man
 *
 */
public class IntroConeSortRecursive extends Sort {

    public IntroConeSortRecursive(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Intro Cone (Recursive)");
        this.setRunAllSortsName("Recursive Introspective Cone Sort");
        this.setRunSortName("Introspective Conesort");
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

    public int conePass(int[] array, int a, int b, int c, int d, int swaps) {
        if (a >= b || a+c >= b-c) return swaps;
        Writes.recordDepth(d++);
        if (Reads.compareIndices(array, a+c, b-c, 0.5, true) > 0) {
            Writes.swap(array, a+c, b-c, 0.5, true, false);
            swaps++;
        }
        int m = (b - a) / 2;
        Writes.recursion();
        swaps = conePass(array, a, a+m, c, d, swaps);
        Writes.recursion();
        swaps = conePass(array, b-m, b, c, d, swaps);
        return swaps;
    }

    public int cone(int[] array, int a, int b, int swaps) {
        if (a >= b) return swaps;
        for (int i = 0; i < (b - a + 1) / 2; i++) swaps = conePass(array, a, b, i, 0, swaps);
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
        } while (cone(array, a, b-1, 0) != 0);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);

    }

}
