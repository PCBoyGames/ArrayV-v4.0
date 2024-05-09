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
public class NitroCircleSortIterative extends Sort {

    public NitroCircleSortIterative(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Nitro Circle (Iterative)");
        this.setRunAllSortsName("Iterative Nitro Circle Sort");
        this.setRunSortName("Iterative Nitro Circlesort");
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

    protected int circlePass(int[] array, int a, int n, int b) {
        int swapCnt = 0;
        for (int g = n / 2; g > 0; g /= 2) {
            for (int s = a; s + g < b; s += 2 * g) {
                int i = s, j = s + 2 * g - 1;
                while (i < j) {
                    if (j < b && Reads.compareIndices(array, i, j, 0.5, true) > 0) {
                        Writes.swap(array, i, j, 1, true, false);
                        swapCnt++;
                    }
                    i++;
                    j--;
                }
            }
        }
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
        } while (circlePass(array, a, n, b) != 0);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);

    }

}
