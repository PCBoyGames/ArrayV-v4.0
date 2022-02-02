package sorts.merge;

import main.ArrayVisualizer;
import sorts.insert.BlockInsertionSortNeon;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class IterativeMilkSort extends Sort {

    BlockInsertionSortNeon insert = new BlockInsertionSortNeon(arrayVisualizer);

    public IterativeMilkSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Iterative Milk");
        this.setRunAllSortsName("Iterative Milk Sort");
        this.setRunSortName("Iterative Milksort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void milk(int[] array, int start, int end) {
        int a = start;
        int b = start + ((end - start) / 2);
        if (Reads.compareIndices(array, b - 1, b, 1, true) > 0) {
            while (a < b) {
                if (Reads.compareIndices(array, a, b, 1, true) > 0) for (int i = a; i < b; i++) Writes.swap(array, i, b + (i - a), 1, true, false);
                a++;
            }
            insert.insertionSort(array, b, end);
        }
    }

    protected boolean non2ninsert(int[] array, int start, int end) {
        // This is cheap. Like, really cheap. You have no idea just how cheap this is.
        // It mimics Neon Blocksert *somehow* returning true if nothing was changed.
        // I couldn't be bothered to make a version of Neon Blocksert that can do that.
        // If I could, I would, so what'd I do instead? I cheated it! Sorry, not sorry!
        // Don't worry, this does the exact same thing in this context.
        int last = array[end - 1];
        insert.insertionSort(array, start, end);
        return last == array[end - 1];
    }

    protected void non2n(int[] array, int start, int end, int len) {
        int a = start;
        int b = start + (len / 2);
        boolean right = true;
        boolean nothing = false;
        if (b < end) {
            if (Reads.compareIndices(array, b - 1, b, 1, true) > 0) {
                while (a < b) {
                    if (Reads.compareIndices(array, a, b, 1, true) > 0) {
                        int last = end;
                        for (int i = a; i < b && b + (i - a) < end; i++) {
                            if (b + (i - a) < end) {
                                Writes.swap(array, i, b + (i - a), 1, true, false);
                                last = b + (i - a) + 1;
                            }
                        }
                        right = !right;
                        if (last == end && right && !nothing) nothing = non2ninsert(array, b, last);
                    }
                    a++;
                }
                insert.insertionSort(array, b, end);
            }
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int len = 2;
        int index = 0;
        while (len < currentLength) {
            index = 0;
            while (index + len <= currentLength) {
                if (len == 2) {
                    if (Reads.compareIndices(array, index, index + 1, 1, true) > 0) Writes.swap(array, index, index + 1, 1, true, false);
                } else milk(array, index, index + len);
                index += len;
            }
            if (index != currentLength) non2n(array, index, currentLength, len);
            len *= 2;
        }
        if (len == currentLength) milk(array, 0, currentLength);
        else non2n(array, 0, currentLength, len);
    }
}