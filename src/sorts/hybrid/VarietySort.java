package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import sorts.insert.InsertionSort;
import sorts.exchange.LRQuickSort;
import java.util.Random;


final public class VarietySort extends Sort {
    public VarietySort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Variety");
        this.setRunAllSortsName("Variety Sort");
        this.setRunSortName("Variety Sort");
        this.setCategory("Esoteric Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(40);
        this.setBogoSort(true);
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

    private int bubbleHalf(int[] array, int start, int end) {
        int mid = start + (end - start) / 2;
        for(int i=end-1; i>=mid; i--) {
            for(int j=start; j<i; j++) {
                if(Reads.compareIndices(array, j, j+1, 0.01, true) > 0) {
                    Writes.swap(array, j, j+1, 0.1, true, false);
                }
            }
        }
        return mid;
    }

    private void shuffle(int[] array, int start, int end) {
        Random r = new Random();
        for(int i=start; i<end; i++) {
            Writes.swap(array, i, i+r.nextInt(end-i), 0.1, true, false);
        }
    }
    private boolean sorted(int[] array, int start, int end) {
        for(int i=start; i<end-1; i++) {
            if(Reads.compareValues(array[i], array[i+1]) == 1)
                return false;
        }
        return true;
    }
    private void bogo(int[] array, int start, int end) {
        do shuffle(array, start, end); while(!sorted(array, start, end));
    }
    public void variety(int[] array, int start, int end) {
        int mid = start+(end-start)/2,
            himid = mid+(end-mid) / 2;
        if(mid==start) {
            bogo(array, start, end);
            return;
        }
        int d = bubbleHalf(array, start, mid);
        InsertionSort i = new InsertionSort(arrayVisualizer);
        i.customInsertSort(array, start, d, 0.1, false);
        bogo(array, mid, himid);
        LRQuickSort lr = new LRQuickSort(arrayVisualizer);
        lr.quickSort(array, himid, end-1, 0);
        int[] tmp = Writes.createExternalArray(end-start);
        classicMerge(array, tmp, mid, himid, end);
        classicMerge(array, tmp, start, mid, end);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        variety(array, 0, currentLength);
    }
}