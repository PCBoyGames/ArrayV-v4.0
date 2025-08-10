package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Flanlaina
in collaboration with Potassium

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Flanlaina
 * @author Potassium
 *
 */
public class RemoteMergeSort extends Sort {
    public RemoteMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Remote Merge");
        this.setRunAllSortsName("Remote Merge Sort");
        this.setRunSortName("Remote Mergesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void optimizedGnomeSort(int[] array, int a, int b, double sleep) {
        for (int i = a + 1; i < b; i++) {
            for (int j = i; j > a; j--)
                if (Reads.compareIndices(array, j, j - 1, sleep, true) < 0)
                    Writes.swap(array, j - 1, j, sleep, true, false);
                else break;
        }
    }

    public void remoteMergeSort(int[] array, int a, int b) {
        int length = b - a;
        for (int l = 2; l <= length; ++l) {
            for (int i = a; i < b; i += l) {
                /*
                 for (int j = i; j < i + l; ++j) {
                 this.smartGnomeSort(array, i, j, 0.5, 1);
                 }
                 */
                optimizedGnomeSort(array, i, Math.min(i + l, b), 0.125);
            }
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        remoteMergeSort(array, 0, sortLength);
    }
}
