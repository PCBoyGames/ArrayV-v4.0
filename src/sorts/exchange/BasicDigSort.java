package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class BasicDigSort extends Sort {
    public BasicDigSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Dig (Basic)");
        this.setRunAllSortsName("Dig Sort (Basic)");
        this.setRunSortName("Digsort (Basic)");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
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

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int lastswap = 0;
        int len = currentLength;
        int maxswap = currentLength - 2;
        boolean maxswapped;
        boolean swap = true;
        int timesdone = 0;
        for (int i = 0; i < currentLength && swap; i = lastswap) {
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
            timesdone++;
        }
        arrayVisualizer.setExtraHeading("");
    }
}