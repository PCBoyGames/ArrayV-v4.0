package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

public final class CheatBogoSort extends BogoSorting {
    public CheatBogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Cheating Bogo");
        this.setRunAllSortsName("Cheating Bogo Sort");
        this.setRunSortName("Cheating Bogo Sort");
        this.setCategory("Bogo Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(true);
    }

    private int partition(int[] array, int lo, int hi) {
        int pivot = array[hi];
        int i = lo;

        for(int j = lo; j < hi; j++) {
            Highlights.markArray(1, j);
            if(Reads.compareValues(array[j], pivot) < 0) {
                Writes.swap(array, i, j, 1, true, false);
                i++;
            }
            Delays.sleep(1);
        }
        Writes.swap(array, i, hi, 1, true, false);
        return i;
    }

    private void quickSort(int[] array, int lo, int hi) {
        if(lo < hi) {
            int p = this.partition(array, lo, hi);
            this.quickSort(array, lo, p - 1);
            this.quickSort(array, p + 1, hi);
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int[] x = Writes.createExternalArray(length);
        int min = array[0];
        for(int i = 1; i < length; i++) {
            if(array[i] < min) min = array[i];
        }
        for(int i = 0; i < length; i++) {
			Writes.write(x, i, array[i]-min, 0, true, true);
		}
        this.quickSort(x, 0, length - 1);
        int oof = 0;
        while(oof < length)
        if (Reads.compareValues(array[oof], x[oof]) == 0){
                    oof = oof + 1;
                }
            else {
            this.bogoSwap(array, oof, length, false);
                }
                
    }
}
