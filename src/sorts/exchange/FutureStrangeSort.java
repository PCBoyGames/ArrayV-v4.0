package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class FutureStrangeSort extends Sort {
    public FutureStrangeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Future Strange");
        this.setRunAllSortsName("Future Strange Sort");
        this.setRunSortName("Future Strangesort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        boolean anyswaps = true;
        int trustlen = 2;
        while (trustlen <= currentLength) trustlen *= 2;
        trustlen /= 2;
        while (anyswaps) {
            anyswaps = false;
            int lastswap = 0;
            int offset = 0;
            while (offset != currentLength - 1) {
                int mult = 1;
                if (trustlen > 1) {
                    while (offset + mult < currentLength) mult *= 2;
                    mult /= 2;
                    while (mult >= trustlen) mult /= 2;
                }
                for (; mult >= 1; mult /= 2) {
                    if (Reads.compareIndices(array, offset, offset + mult, 0.25, true) > 0) {
                        Writes.swap(array, offset, offset + mult, 0.25, true, false);
                        anyswaps = true;
                        lastswap = offset;
                    }
                }
                offset++;
            }
            currentLength = lastswap + 2 < currentLength ? lastswap + 1 : currentLength - 1;
            if (trustlen > 1) trustlen /= 2;
        }
    }
}