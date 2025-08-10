package sorts.insert;

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

what am i doing

 */

public class InPlaceTreeSort extends Sort {
    public InPlaceTreeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("In-Place Tree");
        this.setRunAllSortsName("In-Place Tree Sort (Unbalanced)");
        this.setRunSortName("In-Place Treesort (Unbalanced)");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void grow(int[] array, int a, int b, double delay) {
        for (int i = a+2; i <= b; i++) {
            for (int j = a; j < i; j++) {
                if (Reads.compareIndices(array, i, j, delay, true) < 0) {
                    if (Reads.compareIndices(array, j, j+1, delay, true) <= 0) {
                        Writes.insert(array, i, j+1, delay, true, false);
                        break;
                    }
                } else {
                    boolean exit = false;
                    int p = -1;
                    for (int k = j-1; k >= a; k--) {
                        if (Reads.compareIndices(array, k, j, delay, true) > 0) {
                            p = k;
                            break;
                        }
                    }
                    for (int k = j+1; k < i; k++) {
                        if (Reads.compareIndices(array, j, k, delay, true) <= 0) {
                            if (p != -1 ? Reads.compareIndices(array, p, k, delay, true) <= 0 : false) {
                                Writes.insert(array, i, k, delay, true, false);
                                exit = true;
                                break;
                            }
                            j = k-1;
                            break;
                        }
                    }
                    if (exit) break;
                }
            }
        }
    }

    public void untree(int[] array, int a, int b) {
        for (int i = a; i < b; i++)
            for (int j = i; j <= b; j++)
                if (j+1 > b || Reads.compareIndices(array, j, j+1, 1, true) <= 0) {
                    if (i != j) Writes.insert(array, j, i, 1, true, false);
                    break;
                }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        grow(array, 0, currentLength-1, 1);
        untree(array, 0, currentLength-1);
    }
}
