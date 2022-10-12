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
final public class OldDigSort extends Sort {
    public OldDigSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Dig (Old)");
        this.setRunAllSortsName("Dig Sort (Old)");
        this.setRunSortName("Digsort (Old)");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
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

    protected int stablereturn(int a) {
        return arrayVisualizer.doingStabilityCheck() ? arrayVisualizer.getStabilityValue(a) : a;
    }

    protected int par(int[] array, int a, int b) {
        boolean[] max = new boolean[b - a];
        int maximum = stablereturn(array[a]);
        for (int i = 1; i < b - a; i++) {
            if (stablereturn(array[a + i]) > maximum) {
                maximum = stablereturn(array[a + i]);
                max[i] = true;
            }
        }
        int i = b - a - 1;
        int p = 1;
        int j = b - a - 1;
        while (j >= 0 && i >= p) {
            while (!max[j] && j > 0) j--;
            maximum = stablereturn(array[a + j]);
            while (maximum <= stablereturn(array[a + i]) && i >= p) i--;
            if (stablereturn(array[a + j]) > stablereturn(array[a + i]) && p < i - j) p = i - j;
            j--;
        }
        return p;
    }

    protected void circle(int[] array, int a, int b) {
        int left = a;
        int right = b;
        while (left < right) {
            if (Reads.compareIndices(array, left, right, 0.1, true) > 0) Writes.swap(array, left, right, 0.1, true, false);
            left++;
            right--;
        }
    }

    protected int checkSegments(int[] array, int start, int end) {
        int segmentcount = 0;
        for (int i = start; i + 1 < end && segmentcount < 2; i++) if (Reads.compareIndices(array, i, i + 1, 0.01, true) > 0) segmentcount++;
        return segmentcount;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        double ratio = arrayVisualizer.getDelays().getSleepRatio();
        BlockInsertionSortAdaRot blocksert = new BlockInsertionSortAdaRot(arrayVisualizer);
        int lastswap = 0;
        int len = currentLength;
        int lastlen = len;
        int seg = checkSegments(array, 0, currentLength);
        if (seg == 2) circle(array, 0, currentLength - 1);
        else {
            arrayVisualizer.setExtraHeading(" / Exit: seg != 2");
            arrayVisualizer.getDelays().setSleepRatio(ratio * 8);
            blocksert.insertionSort(array, lastswap, currentLength);
            arrayVisualizer.getDelays().setSleepRatio(ratio);
            arrayVisualizer.setExtraHeading("");
            return;
        }
        if (patternDefeat(array, 0, currentLength)) {
            arrayVisualizer.setExtraHeading(" / Exit: patternDefeat(array, 0, currentLength)");
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
            arrayVisualizer.setExtraHeading("");
            return;
        }
        int maxswap = currentLength - 2;
        boolean maxswapped;
        boolean swap = true;
        int timesdone = 0;
        for (int i = 0; i < currentLength && swap; i = lastswap) {
            lastlen = len;
            len = par(array, 0, currentLength);
            arrayVisualizer.setExtraHeading(" / Par(X): " + len + " / Iteration: " + (timesdone + 1));
            swap = false;
            maxswapped = false;
            for (int j = maxswap; j >= i; j--) {
                if (Reads.compareIndices(array, j, j + 1, 0.1, true) > 0) {
                    Writes.swap(array, lastswap = j, j + 1, 0.1, swap = true, false);
                    if (!maxswapped) {
                        maxswapped = true;
                        maxswap = j + 1 < currentLength - 1 ? j + 1 : currentLength - 2;
                    }
                }
            }
            seg = checkSegments(array, lastswap, currentLength);
            if (len == 1 || seg == 1 || (timesdone > 0 && lastlen - len <= 3)) break;
            if (swap) {
                for (int left = lastswap + 1; left <= maxswap; left++) {
                    if (Reads.compareIndices(array, left, left + 1, 0.1, true) > 0) {
                        int boundlen = left + len < currentLength - 1 ? left + len : currentLength - 2;
                        for (int right = left + 1; right <= boundlen; right++) {
                            if (Reads.compareIndices(array, left, right, 0.1, true) > 0) {
                                Writes.swap(array, left++, right, 0.1, true, false);
                                if (right > maxswap) maxswap = right;
                            }
                        }
                    }
                }
            }
            if (timesdone < 2) patternDefeat(array, 0, currentLength);
            timesdone++;
        }
        if (len != 1 && seg == 1 || (timesdone > 0 && lastlen - len <= 3)) {
            if (len != 1 && seg == 1) arrayVisualizer.setExtraHeading(" / Exit: len != 1 && seg == 1");
            if (timesdone > 0 && lastlen - len <= 3) arrayVisualizer.setExtraHeading(" / Exit: timesdone > 0 && lastlen - len <= 3");
            arrayVisualizer.getDelays().setSleepRatio(ratio * 8);
            blocksert.insertionSort(array, lastswap, currentLength);
            arrayVisualizer.getDelays().setSleepRatio(ratio);
        }
        arrayVisualizer.setExtraHeading("");
    }
}