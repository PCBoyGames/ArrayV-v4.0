package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import utils.IndexedRotations;

/**
 * @author Yuri-chan2007
 *
 */
public final class SmartLazyStableSort extends Sort {

    public SmartLazyStableSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Smart Lazy Stable");
        this.setRunAllSortsName("Smart Lazy Stable Sort");
        this.setRunSortName("Smart Lazy Stable Sort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int leftBinSearch(int[] array, int a, int b, int val) {
        while (a < b) {
            int m = a + (b - a) / 2;

            if (Reads.compareValues(val, array[m]) <= 0)
                b = m;
            else
                a = m + 1;
        }

        return a;
    }

    private int rightBinSearch(int[] array, int a, int b, int val) {
        while (a < b) {
            int m = a + (b - a) / 2;

            if (Reads.compareValues(val, array[m]) < 0)
                b = m;
            else
                a = m + 1;
        }

        return a;
    }

    private int leftBoundSearch(int[] array, int a, int b, int val) {
        int i = 1;
        while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) >= 0)
            i *= 2;

        return this.rightBinSearch(array, a + i / 2, Math.min(b, a - 1 + i), val);
    }

    private int rightBoundSearch(int[] array, int a, int b, int val) {
        int i = 1;
        while (b - i >= a && Reads.compareValues(val, array[b - i]) <= 0)
            i *= 2;

        return this.leftBinSearch(array, Math.max(a, b - i + 1), b - i / 2, val);
    }

    private void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);
        if (a > b) {
            int temp = array[a];
            do
                Writes.write(array, a, array[--a], 0.5, true, false);
            while (a > b);
            Writes.write(array, b, temp, 0.5, true, false);
        }
    }

    private void buildRuns(int[] array, int a, int b, int mRun) {
        int i = a + 1, j = a;

        while (i < b) {
            if (Reads.compareIndices(array, i - 1, i++, 1, true) == 1) {
                while (i < b && Reads.compareIndices(array, i - 1, i, 1, true) == 1)
                    i++;
                Writes.reversal(array, j, i - 1, 1, true, false);
            } else
                while (i < b && Reads.compareIndices(array, i - 1, i, 1, true) <= 0)
                    i++;

            if (i < b)
                j = i - (i - j - 1) % mRun - 1;

            while (i - j < mRun && i < b) {
                this.insertTo(array, i, this.rightBinSearch(array, j, i, array[i]));
                i++;
            }
            j = i++;
        }
    }

    private void rotate(int[] array, int a, int m, int b) {
        IndexedRotations.holyGriesMills(array, a, m, b, 1.0, true, false);
    }

    private void inPlaceMergeFW(int[] array, int a, int m, int b) {
        int i = a, j = m, k;

        while (i < j && j < b) {
            if (Reads.compareValues(array[i], array[j]) > 0) {
                k = this.leftBinSearch(array, j + 1, b, array[i]);
                this.rotate(array, i, j, k);

                i += k - j;
                j = k;
            } else
                i++;
        }
    }

    private void inPlaceMergeBW(int[] array, int a, int m, int b) {
        int i = m - 1, j = b - 1, k;

        while (j > i && i >= a) {
            if (Reads.compareValues(array[i], array[j]) > 0) {
                k = this.rightBinSearch(array, a, i, array[j]);
                this.rotate(array, k, i + 1, j + 1);

                j -= (i + 1) - k;
                i = k - 1;
            } else
                j--;
        }
    }

    public void smartInPlaceMerge(int[] array, int a, int m, int b) {
        if (Reads.compareIndices(array, m - 1, m, 0.0, true) <= 0)
            return;
        a = this.leftBoundSearch(array, a, m, array[m]);
        b = this.rightBoundSearch(array, m, b, array[m - 1]);
        if (Reads.compareIndices(array, a, b - 1, 0.0, true) > 0)
            rotate(array, a, m, b);
        else if (b - m < m - a)
            inPlaceMergeBW(array, a, m, b);
        else
            inPlaceMergeFW(array, a, m, b);
    }

    public void lazyStableSort(int[] array, int start, int end) {
        int mRun = end - start;
        for (; mRun >= 32; mRun = (mRun + 1) / 2)
            ;
        buildRuns(array, start, end, mRun);
        for (int i, j = mRun; j < (end - start); j *= 2) {
            for (i = start; i + 2 * j <= end; i += 2 * j)
                smartInPlaceMerge(array, i, i + j, i + 2 * j);
            if (i + j < end)
                smartInPlaceMerge(array, i, i + j, end);
        }

    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        lazyStableSort(array, 0, sortLength);

    }

}