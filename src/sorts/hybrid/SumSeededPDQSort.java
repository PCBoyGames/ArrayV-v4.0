package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.insert.AdaptiveSquareInsertionSort;
import sorts.templates.Sort;

/*
 *
pdqsort.h - Pattern-defeating quicksort.
Copyright (c) 2015 Orson Peters
This software is provided 'as-is', without any express or implied warranty. In no event will the
authors be held liable for any damages arising from the use of this software.
Permission is granted to anyone to use this software for any purpose, including commercial
applications, and to alter it and redistribute it freely, subject to the following restrictions:
1. The origin of this software must not be misrepresented; you must not claim that you wrote the
   original software. If you use this software in a product, an acknowledgment in the product
   documentation would be appreciated but is not required.
2. Altered source versions must be plainly marked as such, and must not be misrepresented as
   being the original software.
3. This notice may not be removed or altered from any source distribution.
 *
 */

final public class SumSeededPDQSort extends Sort {
    public SumSeededPDQSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Sum-Seeded Pattern-Defeating Quick");
        this.setRunAllSortsName("Sum-Seeded Pattern-Defeating Quick Sort");
        this.setRunSortName("Sum-Seeded Pattern-Defeating Quicksort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    AdaptiveSquareInsertionSort sort = new AdaptiveSquareInsertionSort(this.arrayVisualizer);

    // stolen from stackoverflow
    public int log2(int n){
        if(n <= 0) throw new IllegalArgumentException();
        return 31 - Integer.numberOfLeadingZeros(n);
    }

    // Thanks to Timo Bingmann for providing a good reference for Quick Sort w/ LR pointers.
    private void quickSort(int[] a, int p, int r) {
        if(r - p > 16) {
            int pivot = (r^p + a[p]^a[r] + a[p] + a[r] + p + r)*(r^p + a[p]^a[r] + a[p] + a[r] + p + r)%(r - p) + p;
            pivot = Math.abs((pivot + a[p]^a[r] + a[p] + a[r] + p + r)*(pivot+ a[p]^a[r] + a[p] + a[r] + p + r)%(r - p) + p)%(r - p - 1) + p;

            int x = a[pivot];

            int i = p;
            int j = r;

            Highlights.markArray(3, pivot);

            while (i <= j) {
                while (Reads.compareValues(a[i], x) == -1){
                    i++;
                    Highlights.markArray(1, i);
                    Delays.sleep(0.5);
                }
                while (Reads.compareValues(a[j], x) == 1){
                    j--;
                    Highlights.markArray(2, j);
                    Delays.sleep(0.5);
                }

                if (i <= j) {
                    // Follow the pivot and highlight it.
                    if(i == pivot) {
                        Highlights.markArray(3, j);
                    }
                    if(j == pivot) {
                        Highlights.markArray(3, i);
                    }

                    Writes.swap(a, i, j, 1, true, false);

                    i++;
                    j--;
                }
            }

            if(p < j) {
                this.quickSort(a, p, j);
            }
            if(i < r) {
                this.quickSort(a, i, r);
            }
        } else sort.customBinaryInsert(a, p, r + 1, 0.5);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.quickSort(array, 0, currentLength - 1);
    }
}