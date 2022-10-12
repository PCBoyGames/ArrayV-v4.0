package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class FiringSquadSortRecursive extends Sort {

    boolean swaps = true;

    public FiringSquadSortRecursive(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Firing Squad (Recursive)");
        this.setRunAllSortsName("Recursive Firing Squad Sort");
        this.setRunSortName("Recursive Firing Squad Sort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void firingSquad(int[] array, int start, int end, boolean dir, int depth) {
        Writes.recordDepth(depth);
        if (dir) {
            int left = start;
            int right = start + 1;
            int lcycle = 1;
            boolean rcycle = true;
            while (left < right) {
                if (Reads.compareIndices(array, left, right, 0.1, true) > 0) Writes.swap(array, left, right, 0.1, swaps = true, false);
                lcycle++;
                if (lcycle > 2) {
                    left++;
                    lcycle = 0;
                }
                if (rcycle) {
                    right++;
                    if (right > end - 1) {
                        right--;
                        rcycle = false;
                    }
                } else right--;
            }
        } else {
            int right = end - 1;
            int left = end - 2;
            int rcycle = 1;
            boolean lcycle = true;
            while (left < right) {
                if (Reads.compareIndices(array, left, right, 0.1, true) > 0) Writes.swap(array, left, right, 0.1, swaps = true, false);
                rcycle++;
                if (rcycle > 2) {
                    right--;
                    rcycle = 0;
                }
                if (lcycle) {
                    left--;
                    if (left < start) {
                        left++;
                        lcycle = false;
                    }
                } else left++;
            }
        }
        if (end - start > 2) {
            Writes.recursion();
            firingSquad(array, start, end - (end - start) / 2, false, depth + 1);
            Writes.recursion();
            firingSquad(array, end - (end - start) / 2, end, true, depth + 1);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int len = 2;
        for (; len <= currentLength; len *= 2);
        len /= 2;
        boolean lencurlendir = false;
        while (swaps) {
            swaps = false;
            if (len == currentLength) firingSquad(array, 0, currentLength, lencurlendir = !lencurlendir, 0);
            else {
                firingSquad(array, 0, len, true, 0);
                if (currentLength - len >= len / 2) firingSquad(array, (currentLength - len) / 2, (currentLength + len) / 2, true, 0);
                firingSquad(array, currentLength - len, currentLength, false, 0);
            }
        }
    }
}