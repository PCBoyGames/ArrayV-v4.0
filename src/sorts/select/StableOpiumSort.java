package sorts.select;

import java.util.HashSet;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public final class StableOpiumSort extends Sort {
    public StableOpiumSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Stable Opium");
        this.setRunAllSortsName("Stable Opium Sort");
        this.setRunSortName("Stable Opium Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(8192);
        this.setBogoSort(false);
    }

    protected int stablereturn(int a) {
        return arrayVisualizer.doingStabilityCheck() ? arrayVisualizer.getStabilityValue(a) : a;
    }

    protected void shellPass(int[] array, int start, int end, int gap) {
        for (int h = gap, i = h + start; i < end; i++) {
            int v = array[i];
            int j = i;
            boolean w = false;
            Highlights.markArray(1, j);
            Highlights.markArray(2, j - h);
            Delays.sleep(1);
            while (j >= h && j - h >= start && Reads.compareValues(array[j - h], v) < 0) {
                Highlights.markArray(1, j);
                Highlights.markArray(2, j - h);
                Delays.sleep(1);
                Writes.write(array, j, array[j - h], 1, true, false);
                j -= h;
                w = true;
            }
            if (w) {
                Writes.write(array, j, v, 1, true, false);
            }
        }
    }

    protected void bwdShell(int[] array, int start, int end) {
        int gap = (int) ((end - start) / 2.3601);
        while (gap > 2) {
            shellPass(array, start, end, gap);
            gap /= 2.3601;
        }
        shellPass(array, start, end, 1);
    }

    protected void dupes(int[] array, int currentLength) {
        boolean found = false;
        HashSet<Integer> map = new HashSet<>();
        for (int i = 0; i < currentLength; i++) {
            Writes.auxWrites++;
            Highlights.markArray(1, i);
            Delays.sleep(1);
            if (!map.add(stablereturn(array[i]))) found = true;
            else Writes.changeAllocAmount(1);
        }
        if (!found) {
            map.clear();
            Writes.changeAllocAmount(-1 * currentLength);
            return;
        }
        int unqs = map.size();
        int[] selections = Writes.createExternalArray(currentLength);
        int l = currentLength - unqs - 1;
        int r = currentLength - 1;
        for (int i = currentLength - 1; i >= 0; i--) {
            Highlights.markArray(2, i);
            if (map.contains(stablereturn(array[i]))) {
                Writes.write(selections, r--, array[i], 1, true, true);
                map.remove(stablereturn(array[i]));
                Writes.changeAllocAmount(-1);
                if (map.isEmpty()) break;
            } else Writes.write(selections, l--, array[i], 1, true, true);
        }
        Highlights.clearAllMarks();
        l++;
        for (; l < currentLength; l++) Writes.write(array, l, selections[l], 1, true, false);
        Writes.deleteExternalArray(selections);
        map.clear();
        bwdShell(array, currentLength - unqs, currentLength);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        dupes(array, currentLength);
        MoreOptimizedOpiumSort opium = new MoreOptimizedOpiumSort(arrayVisualizer);
        opium.runSort(array, currentLength, 574873);
    }
}
