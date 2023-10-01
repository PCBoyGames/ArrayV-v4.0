package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

And it's ternary, too! How about that, aphi?

*/
public class ScrambleQuickSort extends BogoSorting {
    public ScrambleQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Scramble Quick");
        this.setRunAllSortsName("Scramble Quick Sort");
        this.setRunSortName("Scramble Quicksort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected int[] partition(int[] array, int start, int end) {
        int pivot = array[start];
        int left = start;
        int right = end;
        while (true) {
            while (left < right && Reads.compareValues(array[left], pivot) < 0) left++;
            while (left < right && Reads.compareValues(array[right], pivot) > 0) right--;
            boolean breakout = false;
            for (int i = left; i < right && !breakout; i++) {
                if (Reads.compareIndices(array, i, i + 1, 0.1, true) == 0) {
                    if (i + 1 == right) {
                        int[] result = {left, right};
                        return result;
                    }
                } else breakout = true;
            }
            if (left < right) bogoSwap(array, left, right + 1, false);
            else {
                int[] result = {left, right};
                return result;
            }
        }
    }

    protected void scrambleQuick(int[] array, int start, int end, int depth) {
        if (end - start <= 0) return;
        if (isRangeSorted(array, start, end + 1)) return;
        Writes.recordDepth(depth);
        int[] pivotpoint = partition(array, start, end);
        if (pivotpoint[0] > start) {
            Writes.recursion();
            scrambleQuick(array, start, pivotpoint[0], depth + 1);
        }
        if (pivotpoint[1] + 1 < end) {
            Writes.recursion();
            scrambleQuick(array, pivotpoint[1] + 1, end, depth + 1);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        delay = 0.1;
        scrambleQuick(array, 0, currentLength - 1, 0);
    }
}