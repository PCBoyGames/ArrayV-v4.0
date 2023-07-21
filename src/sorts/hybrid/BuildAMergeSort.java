package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

// Replace this with the path of the target algorithm.
import sorts.exchange.OptimizedForwardRunShoveSort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class BuildAMergeSort extends Sort {

    // Replace both filenames with the filename of the target algorithm.
    OptimizedForwardRunShoveSort sort = new OptimizedForwardRunShoveSort(arrayVisualizer);

    // Optional for most sorts, but required for some.
    int NUMBER_Base = 2;

    public BuildAMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Build-A-Merge");
        this.setRunAllSortsName(sort.getRunAllSortsName() + " as Merge Sort");
        this.setRunSortName(sort.getRunSortName() + " as Mergesort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Enter the base for this sort:", 2);
    }

    protected void method(int[] array, int start, int len) {
        if (start != 0) for (int g = 0; g < len; g++) Writes.swap(array, g, start + g, 0.125, true, false);
        try {sort.runSort(array, len, NUMBER_Base);}
        catch (Exception e) {e.printStackTrace();}
        if (start != 0) for (int g = 0; g < len; g++) Writes.swap(array, g, start + g, 0.125, true, false);
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 2) return 2;
        return answer;
    }

    @Override
    public void runSort(int[] array, int currentLength, int base) {
        for (int len = base; len < currentLength; len *= base) for (int index = 0; index + len - 1 < currentLength; index += len) method(array, index, len);
        method(array, 0, currentLength);
    }
}