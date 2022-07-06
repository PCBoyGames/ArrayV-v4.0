package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class EvubSort extends Sort {
    public EvubSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Evub");
        this.setRunAllSortsName("Evub Sort");
        this.setRunSortName("Evub Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Enter the base for this sort:", 3);
    }

    protected void compare(int[] array, int z) {
        if (Reads.compareIndices(array, z, z + 1, 0.001, true) > 0) Writes.swap(array, z, z + 1, 0.001, true, false);
    }

    protected void recur(int[] array, int a, int base, int depth) {
        Writes.recordDepth(depth);
        if (base == 1) for (int b = 0; b <= a; b++) compare(array, b);
        else {
            for (int b = 0; b <= a; b++) {
                Writes.recursion();
                recur(array, b, base - 1, depth + 1);
            }
        }
    }

    public int validateAnswer(int answer) {
        if (answer < 2) return 2;
        return answer;
    }

    @Override
    public void runSort(int[] array, int currentLength, int base) {
        for (int a = currentLength - 1; a > 0; a--) {
            for (int b = 0; b + 1 <= a; b++) {
                if (base == 2) compare(array, b);
                else recur(array, b, base - 2, 0);
            }
        }
    }
}