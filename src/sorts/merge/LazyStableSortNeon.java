package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.GrailSorting;
import utils.Rotations;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class LazyStableSortNeon extends GrailSorting {

    public LazyStableSortNeon(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Lazy Stable (Neon Rotations)");
        this.setRunAllSortsName("Lazy Stable Sort (Neon Rotations)");
        this.setRunSortName("Lazy Stable Sort (Neon Rotations)");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void grailRotate(int[] array, int pos, int lenA, int lenB) {
        Highlights.clearAllMarks();
        Rotations.neon(array, pos, lenA, lenB, 0.5, true, false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        grailLazyStableSort(array, 0, currentLength);
    }
}