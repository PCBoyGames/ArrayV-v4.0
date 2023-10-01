package sorts.concurrent;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 *
MIT License

Copyright (c) 2023 aphitorite

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 *
 */

public class AdaptiveFoldSort extends Sort {
    public AdaptiveFoldSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Adaptive Fold");
        this.setRunAllSortsName("Adaptive Fold Sorting Network");
        this.setRunSortName("Adaptive Fold Sort");
        this.setCategory("Concurrent Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int end;

    private boolean compSwap(int[] array, int a, int b) {
        if (b < this.end && Reads.compareIndices(array, a, b, 0.5, true) > 0) {
            Writes.swap(array, a, b, 0.5, false, false);
            return true;
        }
        return false;
    }

    @Override
    public void runSort(int[] array, int n, int bucketCount) {
        int ceilLog = 1;
        for (; (1 << ceilLog) < n; ceilLog++);

        this.end = n;
        n = 1 << ceilLog;
        boolean swapped;

        for (int k = n/2; k > 0; k /= 2) {
            swapped = false;

            for (int j = n; j >= k && j > 0; j /= 2) {
                for (int i = 0; i < n; i += j)
                    for (int m = 0; m < j/2; m++)
                        swapped |= this.compSwap(array, i+m, i+j-1-m);

                swapped &= j < n;
                if (!swapped && j == k) k /= 2;
            }
        }
    }
}
