package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class QuickShellSort extends Sort {

    public QuickShellSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Quick Shell");
        this.setRunAllSortsName("Quick Shell Sort");
        this.setRunSortName("Quick Shellsort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    int incs[] = {48, 21, 7, 3, 1};

    // Easy patch to avoid self-swaps.
    public void swap(int[] array, int a, int b, double pause, boolean mark, boolean aux) {
        if (a != b)
            Writes.swap(array, a, b, pause, mark, aux);
    }

    protected void insertSort(int[] array, int a, int b) {
        for (int i = a + 1; i < b; i ++) {
            int t = array[i];
            int j = i;
            while (j > a && Reads.compareValues(array[j - 1], t) > 0) {
                Writes.write(array, j, array[j - 1], 0.25, true, false);
                j--;
            }
            if (j < i) Writes.write(array, j, t, 0.25, true, false);
        }
    }

    protected void shellSort(int[] array, int lo, int hi) {
        Highlights.clearAllMarks();
        for (int k = 0; k < this.incs.length; k++) {
            for (int h = this.incs[k], i = h + lo; i < hi; i++) {
                int v = array[i];
                int j = i;
                while (j >= h + lo && Reads.compareValues(array[j - h], v) == 1) {
                    Highlights.markArray(1, j);
                    Writes.write(array, j, array[j - h], 0.75, true, false);
                    j -= h;
                }
                if (j != i) Writes.write(array, j, v, 0.75, true, false);
            }
        }
        Highlights.clearAllMarks();
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

    // lite version
    void medianOfMedians(int[] array, int a, int b, int s) {
        int end = b, start = a, i, j;
        boolean ad = true;

        while (end - start > 1) {
            j = start;
            Highlights.markArray(2, j);
            for (i = start; i + 2 * s <= end; i += s) {
                insertSort(array, i, i + s);
                swap(array, j++, i + s / 2, 1, false, false);
                Highlights.markArray(2, j);
            }
            if (i < end) {
                insertSort(array, i, end);
                swap(array, j++, i + (end - (ad ? 1 : 0) - i) / 2, 1, false, false);
                Highlights.markArray(2, j);
                if ((end - i) % 2 == 0) ad = !ad;
            }
            end = j;
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

    void sortHelper(int[] array, int a, int b, boolean bad) {
        while (b - a > 32) {
            if (bad) {
                medianOfMedians(array, a, b, 5);
                bad = false;
            } else medianOfThree(array, a, b);
            int p = partition(array, a, b);
            int lSize = p - a, rSize = b - (p + 1);
            bad = Math.min(lSize, rSize) < Math.max(lSize, rSize) / 16;
            if (rSize < lSize) {
                sortHelper(array, p + 1, b, bad);
                b = p;
            } else {
                sortHelper(array, a, p, bad);
                a = p + 1;
            }
        }
    }

    public void quickShellSort(int[] array, int a, int b) {
        sortHelper(array, a, b, false);
        shellSort(array, a, b);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickShellSort(array, 0, sortLength);

    }

}
