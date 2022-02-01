//
// Decompiled by Procyon v0.5.36
//

package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public final class AdaptiveSquareInsertionSort extends Sort
{
    public AdaptiveSquareInsertionSort(final ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Adaptive Square Insert");
        this.setRunAllSortsName("Adaptive Square Insertion Sort");
        this.setRunSortName("Adaptive Square Insertsort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public boolean inbetween(final int a, final int b, final int c) {
        return b <= c && b >= a;
    }

    public void binaryInsertSort(final int[] array, final int start, final int end, final double compSleep, final double writeSleep, final boolean direction) {
        int x = start;
        if (this.Reads.compareIndices(array, x, x + 1, compSleep, true) >= 0) {
            while (this.Reads.compareValues(array[x + 1], array[x]) < 0 && x < end) {
                ++x;
            }
            this.Writes.reversal(array, start, x, writeSleep, true, false);
        }
        for (int i = ++x; i < end; ++i) {
            boolean dir = direction;
            final int num = array[i];
            int lo = start;
            int hi = i;
            if (this.Reads.compareIndices(array, i - 1, i, compSleep, true) > 0) {
                while (lo < hi) {
                    int mid = (hi - lo) / 2 + lo;
                    if (dir) {
                        mid = (int)Math.sqrt(hi * lo);
                    }
                    else {
                        mid = (int)(hi - (Math.sqrt(hi * lo) - lo));
                    }
                    this.Highlights.markArray(1, lo);
                    this.Highlights.markArray(2, mid);
                    this.Highlights.markArray(3, hi);
                    this.Delays.sleep(compSleep);
                    if (this.Reads.compareValues(num, array[mid]) < 0) {
                        hi = mid;
                        dir = true;
                    }
                    else {
                        lo = mid + 1;
                        dir = false;
                    }
                }
                this.Highlights.clearMark(3);
                for (int j = i - 1; j >= lo; --j) {
                    this.Writes.write(array, j + 1, array[j], writeSleep, true, false);
                }
                this.Writes.write(array, lo, num, writeSleep, true, false);
            }
            this.Highlights.clearAllMarks();
        }
    }

    public void customBinaryInsert(final int[] array, final int start, final int end, final double sleep) {
        this.binaryInsertSort(array, start, end, sleep, sleep, false);
    }

    @Override
    public void runSort(final int[] array, final int currentLength, final int bucketCount) {
        this.binaryInsertSort(array, 0, currentLength, 1.0, 0.05, true);
    }
}
