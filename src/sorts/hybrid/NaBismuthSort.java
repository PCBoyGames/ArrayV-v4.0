package sorts.hybrid;

import main.ArrayVisualizer;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class NaBismuthSort extends BismuthSort {
    public NaBismuthSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Natural Bismuth [WIP]");
        this.setRunAllSortsName("Natural Bismuth Sort");
        this.setRunSortName("Natural Bismuth Sort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Enter the segment size to be used for this sort:\n0: Automatic (default)", 0);
    }

    public void nabismuthSort(int[] array, int start, int end, int getSize, boolean useBuffers, boolean silentCase, boolean doPD, int depth) {
        Writes.recordDepth(depth);
        Highlights.retainColorMarks(true);
        getSize = getSize == 0 ? (int) Math.sqrt(end - start) : Math.min(getSize, end - start);
        String caseType = silentCase ? "" : end - start < 33 ? "SMALL" : getSize == 1 ? "1A" : getSize == 2 ? "1B" : getSize < 11 ? "1C" : !useBuffers && getSize < 33 ? "1D" : !useBuffers && getSize > 32 ? "1E" : getSize < 33 ? "2A" : getSize > 32 ? "2B" : "UNKNOWN";
        if (!caseType.equals("")) arrayVisualizer.setExtraHeading(" / bSize = " + getSize + " (Case " + caseType + ")");
        int balance = (end - start) % getSize, deadEnd = end - balance;
        if (end - start < 33) insertions(array, start, end);
        if (end - start < 33 || (doPD && patternDefeat(array, start, end, false, 1, true, false))) return;
        int i = getSize < 11 || !useBuffers ? start : start + getSize;
        if (getSize > 10 && useBuffers) colorRange(depth > 0 ? "subBuffer" : "buffer", depth > 0 ? java.awt.Color.GRAY : java.awt.Color.DARK_GRAY, start, start + getSize);
        for (; i + getSize <= deadEnd; i += getSize) insertions(array, i, i + getSize);
        boolean one = false;
        if ((getSize > 10 && useBuffers ? start + getSize : start) < deadEnd) {
            while (!one) {
                for (int a = getSize > 10 && useBuffers ? start + getSize : start, b = a + getSize, c = b + getSize; a < deadEnd; a = c, b = a + getSize) {
                    while (b < deadEnd && Reads.compareIndices(array, b - 1, b, 1, true) <= 0) b += getSize;
                    if (b >= deadEnd && a == (getSize > 10 && useBuffers ? start + getSize : start)) {
                        one = true;
                        break;
                    }
                    c = b + getSize;
                    while (c < deadEnd && Reads.compareIndices(array, c - 1, c, 1, true) <= 0) c += getSize;
                    merge(array, a, c >= deadEnd ? deadEnd : c, b, start, getSize, useBuffers, 0);
                    if (c >= deadEnd && a == (getSize > 10 && useBuffers ? start + getSize : start)) {
                        one = true;
                        break;
                    }
                }
            }
        }
        if (getSize > 10 && useBuffers) giveUpFair(array, start, start + getSize, deadEnd, true, true, true, depth);
        if (balance > 0) giveUpFair(array, start, deadEnd, end, false, true, true, depth);
    }

    public void giveUpFair(int[] array, int start, int mid, int end, boolean side, boolean searches, boolean pre, int depth) {
        Writes.recordDepth(depth);
        while (true) {
            if (pre) insertions(array, side ? start : mid, side ? mid : end);
            if (Reads.compareIndices(array, mid - 1, mid, 1, true) <= 0) return;
            if (searches) start = minExponentialSearch(array, start, mid, array[mid], false, 1, true);
            if (searches) end = maxExponentialSearch(array, mid, end, array[mid - 1], true, 1, true);
            if (doesRotate(array, start, mid, end)) return;
            else if (Math.min(mid - start, end - mid) < 10) {
                colorRange("small", java.awt.Color.getHSBColor(0.8f, 1f, 1f), start, end);
                grailMergeWithoutBuffer(array, start, mid - start, end - mid);
                return;
            } else if ((double) ((double) (mid - start) / (double) (end - mid)) > 0.66667 && (double) ((double) (mid - start) / (double) (end - mid)) < 1.5) {
                Writes.recursion();
                nabismuthSort(array, start, end, 0, true, true, false, depth + 1);
                return;
            } else {
                nudge(array, start, mid, end, depth);
                pre = false;
                searches = true;
            }
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int getSize) {
        nabismuthSort(array, 0, currentLength, getSize, true, false, true, 0);
    }
}