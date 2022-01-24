package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

public final class BogoSortingNetwork extends BogoSorting {
    public BogoSortingNetwork(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("BogoNetwork");
        this.setRunAllSortsName("BogoNetwork Sort");
        this.setRunSortName("BogoNetworksort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(true);
    }

    public void networksort(int[] array, int[] indexnetwork, int length){
        for(int i = 1; i<length; i+=2){
            pairsort(array, indexnetwork[i-1], indexnetwork[i]);
        }
    }

    public void pairsort(int[] array, int i, int j){
        if(i > j){
            int temp = i; i = j; j = temp;
        }
        if(Reads.compareIndices(array, i, j, 1, true) > 0){
            Writes.swap(array, i, j, 1, true, false);
        }
    }

    protected void bogoSwap(int[] array, int start, int end, boolean aux){
        for (int i = start; i < end; ++i)
            Writes.swap(array, i, BogoSorting.randInt(i, end), this.delay, true, aux);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int[] indexnetwork = Writes.createExternalArray(length);
        for(int i = 0; i<length; i++) indexnetwork[i] = i;
        while(!this.isArraySorted(array, length)){
            bogoSwap(indexnetwork, 0, length, true);
            networksort(array, indexnetwork, length);
        }
    }
}