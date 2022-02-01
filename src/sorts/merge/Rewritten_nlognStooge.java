package sorts.merge;

import main.ArrayVisualizer;
import sorts.insert.InsertionSort;
import sorts.templates.MergeSorting;

/**
 * version of nlognStoogesort, optimized and fixed for non-powers of three
 * (I never looked at the code that Control had sent, so this was all made off of assumptions)
 * @credit2 Lord Control (original impl)
 * @see https://www.youtube.com/watch?v=-htVEpAzsVM
 **/

final public class Rewritten_nlognStooge extends MergeSorting {
    public Rewritten_nlognStooge(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("C/D nlognStooge");
        this.setRunAllSortsName("nlognStooge+");
        this.setRunSortName("Distray's Rewritten Control's nlognStooge");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void leftMerge(int[] array, int[] tmp, int start, int mid, int end) {
        if(start == mid) return;

        Writes.arraycopy(array, start, tmp, 0, mid-start, 0.5, true, true);

        int low = 0, high = mid, nxt = start;

        while(low < mid-start && high < end) {
            Highlights.markArray(1, low+start);
            Highlights.markArray(2, high);
            if(Reads.compareValues(tmp[low], array[high]) <= 0){
                Writes.write(array, nxt++, tmp[low++], 1, false, false);
            } else {
                Writes.write(array, nxt++, array[high++], 1, false, false);
            }
        }

        while(low < mid-start) {
            Highlights.markArray(1, low+start);
            Writes.write(array, nxt++, tmp[low++], 1, false, false);
        }

        Highlights.clearMark(1);
        Highlights.clearMark(2);
    }

    private int binsearch(int[] array, int start, int end, int key, double sleep) {
        while(start < end) {
            int mid = start + ((end - start) / 2);
            if(Reads.compareValues(array[mid], key) == -1) {
                start = mid + 1;
            } else {
                end = mid;
            }
            Highlights.markArray(1, mid);
            Delays.sleep(sleep);
        }
        return start;
    }

    private void sort3(int[] array, int a, int b, int c) {
        if(Reads.compareIndices(array, a, b, 0.1, false) == 1) {
            Writes.swap(array, a, b, 0.1, true, false);
        }
        if(Reads.compareIndices(array, b, c, 0.1, false) == 1) {
            Writes.swap(array, b, c, 0.1, true, false);
            if(Reads.compareIndices(array, a, b, 0.1, false) == 1) { // cheeky optimization
                Writes.swap(array, a, b, 0.1, true, false);
            }
        }
    }
    private void NLNStooge(int[] array, int[] tmp, int start, int end) {
        if(start == end)
            return;

        int third = (end-start+1) / 3;

        if(third == 0) {
            if(end-start != 2) {
                InsertionSort i = new InsertionSort(arrayVisualizer);
                i.customInsertSort(array, start, end, 0.5, false);
            }
            return;
        } else if(end-start == 2) {
            this.sort3(array, start, start+1, start+2);
            return;
        }

        int midA = start+third, midB = end-third;

        this.NLNStooge(array, tmp, start, midA);
        this.NLNStooge(array, tmp, midA,  midB);
        this.NLNStooge(array, tmp, midB,  end);

        this.leftMerge(array, tmp, start, midA, midB);
        this.leftMerge(array, tmp, midA, midB, end);
        int bin = start;
        if(end-start >= 191) {
            bin = this.binsearch(array, start, midA, array[midA], 0.25);
        }
        this.leftMerge(array, tmp, bin, midA, end);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int[] t = Writes.createExternalArray((length + 1) / 3); // less space means less pain
        this.NLNStooge(array, t, 0, length);
        Writes.deleteExternalArray(t);
    }
}