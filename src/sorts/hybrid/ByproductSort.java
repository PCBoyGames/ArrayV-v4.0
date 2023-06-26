package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.GrailSorting;
import utils.Rotations;
import visuals.VisualStyles;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class ByproductSort extends GrailSorting {

    boolean checks;

    public ByproductSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Byproduct");
        this.setRunAllSortsName("Byproduct Sort");
        this.setRunSortName("Byproduct Sort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected int stablereturn(int value) {
        return arrayVisualizer.doingStabilityCheck() ? arrayVisualizer.getStabilityValue(value) : value;
    }

    protected int par(int[] array, int start, int end) {
        boolean[] max = new boolean[end - start];
        int maximum = stablereturn(array[start]);
        for (int i = 1; i < end - start; i++) {
            if (stablereturn(array[start + i]) > maximum) {
                maximum = stablereturn(array[start + i]);
                max[i] = true;
            }
        }
        int p = 1;
        for (int i = end - start - 1, j = end - start - 1; j >= 0 && i >= p; j--) {
            while (!max[j] && j > 0) j--;
            maximum = stablereturn(array[start + j]);
            while (maximum <= stablereturn(array[start + i]) && i >= p) i--;
            if (stablereturn(array[start + j]) > stablereturn(array[start + i]) && p < i - j) p = i - j;
            j--;
        }
        return p;
    }

    protected void shellPass(int[] array, int start, int end, int gap) {
        if (par(array, start, end) < gap) return;
        for (int h = gap, i = h + start; i < end; i++) {
            int v = array[i];
            int j = i;
            boolean w = false;
            Highlights.markArray(1, j);
            Highlights.markArray(2, j - h);
            Delays.sleep(0.5);
            for (; j >= h && Reads.compareValues(array[j - h], v) == 1; j -= h) {
                Highlights.markArray(2, j - h);
                Writes.write(array, j, array[j - h], 0.5, w = true, false);
            }
            if (w) Writes.write(array, j, v, 0.5, checks = true, false);
        }
    }

    protected void shell(int[] array, int start, int end) {
        int gap = (int) ((end - start) / 2.3601);
        while (gap > 2) {
            shellPass(array, start, end, gap);
            gap /= 2.3601;
        }
        shellPass(array, start, end, 1);
    }

    protected void grailRotate(int[] array, int pos, int lenA, int lenB) {
        checks = true;
        if (lenA % lenB == 0 || lenB % lenA == 0) Rotations.holyGriesMills(array, pos, lenA, lenB, 1, true, false);
        else Rotations.cycleReverse(array, pos, lenA, lenB, 1, true, false);
    }

    protected int findRun(int[] array, int start, int end) {
        int i = start + 1;
        if (i == end) return i;
        int cmp = Reads.compareIndices(array, i - 1, i++, 0.5, true);
        while (cmp == 0 && i < end) cmp = Reads.compareIndices(array, i - 1, i++, 0.5, true);
        if (cmp == 1) {
            checks = true;
            while (i < end && Reads.compareIndices(array, i - 1, i, 0.5, true) >= 0) i++;
            if (i - start < 4) Writes.swap(array, start, i - 1, 1, true, false);
            else Writes.reversal(array, start, i - 1, 1, true, false);
        }
        else while (i < end && Reads.compareIndices(array, i - 1, i, 0.5, true) <= 0) i++;
        Highlights.clearMark(2);
        return i;
    }

    protected void block(int[] array, int start, int end) {
        int j;
        for (int i = findRun(array, start, end); i < end; i = j) {
            j = findRun(array, i, end);
            grailMergeWithoutBuffer(array, start, i - start, j - i);
        }
    }

    protected boolean coprime(int a, int b) {
        for (int i = 2; i <= Math.min(a, b); i++) if (a % i == 0 && b % i == 0) return false;
        return true;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        VisualStyles vis = arrayVisualizer.getVisualStyles();
        int length = currentLength;
        for (int p = (int) Math.ceil(Math.sqrt(currentLength)); p < currentLength; p++) {
            if (currentLength % p == 0) {
                length = p;
                break;
            }
        }
        int height = currentLength / length;
        while (true) {
            if (length == height) arrayVisualizer.setExtraHeading(" / Factor Pair: SQRT = " + height);
            else if (length != 1 && height != 1) arrayVisualizer.setExtraHeading(" / Factor Pair: (" + height + "," + length + ")");
            else arrayVisualizer.setExtraHeading(" / Factor Pair: PRIME");
            checks = false;
            for (int i = 0; i < currentLength; i += length) {
                if (length < 65) block(array, i, i + length);
                else shell(array, i, i + length);
            }
            if (length != 1 && height != 1) {
                int mult = length;
                if (length % 2 == 0 && height % 2 == 0) {
                    for (; mult * 3 < currentLength; mult *= 3);
                    for (; mult >= length; mult /= 3) shellPass(array, 0, currentLength, mult);
                } else {
                    for (; mult * 2 < currentLength; mult *= 2);
                    for (; mult >= length; mult /= 2) shellPass(array, 0, currentLength, mult);
                }
            }
            if (checks && length != height) {
                int tmp = height;
                height = length;
                length = tmp;
                if (length != 1 && height != 1) vis.swapvals();
            } else break;
        }
        if (!coprime(length, height)) {
            arrayVisualizer.setExtraHeading(" / Factor Pair: FINAL PASS");
            if (length != 1 && height != 1 && currentLength < 4097) block(array, 0, currentLength);
            else if (currentLength > 4096) shell(array, 0, currentLength);
        }
        arrayVisualizer.setExtraHeading("");
        vis.resetvals();
    }
}