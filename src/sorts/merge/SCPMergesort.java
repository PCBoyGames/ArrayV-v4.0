package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;


final public class SCPMergesort extends Sort {
    public SCPMergesort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("SCP-??? Merge");
        this.setRunAllSortsName("SCP Merge Sort");
        this.setRunSortName("SCP-9139 Mergesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    public boolean isPrime(long num) {
        return ((num%2)!=0 && (num%3)!=0 && (num%5)!=0 && (num%7)!=0) ||
               (num==2||num==3||num==5||num==7);
    }
    public long prime(long loc) {
        long primeHits = 0;
        long n = 1;
        while(true) {
            if(primeHits >= loc) {
                if(!isPrime(n))
                    n--;
                return n;
            }
            n++;
            if(isPrime(n))
                primeHits++;
        }
    }


    private void classicMerge(int[] array, int[] tmp, int start, int mid, int end) {
        if(start == mid) return;

        int low = start, high = mid, nxt = 0;

        while(low < mid && high < end) {
            Highlights.markArray(1, low);
            Highlights.markArray(2, high);
            if(Reads.compareValues(array[low], array[high]) <= 0){
                Writes.write(tmp, nxt++, array[low++], 1, false, true);
            } else {
                Writes.write(tmp, nxt++, array[high++], 1, false, true);
            }
        }

        while(low < mid) {
            Writes.write(tmp, nxt++, array[low++], 1, false, true);
        }

        Highlights.clearMark(2);

        Writes.arraycopy(tmp, 0, array, start, nxt, 1, true, false);
    }

    private void scpMerge(int[] array, int[] tmp, int start, int end) {
        if(start == end)
            return;
        int m = start + (int) (prime(end)%(end-start));
        this.scpMerge(array, tmp, start, m);
        if(m == start)
            m++;
        this.scpMerge(array, tmp, m, end);
        this.classicMerge(array, tmp, start, m, end);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int[] tmp = Writes.createExternalArray(length);
        this.scpMerge(array, tmp, 0, length);
    }
}