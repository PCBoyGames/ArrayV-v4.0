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
final public class DigSort extends Sort {

    int segends = 0;

    public DigSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Dig (New)");
        this.setRunAllSortsName("Dig Sort (New)");
        this.setRunSortName("Digsort (New)");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected int log2(int x) {
        int n = 1;
        while (1 << n < x) n++;
        if (1 << n > x) n--;
        return n;
    }

    protected int pow2lte(int value) {
        int val;
        for (val = 1; val <= value; val <<= 1);
        return val >> 1;
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
        for (int i = start; i + 1 < end && segmentcount < 2; i++) if (Reads.compareIndices(array, segends = i, i + 1, 0.01, true) > 0) segmentcount++;
        return segmentcount;
    }

    protected void shellPass(int[] array, int currentLength, int gap) {
        for (int h = gap, i = h; i < currentLength; i++) {
            int v = array[i];
            int j = i;
            boolean w = false;
            Highlights.markArray(1, j);
            Highlights.markArray(2, j - h);
            Delays.sleep(0.25);
            while (j >= h && Reads.compareValues(array[j - h], v) == 1) {
                Highlights.markArray(1, j);
                Highlights.markArray(2, j - h);
                Delays.sleep(0.25);
                Writes.write(array, j, array[j - h], 0.25, true, false);
                j -= h;
                w = true;
            }
            if (w) Writes.write(array, j, v, 0.25, true, false);
        }
    }

    protected void shell(int[] array, int currentLength) {
        for (int i = (int) (par(array, 0, currentLength) / 2.22222); i > 5; i /= 2.22222) shellPass(array, currentLength, i);
        shellPass(array, currentLength, 1);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        double ratio = arrayVisualizer.getDelays().getSleepRatio();
        BlockInsertionSortAdaRot blocksert = new BlockInsertionSortAdaRot(arrayVisualizer);
        int lastswap = 0;
        int len = currentLength;
        int lastlen = len;
        int seg = checkSegments(array, 0, currentLength);
        if (seg == 0) {
            arrayVisualizer.setExtraHeading(" / Exit: seg == 0");
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
            arrayVisualizer.setExtraHeading("");
            return;
        }
        else if (seg == 2) circle(array, 0, currentLength - 1);
        else {
            arrayVisualizer.setExtraHeading(" / Exit: seg == 1");
            shellPass(array, currentLength, (int) (len / 2.22222));
            if (par(array, 0, currentLength) < currentLength / 64) {
                arrayVisualizer.getDelays().setSleepRatio(ratio * 8);
                blocksert.insertionSort(array, 0, currentLength);
                arrayVisualizer.getDelays().setSleepRatio(ratio);
            } else shell(array, currentLength);
            arrayVisualizer.setExtraHeading("");
            return;
        }
        if (segends >= currentLength * 2 / 3) {
            arrayVisualizer.setExtraHeading(" / Exit: segends >= currentLength * 2 / 3");
            shell(array, currentLength);
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
        int limit = (int) (pow2lte(log2(currentLength)) * 2.5);
        for (int i = 0; i < currentLength && swap; i = lastswap) {
            lastlen = len;
            len = par(array, 0, currentLength);
            arrayVisualizer.setExtraHeading(" / Par(X): " + len + " / Iteration: " + (timesdone + 1));
            if (timesdone >= limit) break;
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
            if (seg == 1) break;
            if (timesdone > 0 && lastlen - len <= limit * 2) break;
            if (len <= currentLength / 64) break;
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
        if (seg == 1 || (timesdone > 0 && lastlen - len <= limit * 2) || timesdone >= limit || (len <= currentLength / 64 || len <= 64)) {
            if (seg == 1) arrayVisualizer.setExtraHeading(" / Exit: seg == 1");
            if (timesdone > 0 && lastlen - len <= limit * 2) arrayVisualizer.setExtraHeading(" / Exit: timesdone > 0 && lastlen - len <= limit * 2");
            if (timesdone >= limit) arrayVisualizer.setExtraHeading(" / Exit: timesdone >= limit");
            if (len <= currentLength / 64 || len <= 64) arrayVisualizer.setExtraHeading(" / Exit: len <= currentLength / 64 || len <= 64");
            shellPass(array, currentLength, (int) (par(array, 0, currentLength) / 2.22222));
            int par = par(array, 0, currentLength);
            if (par < currentLength / 64) {
                arrayVisualizer.getDelays().setSleepRatio(ratio * 8);
                blocksert.insertionSort(array, lastswap, currentLength);
                arrayVisualizer.getDelays().setSleepRatio(ratio);
            } else shell(array, currentLength);
        }
        arrayVisualizer.setExtraHeading("");
    }
}