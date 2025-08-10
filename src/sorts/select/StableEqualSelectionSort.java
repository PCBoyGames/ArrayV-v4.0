package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

import utils.IndexedRotations;

/*

/------------------/
|   SORTS GALORE   |
|------------------|
|  courtesy of     |
|  meme man        |
|  (aka gooflang)  |
/------------------/

good against runs of equal elements

 */

public final class StableEqualSelectionSort extends Sort {
    public StableEqualSelectionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Stable Equal Selection");
        this.setRunAllSortsName("Stable Equal Selection Sort");
        this.setRunSortName("Stable Equal Selection Sort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void equalSelect(int[] array, int a, int b) {
        for (int i = a; i < b;) {
            int minA = i;
            for (int j = i+1; j <= b; j++) if (Reads.compareIndices(array, minA, j, 0.25, true) > 0) minA = j;
            int minB = minA+1;
            for (; minB <= b && Reads.compareIndices(array, minA, minB, 0.25, true) == 0; minB++);
            if (i != minA) IndexedRotations.adaptable(array, i, minA, minB, 0.25, true, false);
            i += minB-minA;
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        equalSelect(array, 0, currentLength-1);
    }
}
