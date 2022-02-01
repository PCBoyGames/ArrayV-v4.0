package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.Sort;


final public class InverseInsertionSort extends Sort {
    public InverseInsertionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Inverse Insertion");
        this.setRunAllSortsName("Inverse Insertion Sort");
        this.setRunSortName("Inverse Insertsort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void customInverseInsert(int[] array, int start, int end, double sleep, boolean aux) {
        for(int i=start+1; i<end; i++) {
            int j = end-1, t = array[j];
            while(j >= i || (j >= start && Reads.compareValues(array[j], t) >= 0)) {
                if(j-1 >= start)
                    Writes.write(array, j, array[j-1], sleep, true, aux);
                j--;
            }
            Writes.write(array, j+1, t, sleep, true, aux);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.customInverseInsert(array, 0, currentLength, 0.015, false);
    }
}