package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class UOptiSelectionSort extends Sort {
    public UOptiSelectionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Unique-Optimized Selection");
        this.setRunAllSortsName("Taihennami's Unique-Optimized Selection Sort");
        this.setRunSortName("Unique-Optimized Selection Sort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int left = 0;
        while (left < currentLength) {
            int uL = left, uR = left, uE = 1;
            for (int right = left + 1; right < currentLength; right++) {
                Highlights.markArray(3, uR);
                Highlights.markArray(4, left);
                int cmp = Reads.compareIndices(array, uL, right, 0.1, true);
                if (cmp > 0) {
                    uL = right;
                    uR = right;
                    uE = 1;
                } else if (cmp == 0) {
                    uR = right;
                    uE++;
                }
            }
            Highlights.clearAllMarks();
            if (uE == currentLength - left) break;
            if (uE <= 2) {
                if (left != uL) Writes.swap(array, left++, uL, 1, true, false);
                else left++;
                if (uE == 2) {
                    while (left < uR && Reads.compareIndices(array, left, uR, 0.1, true) == 0) left++;
                    if (left != uR) Writes.swap(array, left++, uR, 1, true, false);
                    else left++;
                }
            } else {
                if (left != uL) Writes.swap(array, left++, uL, 1, true, false);
                else left++;
                if (Reads.compareIndices(array, left, uR, 0.1, true) != 0) Writes.swap(array, left++, uR, 1, true, false);
                else left++;
                for (int inner = Math.max(uL, left); inner <= uR; inner++) {
                    if (uL > left + 1) Highlights.markArray(3, uL);
                    else Highlights.clearMark(3);
                    Highlights.markArray(4, uR);
                    if (Reads.compareIndices(array, inner, left - 1, 0.1, true) == 0) {
                        while (left < inner && Reads.compareIndices(array, left, inner, 0.1, true) == 0) left++;
                        if (inner != left) Writes.swap(array, inner, left++, 1, true, false);
                        else left++;
                    }
                }
            }
            Highlights.clearAllMarks();
        }
    }
}