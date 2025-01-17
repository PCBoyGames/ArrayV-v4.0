package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 *
Copyright (c) rosettacode.org.
Permission is granted to copy, distribute and/or modify this document
under the terms of the GNU Free Documentation License, Version 1.2
or any later version published by the Free Software Foundation;
with no Invariant Sections, no Front-Cover Texts, and no Back-Cover
Texts.  A copy of the license is included in the section entitled "GNU
Free Documentation License".
 *
 */

/*
modified by Lucy Phipps from ../templates/HeapSorting.java and MinHeapSort.java
the only real changes are subtracting every array access from (length - 1)
and removing the Writes.reverse() at the end
the rest is just compacting the code a bit
*/

public class FlippedMinHeapSort extends Sort {
    public FlippedMinHeapSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Flipped Min Heap");
        this.setRunAllSortsName("Flipped Min Heap Sort");
        this.setRunSortName("Flipped Reverse Heapsort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void siftDown(int[] array, int length, int root, int dist, boolean shuffle) {
        while (root <= dist / 2) {
            int leaf = 2 * root;
            if (leaf < dist && Reads.compareValues(array[length - leaf], array[length - leaf - 1]) == 1) {
                leaf++;
            }
            Highlights.markArray(1, length - root);
            Highlights.markArray(2, length - leaf);
            Delays.sleep(shuffle ? 0 : 1);
            if (Reads.compareValues(array[length - root], array[length - leaf]) == 1) {
                Writes.swap(array, length - root, length - leaf, 0, true, false);
                root = leaf;
            } else break;
        }
    }

    public void makeHeap(int[] array, int length, boolean shuffle) {
        for (int i = length / 2; i >= 1; i--) {
            siftDown(array, length, i, length, shuffle);
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        for (int i = length / 2; i >= 1; i--) {
            siftDown(array, length, i, length, false);
        }
        for (int i = length; i > 1; i--) {
            Writes.swap(array, length - 1, length - i, 1, true, false);
            siftDown(array, length, 1, i - 1, false);
        }
    }
}