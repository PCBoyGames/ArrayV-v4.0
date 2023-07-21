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

HELL

 */

public final class TripleTrialSort extends Sort {
    public TripleTrialSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Triple Trial");
        this.setRunAllSortsName("Triple Trial Sort");
        this.setRunSortName("Triple Trial Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(2);
        this.setBogoSort(false);
    }

    private void compareReversal(int O, int[] array, int a, int b, boolean dir, double delay, int depth) {
        if (a >= b) return;
        Writes.recordDepth(depth++);
        if (O == 0) {
            if (dir) {
                do {
                    for (int i = 0; i < arrayVisualizer.getCurrentLength(); i++) {
                        Writes.reversal(array, a, b, delay, true, false);
                    }
                } while (Reads.compareValues(array[a], array[b]) > 0);
                Writes.recursion();
                compareReversal(O, array, a+1, b-1, dir, delay, depth);
            } else {
                do {
                    for (int i = 0; i < arrayVisualizer.getCurrentLength(); i++) {
                        Writes.reversal(array, a, b, delay, true, false);
                    }
                } while (Reads.compareValues(array[a], array[b]) < 0);
                Writes.recursion();
                compareReversal(O, array, a+1, b-1, dir, delay, depth);
            }
        } else {
            if (dir) {
                do {
                    for (int i = 0; i < arrayVisualizer.getCurrentLength(); i++) {
                        Writes.recursion();
                        compareReversal(O - 1, array, a, b, dir, delay, depth);
                    }
                } while (Reads.compareValues(array[a], array[b]) > 0);
                Writes.recursion();
                compareReversal(O, array, a+1, b-1, dir, delay, depth);
            } else {
                do {
                    for (int i = 0; i < arrayVisualizer.getCurrentLength(); i++) {
                        Writes.recursion();
                        compareReversal(O - 1, array, a, b, dir, delay, depth);
                    }
                } while (Reads.compareValues(array[a], array[b]) < 0);
                Writes.recursion();
                compareReversal(O, array, a+1, b-1, dir, delay, depth);
            }
            
        }
    }

    private void triple(int[] array, int a, int b, boolean dir, int depth) {
        if (a >= b) return;
        Writes.recordDepth(depth++);
        if (Reads.compareValues(array[a], array[b]) > 0) {
            do {
                for (int i = 0; i < arrayVisualizer.getCurrentLength(); i++) {
                    compareReversal(arrayVisualizer.getCurrentLength(), array, a, b, true, 0.005, 0);
                    Writes.recursion();
                    triple(array, a+1, b, dir, depth);
                    Writes.recursion();
                    triple(array, a+1, b-1, dir, depth);
                    Writes.recursion();
                    triple(array, a, b-1, dir, depth);
                    Writes.recursion();
                    triple(array, a+1, b-1, dir, depth);
                }
            } while (Reads.compareValues(array[a], array[b]) > 0);
        } else if (Reads.compareValues(array[a], array[b]) < 0) {
            do {
                for (int i = 0; i < arrayVisualizer.getCurrentLength(); i++) {
                    compareReversal(arrayVisualizer.getCurrentLength(), array, a, b, false, 0.005, 0);
                    Writes.recursion();
                    triple(array, a+1, b, dir, depth);
                    Writes.recursion();
                    triple(array, a+1, b-1, dir, depth);
                    Writes.recursion();
                    triple(array, a, b-1, dir, depth);
                    Writes.recursion();
                    triple(array, a+1, b-1, dir, depth);
                }
            } while (Reads.compareValues(array[a], array[b]) > 0);
        }
        Writes.recursion();
        triple(array, a, b, dir, depth);
        trial(array, a, b);
    }

    private void trial(int[] array, int a, int b) {
        if (a >= b) return;
        triple(array, a, b, false, 0);
        triple(array, a, b, true, 0);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        trial(array, 0, currentLength-1);
    }
}
