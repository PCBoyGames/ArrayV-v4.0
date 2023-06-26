package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

/------------------/
|   SORTS GALORE   |
|------------------|
|  courtesy of     |
|  meme man        |
|  (aka gooflang)  |
/------------------/

Credit to Tycho/Äonothem for the original concept of Fast-growing Hierachy Sort.

Crashes on n=3.

 */


public class MegaHierarchySort extends BogoSorting {
    public MegaHierarchySort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Megahierarchy");
        this.setRunAllSortsName("Megahierarchy Sort");
        this.setRunSortName("Megahierarchy Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(2);
        this.setBogoSort(false);
    }

    private long fgh(int a, long n) {
        if (a == 0) {
            return n+1;
        } else {
            long b = n;
            for (long i = 0; i < n; i++) b = fgh(a-1, b);
            System.out.println(a + " " + b);
            return b;
        }
    }

    private long f(int a, long n, int[] array, int length) {
        long limit = fgh(a, n);
        if (a == 0) {
            return n+1;
        } else {
            long b = n;
            for (long i = 0; i < n; i++) {
                long holder = -fgh(a, n) + (long) (Math.random() * (limit - fgh(a, n)));
                b = f(a-1, b, array, length);
                if (Reads.compareIndices(array, a-1, a, 0.01, true) > 0) {
                    Writes.swap(array, a-1, a, 0.01, true, false);
                    if (holder != limit){
                        i = 0;
                        limit--;
                    }
                }
            }
            System.out.println(a + " " + b);
            return b;
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        f(sortLength, sortLength, array, sortLength);
    }

}
