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

    public void milkpass(int[] array, int start, int end, double delay) {
        int a = start;
        int b = start + ((end - start) / 2);
        if (Reads.compareIndices(array, b - 1, b, delay, true) > 0) {
            while (a < b) {
                if (Reads.compareIndices(array, a, b, delay, true) > 0) for (int i = a; i < b; i++) Writes.swap(array, i, b + (i - a), delay, true, false);
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

    public void non2n(int[] array, int start, int end, int len, double delay) {
        int a = start;
        int b = start + (len / 2);
        boolean right = true;
        boolean nothing = false;
        if (b < end) {
            insert.insertionSort(array, b, end);
            if (Reads.compareIndices(array, b - 1, b, delay, true) > 0) {
                while (a < b) {
                    if (Reads.compareIndices(array, a, b, delay, true) > 0) {
                        int last = end;
                        for (int i = a; i < b && b + (i - a) < end; i++) {
                            if (b + (i - a) < end) {
                                Writes.swap(array, i, b + (i - a), delay, true, false);
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
    
    public void milksort(int[] array, int start, int end, double delay) {
        int len = 2;
        int index = start;
        while (len < end - start) {
            index = start;
            while (index + len <= end) {
                if (len == 2) {
                    if (Reads.compareIndices(array, index, index + 1, delay, true) > 0) Writes.swap(array, index, index + 1, delay, true, false);
                } else milkpass(array, index, index + len, delay);
                index += len;
            }
            if (index != end) non2n(array, index, end, len, delay);
            len *= 2;
        }
        if (len == end - start) milkpass(array, start, end, delay);
        else non2n(array, start, end, len, delay);
    }
    
    public void milksortlen(int[] array, int start, int end, int lengths, double delay) {
        int len = lengths;
        int index = start;
        while (len < end - start) {
            index = start;
            while (index + len <= end) {
                if (len == 2) {
                    if (Reads.compareIndices(array, index, index + 1, delay, true) > 0) Writes.swap(array, index, index + 1, delay, true, false);
                } else milkpass(array, index, index + len, delay);
                index += len;
            }
            if (index != end) non2n(array, index, end, len, delay);
            len *= 2;
        }
        if (len == end - start) milkpass(array, start, end, delay);
        else non2n(array, start, end, len, delay);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        milksort(array, 0, currentLength, 1);
    }
}