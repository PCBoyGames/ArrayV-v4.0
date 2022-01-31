package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

public final class BonoSort extends BogoSorting {
    public BonoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Bono");
        this.setRunAllSortsName("Bono Sort");
        this.setRunSortName("Bonosort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(10);
        this.setBogoSort(true);
    }
    private void convergePull(int[] array, int start, int end) {
        int l = start+1, r=end;
        while(l < r) {
            int a = randInt(l, r),
                b = randInt(a, r);
            if(Reads.compareValues(array[start], array[a]) >= 0) {
                l = a;
            } else {
                l = b;
            }
            if(Reads.compareValues(array[start], array[b]) <= 0) {
                r = b;
            } else {
                r = a;
            }
        }
        Writes.multiSwap(array, start, l, 1, true, false);
    }
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        while(!this.isArraySorted(array, length))
            convergePull(array, 0, length);
    }
}
