package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

CHANGELOG:

SerSan-1.1 (05/20/2023)
 - Make IPU, IP, and OIP versions.
   + There was never a 1.0 for these, if you ask.
 - Assure better recursion behavior. All "main" loops now use a tail recursion method.
   + This required fixing the partition outputs of OOP Sergio to be more like the others in development.
 - Fix visual inconsistency with HGM rotations.
   + Before, rotating backwards equally would rotate forward on the last iteration.
   + This was escpecially noticeable in IP and OIP during development.

SerSan-1.0 (03/21/2023)
 - Initial version.
   + Only OOP Sergio was made and released with this version tag.

*/
/**A Gapped Median-of-7 In-Place Unstable Quicksort.<p>
 * To use this algorithm in another, use {@code runSergioSort()} from a reference instance.
 * @version SerSan-1.1
 * @author PCBoy
*/
final public class InPlaceUnstableSergioSort extends Sort {
    public InPlaceUnstableSergioSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("In-Place Unstable Sergio");
        this.setRunAllSortsName("In-Place Unstable Sergio Quick Sort");
        this.setRunSortName("In-Place Unstable Sergio Quicksort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected boolean sorted(int[] array, int start, int end) {
        for (int i = start; i + 1 <= end; i++) if (Reads.compareIndices(array, i, i + 1, 0, false) > 0) return false;
        return true;
    }

    protected void swap(int[] array, int a, int b, double pause, boolean mark, boolean auxwrite) {
        if (a != b) Writes.swap(array, a, b, pause, mark, auxwrite);
    }

    protected int pd(int[] array, int start, int end) {
        int reverse = start;
        boolean different = false;
        int cmp = Reads.compareIndices(array, reverse, reverse + 1, 0.5, true);
        while (cmp >= 0 && reverse + 1 < end) {
            if (cmp > 0) different = true;
            reverse++;
            if (reverse + 1 < end) cmp = Reads.compareIndices(array, reverse, reverse + 1, 0.5, true);
        }
        if (reverse > start && different) {
            if (reverse < start + 3) Writes.swap(array, start, reverse, 0.75, true, false);
            else Writes.reversal(array, start, reverse, 0.75, true, false);
        }
        return reverse;
    }

    protected int gmo7(int[] array, int start, int end) {
        int[][] network = {{0,6},{1,5},{1,2},{4,5},{0,1},{5,6},{1,5},{2,4},{1,2},{4,5},{3,4},{2,3},{3,4}};
        for (int i = 0; i < network.length; i++) {
            int a = start + network[i][0] * ((end - start) / 6);
            int b = start + network[i][1] * ((end - start) / 6);
            if (Reads.compareIndices(array, a, b, 10, true) > 0) Writes.swap(array, a, b, 0, false, false);
        }
        return array[start + ((end - start) / 2)];
    }

    protected void shellPass(int[] array, int start, int end, int gap) {
        for (int h = gap, i = h + start; i < end; i++) {
            int v = array[i];
            int j = i;
            boolean w = false;
            Highlights.markArray(1, j);
            Highlights.markArray(2, j - h);
            Delays.sleep(0.25);
            for (; j >= h && j - h >= start && Reads.compareValues(array[j - h], v) == 1; j -= h) {
                Highlights.markArray(2, j - h);
                Writes.write(array, j, array[j - h], 0.25, w = true, false);
            }
            if (w) Writes.write(array, j, v, 0.25, true, false);
        }
    }

    protected void shellSort(int[] array, int start, int end) {
        for (int gap = (int) ((end - start) / 2.3601); gap >= 2; gap /= 2.3601) shellPass(array, start, end, gap);
        shellPass(array, start, end, 1);
    }

    protected int[] partition(int[] array, int a, int b, int piv) {
        int i1 = a, i = a-1, j = b, j1 = b;
        for (;;) {
            while (++i < j) {
                int cmp = Reads.compareIndexValue(array, i, piv, 0.5, true);
                if (cmp == 0) swap(array, i1++, i, 1, true, false);
                else if (cmp > 0) break;
            }
            Highlights.clearMark(2);
            while (--j > i) {
                int cmp = Reads.compareIndexValue(array, j, piv, 0.5, true);
                if (cmp == 0) swap(array, --j1, j, 1, true, false);
                else if (cmp < 0) break;
            }
            Highlights.clearMark(2);
            if (i < j) Writes.swap(array, i, j, 1, true, false);
            else {
                if (i1 == b) return new int[] {a, b};
                else if (j < i) j++;
                while (i1 > a) swap(array, --i, --i1, 1, true, false);
                while (j1 < b) swap(array, j++, j1++, 1, true, false);
                break;
            }
        }
        return new int[] {i, j};
    }

    protected void sergio(int[] array, int start, int end, int depth, int depthlimit) {
        Writes.recordDepth(depth);
        int[] part = {end + 1, 0};
        while (true) {
            end = part[0] - 1;
            if (end - start < 31 || depth > depthlimit) {
                shellSort(array, start, end + 1);
                break;
            } else if (pd(array, start, end + 1) < end) {
                if (!sorted(array, start, end)) {
                    int pivot = gmo7(array, start, end);
                    part = partition(array, start, end + 1, pivot);
                    Writes.recursion();
                    sergio(array, part[1], end, depth + 1, depthlimit);
                } else break;
            } else break;
        }
    }

    /**Runs a Gapped Median-of-7 In-Place Unstable Quicksort on the input.*/
    public void runSergioSort(int[] array, int start, int length) {
        sergio(array, start, start + length - 1, 0, (int) ((Math.E / 2) * (Math.log(length) / Math.log(2))));
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        runSergioSort(array, 0, currentLength);
    }
}