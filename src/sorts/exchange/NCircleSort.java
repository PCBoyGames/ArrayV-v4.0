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

Congrats, you sorted the array.
Okay you can stop now.
That's good.
You gotta stop at some point.
Come on, man.
QUIT HAVING FUN!!!

 */

public class NCircleSort extends Sort {

    int zwaps = 0, n;

    public NCircleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("N-Circle");
        this.setRunAllSortsName("N-Circle Sort");
        this.setRunSortName("N-Circlesort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(16);
        this.setBogoSort(false);
    }

    private void c(int[] array, int a, int b) {
        if (b < n && Reads.compareIndices(array, a, b, 0.5, true) > 0) {
            Writes.swap(array, a, b, 0.5, true, false);
            zwaps++;
        }
    }

    public void circleCircle(int O, int[] array, int a, int b, int d) {
        Writes.recordDepth(d++);
        if (a >= b) return;
        int l = a; int h = b;
        int m = (l+h) >> 1;
        if (O == 1) {
            while (a < b) c(array, a++, b--);
            Writes.recursion();
            circleCircle(O, array, l, m, d);
            Writes.recursion();
            circleCircle(O, array, m+1, h, d);
        } else {
            while (a < b) {Writes.recursion(); circleCircle(O-1, array, a++, b--, d);}
            Writes.recursion();
            circleCircle(O, array, l, m, d);
            Writes.recursion();
            circleCircle(O, array, m+1, h, d);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int l = 1;
        for (; (l << 1) < currentLength; l <<= 1);
        n = currentLength;
        currentLength = l << 1;
        do {
            zwaps = 0;
            circleCircle(n, array, 0, currentLength-1, 0);
        } while (zwaps != 0);
    }
}
