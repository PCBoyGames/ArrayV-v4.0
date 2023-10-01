package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

/------------------/
|   SORTS GALORE   |
|------------------|
|  courtesy of     |
|  meme man        |
|  (aka gooflang)  |
/------------------/

A really cursed mix between Bubble and Stooge.

 */

public class StruggleSort extends Sort {
    public StruggleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Struggle");
        this.setRunAllSortsName("Struggle Sort");
        this.setRunSortName("Strugglesort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(8192);
        this.setBogoSort(false);
    }

    private void struggle(int[] array, int a, int b) {
        boolean sorted = false;
        while (!sorted) {
            sorted = true;
            if (b - a + 1 < 3) {
                for (int i = a; i + 1 < b; i++) if (Reads.compareIndices(array, i, i + 1, 0.025, true) > 0) Writes.swap(array, i, i + 1, 0.075, true, sorted = false);
            } else {
                for (int i = a; i + 2 < b; i++) {
                    if (Reads.compareIndices(array, i, i + 1, 0.025, true) > 0) Writes.swap(array, i, i + 1, 0.075, true, sorted = false);
                    if (Reads.compareIndices(array, i + 1, i + 2, 0.025, true) > 0) Writes.swap(array, i + 1, i + 2, 0.075, true, sorted = false);
                    if (Reads.compareIndices(array, i, i + 1, 0.025, true) > 0) Writes.swap(array, i, i + 1, 0.075, true, sorted = false);
                }
            }
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        struggle(array, 0, currentLength);
    }
}
