package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan
in collaboration with aphitorite

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 * @author aphitorite
 *
 */
public class AdaptiveForcedStableQuickSort extends Sort {

    public AdaptiveForcedStableQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Adaptive Forced Stable Quick");
        this.setRunAllSortsName("Adaptive Forced Stable Quick Sort");
        this.setRunSortName("Adaptive Forced Stable Quicksort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    int ofs;

    protected void stableSegmentReversal(int[] array, int start, int end) {
        if (end - start < 3) Writes.swap(array, start, end, 0.75, true, false);
        else Writes.reversal(array, start, end, 0.75, true, false);
        int i = start;
        int left;
        int right;
        while (i < end) {
            left = i;
            while (i < end && Reads.compareIndices(array, i, i + 1, 0.5, true) == 0) i++;
            right = i;
            if (left != right) {
                if (right - left < 3) Writes.swap(array, left, right, 0.75, true, false);
                else Writes.reversal(array, left, right, 0.75, true, false);
            }
            i++;
        }
    }

    boolean stableComp(int[] array, int[] key, int a, int b) {
        int comp = Reads.compareIndices(array, ofs + a, ofs + b, 0.5, true);
        return comp > 0 || (comp == 0 && Reads.compareOriginalIndices(key, a, b, 0.5, false) > 0);
    }

    void stableSwap(int[] array, int[] key, int a, int b) {
        if (a == b) return;
        Writes.swap(array, ofs + a, ofs + b, 0, true, false);
        Writes.swap(key, a, b, 1, false, true);
    }

    void smallSort(int[] array, int[] key, int a, int b) {
        for (int i = a + 1; i < b; i++) {
            int j = i;
            while (j > 0 && stableComp(array, key, j - 1, j)) {
                stableSwap(array, key, j - 1, j);
                j--;
            }
        }
    }

    int medianOfThree(int[] array, int[] key, int i0, int i1, int i2) {
        int tmp;
        if (stableComp(array, key, i0, i1)) {
            tmp = i1;
            i1 = i0;
        } else tmp = i0;
        if (stableComp(array, key, i1, i2)) {
            if (stableComp(array, key, tmp, i2)) return tmp;
            return i2;
        }
        return i1;
    }

    void medianOfMedians(int[] array, int[] key, int a, int b) {
        while (b-a > 2) {
            int m = a, i = a;
            for (; i+2 < b; i += 3) stableSwap(array, key, m++, medianOfThree(array, key, i, i+1, i+2));
            while (i < b) stableSwap(array, key, m++, i++);
            b = m;
        }
    }

    int partition(int[] array, int[] key, int a, int b) {
        int i = a, j = b;
        Highlights.markArray(3, a);
        while (true) {
            do i++;
            while (i < j && !this.stableComp(array, key, i, a));

            do j--;
            while (j >= i && this.stableComp(array, key, j, a));

            if (i >= j) {
                Highlights.clearMark(3);
                stableSwap(array, key, a, j);
                return j;
            }
            this.stableSwap(array, key, i, j);
        }
    }

    void innerSort(int[] array, int[] key, int a, int b, boolean bad) {
        while (b - a > 16) {
            if (bad) medianOfMedians(array, key, a, b);
            else stableSwap(array, key, a, medianOfThree(array, key, a, a + (b - a) / 2, b - 1));
            int p = this.partition(array, key, a, b);
            int lLen = p - a, rLen = b - (p + 1);
            bad = lLen < (b - a) / 16 || rLen < (b - a) / 16;
            if (lLen > rLen) {
                this.innerSort(array, key, p + 1, b, bad);
                b = p;
            }
            else {
                this.innerSort(array, key, a, p, bad);
                a = p + 1;
            }
        }
        smallSort(array, key, a, b);
    }

    public void quickSort(int[] array, int a, int b) {
        int z = 0, e = 0;
        for (int i = a; i < b - 1; i++) {
            int cmp = Reads.compareIndices(array, i, i + 1, 0.5, true);
            z += cmp > 0 ? 1 : 0;
            e += cmp == 0 ? 1 : 0;
        }
        if (z == 0) return;
        if (z + e == b - a - 1) {
            if (e > 0) stableSegmentReversal(array, a, b - 1);
            else if (b - a < 4) Writes.swap(array, a, b - 1, 0.75, true, false);
            else Writes.reversal(array, a, b - 1, 0.75, true, false);
            return;
        }
        int len = b - a;
        int[] keys = Writes.createExternalArray(len);
        for (int i = 0; i < len; i++) {
            Highlights.markArray(1, a + i);
            Writes.write(keys, i, i, 0.5, false, true);
        }
        ofs = a;
        innerSort(array, keys, 0, len, false);
        Writes.deleteExternalArray(keys);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickSort(array, 0, sortLength);

    }

}
