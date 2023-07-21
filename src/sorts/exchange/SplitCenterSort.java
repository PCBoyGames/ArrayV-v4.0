package sorts.exchange;
import main.ArrayVisualizer;
import sorts.templates.Sort;
/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class SplitCenterSort extends Sort {
    public SplitCenterSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Split Center");
        this.setRunAllSortsName("Split Center Sort");
        this.setRunSortName("Split Center Sort");
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
        int way = 1;
        for (int r = 1; r < currentLength; r++, way *= -1) for (int i = (int) Math.floor(currentLength / 2); i < currentLength && i > 0; i += way) if (Reads.compareIndices(array, i - 1, i, 0.005, true) > 0) Writes.swap(array, i - 1, i, 0.005, true, false);
    }
}