package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

public class RecursiveExchangeBogoSort extends BogoSorting {

    public RecursiveExchangeBogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Recursive Exchange Bogo");
        this.setRunAllSortsName("Recursive Exchange Bogo Sort");
        this.setRunSortName("Recursive Exchange Bogosort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(512);
        this.setBogoSort(true);
    }

    public void recShuffle(int[] array, int a, int b, int d) {
        if (b - a < 2) return;
        Writes.recordDepth(d++);
        int r1 = randInt(a, b);
        int r2 = randInt(a, b);
        if (r1 > r2) {
            int t = r1; r1 = r2; r2 = t;
        }
        if (Reads.compareIndices(array, r1, r2, 0.5, true) > 0) Writes.swap(array, r1, r2, 0.5, true, false);
        int m = (b - a) / 2;
        Writes.recursion();
        recShuffle(array, a, a+m, d);
        Writes.recursion();
        recShuffle(array, a + m, b, d);
    }

    public void recBogo(int[] array, int a, int b) {
        while (!isRangeSorted(array, a, b)) recShuffle(array, a, b, 0);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        recBogo(array, 0, sortLength);

    }

}
