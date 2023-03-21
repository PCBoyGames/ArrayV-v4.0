package sorts.tests;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class TestingDivisions extends Sort {
    public TestingDivisions(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Testing Divisions");
        this.setRunAllSortsName("Testing Divisions");
        this.setRunSortName("Testing Divisions");
        this.setCategory("Tests");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        System.err.println("A:");
        for (int i = 0; i < currentLength; i++) {
            Highlights.markArray(1, i);
            System.err.print(array[i] + " ");
            Delays.sleep(2);
        }
        for (int i = 0; i < currentLength; i++) Writes.write(array, i, array[i] / 2, 1, true, false);
        for (int i = 0; i < currentLength; i++) Writes.write(array, i, array[i] * 2, 1, true, false);
        System.err.println("");
        System.err.println("B:");
        for (int i = 0; i < currentLength; i++) {
            Highlights.markArray(1, i);
            System.err.print(array[i] + " ");
            Delays.sleep(2);
        }
        System.err.println("");
    }
}