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

online

 */

public class FracticSort extends Sort {
    public FracticSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Fractic");
        this.setRunAllSortsName("Fractic Sort");
        this.setRunSortName("Fracticsort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(22);
        this.setBogoSort(false);
    }

    public void cPush(int[] array, int a, int b, double delay, int d) {
        if (a >= b) return;
        Writes.recordDepth(d++);
        do {
            for (int i = 0; i < b-a; i++) Writes.multiSwap(array, b, a, (delay != 0) ? delay /= 2 : 0, true, false);
        } while (Reads.compareValues(array[a], array[b]) > 0);
        Writes.recursion();
        cPush(array, a+1, b, (delay != 0) ? delay/2 : 0, d);
        Writes.recursion();
        cPush(array, a, b-1, (delay != 0) ? delay/2 : 0, d);
    }

    public void fractic(int[] array, int a, int b, int d) {
        if (a >= b) return;
        Writes.recordDepth(d++);
        int m = (b - a) / 2;
        Writes.recursion();
        fractic(array, a, a+m, d);
        Writes.recursion();
        fractic(array, b-m, b, d);
        cPush(array, a, b, 2, 0);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        fractic(array, 0, currentLength-1, 0);
    }
}
