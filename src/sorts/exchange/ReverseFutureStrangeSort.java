package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class ReverseFutureStrangeSort extends Sort {
    public ReverseFutureStrangeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Reverse Future Strange");
        this.setRunAllSortsName("Reverse Future Strange Sort");
        this.setRunSortName("Reverse Future Strangesort");
        this.setCategory("Impractical Sorts");
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
            for (int offset = 0; offset != currentLength - 1; offset++) {
                int mult = 1;
                for (; offset + mult < currentLength && mult <= trustlen; mult *= 2) if (Reads.compareIndices(array, offset, offset + mult, 0.25, true) > 0) Writes.swap(array, lastswap = offset, offset + mult, 0.25, anyswaps = true, false);
            }
            currentLength = lastswap + 2 < currentLength ? lastswap + 1 : currentLength - 1;
        }
    }
}