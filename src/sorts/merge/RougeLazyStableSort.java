package sorts.merge;

import main.ArrayVisualizer;
import sorts.insert.BlockInsertionSortAdaRot;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class RougeLazyStableSort extends Sort {
    public RougeLazyStableSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Rouge Lazy Stable");
        this.setRunAllSortsName("Rouge Lazy Stable Sort");
        this.setRunSortName("Rouge Lazy Stable Sort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        BlockInsertionSortAdaRot blocksert = new BlockInsertionSortAdaRot(arrayVisualizer);
        for (int j = 2; j <= currentLength; j++) for (int i = 0; i < currentLength; i += j) blocksert.insertionSort(array, i, Math.min(i + j, currentLength));
    }
}