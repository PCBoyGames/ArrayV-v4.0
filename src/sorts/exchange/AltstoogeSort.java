package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 * THE WORK (AS DEFINED BELOW) IS PROVIDED UNDER THE TERMS OF THIS CREATIVE COMMONS PUBLIC LICENSE ("CCPL" OR "LICENSE").
 * THE WORK IS PROTECTED BY COPYRIGHT AND/OR OTHER APPLICABLE LAW. ANY USE OF THE WORK OTHER THAN AS AUTHORIZED UNDER THIS
 * LICENSE OR COPYRIGHT LAW IS PROHIBITED.
 *
 * BY EXERCISING ANY RIGHTS TO THE WORK PROVIDED HERE, YOU ACCEPT AND AGREE TO BE BOUND BY THE TERMS OF THIS LICENSE.
 * TO THE EXTENT THIS LICENSE MAY BE CONSIDERED TO BE A CONTRACT, THE LICENSOR GRANTS YOU THE RIGHTS CONTAINED HERE IN
 * CONSIDERATION OF YOUR ACCEPTANCE OF SUCH TERMS AND CONDITIONS.
 */

// Code refactored from: https://en.wikipedia.org/wiki/Stooge_sort
final public class AltstoogeSort extends Sort {
    public AltstoogeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Altstooge");
        this.setRunAllSortsName("Altstooge Sort");
        this.setRunSortName("Altstoogesort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(384);
        this.setBogoSort(false);
    }

    private void stoogeSort(int[] A, int i, int j, boolean base) {
        if (Reads.compareValues(A[i], A[j]) == 1) {
            Writes.swap(A, i, j, 0.005, true, false);
        }

        Delays.sleep(0.0025);

        Highlights.markArray(1, i);
        Highlights.markArray(2, j);

        if (j - i + 1 >= 3) {
            int t = (j - i + 1) / 3;
            int mid = i+t, mid2 = j-t;

            Highlights.markArray(3, j - t);
            Highlights.markArray(4, i + t);
            if(!base) {
                this.stoogeSort(A, mid, mid2, true);
            }

            this.stoogeSort(A, mid, j, true);
            this.stoogeSort(A, mid2, j, true);
            this.stoogeSort(A, i, mid, true);
            this.stoogeSort(A, mid, mid2, true);
            this.stoogeSort(A, i, mid2, true);
            this.stoogeSort(A, mid2, j, true);
            this.stoogeSort(A, mid, j, true);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.stoogeSort(array, 0, currentLength - 1, false);
    }
}