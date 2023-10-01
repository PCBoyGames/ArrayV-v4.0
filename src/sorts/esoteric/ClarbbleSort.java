package sorts.esoteric;

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

world record for dumbest bubble variant

 */

public class ClarbbleSort extends Sort {
    public ClarbbleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Clarbble");
        this.setRunAllSortsName("Clarbble Sort");
        this.setRunSortName("Clarbblesort");
        this.setCategory("Esoteric Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(false);
    }

    protected int findRun(int[] array, int a, int b) {
        int i = a + 1;
        if (i == b) return i;
        int cmp = Reads.compareIndices(array, i - 1, i++, 0.5, true);
        while (cmp == 0 && i < b) cmp = Reads.compareIndices(array, i - 1, i++, 0.5, true);
        if (cmp == 1) {
            while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) >= 0) i++;
            if (i - a < 4) Writes.swap(array, a, i - 1, 1.0, true, false);
            else Writes.reversal(array, a, i - 1, 1.0, true, false);
        }
        else while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) <= 0) i++;
        Highlights.clearMark(2);
        return i;
    }

    protected boolean pd(int[] array, int a, int b) {
        int i = a + 1, j = a;
        boolean noSort = true;
        while (i < b) {
            i = findRun(array, j, b);
            if (i < b) noSort = false;
            j = i++;
        }
        return noSort;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        for (int i = currentLength; i > 0; i--) for (int j = 0; j < i; j++) pd(array, j, i);
    }
}
