package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class UniquePatterns extends Sort {
    public UniquePatterns(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("ZZZ - No Sort");
        this.setRunAllSortsName(" ");
        this.setRunSortName(" ");
        this.setCategory(" ");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        
    }
}