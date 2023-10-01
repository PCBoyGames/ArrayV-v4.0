package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;
import java.util.concurrent.ThreadLocalRandom;

/*

/------------------/
|   SORTS GALORE   |
|------------------|
|  courtesy of     |
|  meme man        |
|  (aka gooflang)  |
/------------------/

FEAR, RESILIENCE, HOPE.

 */


public class MegaHierarchySort extends BogoSorting {

    ThreadLocalRandom rand = ThreadLocalRandom.current();

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

    private long fgh(int a, long n, int d) {
        Writes.recordDepth(d++);
        if (a == 0) {
            return n+1;
        } else {
            long b = n;
            for (long i = 0; i < n; i++) {
                Writes.recursion();
                b = fgh(a-1, b, d);
            }
            System.out.println(a + " " + b);
            return b;
        }
    }

    private long f(int a, long n, int[] array, int length, int d) {
        Writes.recordDepth(d++);
        long f = fgh(length, length, 0);
        long limit = 2 * f;
        if (a == 0) {
            return n+1;
        } else {
            long b = n;
            for (long i = 0; i < n;) {
                Writes.recursion();
                b = f(a-1, b, array, length, d);
                if (Reads.compareValues(array[(a-2 < 0) ? a : a-2], array[(a-1 < 1) ? a+1 : a-1]) > 0) {
                    Writes.swap(array, (a-2 < 0) ? a : a-2, (a-1 < 1) ? a+1 : a-1, 0.01, true, false);
                }
                if (0 != limit) {
                    long holder = rand.nextLong(0, limit);
                    if (holder != limit) {
                        i = 0;
                        limit--;
                    } else {
                        i++;
                    }
                } else {
                    i++;
                }
            }
            System.out.println(a + " " + b);
            return b;
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        f(sortLength, sortLength, array, sortLength, 0);
    }
}
