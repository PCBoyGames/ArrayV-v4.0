package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import utils.IndexedRotations;

public final class LaziceSort extends Sort {

    public LaziceSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Lazice Stable");
        this.setRunAllSortsName("Lazice Stable Sort");
        this.setRunSortName("Lazice Sort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected int leftBinSearch(int[] array, int a, int b, int val) {
        while (a < b) {
            int m = a + (b - a) / 2;
            Highlights.markArray(2, m);
            Delays.sleep(0.25);
            if (Reads.compareValues(val, array[m]) <= 0)
                b = m;
            else
                a = m + 1;
        }
        return a;
    }

    protected int rightBinSearch(int[] array, int a, int b, int val) {
        while (a < b) {
            int m = a + (b - a) / 2;
            Highlights.markArray(2, m);
            Delays.sleep(0.25);
            if (Reads.compareValues(val, array[m]) < 0)
                b = m;
            else
                a = m + 1;
        }
        return a;
    }

    protected void rotate(int[] array, int a, int m, int b) {
        IndexedRotations.cycleReverse(array, a, m, b, 0.5, true, false);
    }

    protected void inPlaceMergeFW(int[] array, int a, int m, int b) {
        int i = a, j = m, k;
        while (i < j && j < b)
            if (Reads.compareValues(array[i], array[j]) > 0) {
                k = leftBinSearch(array, j + 1, b, array[i]);
                rotate(array, i, j, k);
                i += k - j;
                j = k;
            } else
                i++;
    }

    protected void inPlaceMergeBW(int[] array, int a, int m, int b) {
        int i = m - 1, j = b - 1, k;
        while (j > i && i >= a)
            if (Reads.compareValues(array[i], array[j]) > 0) {
                k = rightBinSearch(array, a, i, array[j]);
                rotate(array, k, i + 1, j + 1);
                j -= (i + 1) - k;
                i = k - 1;
            } else
                j--;
    }

    public void merge(int[] array, int a, int m, int b) {
        if (m - a > b - m)
            inPlaceMergeBW(array, a, m, b);
        else
            inPlaceMergeFW(array, a, m, b);
    }

    protected int findRun(int[] array, int a, int b) {
        int i = a + 1;
        boolean dir;
        if (i < b)
            dir = Reads.compareIndices(array, i - 1, i++, 0.5, true) <= 0;
        else
            dir = true;
        if (dir)
            while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) <= 0)
                i++;
        else {
            while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) > 0)
                i++;
            if (i - a < 4)
                Writes.swap(array, a, i - 1, 1.0, true, false);
            else
                Writes.reversal(array, a, i - 1, 1.0, true, false);
        }
        Highlights.clearMark(2);
        return i;
    }

    public void mergeSort(int[] array, int a, int b) {
        int i, j, k;
        while (true) {
            i = findRun(array, a, b);
            if (i >= b)
                return;
            j = findRun(array, i, b);
            merge(array, a, i, j);
            Highlights.clearMark(2);
            if (j >= b)
                return;
            k = j;
            while (true) {
                i = findRun(array, k, b);
                if (i >= b)
                    break;
                j = findRun(array, i, b);
                merge(array, k, i, j);
                if (j >= b)
                    break;
                k = j;
            }
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        mergeSort(array, 0, sortLength);

    }

}
