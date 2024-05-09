package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan
in collaboration with yuji

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 * @author yuji
 *
 */
public final class IntroCircloidSortRecursive extends Sort {

    public IntroCircloidSortRecursive(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Intro Circloid (Recursive)");
        this.setRunAllSortsName("Recursive Introspective Circloid Sort");
        this.setRunSortName("Introspective Circloid Sort");
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

    protected boolean circle(int[] array, int left, int right) {
        int a = left;
        int b = right;
        boolean swapped = false;
        while (a < b) {
            if (Reads.compareIndices(array, a, b, 0.25, true) > 0) {
                Writes.swap(array, a, b, 1, true, false);
                swapped = true;
            }
            a++;
            b--;
            if (a == b) b++;
        }
        return swapped;
    }

    public boolean circlePass(int[] array, int left, int right) {
        if (left >= right) return false;
        int mid = left + (right - left) / 2; //avoid integer overflow
        boolean l = this.circlePass(array, left, mid);
        boolean r = this.circlePass(array, mid+1, right);
        return this.circle(array, left, right) || l || r;
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
        } while (circlePass(array, a, b - 1));
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        sort(array, 0, length);
    }

}
