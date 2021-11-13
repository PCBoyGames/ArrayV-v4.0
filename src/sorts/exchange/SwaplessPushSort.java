package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class SwaplessPushSort extends Sort {
    public SwaplessPushSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Swapless Push");
        this.setRunAllSortsName("Swapless Push Sort");
        this.setRunSortName("Swapless Pushsort");
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
        int i = 1;
        int first = 1;
        while (anyswaps) {
            anyswaps = false;
            if (first > 1) i = first - 1;
            else i = 1;
            int gap = 1;
            while (i + gap <= currentLength) {
                Highlights.markArray(1, i - 1);
                Highlights.markArray(2, (i - 1) + gap);
                Delays.sleep(0.01);
                if (Reads.compareValues(array[i - 1], array[(i - 1) + gap]) > 0) {
                    Highlights.clearMark(2);
                    int item = array[(i - 1) + gap];
                    for (int j = gap; j > 0; j--) Writes.write(array, i - 1 + j, array[i - 1 + j - 1], 0.01, true, false);
                    Writes.write(array, (i - 1), item, 0.01, true, false);
                    if (!anyswaps) first = i;
                    anyswaps = true;
                    gap++;
                } else i++;
            }
        }
    }
}