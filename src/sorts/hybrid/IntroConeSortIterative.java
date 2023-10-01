package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan
in collaboration with Meme Man and aphitorite

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 * @author Meme Man
 * @author aphitorite
 *
 */
public class IntroConeSortIterative extends Sort {

    public IntroConeSortIterative(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Intro Cone (Iterative)");
        this.setRunAllSortsName("Iterative Introspective Cone Sort");
        this.setRunSortName("Iterative Introspective Conesort");
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

    boolean compSwap(int[] array, int a, int b) {
        if (Reads.compareIndices(array, a, b, 0.5, true) > 0) {
            Writes.swap(array, a, b, 0.5, true, false);
            return true;
        }
        return false;
    }

    public void sort(int[] array, int a, int b) {
        int currentLength = b - a;
        int threshold = 0, n = 1;
        for (; n < currentLength; n *= 2, threshold++) ;
        threshold /= 2;
        int iterations = 0;
        for (boolean s = true; s;) {
            iterations++;
            if (iterations >= threshold) {
                shellSort(array, a, b);
                break;
            }
            s = false;
            for (int k = 0; k < n / 2; k++)
                for (int j = n; j > 1 && k < j - 1 - k; j /= 2)
                    for (int i = a; i + j - 1 - k < b; i += j)
                        s |= this.compSwap(array, i + k, i + j - 1 - k);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);

    }

}
