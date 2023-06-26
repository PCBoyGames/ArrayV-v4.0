package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

// Replace this with the path of the target algorithm.
import sorts.select.MoreOptimizedOpiumSort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class BuildAnInsertionSort extends Sort {

    // Replace both filenames with the filename of the target algorithm.
    MoreOptimizedOpiumSort sort = new MoreOptimizedOpiumSort(arrayVisualizer);

    // Optional for most sorts, but required for some.
    int NUMBER_Base = 2;

    public BuildAnInsertionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Build-An-Insertion");
        this.setRunAllSortsName(sort.getRunAllSortsName() + " as Insertion Sort");
        this.setRunSortName(sort.getRunSortName() + " as Insertsort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void method(int[] array, int len) {
        try {sort.runSort(array, len, NUMBER_Base);}
        catch (Exception e) {e.printStackTrace();}
    }

    @Override
    public void runSort(int[] array, int currentLength, int base) {
        for (int len = 2; len <= currentLength; len++) method(array, len);
    }
}