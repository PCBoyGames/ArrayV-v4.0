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

circle sortb ut ballsk

 */

public class BallsCircleSort extends Sort {
    public BallsCircleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Balls Circle");
        this.setRunAllSortsName("Balls Circle Sort");
        this.setRunSortName("Balls Circlesort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(25);
        this.setBogoSort(false);
    }

    public void ballsCirclePass(int[] array, int a, int b, int d) {
        if (a >= b) return;
        Writes.recordDepth(d++);
        int l = a, r = b;
        while (l < r) {
            if (Reads.compareIndices(array, l, r, 0.5, true) > 0) Writes.swap(array, l, r, 0.5, true, false);
            l++; r--;
        }
        Writes.recursion();
        ballsCirclePass(array, a, b-1, d);
        Writes.recursion();
        ballsCirclePass(array, a+1, b, d);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        ballsCirclePass(array, 0, currentLength-1, 0);
    }
}
