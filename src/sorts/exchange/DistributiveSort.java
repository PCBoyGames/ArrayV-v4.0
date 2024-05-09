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

based off of the distributive property

 */

public final class DistributiveSort extends Sort {
    public DistributiveSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Distributive");
        this.setRunAllSortsName("Distributive Sort");
        this.setRunSortName("Distributive Sort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void distribuive(int[] array, int a, int b, int d) {
        if (a >= b) return;
        Writes.recordDepth(d++);
        int m = (b - a) / 2;
        for (int i = a; i <= a+m; i++)
            for (int j = b-m; j <= b; j++)
                if (Reads.compareIndices(array, i, j, 0.5, true) > 0)
                    Writes.swap(array, i, j, 0.5, true, false);
        Writes.recursion();
        distribuive(array, a, a+m, d);
        Writes.recursion();
        distribuive(array, b-m, b, d);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        distribuive(array, 0, currentLength-1, 0);
    }
}
