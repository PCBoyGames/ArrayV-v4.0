package sorts.quick;

import java.util.PriorityQueue;

import main.ArrayVisualizer;
import sorts.hybrid.MiepurgeSort;
import sorts.insert.BinaryDoubleInsertionSort;
import sorts.insert.InsertionSort;
import sorts.templates.Sort;

final public class HeadQuicksort extends Sort {
    public HeadQuicksort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        arrayVisualizer.getTimer();
        this.setSortListName("Head-Quick");
        this.setRunAllSortsName("Head-Quick Sort");
        this.setRunSortName("Head-Quicksort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    private InsertionSort insert;
    private class Head implements Comparable<Head> {
        private int start, end;
        public int depth;
        public Head(int start, int end, int depth) {
            this.start = start;
            this.end = end;
            this.depth = depth;
        }

        public int length() {
            return this.end-this.start;
        }

        public int branchlessCompare(int a, int b) {
            return ((a-b)>>31)|-((b-a)>>31);
        }

        @Override
        public int compareTo(Head head) {
            return branchlessCompare(length(),head.length());
        }
    }

    public void merge(int[] array, int[] tmp, int start, int mid, int end) {
        if(start >= mid) return;

        merge(array, tmp, start, (mid+start)/2, mid);
        merge(array, tmp, mid, (mid+end)/2, end);

        if(Reads.compareValues(array[mid-1], array[mid]) <= 0) {
            return;
        }

        Writes.arraycopy(array, start, tmp, 0, mid-start, 1, true, true);
        int low = 0, high = mid, nxt = start;

        while(low < mid-start && high < end) {
            if(Reads.compareValues(tmp[low], array[high]) <= 0) {
                Writes.write(array, nxt++, tmp[low++], 1, true, false);
            } else {
                Writes.write(array, nxt++, array[high++], 1, true, false);
            }
        }
        while(low < mid-start) {
            Writes.write(array, nxt++, tmp[low++], 1, true, false);
        }
    }

    private int[] med15Swaps = new int[] {
        1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
        1, 3, 5, 7, 9, 11, 13, 15,
        2, 4, 6, 8, 10, 12,
        1, 5, 9, 13, 2, 6, 10, 14, 3, 7, 11, 15, 4, 8,
        1, 9, 2, 10, 3, 11, 4, 12, 5, 13, 6, 14, 7, 15,
        6, 11, 7, 10, 4, 13, 14, 15, 8, 12, 2, 3, 5, 9,
        2, 5, 8, 14, 3, 9, 12, 15, 6, 7, 10, 11, 3, 5,
        12, 14, 4, 9, 8, 13, 7, 9, 11, 13, 4, 6, 8, 10,
        4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 7, 8, 9, 10
    };
    private void fifteen(int[] A, int c) {
        for(int i=0; i<med15Swaps.length; i+=2) {
            int a = c - 16 + med15Swaps[i],
                b = c - 16 + med15Swaps[i+1];
            if(Reads.compareValues(A[a], A[b]) == 1) {
                Writes.swap(A, a, b, 10, true, false);
            }
        }
    }

    private void quickSort(int[] a, PriorityQueue<Head> q, int p, int r, int d) {
        int x;
        if(r-p > 128 && d < Math.log1p(r-p) / Math.log(6)) {
            int e = p + (r - p + 15) / 2;
            this.fifteen(a, e);
            x = a[e-7];
        } else if(r-p < 16) {
            this.insert.customInsertSort(a, p, r+1, 0.25, false);
            return;
        } else {
            long m = a[p];
            for(int i=p+1; i<=r; i++) {
                m += a[i];
            }
            x = (int) (m / (long) (r-p+1));
        }

        int i = p, j = r;

        while (i <= j) {
            while (i <= j && Reads.compareValues(a[i], x) == -1){
                i++;
                Highlights.markArray(1, i);
                Delays.sleep(0.5);
            }
            while (i <= j && Reads.compareValues(a[j], x) == 1){
                j--;
                Highlights.markArray(2, j);
                Delays.sleep(0.5);
            }

            if (i <= j) {
                Writes.swap(a, i++, j--, 1, true, false);
            }
        }

        q.offer(new Head(p, j, d));
        q.offer(new Head(i, r, d));
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        PriorityQueue<Head> q = new PriorityQueue<>();
        int sqrt = (int) Math.sqrt(currentLength), cbrt = (int) Math.cbrt(currentLength);
        int lg = (int) (Math.log(currentLength) / Math.log(6));
        this.insert = new InsertionSort(arrayVisualizer);
        int[] tmp = Writes.createExternalArray(sqrt);
        this.quickSort(array, q, 0, currentLength - 1, 0);
        MiepurgeSort mp = new MiepurgeSort(arrayVisualizer);
        mp.smallSort = new BinaryDoubleInsertionSort(arrayVisualizer);
        mp.threshold = cbrt;
        while(q.size() > 0) {
            Head poll = q.poll();
            if(poll.length() == 0)
                continue;
            if(poll.length() < 2*sqrt) {
                this.merge(array, tmp, poll.start, poll.start + (poll.end - poll.start + 1) / 2, poll.end + 1);
            } else if(poll.depth >= cbrt) {
                Writes.deleteExternalArray(tmp);
                mp.miepurge(array, poll.start, poll.end + 1, lg);
                tmp = Writes.createExternalArray(sqrt);
            } else {
                this.quickSort(array, q, poll.start, poll.end, poll.depth + 1);
            }
        }
        Writes.deleteExternalArray(tmp);
    }
}