package sorts.quick;

import main.ArrayVisualizer;
import sorts.exchange.CircleSortRecursive;
import sorts.exchange.CircloidSort;
import sorts.insert.PDBinaryInsertionSort;
import sorts.templates.MadhouseTools;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

Getting close to halving, and then actually halving.

*/
public class NoPivotQuickSort extends MadhouseTools {
    public NoPivotQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("No-Pivot Quick (Circle + Select Halver)");
        this.setRunAllSortsName("No-Pivot Quick Sort (Circle + Select Halver)");
        this.setRunSortName("No-Pivot Quicksort (Circle + Select Halver)");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void strangePass(int[] array, int start, int end) {
        int currentLen = end;
        int offset = start + 1;
            double mult = 1.0;
            int bound = 1;
            while (offset <= (start + end) / 2 && offset < currentLen) {
                mult = 1;
                bound = 1;
                while (offset + mult <= currentLen) {
                    if (Reads.compareIndices(array, (int) (offset + mult / 2) - 1, (int) (offset + mult) - 1, 0.05, true) > 0) {
                        Writes.swap(array, (int) (offset + mult / 2) - 1, (int) (offset + mult) - 1, 0.05, true, false);
                        if (mult == 1 / 2) {
                            bound *= 2;
                            mult = bound;
                        } else {
                            mult /= 2;
                        }
                    } else {
                        bound *= 2;
                        mult = bound;
                    }
                }
                offset++;
            }
    }

    protected boolean cs(int[] array, double a, double b) {
        if ((int) a != (int) b) {
            if (Reads.compareIndices(array, (int) a, (int) b, 0.1, true) > 0) {
                Writes.swap(array, (int) a, (int) b, 0.1, true, false);
                return true;
            }
        }
        return false;
    }

    protected void spread(int[] array, int start, int end, int over) {
        int depth = 0;
        while (true) {
            int l = end - start;
            if (l < over) {
                if (l < 0) return;
                for (int a = start, b = end - 1; a < b;) {
                    int c = 1;
                    for (int i = a; i < b; i++) {
                        if (cs(array, i, i + 1)) c = 1;
                        else c++;
                    }
                    b -= c;
                    c = 1;
                    for (int i = b; i > a; i--) {
                        if (cs(array, i - 1, i)) c = 1;
                        else c++;
                    }
                    a += c;
                }
                return;
            }
            // TODO: Manage into bidirectional something or another.
            // start + (1.0 * i / (over - 1)) * (l - 1), where 0 <= i < over.

            for (int i = 0; i < over - 1; i++) {
                for (int j = i; j >= 0; j--) if (!cs(array, start + (1.0 * j / (over - 1)) * (l - 1), start + (1.0 * (j + 1) / (over - 1)) * (l - 1))) break;
            }

            int tstart = start;
            if (depth % 2 == 1) {
                if (end - 1 >= start) {
                    start = minSorted(array, start, end - 1, 0, true);
                    end = maxSorted(array, tstart, end - 1, 0, true);
                    depth++;
                }
            } else {
                if (start + 1 <= end) {
                    start = minSorted(array, start + 1, end, 0, true);
                    end = maxSorted(array, tstart + 1, end, 0, true);
                    depth++;
                }
            }
        }
    }

    protected int partition(int[] array, int start, int end) {
        int Tstart = start;
        int Tend = end;
        //for (int left = start, right = end - 1; left < right; left++, right--) if (Reads.compareIndices(array, left, right, 1, true) > 0) Writes.swap(array, left, right, 0.1, true, false);
        //CircloidSort circloid = new CircloidSort(arrayVisualizer);
        //circloid.circlePass(array, start, end - 1);
        //CircleSortRecursive circle = new CircleSortRecursive(arrayVisualizer);
        //circle.singleRoutine(array, start, end);
        strangePass(array, Tstart, Tend);
        //spread(array, start, end, 128);
        int times = 0;
        while (true) {
            int selA = start;
            for (int i = start + 1; i < (start + end) / 2; i++) if (Reads.compareIndices(array, selA, i, 0, true) < 0) selA = i;
            int selB = (start + end) / 2;
            for (int i = (start + end) / 2 + 1; i < end; i++) if (Reads.compareIndices(array, selB, i, 0, true) > 0) selB = i;
            if (Reads.compareIndices(array, selA, selB, 0, true) <= 0) break;
            else {
                Highlights.markArray(1, selA);
                Highlights.markArray(2, selB);
                Delays.sleep(250);
            }
            /*if (selA != start) Writes.swap(array, selA, start, 0.1, true, false);
            if (selB != end - 1) Writes.swap(array, selB, end - 1, 0.1, true, false);
            Writes.swap(array, start++, end-- - 1, 0.1, true, false);*/
            Writes.swap(array, selA, selB, 250, true, false);
            times++;
        }
        System.err.println(times + " TIMES IN " + ((Tend - Tstart) / 2) + " IS " + (1.0 * times / ((Tend - Tstart) / 2)));
        return (start + end) / 2;
    }

    public void noPivotQuick(int[] array, int start, int end, int depth) {
        Writes.recordDepth(depth);
        if (end - start < 16) {
            PDBinaryInsertionSort binsert = new PDBinaryInsertionSort(arrayVisualizer);
            binsert.pdbinsertUnstable(array, start, end, 1, false);
            return;
        }
        if (isSorted(array, start, end)) return;
        int result = partition(array, start, end);
        Highlights.clearAllMarks();
        Writes.recursion();
        noPivotQuick(array, start, result, depth + 1);
        Highlights.clearAllMarks();
        Writes.recursion();
        noPivotQuick(array, result, end, depth + 1);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        noPivotQuick(array, 0, currentLength, 0);
    }
}