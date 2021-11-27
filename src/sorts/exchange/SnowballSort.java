package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class SnowballSort extends Sort {
    public SnowballSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Snowball");
        this.setRunAllSortsName("Snowball Sort");
        this.setRunSortName("Snowball Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(2048);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int gap = 0;
        int begin = 0;
        while (gap != 1) {
            gap = 1;
            for (int i = begin - 1 > 0 ? begin - 1 : 0; i + gap < currentLength; i++) {
                if (Reads.compareIndices(array, i, i + gap, 0.001, true) > 0) {
                    if (gap > 2) Writes.reversal(array, i, i + gap, 0.01, true, false);
                    else Writes.swap(array, i, i + gap, 0.01, true, false);
                    if (gap == 1) begin = i;
                    gap *= 2;
                }
            }
        }
    }
}