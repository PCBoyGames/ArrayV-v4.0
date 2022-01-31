package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

public final class FordSort extends BogoSorting {

    public FordSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        setSortListName("Ford");
        setRunAllSortsName("Ford Sort");
        setRunSortName("Ford Sort");
        setCategory("Exchange Sorts");
        setComparisonBased(true);
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(true);
        setUnreasonableLimit(8192);
        setBogoSort(false);

    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int q=0;
        while(!isArraySorted(array, length)) {
            for(int k=length/2; k>0; k--) {
                for(int i=q; i<length; i+=2*k) {
                    int l = i;
                    boolean change = false;
                    while(l > 0 && Reads.compareValues(array[l], array[l-1]) == -1) {
                        Writes.swap(array,l,l-1,1,true,false);
                        l-=2;
                        if(i>0) i--;
                        change = true;
                    }
                    if(change)
                        i++;
                    else
                        i--;
                }
            }
            q=(q+1)&1;
        }
    }

}
