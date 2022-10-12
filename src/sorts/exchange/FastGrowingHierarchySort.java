package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class FastGrowingHierarchySort extends Sort {




    public FastGrowingHierarchySort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Fast-growing Hierarchy");
        this.setRunAllSortsName("Fast-growing Hierarchy Sort");
        this.setRunSortName("Fast-growing Hierarchy Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(3);
        this.setBogoSort(false);
    }

    private long f(int a, long n, int[] array, int length) {

        if (a == 0) {
            return n+1;
        } else {
            long b = n;

            for (long i = 0; i < n; i++) {
                b = f(a-1, b, array, length);

                if (Reads.compareIndices(array, a-1, a, 0.01, true) == 1) {
                    Writes.swap(array, a-1, a, 0.01, true, false);
                }
            }
            System.out.println(a + " " + b);
            return b;
        }

    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {

        f(sortLength-1, sortLength-1, array, sortLength);
    }

}
