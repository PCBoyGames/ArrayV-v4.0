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

circloid sortb ut ballsk

 */

public class BallsCircloidSort extends Sort {
    public BallsCircloidSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Balls Circloid");
        this.setRunAllSortsName("Balls Circloid Sort");
        this.setRunSortName("Balls Circloid Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(25);
        this.setBogoSort(false);
    }

    public boolean ballsCircloidPass(int[] array, int a, int b, int d) {
        if (a >= b) return false;
        Writes.recordDepth(d++);
        Writes.recursion();
        boolean l = ballsCircloidPass(array, a, b-1, d);
        Writes.recursion();
        boolean r = ballsCircloidPass(array, a+1, b, d);
        boolean swaps = false;
        while (a < b) {
            if (Reads.compareIndices(array, a, b, 0.5, true) > 0) Writes.swap(array, a, b, 0.5, swaps = true, false);
            a++; b--;
        }
        return swaps || l || r;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        while (ballsCircloidPass(array, 0, currentLength-1, 0));
    }
}
