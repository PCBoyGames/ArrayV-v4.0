package sorts.quick;

import java.util.Random;

import main.ArrayVisualizer;
import sorts.insert.PDBinaryInsertionSort;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class UnstableSingularityQuickSort extends Sort {

    int depthlimit;
    int insertlimit;
    int replimit;

    public UnstableSingularityQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Unstable Singularity Quick");
        this.setRunAllSortsName("Unstable Singularity Quick Sort");
        this.setRunSortName("Unstable Singularity Quicksort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected int log2(int x) {
        int n = 1;
        while (1 << n < x) n++;
        if (1 << n > x) n--;
        return n;
    }

    protected int unstablepd(int[] array, int start, int end) {
        int reverse = start;
        boolean different = false;
        int cmp = Reads.compareIndices(array, reverse, reverse + 1, 0.5, true);
        while (cmp >= 0 && reverse + 1 < end) {
            if (cmp == 1) different = true;
            reverse++;
            cmp = Reads.compareIndices(array, reverse, reverse + 1, 0.5, true);
        }
        if (reverse > start && different) {
            if (reverse < start + 3) Writes.swap(array, start, reverse, 0.75, true, false);
            else Writes.reversal(array, start, reverse, 0.75, true, false);
        }
        return reverse;
    }

    protected void shuffle(int[] array, int start, int end, int extra) {
        Random random = new Random(extra * (extra - start));
        for (int i = start; i < end; i++) {
            int randomIndex = random.nextInt(end - i) + i;
            if (randomIndex != i) Writes.swap(array, i, randomIndex, 0.1, true, false);
        }
    }

    protected void binsert(int[] array, int start, int end) {
        PDBinaryInsertionSort bin = new PDBinaryInsertionSort(arrayVisualizer);
        bin.pdbinsert(array, start - 1, end, 0.1, false);
    }

    protected void singularityQuick(int[] array, int start, int offset, int end, int depth, int realdepth, int rep) {
        Writes.recordDepth(realdepth);
        if (end - start > insertlimit && (depth == depthlimit || rep == 4)) {
            shuffle(array, start - 1, end - 1, offset);
            offset = start;
            depth = 0;
            rep = 0;
        }
        Highlights.clearAllMarks();
        if (end - start > insertlimit && depth < depthlimit) {
            int left = offset;
            while (Reads.compareIndices(array, left - 1, left, 0.05, true) <= 0 && left < end) left++;
            if (left < end) {
                int pivot = array[left - 1];
                int pivotpos = left - 1;
                int originalpos = left - 1;
                int right = left + 1;
                int item = 1;
                boolean brokeloop = false;
                for (boolean founditem = false, brokencond = false; right <= end; right++) {
                    if (Reads.compareValues(pivot, array[right - 1]) > 0) {
                        Highlights.markArray(3, pivotpos);
                        Highlights.clearMark(2);
                        if (right - left == 1) {
                            if (!founditem) item = array[left - 1];
                            Writes.write(array, left - 1, array[left], 0.1, founditem = true, false);
                        } else brokeloop = true;
                        if (brokeloop && !brokencond) Writes.write(array, left - 1, item, 0.5, brokencond = true, false);
                        if (right - left > 1) Writes.swap(array, left - 1, right - 1, 0.5, true, false);
                        if (pivotpos == left - 1) pivotpos = right - 1;
                        left++;
                    }
                }
                if (right > end && !brokeloop) Writes.write(array, left - 1, item, 0.5, true, false);
                Highlights.clearAllMarks();
                if (pivotpos != left - 1) Writes.swap(array, pivotpos, left - 1, 0.5, true, false);
                boolean lsmall = left - start < end - (left + 1);
                if (lsmall && (left - 1) - start > 0) {
                    Writes.recursion();
                    if (end - replimit <= left || left <= start + replimit) singularityQuick(array, start, originalpos - 1 > start ? originalpos - 1 : start, left - 1, depth + 1, realdepth + 1, rep + 1);
                    else singularityQuick(array, start, originalpos - 1 > start ? originalpos - 1 : start, left - 1, depth + 1, realdepth + 1, 0);
                }
                if (end - (left + 1) > 0) {
                    Writes.recursion();
                    if (end - replimit <= left || left <= start + replimit) singularityQuick(array, left + 1, left + 1, end, depth + 1, realdepth + 1, rep + 1);
                    else singularityQuick(array, left + 1, left + 1, end, depth + 1, realdepth + 1, 0);
                }
                if (!lsmall && (left - 1) - start > 0) {
                    Writes.recursion();
                    if (end - replimit <= left || left <= start + replimit) singularityQuick(array, start, originalpos - 1 > start ? originalpos - 1 : start, left - 1, depth + 1, realdepth + 1, rep + 1);
                    else singularityQuick(array, start, originalpos - 1 > start ? originalpos - 1 : start, left - 1, depth + 1, realdepth + 1, 0);
                }
            }
        } else binsert(array, start, end);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        depthlimit = (int) Math.min(Math.sqrt(currentLength), 2 * log2(currentLength));
        insertlimit = Math.max((depthlimit / 2) - 1, 15);
        replimit = Math.max((depthlimit / 4), 2);
        int realstart = unstablepd(array, 0, currentLength);
        if (realstart + 1 < currentLength) singularityQuick(array, 1, realstart + 1, currentLength, 0, 0, 0);
    }
}