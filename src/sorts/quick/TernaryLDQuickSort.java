package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class TernaryLDQuickSort extends Sort {

    public TernaryLDQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Ternary LD Quick");
        this.setRunAllSortsName("Ternary LD Quick Sort");
        this.setRunSortName("Ternary LD Quicksort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected int medOf3(int[] array, int i0, int i1, int i2) {
        int tmp;
        if (Reads.compareIndices(array, i0, i1, 1, true) > 0) {
            tmp = i1;
            i1 = i0;
        } else tmp = i0;
        if (Reads.compareIndices(array, i1, i2, 1, true) > 0) {
            if (Reads.compareIndices(array, tmp, i2, 1, true) > 0) return tmp;
            return i2;
        }
        return i1;
    }

    int[] partition(int[] array, int a, int b, int piv) {
        Highlights.clearMark(2);
        int i = a, j = a;
        for (int k = a; k < b; k++) {
            int cmp = Reads.compareIndexValue(array, k, piv, 0.5, true);
            if (cmp <= 0) {
                int t = array[k];

                Writes.write(array, k, array[j], 0.25, true, false);
                if (cmp < 0) {
                    Writes.write(array, j, array[i], 0.25, true, false);
                    Writes.write(array, i++, t, 0.25, true, false);
                } else Writes.write(array, j, t, 0.25, true, false);
                j++;
            }
        }
        return new int[] {i, j};
    }

    public void quickSort(int[] array, int a, int b) {
        while (b - a > 1) {
            int pIdx = medOf3(array, a, a + (b - a) / 2, b - 1);
            int[] pr = partition(array, a, b, array[pIdx]);
            if (b - pr[1] < pr[0] - a) {
                quickSort(array, pr[1], b);
                b = pr[0];
            } else {
                quickSort(array, a, pr[0]);
                a = pr[1];
            }
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickSort(array, 0, sortLength);

    }

}
