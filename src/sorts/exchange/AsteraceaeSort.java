package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class AsteraceaeSort extends Sort {
    public AsteraceaeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Asteraceae");
        this.setRunAllSortsName("Asteraceae Sort");
        this.setRunSortName("Asteraceae Sort");
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
        int i;
        int firstswap = 2;
        boolean anyswaps = true;
        while (anyswaps) {
            if (firstswap - 1 == 0) i = 1;
            else i = firstswap - 1;
            anyswaps = false;
            boolean lastswap = false;
            while (i + 1 <= currentLength) {
                if (Reads.compareIndices(array, i - 1, i, 0.1, true) > 0) {
                    Writes.swap(array, i - 1, i++, 0.1, true, false);
                    if (!anyswaps) firstswap = i - 1;
                    anyswaps = true;
                    lastswap = true;
                } else {
                    if (lastswap) i += (int) Math.floor(Math.sqrt(currentLength));
                    else i++;
                    lastswap = false;
                }
            }
        }
    }
}