package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Harumi
in collaboration with aphitorite

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Harumi
 * @author aphitorite
 *
 */
public final class GanyuSort extends Sort {

    public GanyuSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Ganyu");
        this.setRunAllSortsName("Ganyu Sort");
        this.setRunSortName("Ganyusort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void indexSort(int[] array, int[] keys, int a, int b) {
        for (int i = 0; i < b - a; i++) {
            Highlights.markArray(2, i);
            if (Reads.compareOriginalValues(i, keys[i]) != 0) {
                int t = array[a+i];
                int j = i, next = keys[i];
                do {
                    Writes.write(array, a+j, array[a+next], 0.0, true, false);
                    Writes.write(keys, j, j, 1.0, true, true);
                    j = next;
                    next = keys[next];
                } while (Reads.compareOriginalValues(next, i) != 0);
                Writes.write(array, a+j, t, 0.0, true, false);
                Writes.write(keys, j, j, 1.0, true, true);
            }
        }
        Highlights.clearMark(2);
    }

    void merge(int[] array, int[] tag0, int[] tag1, int o, int a, int m, int b) {
        int ta = a - o, tm = m - o, tb = b - o, ti = ta, tj = tm,
                t = ta;
        if (ti == tm) {
            while (tj < tb) Writes.write(tag1, t++, tag0[tj++]+(tm-ta), 0.5, false, true);
            return;
        }
        if (tj == tb) {
            while (ti < tm) Writes.write(tag1, t++, tag0[ti++], 0.5, false, true);
            return;
        }
        int i = a + tag0[ti], j = m + tag0[tj];
        Highlights.markArray(2, i);
        Highlights.markArray(3, j);
        while (ti < tm || tj < tb) {
            if (ti < tm && (tj >= tb || Reads.compareIndices(array, i, j, 0, false) <= 0)) {
                Writes.write(tag1, t++, tag0[ti], 0.5, false, true);
                if (++ti < tm) {
                    i = a+tag0[ti];
                    Highlights.markArray(2, i);
                }
                else {
                    Highlights.clearMark(2);
                }
            } else {
                Writes.write(tag1, t++, tag0[tj]+(tm-ta), 0.5, false, true);
                if (++tj < tb) {
                    j = m+tag0[tj];
                    Highlights.markArray(3, j);
                }
                else {
                    Highlights.clearMark(3);
                }
            }
        }
    }

    public void mergeSort(int[] array, int a, int b) {
        int len = b - a;
        int[] tag0 = Writes.createExternalArray(len);
        int[] tag1 = Writes.createExternalArray(len);
        boolean inv = false;
        for (int j = 1; j < b - a; j *= 2) {
            for (int i = a; i < b; i += 2 * j)
                merge(array, inv ? tag0 : tag1, inv ? tag1 : tag0, a, i, Math.min(i + j, b), Math.min(i + 2 * j, b));
            inv = !inv;
        }
        indexSort(array, inv ? tag0 : tag1, a, b);
        Writes.deleteExternalArray(tag0);
        Writes.deleteExternalArray(tag1);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        mergeSort(array, 0, sortLength);

    }

}
