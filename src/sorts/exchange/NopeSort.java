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

Time's up.

 */

public final class NopeSort extends Sort {
    public NopeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Nope");
        this.setRunAllSortsName("Nope Sort");
        this.setRunSortName("Nopesort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(4);
        this.setBogoSort(false);
    }

    protected void bubble(int[] array, int a, int b, int dir) {
        int i = a;
        int j = a;
        int lastswap = b - 1;
        while (lastswap >= a + 1) {
            i = a;
            j = a;
            while (i <= lastswap) {
                if (Reads.compareIndices(array, i - 1, i, 0.001, true) == dir) {
                    Writes.swap(array, i - 1, i, 0.001, true, false);
                    j = i;
                }
                i++;
            }
            lastswap = j;
        }
    }

    protected void nopePop(int[] array, int a, int b, int dir, int HELP, int depth) {
        if (HELP == 1) {
            bubble(array, a, a + (int) Math.floor((b - a) / 4), 0 - dir);
            bubble(array, a + (int) Math.floor((b - a) / 4) + 1, (int) Math.floor((a + b) / 2), dir);
            bubble(array, (int) Math.floor((a + b) / 2) + 1, a + (int) Math.floor(((b - a) * 3) / 4), 0 - dir);
            bubble(array, a + (int) Math.floor(((b - a) * 3) / 4) + 1, b, dir);
            bubble(array, a, (int) Math.floor((a + b) / 2), 0 - dir);
            bubble(array, (int) Math.floor((a + b) / 2) + 1, b, dir);
            bubble(array, a, b, 0 - dir);
            bubble(array, a, b, dir);
        } else {
            Writes.recordDepth(depth);
            if (b - a < 4) {
                Writes.recursion();
                nopePop(array, a, b, dir, 1, depth+1);
            } else {
                Writes.recursion();
                nopePop(array, a, a + (int) Math.floor((b - a) / 4), 0 - dir, HELP-1, depth+1);
                Writes.recursion();
                nopePop(array, a + (int) Math.floor((b - a) / 4) + 1, (int) Math.floor((a + b) / 2), dir, HELP-1, depth+1);
                Writes.recursion();
                nopePop(array, (int) Math.floor((a + b) / 2) + 1, a + (int) Math.floor(((b - a) * 3) / 4), 0 - dir, HELP-1, depth+1);
                Writes.recursion();
                nopePop(array, a + (int) Math.floor(((b - a) * 3) / 4) + 1, b, dir, HELP-1, depth+1);
                Writes.recursion();
                nopePop(array, a, (int) Math.floor((a + b) / 2), 0 - dir, HELP-1, depth+1);
                Writes.recursion();
                nopePop(array, (int) Math.floor((a + b) / 2) + 1, b, dir, HELP-1, depth+1);
                Writes.recursion();
                nopePop(array, a, b, 0 - dir, HELP-1, depth+1);
                Writes.recursion();
                nopePop(array, a, b, dir, HELP-1, depth+1);
            }
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        nopePop(array, 1, currentLength, 1, currentLength * 20, 0);
    }
}
