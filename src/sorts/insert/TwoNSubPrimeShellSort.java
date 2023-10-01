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
public class TwoNSubPrimeShellSort extends Sort {
    public TwoNSubPrimeShellSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("<2^n Prime Shell");
        this.setRunAllSortsName("<2^n Prime Shell Sort");
        this.setRunSortName("<2^n Prime Shell Sort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected int lessPrime(int n) {
        boolean broken = false;
        if (n <= 2) return 1;
        while (!broken) {
            n--;
            for (int i = (int) Math.sqrt(n); i < n; i++) {
                if (n % i == 0) break;
                if (i == n - 1) broken = true;
            }
        }
        return n;
    }

    protected int greaterPrime(int n) {
        boolean broken = false;
        while (!broken) {
            n++;
            for (int i = (int) Math.sqrt(n); i < n; i++) {
                if (n % i == 0) break;
                if (i == n - 1) broken = true;
            }
        }
        return n;

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
        gaps.addLast(3);
        for (int g = 8; g <= currentLength; g *= 2) {
            gaps.addLast(lessPrime(g));
        }
        System.err.println(gaps);
        for (int i = gaps.removeLast(); !gaps.isEmpty(); i = gaps.removeLast()) shellPass(array, currentLength, i);
        shellPass(array, currentLength, 1);
        gaps.clear();
    }
}