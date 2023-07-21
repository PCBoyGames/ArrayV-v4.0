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
final public class ForceContCoprimeShellSort extends Sort {
    public ForceContCoprimeShellSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Forced Continued Coprime Shell");
        this.setRunAllSortsName("Forced Continued Coprime Shell Sort");
        this.setRunSortName("Forced Continued Coprime Shell Sort");
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

    protected void shellPass(int[] array, int currentLength, int gap) {
        for (int h = gap, i = h; i < currentLength; i++) {
            int v = array[i];
            int j = i;
            boolean w = false;
            Highlights.markArray(1, j);
            Highlights.markArray(2, j - h);
            Delays.sleep(0.25);
            for (; j >= h && Reads.compareValues(array[j - h], v) == 1; j -= h) {
                Highlights.markArray(2, j - h);
                Writes.write(array, j, array[j - h], 0.25, w = true, false);
            }
            if (w) Writes.write(array, j, v, 0.25, true, false);
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
                boolean forces = true;
                for (int j = 1; j < gaps.size() - 1 && forces; j++) if (!coprime(gaps.get(j), i)) forces = false;
                if (forces) {
                    if (coprime(i, g)) {
                        keeps = i;
                        loop = false;
                    }
                }
            }
            gaps.addLast(keeps);
        }
        gaps.removeLast();
        System.err.println(gaps);
        for (int i = gaps.removeLast(); !gaps.isEmpty(); i = gaps.removeLast()) shellPass(array, currentLength, i);
        shellPass(array, currentLength, 1);
        gaps.clear();
    }
}