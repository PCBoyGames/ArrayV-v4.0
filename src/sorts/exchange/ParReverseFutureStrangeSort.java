package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class ParReverseFutureStrangeSort extends Sort {
    public ParReverseFutureStrangeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Par Reverse Future Strange");
        this.setRunAllSortsName("Par(X) Reverse Future Strange Sort");
        this.setRunSortName("Par(X) Reverse Future Strangesort");
        this.setCategory("Impractical Sorts");
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

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int trustlen = par(array, currentLength);
        int passmult = 0;
        while (trustlen > 1) {
            trustlen = par(array, currentLength);
            int lastswap = 0;
            int offset = 0;
            while (offset != currentLength - 1) {
                int mult = 1;
                int parsemult = 1;
                if (trustlen > 1) {
                    while (offset + parsemult < currentLength) parsemult *= 2;
                    parsemult /= 2;
                    while (parsemult >= trustlen) parsemult /= 2;
                }
                if (offset == 0) passmult = parsemult;
                arrayVisualizer.setExtraHeading(" / Par(X): " + trustlen + " / Trusted: " + passmult);
                for (; offset + mult < currentLength && mult <= trustlen; mult *= 2) {
                    if (Reads.compareIndices(array, offset, offset + mult, 0.25, true) > 0) {
                        Writes.swap(array, offset, offset + mult, 0.25, true, false);
                        lastswap = offset;
                    }
                }
                offset++;
            }
            currentLength = lastswap + 2 < currentLength ? lastswap + 1 : currentLength - 1;
        }
        arrayVisualizer.setExtraHeading("");
    }
}