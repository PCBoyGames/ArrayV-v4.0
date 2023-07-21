package sorts.merge;

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

I've been on fire lately.

 */

public final class TernaryMergeSort extends Sort {
    public TernaryMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Ternary Merge");
        this.setRunAllSortsName("Ternary Merge Sort");
        this.setRunSortName("Ternary Mergesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public int ternary(int[] array, int start, int end, int key, double sleep) {
        while (start < end-1) {
            int third = (end - start + 1) / 3,
                midA = start + third,
                midB = end - third;
            if (Reads.compareValues(array[midA], key) == 1) {
                end = midA;
            } else if (Reads.compareValues(array[midB], key) == -1) {
                start = midB;
            } else {
                start = midA;
                end = midB;
            }
            Highlights.markArray(2, midA);
            Highlights.markArray(3, midB);
            Delays.sleep(sleep);
        }
        Highlights.clearMark(2);
        Highlights.clearMark(3);
        return Reads.compareValues(array[start], key) == 1 ? start : end;
    }

    private void ternaryInsert(int[] array, int a, int b) {
        for (int i = a+1; i < b; i++) {
            Writes.insert(array, i, ternary(array, a, i, array[i], 1), 0.05, true, false);
        }
    }

    private void merge(int[] array, int[] tmp, int start, int mid, int end) {
        if (start == mid) return;

        if (end - start < 32) {
            return;
        }
        else if (end - start < 64) {
            ternaryInsert(array, start, end);
        }
        else {
            merge(array, tmp, start, (mid+start)/2, mid);
            merge(array, tmp, mid, (mid+end)/2, end);

            int low = start;
            int high = mid;

            for (int nxt = 0; nxt < end - start; nxt++) {
                if (low >= mid && high >= end) break;

                Highlights.markArray(1, low);
                Highlights.markArray(2, high);

                if (low < mid && high >= end) {
                    Highlights.clearMark(2);
                    Writes.write(tmp, nxt, array[low++], 1, false, true);
                }
                else if (low >= mid && high < end) {
                    Highlights.clearMark(1);
                    Writes.write(tmp, nxt, array[high++], 1, false, true);
                }
                else if (Reads.compareValues(array[low], array[high]) <= 0) {
                    Writes.write(tmp, nxt, array[low++], 1, false, true);
                }
                else{
                    Writes.write(tmp, nxt, array[high++], 1, false, true);
                }
            }
            Highlights.clearMark(2);

            for (int i = 0; i < end - start; i++) {
                Writes.write(array, start + i, tmp[i], 1, true, false);
            }
        }
    }

    protected void mergeSort(int[] array, int length) {
        if (length < 32) {
            ternaryInsert(array, 0, length);
            return;
        }

        int[] tmp = Writes.createExternalArray(length);

        int start = 0;
        int end = length;
        int mid = start + ((end - start) / 2);

        merge(array, tmp, start, mid, end);

        Writes.deleteExternalArray(tmp);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        mergeSort(array, currentLength);
    }
}
