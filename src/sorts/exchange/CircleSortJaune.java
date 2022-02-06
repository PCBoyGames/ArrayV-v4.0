package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class CircleSortJaune extends Sort {
    public CircleSortJaune(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Circle (Jaune)");
        this.setRunAllSortsName("Jaune Circle Sort");
        this.setRunSortName("Jaune Circlesort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected boolean circle(int[] array, int a, int b, boolean anyswaps) {
    if(a >= b) return anyswaps;
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
            } else
            left++;
            right--;
        }
        return circlex(array, a, left, anyswaps || swaphere) | circlex(array, left, b, anyswaps || swaphere);
    }

    protected boolean circlex(int[] array, int a, int b, boolean anyswaps) {
    if(a >= b) return anyswaps;
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
            } else break;
            left++;
            right--;
        }
        return circle(array, left, b-1, anyswaps || swaphere) | circlex(array, a+1, right, anyswaps || swaphere);
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
                anyswaps = circle(array, 0, currentLength-1, anyswaps);
                offset = 0;
                while (offset < currentLength) {
                    anyswaps = circlex(array, offset, Math.min(offset + (gap - 1), currentLength-1), anyswaps);
                    offset += gap;
                }
                gap--;
            }
        }
    }
}
