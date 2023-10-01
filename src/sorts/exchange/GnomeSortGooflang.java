package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

/------------------/
|   SORTS GALORE   |
|------------------|
|  courtesy of     |
|  meme man        |
|  (aka gooflang)  |
/------------------/

beyond death

 */

public class GnomeSortGooflang extends Sort {
    public GnomeSortGooflang(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Gooflang's Gnome");
        this.setRunAllSortsName("Gooflang's Gnome Sort");
        this.setRunSortName("Gooflang's Gnomesort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(4);
        this.setBogoSort(false);
    }

    public void nOmegaSwap(int O, int[] array, int a, int b, int d) {
        if (a != b) {
            Writes.recordDepth(d++);
            int temp = array[a];
            if (O == 1) {
                for (int i = 0; i < arrayVisualizer.getCurrentLength(); i++) {
                    Writes.swap(array, a, b, 0.1, true, false);
                }
            } else {
                for (int i = 0; i < arrayVisualizer.getCurrentLength(); i++) {
                    Writes.recursion();
                    nOmegaSwap(O - 1, array, a, b, d);
                }
            }
            while (array[b] != temp) {
                Writes.swap(array, a, b, 0.1, true, false);
            }
        }
    }

    private void goofGnome(int O, int[] array, int e, int d) {
        if (e <= 0) return;
        Writes.recordDepth(d++);
        if (O == 1) {
            Writes.recursion();
            goofGnome(O, array, e-1, d);
            for (int i = e; i > 0; i--) {
                nOmegaSwap(arrayVisualizer.getCurrentLength(), array, i, i-1, 0);
                if (Reads.compareValues(array[i], array[i-1]) < 0) {
                    nOmegaSwap(arrayVisualizer.getCurrentLength(), array, i, i-1, 0);
                } else {
                    Writes.recursion();
                    goofGnome(O, array, e-1, d);
                }
                Writes.recursion();
                goofGnome(O, array, i-1, d);
            }
        } else {
            Writes.recursion();
            goofGnome(O, array, e-1, d);
            for (int i = e; i > 0; i--) {
                goofGnome(O-1, array, i+1, d);
                if (Reads.compareValues(array[i], array[i-1]) < 0) {
                    Writes.recursion();
                    goofGnome(O-1, array, i+1, d);
                } else {
                    Writes.recursion();
                    goofGnome(O, array, e-1, d);
                }
                Writes.recursion();
                goofGnome(O, array, i-1, d);
            }
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        goofGnome(currentLength, array, currentLength-1, 0);
    }
}
