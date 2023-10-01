package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan
in collaboration with aphitorite and yuji

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 * @author aphitorite
 * @author yuji
 *
 */
public class AdaptiveMedianMergeSort extends Sort {

    public AdaptiveMedianMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Adaptive Median Merge");
        this.setRunAllSortsName("Adaptive Median Merge Sort");
        this.setRunSortName("Adaptive Median Mergesort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    // Easy patch to avoid self-swaps.
    public void swap(int[] array, int a, int b, double pause, boolean mark, boolean aux) {
        if (a != b)
            Writes.swap(array, a, b, pause, mark, aux);
    }

    void medianOfThree(int[] array, int a, int b) {
        int m = a + (b - 1 - a) / 2;
        if (Reads.compareIndices(array, a, m, 1, true) > 0)
            swap(array, a, m, 1, true, false);
        if (Reads.compareIndices(array, m, b - 1, 1, true) > 0) {
            swap(array, m, b - 1, 1, true, false);
            if (Reads.compareIndices(array, a, m, 1, true) > 0)
                return;
        }
        swap(array, a, m, 1, true, false);
    }

    int medianOfThree(int[] array, int i0, int i1, int i2) {
        int tmp;
        if (Reads.compareIndices(array, i0, i1, 1, true) > 0) {
            tmp = i1;
            i1 = i0;
        } else tmp = i0;
        if (Reads.compareIndices(array, i1, i2, 1, true) > 0) {
            if (Reads.compareIndices(array, tmp, i2, 1, true) > 0) return tmp;
            return i2;
        }
        return i1;
    }

    void medianOfMedians(int[] array, int a, int b) {
        while (b-a > 2) {
            int m = a, i = a;

            for (; i+2 < b; i += 3)
                swap(array, m++, this.medianOfThree(array, i, i+1, i+2), 1, true, false);
            while (i < b)
                swap(array, m++, i++, 1, true, false);

            b = m;
        }
    }

    public int partition(int[] array, int a, int b) {
        int i = a, j = b;
        Highlights.markArray(3, a);
        do {
            do {
                i++;
                Highlights.markArray(1, i);
                Delays.sleep(0.5);
            } while (i < j && Reads.compareValues(array[i], array[a]) < 0);
            do {
                j--;
                Highlights.markArray(2, j);
                Delays.sleep(0.5);
            } while (j >= i && Reads.compareValues(array[j], array[a]) > 0);
            if (i < j) Writes.swap(array, i, j, 1, false, false);
            else {
                Highlights.clearMark(3);
                swap(array, a, j, 1, true, false);
                return j;
            }
        } while (true);
    }

    protected int binSearch(int[] array, int a, int b, int val, boolean left) {
        while (a < b) {
            int m = a + (b - a) / 2;
            Highlights.markArray(2, m);
            Delays.sleep(0.25);
            int c = Reads.compareValues(val, array[m]);
            if (c < 0 || (left && c == 0))
                b = m;
            else
                a = m + 1;
        }
        return a;
    }

    protected int leftExpSearch(int[] array, int a, int b, int val, boolean left) {
        int i = 1;
        if (left)
            while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) > 0)
                i *= 2;
        else
            while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) >= 0)
                i *= 2;
        int a1 = a + i / 2, b1 = Math.min(b, a - 1 + i);
        return binSearch(array, a1, b1, val, left);
    }

    protected int rightExpSearch(int[] array, int a, int b, int val, boolean left) {
        int i = 1;
        if (left)
            while (b - i >= a && Reads.compareValues(val, array[b - i]) <= 0)
                i *= 2;
        else
            while (b - i >= a && Reads.compareValues(val, array[b - i]) < 0)
                i *= 2;
        int a1 = Math.max(a, b - i + 1), b1 = b - i / 2;
        return binSearch(array, a1, b1, val, left);
    }

    private void multiSwap(int[] array, int a, int b, int len) {
        if (a != b)
            for (int i = 0; i < len; i++)
                Writes.swap(array, a+i, b+i, 1, true, false);
    }

    protected void mergeFW(int[] array, int a, int m, int b, int p) {
        int pLen = m - a;
        multiSwap(array, a, p, pLen);
        int i = 0, j = m, k = a;
        while (i < pLen && j < b)
            if (Reads.compareValues(array[p + i], array[j]) <= 0)
                swap(array, k++, p + (i++), 1, true, false);
            else
                swap(array, k++, j++, 1, true, false);
        while (i < pLen)
            swap(array, k++, p + (i++), 1, true, false);
    }

    protected void mergeBW(int[] array, int a, int m, int b, int p) {
        int pLen = b - m;
        multiSwap(array, m, p, pLen);
        int i = pLen - 1, j = m - 1, k = b - 1;
        while (i >= 0 && j >= a)
            if (Reads.compareValues(array[p + i], array[j]) >= 0)
                swap(array, k--, p + (i--), 1, true, false);
            else
                swap(array, k--, j--, 1, true, false);
        while (i >= 0)
            swap(array, k--, p + (i--), 1, true, false);
    }

    public void smartMerge(int[] array, int a, int m, int b, int p) {
        if (Reads.compareIndices(array, m - 1, m, 0.5, true) <= 0)
            return;
        a = leftExpSearch(array, a, m, array[m], false);
        b = rightExpSearch(array, m, b, array[m - 1], true);
        if (b - m < m - a) mergeBW(array, a, m, b, p);
        else mergeFW(array, a, m, b, p);
    }

    public static int getMinLevel(int n) {
        while (n >= 32) n = (n+1)/2;
        return n;
    }

    protected void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);
        int temp = array[a];
        int d = (a > b) ? -1 : 1;
        for (int i = a; i != b; i += d)
            Writes.write(array, i, array[i + d], 0.5, true, false);
        if (a != b)
            Writes.write(array, b, temp, 0.5, true, false);
    }

    protected boolean buildRuns(int[] array, int a, int b, int mRun) {
        int i = a + 1, j = a;
        boolean noSort = true;
        while (i < b) {
            if (Reads.compareIndices(array, i - 1, i++, 1, true) > 0) {
                while (i < b && Reads.compareIndices(array, i - 1, i, 1, true) > 0) i++;
                if (i - j < 4) Writes.swap(array, j, i - 1, 1.0, true, false);
                else Writes.reversal(array, j, i - 1, 1.0, true, false);
            } else
                while (i < b && Reads.compareIndices(array, i - 1, i, 1, true) <= 0) i++;
            if (i < b) {
                noSort = false;
                j = i - (i - j - 1) % mRun - 1;
            }
            while (i - j < mRun && i < b) {
                insertTo(array, i, rightExpSearch(array, j, i, array[i], false));
                i++;
            }
            j = i++;
        }
        return noSort;
    }

    public void mergeSort(int[] array, int a, int b, int p) {
        int length = b-a;
        if (length < 2) return;

        int i, j = getMinLevel(length);

        if (buildRuns(array, a, b, j)) return;

        while (j < length) {
            for (i = a; i+2*j <= b; i+=2*j)
                this.smartMerge(array, i, i+j, i+2*j, p);
            if (i + j < b)
                this.smartMerge(array, i, i+j, b, p);
            j *= 2;
        }
    }

    public void medianMergeSort(int[] array, int a, int b) {
        int start = a, end = b;
        boolean badPartition = false;

        while (end - start > 32) {
            if (badPartition) this.medianOfMedians(array, start, end);
            else medianOfThree(array, start, end);

            int p = this.partition(array, start, end);
            int left  = p-start;
            int right = end-(p+1);
            badPartition = Math.min(left, right) < Math.max(left, right) / 16;

            if (left <= right) {
                this.mergeSort(array, start, p, p+1);
                start = p+1;
            }
            else {
                this.mergeSort(array, p+1, end, start);
                end = p;
            }
        }
        // insertion sort
        buildRuns(array, start, end, end - start);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        medianMergeSort(array, 0, sortLength);

    }

}
