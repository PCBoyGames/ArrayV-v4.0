package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

public final class ExchangeGoroSort extends BogoSorting {

    public ExchangeGoroSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Exchange Goro");
        this.setRunAllSortsName("Exchange Goro Sort");
        this.setRunSortName("Exchange Gorosort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    public void compSwap(int[] array, int a, int b, double pause, boolean mark, boolean aux) {
        if (a > b) {
            int t = a;
            a = b;
            b = t;
        }
        if (Reads.compareIndices(array, a, b, pause, mark) > 0)
            Writes.swap(array, a, b, pause, mark, aux);
    }
    
    void goroSwap(int[] array, int[] indices, int a, int len) {
        for (int i = 0; i < len; i++) Writes.visualClear(indices, i);
        int cnt = 0;
        for (int i = 0; i < len; i++)
            if (BogoSorting.randBoolean())
                Writes.write(indices, cnt++, i, delay, false, true);
        if (cnt < 2) {
            int i1 = BogoSorting.randInt(0, len);
            int i2 = BogoSorting.randInt(0, len);
            compSwap(array, a + i1, a + i2, delay, true, false);
        } else {
            for (int i = 0; i < cnt; i++) {
                compSwap(array, a + indices[i], a + indices[BogoSorting.randInt(i, cnt)], delay, true, false);
            }
        }
    }
    
    public void sort(int[] array, int a, int b) {
        int sortLength = b - a;
        int[] indices = Writes.createExternalArray(sortLength);
        while (!isRangeSorted(array, a, b, false, true))
            goroSwap(array, indices, a, sortLength);
        Writes.deleteExternalArray(indices);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);

    }

}
