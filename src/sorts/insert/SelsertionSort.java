package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.MadhouseTools;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class SelsertionSort extends MadhouseTools {
    public SelsertionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Selsertion");
        this.setRunAllSortsName("Selsertion Sort");
        this.setRunSortName("Selsertion Sort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void insert(int[] array, int start, int mid, int end, boolean search) {
        if (search) {
            start = minExponentialSearch(array, start, mid, stableReturn(array[mid]), true, 0.5, true);
            end = maxExponentialSearch(array, mid, end, stableReturn(array[mid - 1]), false, 0.5, true);
        }
        if (end - mid >= mid - start) {
            int bound = mid;
            int seg = mid;
            for (int i = start; i < bound; i++) {
                if (i >= seg) {
                    seg = i;
                    while (seg < bound && Reads.compareIndices(array, seg, seg + 1, 0.5, true) <= 0) seg++;
                }
                int sel = i;
                for (int j = Math.max(seg, mid); j < bound; j++) if (Reads.compareIndices(array, sel, j, 0.5, true) > 0) sel = j;
                int dest = minExponentialSearch(array, bound, end, stableReturn(array[sel]), true, 0.5, true);
                while (bound < dest) {
                    Writes.swap(array, bound, i, 1, true, false);
                    if (sel == i) sel = bound;
                    bound++;
                    i++;
                }
                if (sel != i) Writes.swap(array, i, sel, 1, true, false);
            }
        } else {
            int bound = mid - 1;
            int seg = mid - 1;
            for (int i = end - 1; i > bound; i--) {
                if (i <= seg) {
                    seg = i;
                    while (seg - 1 > bound && Reads.compareIndices(array, seg - 1, seg, 0.5, true) <= 0) seg--;
                }
                int sel = i;
                for (int j = Math.min(seg, mid - 1); j > bound; j--) if (Reads.compareIndices(array, j, sel, 0.5, true) > 0) sel = j;
                int dest = maxExponentialSearch(array, start, bound + 1, stableReturn(array[sel]), false, 0.5, true);
                while (bound >= dest) {
                    Writes.swap(array, bound, i, 1, true, false);
                    if (sel == i) sel = bound;
                    bound--;
                    i--;
                }
                if (sel != i) Writes.swap(array, i, sel, 1, true, false);
            }
        }
    }

    public void selsert(int[] array, int a, int b, boolean search) {
        if (a >= b) return;
        int i, j;
        i = findRun(array, a, b, 0.5, true, false);
        while (i < b) {
            j = findRun(array, i, b, 0.5, true, false);
            insert(array, a, i, j, search);
            i = j;
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int blockSize) {
        selsert(array, 0, currentLength, false);
    }
}