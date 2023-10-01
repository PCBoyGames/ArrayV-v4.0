package sorts.merge;

import java.util.ArrayList;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import utils.IndexedRotations;

/*

CODED FOR ARRAYV BY PCBOYGAMES
EXTENDING CODE BY AYAKO-CHAN AND APHITORITE

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class PseudoPriorityRotateMergeSort extends Sort {

    protected ArrayList<int[]> rotates;

    public PseudoPriorityRotateMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Pseudo-Priority Rotate Merge");
        this.setRunAllSortsName("Pseudo-Priority Rotate Merge Sort");
        this.setRunSortName("Pseudo-Priority Rotate Mergesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void rotate(int[] array, int a, int m, int b) {
        IndexedRotations.adaptable(array, a, m, b, 1.0, true, false);
    }

    protected int binarySearch(int[] array, int a, int b, int value, boolean left) {
        while (a < b) {
            int m = a + (b - a) / 2;
            boolean comp = left
                    ? Reads.compareValues(value, array[m]) <= 0
                    : Reads.compareValues(value, array[m]) < 0;
            if (comp)
                b = m;
            else
                a = m + 1;
        }
        return a;
    }

    protected void fromSetup(int[] array, int a, int m, int b) {
        if (m - a < 1 || b - m < 1)
            return;
        int m1, m2, m3;
        if (m - a >= b - m) {
            m1 = a + (m - a) / 2;
            m2 = binarySearch(array, m, b, array[m1], true);
            m3 = m1 + (m2 - m);
        } else {
            m2 = m + (b - m) / 2;
            m1 = binarySearch(array, a, m, array[m2], false);
            m3 = (m2++) - (m - m1);
        }
        rotate(array, m1, m, m2);
        if (a < m1 && m1 < m3) {
            rotates.add(new int[] {a, m1, m3});
            Writes.auxWrites++;
        }
        if (m3 + 1 < m2 && m2 < b) {
            rotates.add(new int[] {m3 + 1, m2, b});
            Writes.auxWrites++;
        }
    }

    protected void toSetup(int[] array) {
        while (rotates.size() > 0) {
            int sel = 0;
            int size = rotates.get(0)[2] - rotates.get(0)[0];
            int selSize = size;
            for (int i = 1; i < rotates.size(); i++) {
                size = rotates.get(i)[2] - rotates.get(i)[0];
                Reads.addComparison();
                if (size > selSize) {
                    sel = i;
                    selSize = size;
                }
            }
            int[] a = rotates.get(sel);
            fromSetup(array, a[0], a[1], a[2]);
            while (rotates.contains(a)) {
                rotates.remove(a);
                Writes.auxWrites++;
            }
        }
    }

    public void merge(int[] array, int a, int m, int b) {
        rotates = new ArrayList<>((b - a) / 2);
        Writes.changeAllocAmount((b - a) / 2);
        rotates.add(new int[] {a, m, b});
        Writes.auxWrites++;
        toSetup(array);
        Writes.changeAllocAmount(-1 * ((b - a) / 2));
        rotates.clear();
    }

    protected int findRun(int[] array, int a, int b) {
        int i = a + 1;
        boolean dir;
        if (i < b)
            dir = Reads.compareIndices(array, i - 1, i++, 0.5, true) <= 0;
        else
            dir = true;
        if (dir)
            while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) <= 0)
                i++;
        else {
            while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) > 0)
                i++;
            if (i - a < 4)
                Writes.swap(array, a, i - 1, 1.0, true, false);
            else
                Writes.reversal(array, a, i - 1, 1.0, true, false);
        }
        Highlights.clearMark(2);
        return i;
    }

    public void mergeSort(int[] array, int a, int b) {
        int i, j, k;
        while (true) {
            i = findRun(array, a, b);
            if (i >= b)
                return;
            j = findRun(array, i, b);
            merge(array, a, i, j);
            Highlights.clearMark(2);
            if (j >= b)
                return;
            k = j;
            while (true) {
                i = findRun(array, k, b);
                if (i >= b)
                    break;
                j = findRun(array, i, b);
                merge(array, k, i, j);
                if (j >= b)
                    break;
                k = j;
            }
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        mergeSort(array, 0, sortLength);

    }

}
