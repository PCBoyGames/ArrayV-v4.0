package sorts.select;

import java.math.BigInteger;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class OptimizedTableSelectionSort extends Sort {

    public OptimizedTableSelectionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Optimized Table Selection");
        this.setRunAllSortsName("Optimized Table Selection Sort");
        this.setRunSortName("Opti-Table Selectsort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private BigInteger bitlist;

    private boolean bitIsSet(BigInteger b, int loc) {
        return b.testBit(loc);
    }
    private void setBit(int loc) {
        bitlist = bitlist.setBit(loc);
    }

    private void swapIndex(int[] array, int[] idx, int a, int b) {
        this.bitlist = BigInteger.ZERO;
        while(a < b) {
            Highlights.markArray(2, a);

            if(!bitIsSet(bitlist, a) && Reads.compareOriginalValues(a, idx[a]) != 0) {
                int t = array[a];
                int i = a, nxt = idx[a];

                do {
                    Writes.write(array, i, array[nxt], 0, true, false);
                    Writes.write(idx, i, i, 0.5, false, true);
                    setBit(i);
                    i = nxt;
                    nxt = idx[nxt];
                }
                while(Reads.compareOriginalValues(nxt, a) != 0);

                Writes.write(array, i, t, 0, true, false);
                Writes.write(idx, i, i, 0.5, false, true);
            }
            a++;
        }
    }

    public void sort(int[] array, int sortLength, int bucketCount) {
        int[] cycles = Writes.createExternalArray(sortLength);
        this.bitlist = BigInteger.ZERO;
        for(int i=0, now = 0; now<sortLength-1; now++) {
            if(i >= sortLength)
                break;
            int min = i;
            for(int j=i+1; j<sortLength; j++) {
                while(j < sortLength && bitIsSet(bitlist, j))
                    j++;
                if(j < sortLength && Reads.compareValues(array[j], array[min]) == -1) {
                    Highlights.markArray(1, min);
                    Highlights.markArray(2, j);
                    min = j;
                    Delays.sleep(0.01);
                }
            }
            setBit(min);
            if(min == i) {
                while(i < sortLength && bitIsSet(bitlist, i)) {
                    i++;
                }
            }
            Writes.write(cycles, now, min, 1, true, true);
        }
        int max = 0;
        while(max < sortLength && bitIsSet(bitlist, max))
            max++;
        if(max < sortLength)
            Writes.write(cycles, sortLength-1, max, 1, true, true);

        this.swapIndex(array, cycles, 0, sortLength);
    }
    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        this.sort(array, sortLength, bucketCount);
    }
}