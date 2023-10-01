package sorts.exchange;

import main.ArrayVisualizer;
import sorts.insert.InsertionSort;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class SelectFutureStrangeSort extends Sort {
    public SelectFutureStrangeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Select Future Strange");
        this.setRunAllSortsName("Select Future Strange Sort");
        this.setRunSortName("Select Future Strangesort");
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

    protected int par(int[] array, int len) {
        boolean[] max = new boolean[len];
        int maximum = stablereturn(array[0]);
        for (int i = 1; i < len; i++) {
            if (stablereturn(array[i]) > maximum) {
                maximum = stablereturn(array[i]);
                max[i] = true;
            }
        }
        int p = 1;
        for (int i = len - 1, j = len - 1; j >= 0 && i >= p; j--) {
            while (!max[j] && j > 0) j--;
            maximum = stablereturn(array[j]);
            while (maximum <= stablereturn(array[i]) && i >= p) i--;
            if (stablereturn(array[j]) > stablereturn(array[i]) && p < i - j) p = i - j;
        }
        return p;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int total = currentLength;
        boolean anyswaps = true;
        int currentpar = par(array, currentLength);
        int lastpar = currentLength;
        boolean partest = false;
        int trustlen = 2;
        while (trustlen <= currentLength) trustlen *= 2;
        trustlen /= 2;
        while (anyswaps) {
            lastpar = currentpar;
            currentpar = par(array, currentLength);
            if (lastpar - currentpar <= Math.sqrt(lastpar) && partest && currentpar > 32) break;
            partest = true;
            anyswaps = false;
            int lastswap = 0;
            for (int offset = 0; offset != currentLength - 1; offset++) {
                int mult = 1;
                if (trustlen > 1) {
                    while (offset + mult < currentLength) mult *= 2;
                    mult /= 2;
                    while (mult >= trustlen) mult /= 2;
                }
                int select = offset;
                for (; mult >= 1; mult /= 2) {
                    if (Reads.compareIndices(array, select, offset + mult, 0.25, true) > 0) {
                        select = offset + mult;
                        anyswaps = true;
                        lastswap = offset;
                    }
                }
                if (select != offset) Writes.swap(array, select, offset, 0.25, true, false);
            }
            currentLength = lastswap + 2 < currentLength ? lastswap + 1 : currentLength - 1;
            if (trustlen > 1) trustlen /= 2;
        }
        if (lastpar - currentpar <= Math.sqrt(lastpar) && partest && currentpar > 32) {
            arrayVisualizer.setExtraHeading(" / This isn't working. Let's try the original...");
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
            arrayVisualizer.setExtraHeading("");
            FutureStrangeSort fallback = new FutureStrangeSort(arrayVisualizer);
            fallback.runSort(array, currentLength, 0);
        }
        InsertionSort clean = new InsertionSort(arrayVisualizer);
        clean.customInsertSort(array, 0, total, 10, false);
    }
}