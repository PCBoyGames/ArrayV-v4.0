package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import utils.IndexedRotations;

/*
 *
The MIT License (MIT)

Copyright (c) 2021 Control, aphitorite

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

public class RotatePartitionMergeSort extends Sort {
    public RotatePartitionMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Rotate Partition Merge");
        this.setRunAllSortsName("Rotate Partition Merge Sort");
        this.setRunSortName("Rotate Partition Mergesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void rotate(int[] array, int a, int m, int b) {
        IndexedRotations.griesMills(array, a, m, b, 1, true, false);
    }

    private void partitionMerge(int[] array, int a, int m, int b) {
        int lenA = m-a, lenB = b-m;

        if (lenA < 1 || lenB < 1) return;

        int c = (lenA+lenB)/2; //!(c < lenA || c < lenB)

        if (lenB < lenA) { //partitions c largest elements
            int r1 = 0, r2 = lenB;

            while (r1 < r2) {
                int ml = (r1+r2)/2;

                if (Reads.compareValues(array[m-(c-ml)], array[b-ml-1]) > 0)
                    r2 = ml;
                else
                    r1 = ml+1;
            }
            //[lenA-(c-r1)][c-r1][lenB-r1][r1]
            //[lenA-(c-r1)][lenB-r1][c-r1][r1]
            this.rotate(array, m-(c-r1), m, b-r1);

            int m1 = b-c;
            this.partitionMerge(array, m1, b-r1, b);
            this.partitionMerge(array, a, m1-(lenB-r1), m1);
        }
        else { //partitions c smallest elements
            int r1 = 0, r2 = lenA;

            while (r1 < r2) {
                int ml = (r1+r2)/2;

                if (Reads.compareValues(array[a+ml], array[m+(c-ml)-1]) > 0)
                    r2 = ml;
                else
                    r1 = ml+1;
            }
            //[r1][lenA-r1][c-r1][lenB-(c-r1)]
            //[r1][c-r1][lenA-r1][lenB-(c-r1)]
            this.rotate(array, a+r1, m, m+(c-r1));

            int m1 = a+c;
            this.partitionMerge(array, m1, m1+(lenA-r1), b);
            this.partitionMerge(array, a, a+r1, m1);
        }
    }

    protected void rotatePartitionMergeSort(int[] array, int a, int b) {
        int len = b-a, i;

        for (int j = 1; j < len; j *= 2) {
            for (i = a; i + 2*j <= b; i += 2*j)
                this.partitionMerge(array, i, i+j, i+2*j);

            if (i + j < b)
                this.partitionMerge(array, i, i+j, b);
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.rotatePartitionMergeSort(array, 0, length);
    }
}