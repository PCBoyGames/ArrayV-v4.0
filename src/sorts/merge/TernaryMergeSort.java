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

public class TernaryMergeSort extends Sort {
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

    public void ternaryInsert(int[] array, int a, int b) {
        for (int i = a; i < b; i++) {
            int lo = a, hi = i;
            int num = array[i];
            while (lo < hi - 1) {
                int third = (hi - lo + 1) / 3,
                    midA = lo + third,
                    midB = hi - third;
                if (Reads.compareValues(array[midA], num) > 0) {
                    hi = midA;
                } else if (Reads.compareValues(array[midB], num) < 0) {
                    lo = midB;
                } else {
                    lo = midA;
                    // hi = midB;
                }
                Highlights.markArray(2, midA);
                Highlights.markArray(3, midB);
                Delays.sleep(1);
            }
            Highlights.clearMark(2);
            Highlights.clearMark(3);
            int place = Reads.compareValues(array[lo], num) > 0 ? lo : hi;

            int j = i - 1;
            boolean change = false;

            while (j >= place) {
                Writes.write(array, j + 1, array[j], 0.05, change = true, false);
                j--;
            }
            if (change) Writes.write(array, place, num, 0.05, true, false);

            Highlights.clearAllMarks();
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
