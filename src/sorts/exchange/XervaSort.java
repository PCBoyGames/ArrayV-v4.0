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

There's good reason for my guzzling skin, and how I shine,
and how my pores are so clean and clear.
I eat Xervasort. It keeps me young.
It keeps me light on my feet.
I spring from activity to activity.
I love my job.
I love my life.
When you eat Xervasort,
you'll wink and nod and hug and high five each other with great enthusiasm.
This is a special time.

 */

public class XervaSort extends Sort {
    public XervaSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Xerva");
        this.setRunAllSortsName("Xerva Sort");
        this.setRunSortName("Xervasort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(15);
        this.setBogoSort(false);
    }

    private void compareReversal(int[] array, int a, int b, double delay, int depth) {
        if (a >= b) return;
        Writes.recordDepth(depth);
        do {
            Writes.reversal(array, a, b, delay, true, false);
        } while (Reads.compareValues(array[a], array[b]) > 0);
        Writes.recursion();
        compareReversal(array, a+1, b-1, delay, depth+1);
    }

    public void xerva(int[] array, int a, int b, int depth) {
        if (a >= b) return;
        Writes.recordDepth(depth);
        compareReversal(array, a, b, 0.005, 0);
        Writes.recursion();
        xerva(array, a+1, b-1, depth+1);
        Writes.recursion();
        xerva(array, a, b-1, depth+1);
        Writes.recursion();
        xerva(array, a+1, b, depth+1);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        xerva(array, 0, currentLength, 0);
    }
}
