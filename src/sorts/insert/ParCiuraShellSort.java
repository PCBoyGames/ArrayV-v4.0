package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public final class ParCiuraShellSort extends Sort {
    public ParCiuraShellSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Par Shell (Ciura)");
        this.setRunAllSortsName("Par(X) Shell Sort (Ciura)");
        this.setRunSortName("Par(X) Shellsort (Ciura)");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
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
            if (w) {
                Writes.write(array, j, v, 0.25, true, false);
            }
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int shell = 0;
        int k;
        for (k = 1; shell < par(array, currentLength); k++) shell = ciura(k);
        while (shell > 1) {
            int par = par(array, currentLength);
            shell = ciura(--k);
            while (shell >= par) {
                if (--k > 0) shell = ciura(k);
                else {
                    shell = 1;
                    break;
                }
            }
            arrayVisualizer.setExtraHeading(" / Par(X): " + par + " / Gap: " + shell);
            shellPass(array, currentLength, shell);
            if (shell == 1) break;
        }
        arrayVisualizer.setExtraHeading("");
    }
}
