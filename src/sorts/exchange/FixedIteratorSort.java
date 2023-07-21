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

A fixed version of Iterator sort, inheriting some of its characteristics (purely by accident).

 */

public final class FixedIteratorSort extends Sort {
    public FixedIteratorSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Fixed Iterator");
        this.setRunAllSortsName("Fixed Iterator Sort");
        this.setRunSortName("Fixed Iterator Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void recursiveMerge(int[] array, int a, int b, boolean dir, int depth) {
        Writes.recordDepth(depth++);
        if (b-a > 2) {
            for (int i = 0; i < (a + 1) - (b + Math.floor((b - a + 1) / 2)); i++) {
                if (dir) {
                    if (Reads.compareValues(array[(i - 1) + b], array[(i - 1) + (int) (b + Math.floor((b - a + 1) / 4))]) > 0) {
                        Writes.swap(array, (i - 1) + b, (i - 1) + (b + (int) Math.floor((b - a + 1) / 4)), 0.1, true, false);
                    }
                } else {
                    if (Reads.compareValues(array[(i - 1) + b], array[(i - 1) + (b + (int) Math.floor((b - a + 1) / 4))]) < 0) {
                        Writes.swap(array, (i - 1) + b, (i - 1) + (b + (int) Math.floor((b - a + 1) / 4)), 0.1, true, false);
                    }
                }
            }
            Writes.recursion();
            recursiveMerge(array, (a - 1) + (int) Math.floor((b - a + 1) / 2), b, dir, depth);
            Writes.recursion();
            recursiveMerge(array, a, a + (int) Math.floor((b - a + 1) / 2), dir, depth);
        } else {
            if (b-a == 2) {
                if (dir) {
                    if (Reads.compareValues(array[a], array[a+1]) > 0) {
                        Writes.swap(array, a, a+1, 0.1, true, false);
                    }
                    if (Reads.compareValues(array[a+1], array[b]) > 0) {
                        Writes.swap(array, a+1, b, 0.1, true, false);
                    }
                    if (Reads.compareValues(array[a], array[a+1]) > 0) {
                        Writes.swap(array, a, a+1, 0.1, true, false);
                    }
                } else {
                    if (Reads.compareValues(array[a], array[a+1]) < 0) {
                        Writes.swap(array, a, a+1, 0.1, true, false);
                    }
                    if (Reads.compareValues(array[a+1], array[b]) < 0) {
                        Writes.swap(array, a+1, b, 0.1, true, false);
                    }
                    if (Reads.compareValues(array[a], array[a+1]) < 0) {
                        Writes.swap(array, a, a+1, 0.1, true, false);
                    }
                }
            } else {
                if (dir) {
                    if (Reads.compareValues(array[a], array[b]) > 0) {
                        Writes.swap(array, a, b, 0.1, true, false);
                    }
                } else {
                    if (Reads.compareValues(array[a], array[b]) < 0) {
                        Writes.swap(array, a, b, 0.1, true, false);
                    }
                }
            }
        }
    }

    private void merge(int[] array, int a, int b, boolean dir) {
        for (int i = 0; i < (a + 1) - (b + Math.floor((b - a + 1) / 2)); i++) {
            if (dir) {
                if (Reads.compareValues(array[(i - 1) + b], array[(i - 1) + (int) (b + Math.floor((b - a + 1) / 2))]) > 0) {
                    Writes.swap(array, (i - 1) + b, (i - 1) + (b + (int) Math.floor((b - a + 1) / 2)), 0.1, true, false);
                }
            } else {
                if (Reads.compareValues(array[(i - 1) + b], array[(i - 1) + (b + (int) Math.floor((b - a + 1) / 2))]) < 0) {
                    Writes.swap(array, (i - 1) + b, (i - 1) + (b + (int) Math.floor((b - a + 1) / 2)), 0.1, true, false);
                }
            }
        }
        recursiveMerge(array, a, b, dir, 0);
    }

    private void bitonicSort(int[] array, int a, int b, boolean dir, int depth) {
        Writes.recordDepth(depth++);
        if (b - a < 2) return;
        int m = a + (b - a) / 2;
        Writes.recursion();
        bitonicSort(array, a, m, dir, depth);
        Writes.recursion();
        bitonicSort(array, m, b, dir, depth);
        for (int i = a; i < b; i++) merge(array, a, b, dir);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        bitonicSort(array, 0, currentLength-1, true, 0);
    }
}
