package sorts.hybrid;

import sorts.insert.InsertionSort;
import main.ArrayVisualizer;
import sorts.templates.Sort;

public class CrunchyCourtyardSort extends Sort {
    public CrunchyCourtyardSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Crunchy Courtyard");
        this.setRunAllSortsName("Crunchy Courtyard Sort");
        this.setRunSortName("Crunchy Courtyard Sort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    private InsertionSort smallSort;

    // *Ported from Scratch, left in its original state*


    // Basically, Crunchy Courtyard inserts if n < 24, then checks for sortedness.
    // If unsorted, it finds the ninther of its subsection. After that, it partitions LR using the
    // ninther.
    // When it hits the depth limit, it runs Weak PDMsort (only finds sorted runs, meaning you get
    // 1-size sorted subsections that can't be sorted quickly).

    private void merge(int[] array, int[] tmp, int start, int mid, int end) {
        if(mid-start <= end-mid) {
            Writes.arraycopy(array, start, tmp, 0, mid-start, 1, true, true);
            int i = 0, j = mid, t = start;
            while(i < mid-start && j < end) {
                if(Reads.compareValues(tmp[i], array[j]) <= 0) {
                    Writes.write(array, t++, tmp[i++], 1, true, false);
                } else {
                    Writes.write(array, t++, array[j++], 1, true, false);
                }
            }
            while(i < mid-start)
                Writes.write(array, t++, tmp[i++], 1, true, false);
        } else {
            Writes.arraycopy(array, mid, tmp, 0, end-mid, 1, true, true);
            int i=mid-1, j=end-mid-1, t=end-1;
            while(i>=start && j>=0) {
                if(Reads.compareValues(array[i], tmp[j]) > 0) {
                    Writes.write(array, t--, array[i--], 1, true, false);
                } else {
                    Writes.write(array, t--, tmp[j--], 1, true, false);
                }
            }
            while(j >= 0)
                Writes.write(array, t--, tmp[j--], 1, true, false);
        }
    }

    private void weakPDMsort(int[] array, int start, int end) {
        int[] runs = Writes.createExternalArray(end-start),
              buf = Writes.createExternalArray((end-start)/2);
        int r = start, rf = 0;
        while(r < end) {
            Writes.write(runs, rf++, r, 1, true, true);
            while(r < end-1 && Reads.compareValues(array[r], array[r+1]) <= 0) {
                r++;
            }
            r++;
        }
        Writes.write(runs, rf++, end, 1, true, true);
        while(rf > 1) {
            int j=0;
            for(int i=0; i<rf; i+=2, j++) {
                if(i+1 >= rf) {
                    Writes.write(runs, j, runs[i], 1, true, true);
                    continue;
                } else if(i+2 >= rf)
                    merge(array, buf, runs[i], runs[i+1], end);
                else
                    merge(array, buf, runs[i], runs[i+1], runs[i+2]);
                Writes.write(runs, j, runs[i], 1, true, true);
            }
            rf = j;
        }
        Writes.deleteExternalArrays(runs, buf);
    }

    private boolean isSorted(int[] array, int start, int end) {
        for(int i=start; i<end; i++) {
            if(Reads.compareValues(array[i], array[i+1]) == 1)
                return false;
        }
        return true;
    }

    private void sort3(int[] array, int a, int b, int c) {
        if(Reads.compareIndices(array, a, b, 1, true) > 0) {
            Writes.swap(array, a, b, 1, true, false);
        }
        if(Reads.compareIndices(array, b, c, 1, true) > 0) {
            Writes.swap(array, b, c, 1, true, false);
            if(Reads.compareIndices(array, a, b, 1, true) > 0) {
                Writes.swap(array, a, b, 1, true, false);
            }
        }
    }

    private int partition(int[] array, int start, int end) {
        int mid = start+(end-start)/2,
            eighth = (end-start)>>3;

        sort3(array, start, start+eighth, start+2*eighth);
        sort3(array, mid-eighth, mid, mid+eighth);
        sort3(array, end-2*eighth, end-eighth, end);
        sort3(array, start+eighth, mid, end-eighth);

        int piv = array[mid], i=start, j=end;

        while(i<j) {
            while(Reads.compareValues(array[i], piv) < 0) {
                Highlights.markArray(1, i);
                i++;
                Delays.sleep(1);
            }
            while(Reads.compareValues(array[j], piv) > 0) {
                Highlights.markArray(1, j);
                j--;
                Delays.sleep(1);
            }
            if(i < j) {
                Writes.swap(array, i, j, 1, true, false);
            }
        }
        return i;
    }

    private void crunchyCourtyard(int[] array, int start, int end, int depthLim) {
        if(end-start < 24) {
            smallSort.customInsertSort(array, start, end+1, 1, false);
            return;
        }
        if(depthLim == 0) {
            weakPDMsort(array, start, end+1);
            return;
        }
        if(isSorted(array, start, end))
            return;
        int p = partition(array, start, end);
        crunchyCourtyard(array, start, p-1, --depthLim);
        crunchyCourtyard(array, p, end, depthLim);
    }

    @Override
    public void runSort(int[] arr, int length, int buckets) {
        smallSort = new InsertionSort(arrayVisualizer);
        int l=1;
        while(1<<l <= length / 2) l++;
        crunchyCourtyard(arr, 0, length-1, 2*l);
    }
}