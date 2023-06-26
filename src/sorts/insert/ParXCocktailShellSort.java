package sorts.insert;

import java.util.BitSet;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan
in collaboration with Control, mg-2018 and PCBoy

-----------------------------
- Sorting Algorithm Scarlet -
-----------------------------

 */

/**
 * @author Ayako-chan
 * @author Control
 * @author mg-2018
 * @author PCBoy
 *
 */
public final class ParXCocktailShellSort extends Sort {

    public ParXCocktailShellSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Par(X) Cocktail Shell");
        this.setRunAllSortsName("Par(X) Cocktail Shell Sort");
        this.setRunSortName("Par(X) Cocktail Shellsort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    boolean dir;
    
    protected int findDisparity(int[] array, int a, int b) {
        int n = b - a;
        BitSet max = new BitSet(n);
        int maxIdx = 0;
        for (int i = 1; i < n; i++) {
            if (Reads.compareIndices(array, a + i, a + maxIdx, 0, false) > 0) {
                maxIdx = i;
                max.set(i);
            }
        }
        int i = n - 1;
        int p = 1;
        int j = n - 1;
        while (j >= 0 && i >= p) {
            while (!max.get(j) && j > 0) j--;
            maxIdx = j;
            while (Reads.compareIndices(array, a + i, a + maxIdx, 0, false) > 0 && i >= p) i--;
            if (Reads.compareIndices(array, a + i, a + j, 0, false) <= 0 && p < i - j) p = i - j;
            j--;
        }
        return p;
    }
    
    protected int shellPass(int[] array, int a, int b, int gap, int par, int lastgap) {
        if (gap >= lastgap) return lastgap;
        if (gap == lastgap - 1 && gap != 1) return lastgap;
        lastgap = gap;
        if (dir) {
            for (int i = a + gap; i < b; i++) {
                int key = array[i];
                int j = i;
                while (j >= a + gap && Reads.compareValues(key, array[j - gap]) < 0) {
                    Writes.write(array, j, array[j - gap], 1, true, false);
                    j -= gap;
                }
                if (j != i) Writes.write(array, j, key, 1, true, false);
            }
        } else {
            for (int i = b - gap; i >= a; i--) {
                int tmp = array[i];
                int j = i;
                while (j < b - gap && Reads.compareValues(array[j + gap], tmp) < 0) {
                    Writes.write(array, j, array[j + gap], 1, true, false);
                    j += gap;
                }
                if (j != i) Writes.write(array, j, tmp, 1, true, false);
            }
        }
        dir = !dir;
        Highlights.clearAllMarks();
        return gap;
    }
    
    public void shellSort(int[] array, int a, int b) {
        double truediv = 3;
        int lastpar = b - a;
        int lastgap = b - a;
        dir = true;
        while (true) {
            int par = findDisparity(array, a, b);
            int passpar = par;
            if (par >= lastpar)
                par = lastpar - (int) truediv;
            if (par / (int) truediv <= 1) {
                shellPass(array, a, b, 1, par, lastgap);
                break;
            }
            lastgap = shellPass(array, a, b, (int) ((par / (int) truediv) + par % (int) truediv), passpar, lastgap);
            if (lastpar - par <= Math.sqrt(lastpar)) truediv *= 1.5;
            lastpar = par;
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        shellSort(array, 0, sortLength);

    }

}
