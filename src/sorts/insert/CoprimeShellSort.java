package sorts.insert;

import java.util.LinkedList;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class CoprimeShellSort extends Sort {
    public CoprimeShellSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Coprime Shell Sort");
        this.setRunAllSortsName("Coprime Shell Sort");
        this.setRunSortName("Coprime Shell Sort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected boolean coprime(int a, int b) {
        for (int i = 2; i <= Math.min(a, b); i++) if (a % i == 0 && b % i == 0) return false;
        return true;
    }

    protected void shellPass(int[] array, int start, int end, int gap) {
        for (int h = gap, i = h + start; i < end; i++) {
            int v = array[i];
            int j = i;
            boolean w = false;
            Highlights.markArray(1, j);
            Highlights.markArray(2, j - h);
            Delays.sleep(0.5);
            while (j >= h && j - h >= start && Reads.compareValues(array[j - h], v) > 0) {
                Highlights.markArray(1, j);
                Highlights.markArray(2, j - h);
                Delays.sleep(0.5);
                Writes.write(array, j, array[j - h], 0.5, true, false);
                j -= h;
                w = true;
            }
            if (w) Writes.write(array, j, v, 0.5, true, false);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        LinkedList<Integer> gaps = new LinkedList<>();
        gaps.addLast(1);
        gaps.addLast(4);
        for (int g = gaps.peekLast(); g < currentLength; g = gaps.peekLast()) {
            boolean loop = true;
            int keeps = g;
            for (int i = (int) (2.3601 * g); loop; i++) {
                if (coprime(i, g)) {
                    keeps = i;
                    loop = false;
                }
            }
            gaps.addLast(keeps);
        }
        System.err.println(gaps);
        for (int i = gaps.removeLast(); !gaps.isEmpty(); i = gaps.removeLast()) shellPass(array, 0, currentLength, i);
        shellPass(array, 0, currentLength, 1);
        gaps.clear();
    }
}