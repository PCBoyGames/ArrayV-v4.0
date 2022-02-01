package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public final class RecursivePopSort extends Sort
{
    private int[] arr;

    public RecursivePopSort(final ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Recursive Pop");
        this.setRunAllSortsName("Recursive Pop Sort");
        this.setRunSortName("Recursive Pop Sort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int compare(final int x, final int y) {
        this.Highlights.markArray(0, x);
        this.Highlights.markArray(1, y);
        this.Delays.sleep(0.02);
        return this.Reads.compareValues(this.arr[x], this.arr[y]);
    }

    private void wrapper(final int start, final int stop, final boolean dir) {
        if (stop - start >= 2) {
            this.wrapper(start, (stop - start) / 2 + start, !dir);
            this.wrapper((stop - start) / 2 + start, stop, dir);
            int right = stop - 1;
            while (true) {
                if (dir) {
                    while (right > start) {
                        if (this.compare(start, right) != -1) {
                            break;
                        }
                        --right;
                    }
                }
                else {
                    while (right > start && this.compare(start, right) == 1) {
                        --right;
                    }
                }
                if (right == start) {
                    break;
                }
                for (int i = start; i < right; ++i) {
                    this.Writes.swap(this.arr, i, i + 1, 0.02, true, false);
                }
                --right;
            }
        }
    }

    @Override
    public void runSort(final int[] array, final int length, final int bucketCount) {
        this.arr = array;
        this.wrapper(0, length, true);
    }
}
