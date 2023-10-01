package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class IterativeSelectionMergeSort extends Sort {
    public IterativeSelectionMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Iterative Selection Merge");
        this.setRunAllSortsName("Iterative Selection Merge Sort");
        this.setRunSortName("Iterative Selection Mergesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void selection(int[] array, int start, int end) {
        if (Reads.compareIndices(array, (end - start) / 2 + start - 1, (end - start) / 2 + start, 0.25, true) > 0) {
            for (int i = start, last = (end - start) / 2 + start + 1, orig = last - 1; i < end - 1; i++) {
                if (orig == i) orig++;
                int min = i;
                for (int j = orig; j < last; j++) if (Reads.compareIndices(array, min, j, 0.25, true) == 1) min = j;
                if (min != i) Writes.swap(array, i, min, 1, true, false);
                if (min == last - 1 && last < end) last++;
            }
        }
    }

    protected void non2n(int[] array, int start, int end) {
        int last = start + 2;
        while (Reads.compareIndices(array, last - 2, last - 1, 0.25, true) <= 0 && last < end) last++;
        if (last - 1 < end) {
            for (int i = start, orig = last - 1; i < end - 1; i++) {
                if (orig == i) orig++;
                int min = i;
                for (int j = orig; j < last; j++) if (Reads.compareIndices(array, min, j, 0.25, true) == 1) min = j;
                if (min != i) Writes.swap(array, i, min, 1, true, false);
                if (min == last - 1 && last < end) last++;
            }
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int len = 2;
        while (len < currentLength) {
            int index = 0;
            for (; index + len <= currentLength; index += len) {
                if (len == 2) {if (Reads.compareIndices(array, index, index + 1, 0.25, true) > 0) Writes.swap(array, index, index + 1, 1, true, false);}
                else selection(array, index, index + len);
            }
            if (index != currentLength) non2n(array, index, currentLength);
            len *= 2;
        }
        if (len == currentLength) selection(array, 0, currentLength);
        else non2n(array, 0, currentLength);
    }
}