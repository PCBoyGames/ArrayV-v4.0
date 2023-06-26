package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public final class PDDeterministicSimpleArgoSort extends Sort {
    public PDDeterministicSimpleArgoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Pattern-Defeating Deterministic Simple Argo");
        this.setRunAllSortsName("Pattern-Defeating Deterministic Simple Argo Sort");
        this.setRunSortName("Pattern-Defeating Deterministic Simple Argosort");
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

    protected int[] gaps = {1, 4, 10, 23, 57, 132, 301, 701, 1750, 3938, 8861};

    protected int ciura(int n) {
        if (n <= gaps.length) {
            return gaps[n - 1];
        }
        return (int)Math.pow(2.25, n);
    }

    protected int stablereturn(int a) {
        return arrayVisualizer.doingStabilityCheck() ? arrayVisualizer.getStabilityValue(a) : a;
    }

    protected int par(int[] array, int len) {
        boolean[] max = new boolean[len];
        int maximum = stablereturn(array[0]);
        for (int i = 1; i < len; i++) {
            if (stablereturn(array[i]) > maximum) {
                maximum = stablereturn(array[i]);
                max[i] = true;
            }
        }
        int i = len - 1;
        int p = 1;
        int j = len - 1;
        while (j >= 0 && i >= p) {
            while (!max[j] && j > 0) j--;
            maximum = stablereturn(array[j]);
            while (maximum <= stablereturn(array[i]) && i >= p) i--;
            if (stablereturn(array[j]) > stablereturn(array[i]) && p < i - j) p = i - j;
            j--;
        }
        return p;
    }

    protected boolean combPass(int[] array, int currentLength, int gap) {
        boolean swaps = false;
        for (int i = 0; i + gap < currentLength; i++) if (Reads.compareIndices(array, i, i + gap, 0.25, true) > 0) Writes.swap(array, i, i + gap, 0.25, swaps = true, false);
        return swaps;
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

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        if (patternDefeat(array, 0, currentLength)) return;
        int comb = currentLength;
        int shell = 0;
        int k;
        for (k = 1; shell < par(array, currentLength); k++) shell = ciura(k);
        boolean choice = true;
        while (true) {
            choice =! choice;
            int par = par(array, currentLength);
            if (choice) {
                comb /= 1.35;
                while (comb >= Math.min(shell, par)) comb /= 1.35;
                arrayVisualizer.setExtraHeading(" / Par(X): " + par + " / CMB: " + comb + " / shl: " + shell);
                if (!combPass(array, currentLength, comb) && comb == 1) break;
            } else {
                if (--k > 0) shell = ciura(k);
                else shell = 1;
                while (shell >= par) {
                    if (--k > 0) shell = ciura(k);
                    else {
                        shell = 1;
                        break;
                    }
                }
                arrayVisualizer.setExtraHeading(" / Par(X): " + par + " / cmb: " + comb + " / SHL: " + shell);
                shellPass(array, currentLength, shell);
                if (shell == 1) break;
            }
        }
        arrayVisualizer.setExtraHeading("");
    }
}
