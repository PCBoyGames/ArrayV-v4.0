package sorts.exchange;

import main.ArrayVisualizer;
import sorts.insert.BlockInsertionSortAdaRot;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class OptimizedShobeSort extends Sort {
    public OptimizedShobeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Optimized Shobe");
        this.setRunAllSortsName("Optimized Shobe Sort");
        this.setRunSortName("Optimized Shobe Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(false);
    }

    protected int findRun(int[] array, int a, int b) {
        int i = a + 1;
        if (i == b) return i;
        int cmp = Reads.compareIndices(array, i - 1, i++, 0.5, true);
        while (cmp == 0 && i < b) cmp = Reads.compareIndices(array, i - 1, i++, 0.5, true);
        if (cmp == 1) {
            while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) >= 0) i++;
            if (i - a < 4) Writes.swap(array, a, i - 1, 1.0, true, false);
            else Writes.reversal(array, a, i - 1, 1.0, true, false);
        }
        else while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) <= 0) i++;
        Highlights.clearMark(2);
        return i;
    }

    protected boolean patternDefeat(int[] array, int a, int b) {
        int i = a + 1, j = a;
        boolean noSort = true;
        while (i < b) {
            i = findRun(array, j, b);
            if (i < b) noSort = false;
            j = i++;
        }
        return noSort;
    }

    protected int checkSegments(int[] array, int start, int end) {
        int seg = 0;
        for (int i = start; i + 1 < end && seg < 2; i++) if (Reads.compareIndices(array, i, i + 1, 0.01, true) > 0) seg++;
        return seg;
    }

    protected boolean segcheck(int[] array, int start, int end) {
        int seg = checkSegments(array, start - 1 > 0 ? start - 1 : 0, end);
        if (seg < 2) {
            if (seg == 1) {
                double ratio = arrayVisualizer.getDelays().getSleepRatio();
                arrayVisualizer.getDelays().setSleepRatio(ratio * 8);
                BlockInsertionSortAdaRot blocksert = new BlockInsertionSortAdaRot(arrayVisualizer);
                blocksert.insertionSort(array, 0, end);
                arrayVisualizer.getDelays().setSleepRatio(ratio);
            }
            return true;
        } else return false;
    }

    protected boolean max(int[] array, int start, int end) {
        for (int i = end - 1; i >= start; i--) if (Reads.compareIndices(array, i, end, 0.01, true) > 0) return false;
        return true;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        if (patternDefeat(array, 0, currentLength)) return;
        boolean sorted = false;
        int start = 0;
        sorted = segcheck(array, 0, currentLength);
        while (!sorted) {
            boolean startfound = false;
            for (int i = start - 1 > 0 ? start - 1 : 0; i + 1 < currentLength; i++) {
                int times = 0;
                while (Reads.compareIndices(array, i, i + 1, 0.01, true) > 0) {
                    Writes.multiSwap(array, i, currentLength - 1, 0.01, true, false);
                    if (!startfound) {
                        start = i;
                        startfound = true;
                    }
                    times++;
                    if (times == currentLength - i - 1) {
                        if (times > 3) Writes.reversal(array, i + 1, currentLength - 1, 0.01, true, false);
                        else if (times > 1) Writes.swap(array, i + 1, currentLength - 1, 0.01, true, false);
                    }
                }
            }
            currentLength--;
            sorted = segcheck(array, start - 1 > 0 ? start - 1 : 0, currentLength);
            if (!sorted) while (max(array, start - 1 > 0 ? start - 1 : 0, currentLength - 1) && currentLength > start) currentLength--;
        }
    }
}