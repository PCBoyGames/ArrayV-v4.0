package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

/------------------/
|   SORTS GALORE   |
|------------------|
|  courtesy of     |
|  meme man        |
|  (aka gooflang)  |
/------------------/



 */

public class RecursiveBogoSort extends BogoSorting {
    public RecursiveBogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Recursive Bogo");
        this.setRunAllSortsName("Recursive Bogo Sort");
        this.setRunSortName("Recursive Bogosort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(11);
        this.setBogoSort(true);
    }

    public void recShuffle(int[] array, int a, int b, int d) {
        if (a >= b) return;
        Writes.recordDepth(d++);
        int r1 = randInt(a, b);
        int r2 = randInt(a, b);
        if (r1 != r2) Writes.swap(array, r1, r2, 1, true, false);
        int m = (b - a) / 2;
        Writes.recursion();
        recShuffle(array, a, a+m, d);
        Writes.recursion();
        recShuffle(array, b-m, b, d);
    }

    public void recBogo(int[] array, int a, int b) {
        while (!isRangeSorted(array, a, b)) recShuffle(array, a, b, 0);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        recBogo(array, 0, currentLength);
    }
}
