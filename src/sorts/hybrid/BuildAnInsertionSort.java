package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

// Replace this with the path of the target algorithm.
import sorts.distribute.FlightSort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class BuildAnInsertionSort extends Sort {

    // Replace both filenames with the filename of the target algorithm.
    FlightSort sort = new FlightSort(arrayVisualizer);

    // Optional for most sorts, but required for some.
    int NUMBER_Base = 2;

    public BuildAnInsertionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Build-An-Insertion");
        this.setRunAllSortsName("Build-An-Insertion Sort");
        this.setRunSortName("Build-An-Insertion Sort");
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
        int len = 2;
        while (len <= currentLength) {
            method(array, len);
            len++;
        }
    }
}