package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

public final class GoroSort extends BogoSorting {

    public GoroSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Goro");
        this.setRunAllSortsName("Goro Sort");
        this.setRunSortName("Gorosort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(16);
        this.setBogoSort(true);
    }
    
    public void swap(int[] array, int a, int b, double pause, boolean mark, boolean aux) {
        if (a != b) Writes.swap(array, a, b, pause, mark, aux);
    }
    
    void goroSwap(int[] array, int[] indices, int len) {
        for (int i = 0; i < len; i++) Writes.visualClear(indices, i);
        int cnt = 0;
        for (int i = 0; i < len; i++)
            if (BogoSorting.randBoolean())
                Writes.write(indices, cnt++, i, delay, false, true);
        if (cnt < 2)
            swap(array, BogoSorting.randInt(0, len), BogoSorting.randInt(0, len), this.delay, true, false);
        else {
            for (int i = 0; i < cnt; i++) {
                swap(array, indices[i], indices[BogoSorting.randInt(i, cnt)], delay, true, false);
            }
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        int[] indices = Writes.createExternalArray(sortLength);
        while (!isArraySorted(array, sortLength))
            goroSwap(array, indices, sortLength);
        Writes.deleteExternalArray(indices);

    }

}
