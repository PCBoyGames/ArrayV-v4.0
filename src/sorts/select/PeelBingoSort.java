package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class PeelBingoSort extends Sort {
    public PeelBingoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Peel Bingo");
        this.setRunAllSortsName("Peel Bingo Sort");
        this.setRunSortName("Peel Bingo Sort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected int collect(int[] array, int end) {
        int collect = 0;
        for (int left = end; left >= 0; left--) {
            int cmp = Reads.compareIndices(array, left, end - collect + 1 < end ? end - collect + 1 : end, 0.25, true);
            if (cmp > 0) {
                Writes.insert(array, left, end, 0.1, true, false);
                collect = 1;
            } else if (cmp == 0) {
                if (left != end - collect) Writes.insert(array, left, end - collect, 0.1, true, false);
                collect++;
            }
        }
        return collect;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        for (int end = currentLength; end > 0; end -= collect(array, end - 1));
    }
}