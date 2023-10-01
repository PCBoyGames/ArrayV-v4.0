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

    private int circlePass(int[] array, int a, int b, int zwapz, int d) {
        if (a >= b) return zwapz;
        Writes.recordDepth(d++);
        int l = a; int h = b;
        while (a < b) {
            if (Reads.compareIndices(array, a, b, 0.005, true) > 0) {
                Writes.swap(array, a, b, 0.0995, true, false);
                zwapz++;
            }
            a++;
            b--;
        }
        int m = l + (h - l) / 2;
        Writes.recursion();
        zwapz = circlePass(array, l, m, zwapz, d);
        Writes.recursion();
        zwapz = circlePass(array, m+1, h, zwapz, d);
        return zwapz;
    }

    public int circleCircle(int O, int[] array, int a, int b, int swapz, int d) {
        if (a >= b) return swapz;
        Writes.recordDepth(d++);
        int l = a; int h = b;
        int m = l + (h - l) / 2;
        if (O == 1) {
            while (a < b) {
                swapz = circlePass(array, a, b, 0, 0);
                a++;
                b--;
            }
            Writes.recursion();
            swapz = circleCircle(O, array, l, m, swapz, d);
            Writes.recursion();
            swapz = circleCircle(O, array, m+1, h, swapz, d);
        } else {
            while (a < b) {
                swapz = circleCircle(O-1, array, a, b, 0, 0);
                a++;
                b--;
            }
            Writes.recursion();
            swapz = circleCircle(O, array, l, m, swapz, d);
            Writes.recursion();
            swapz = circleCircle(O, array, m+1, h, swapz, d);
        }
        return swapz;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int zwaps = 0;
        do {
            zwaps = circleCircle(currentLength, array, 0, currentLength-1, 0, 0);
        } while (zwaps != 0);
    }
}
