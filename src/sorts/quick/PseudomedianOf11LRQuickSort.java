package sorts.quick;

import main.ArrayVisualizer;
import sorts.insert.InsertionSort;
import sorts.templates.Sort;

final public class PseudomedianOf11LRQuickSort extends Sort {
    public PseudomedianOf11LRQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Pseudomedian Of 11 Left/Right Quick");
        this.setRunAllSortsName("Quick Sort, LR Pointers, Pseudomedian Of 11");
        this.setRunSortName("Pseudomedian Of 11 LR Quicksort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    private InsertionSort inserter;

    private int disparity(int[] array, int[] list, int index, int value) {
        return Math.abs(array[list[index]]-value);
    }
    private int medianMean(int[] a, int... qs) {
        int mean = 0, medIndex = 0;
        for(int i : qs) {
            mean += a[i];
        }
        mean /= qs.length;

        for(int i=1; i<qs.length; i++) {
            if(Reads.compareValues(
                disparity(a, qs, i, mean),
                disparity(a, qs, medIndex, mean)
            ) == -1)
                medIndex=i;
        }
        return qs[medIndex];
    }
    // slight mod to LRquick
    private void quickSort(int[] A, int p, int r, boolean low) {
        double v = (r-p) / 10d;

        if(v <= 1d) {
            this.inserter.customInsertSort(A, p, r+1, 0.1, false);
            return;
        }

        int a = (int) (p+v),
            b = (int) (p+2*v),
            c = (int) (p+3*v),
            d = (int) (p+4*v),
            e = (int) (r-4*v),
            f = (int) (r-3*v),
            g = (int) (r-3*v),
            h = (int) (r-2*v),
            i = (int) (r-v);

        int piv = this.medianMean(A, p, a, b, c, d, e, f, g, h, i, r),
            x = A[piv];

        int y = p;
        int z = r;

        while (y <= z) {
            while (Reads.compareValues(A[y], x) == -1){
                y++;
                Highlights.markArray(1, y);
                Delays.sleep(0.5);
            }
            while (Reads.compareValues(A[z], x) == 1){
                z--;
                Highlights.markArray(2, z);
                Delays.sleep(0.5);
            }

            if (y <= z) {
                Writes.swap(A, y++, z--, 1, true, false);
            }
        }

        if(p < z) {
            this.quickSort(A, p, z, true);
        }
        if(y < r) {
            this.quickSort(A, y, r, false);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.inserter = new InsertionSort(arrayVisualizer);
        this.quickSort(array, 0, currentLength - 1, true);
    }
}