package sorts.concurrent;

import sorts.templates.Sort;
import main.ArrayVisualizer;

final public class XORSort extends Sort {
    public XORSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("XOR");
        this.setRunAllSortsName("XOR Sorting Network");
        this.setRunSortName("XOR Sort");
        this.setCategory("Concurrent Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    int end;
    
    void compSwap(int[] array, int a, int b) {
        if(b < end && Reads.compareIndices(array, a, b, 0.5, true) == -1)
            Writes.swap(array, a, b, 0.5, true, false);
    }

    @Override
    public void runSort(int[] array, int size, int bucketCount) {
        end = size;
        for(int k=size-1; k>0; k--) {
            for(int l=1;l<=k;l++)
                for(int i=0;i<size;
                i++) {this.compSwap
                (array,i,i^(k%(l+1)
                ));
            }
        }
    }
}