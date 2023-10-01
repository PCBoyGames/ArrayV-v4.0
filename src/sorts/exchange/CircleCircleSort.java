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
    public CircleCircleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Circle Circle");
        this.setRunAllSortsName("Circle Circle Sort");
        this.setRunSortName("Circlecirclesort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int circlePass(int[] array, int a, int b, int zwapz, int d) {
        if (a >= b) return zwapz;
        Writes.recordDepth(d++);
        int l = a; int h = b;
        while (a < b) {
            if (Reads.compareValues(array[a], array[b]) > 0) {
                Writes.swap(array, a, b, 1, true, false);
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

    public int circleCircle(int[] array, int a, int b, int swapz, int d) {
        if (a >= b) return swapz;
        Writes.recordDepth(d++);
        int l = a; int h = b;
        while (a < b) {
            swapz = circlePass(array, a, b, 0, 0);
            a++;
            b--;
        }
        int m = l + (h - l) / 2;
        Writes.recursion();
        swapz = circleCircle(array, l, m, swapz, d);
        Writes.recursion();
        swapz = circleCircle(array, m+1, h, swapz, d);
        return swapz;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int zwaps = 0;
        do {
            zwaps = circleCircle(array, 0, currentLength-1, 0, 0);
        } while (zwaps != 0);
    }
}
