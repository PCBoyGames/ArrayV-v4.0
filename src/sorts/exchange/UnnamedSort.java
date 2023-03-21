package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public final class UnnamedSort extends Sort {
    public UnnamedSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("AAA - UNNAMED");
        this.setRunAllSortsName("??? Sort");
        this.setRunSortName("???");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected int maxsorted(int[] array, int e, int i) {
        int a = i - 1;
        int b = e;
        boolean segment = true;
        while (segment) {
            if (b - 1 < 0) return 0;
            if (Reads.compareIndices(array, b - 1, b, 0.1, true) > 0) segment = false;
            else b--;
        }
        int sel = b - 1;
        for (int s = b - 2; s >= 0; s--) if (Reads.compareIndices(array, sel, s, 0.1, true) < 0) sel = s;
        while (Reads.compareIndices(array, sel, a, 0.1, true) <= 0) a--;
        return a + 1;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        boolean any = true;
        while (any) {
            any = false;
            int j = 1;
            while (j < currentLength) {
                int i = j;
                int k = 1;
                while ((j - 1) + (int) Math.pow(2, k) <= currentLength) {
                    if (Reads.compareIndices(array, i - 1, ((j - 1) + (int) Math.pow(2, k)) - 1, 0.001, true) > 0) {
                        Writes.insert(array, i - 1, currentLength - 1, 0.001, any = true, false);
                        //if (j > 1) j--;
                        k = 1;
                        //j = 1;
                        i = j;
                    }else{
                    i = (j - 1) + (int) Math.pow(2, k);
                    k++;
                    }
                }
                j++;
            }
            currentLength = maxsorted(array, currentLength, currentLength);
        }
    }
}
