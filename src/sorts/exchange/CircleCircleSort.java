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

"Take the shit, jump off of the shit and then twirl off of the shit."

 */

public class CircleCircleSort extends Sort {

    int zwaps = 0, n;

    public CircleCircleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Circle Circle");
        this.setRunAllSortsName("Circle Circle Sort");
        this.setRunSortName("Circle Circlesort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void c(int[] array, int a, int b) {
        if (b < n && Reads.compareIndices(array, a, b, 0.5, true) > 0) {
            Writes.swap(array, a, b, 0.5, true, false);
            zwaps++;
        }
    }

    public void circle(int[] array, int a, int b, int d) {
        Writes.recordDepth(d++);
        if (a >= b) return;
        int l = a, h = b;
        int m = (l+h) >> 1;
        while (a < b) c(array, a++, b--);
        Writes.recursion();
        circle(array, l, m, d);
        Writes.recursion();
        circle(array, m+1, h, d);
    }

    public void circleCircle(int[] array, int a, int b, int d) {
        Writes.recordDepth(d++);
        if (a >= b) return;
        int l = a, h = b;
        int m = (l+h) >> 1;
        while (a < b) circle(array, a++, b--, 0);
        Writes.recursion();
        circleCircle(array, l, m, d);
        Writes.recursion();
        circleCircle(array, m+1, h, d);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int l = 1;
        for (; (l << 1) < currentLength; l <<= 1);
        n = currentLength;
        currentLength = l << 1;
        do {
            zwaps = 0;
            circleCircle(array, 0, currentLength-1, 0);
        } while (zwaps != 0);
    }
}
