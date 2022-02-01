package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class CircleSortRouge extends Sort {
    public CircleSortRouge(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Circle (Rouge)");
        this.setRunAllSortsName("Rouge Circle Sort");
        this.setRunSortName("Rouge Circlesort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected boolean circle(int[] array, int a, int b, boolean anyswaps) {
        int left = a;
        int right = b;
        boolean swaphere = false;
        while (left < right) {
            Highlights.markArray(1, left);
            Highlights.markArray(2, right);
            Delays.sleep(0.01);
            if (Reads.compareValues(array[left], array[right]) > 0) {
                Writes.swap(array, left, right, 0.25, true, false);
                swaphere = true;
            }
            left++;
            right--;
        }
        if (anyswaps) return anyswaps;
        else return swaphere;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int offset = 0;
        int gap = currentLength;
        boolean anyswaps = true;
        while (anyswaps) {
            anyswaps = false;
            gap = currentLength;
            while (gap > 0) {
                offset = 0;
                while (offset + (gap - 1) < currentLength) {
                    anyswaps = circle(array, offset, offset + (gap - 1), anyswaps);
                    offset += gap;
                }
                gap--;
            }
        }
    }
}