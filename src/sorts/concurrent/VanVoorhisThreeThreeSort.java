package sorts.concurrent;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 *
The MIT License (MIT)

Copyright (c) 2022 Control, aphitorite

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

public class VanVoorhisThreeThreeSort extends Sort {
    public VanVoorhisThreeThreeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("[3,3] Van Voorhis Sort");
        this.setRunAllSortsName("[3,3] Van Voorhis Sorting Network");
        this.setRunSortName("[3,3] Van Voorhis Sorting Network");
        this.setCategory("Concurrent Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void compSwap(int[] array, int a, int b) {
        if (Reads.compareIndices(array, a, b, 0.5, true) > 0)
            Writes.swap(array, a, b, 0.5, true, false);
    }

    private void compRange(int[] array, int a, int b, int s) {
        while (s-- > 0) this.compSwap(array, a++, b++);
    }

    private void merge(int[] array, int p, int n) {
        int t = n/3;

        this.compRange(array, p+t, p+t+t, t);
        this.compRange(array, p,   p+t,   t);
        this.compRange(array, p+t, p+t+t, t);

        for (int k = 9; k <= n; k *= 3) {
            int s = n/k;

            for (int i = 2; i+4 < k; i += 3)
                this.compRange(array, p+i*s, p+(i+4)*s, s);
            for (int i = 1; i+2 < k; i += 3)
                this.compRange(array, p+i*s, p+(i+2)*s, s);
            for (int i = 2; i+2 < k; i += 3)
                this.compRange(array, p+i*s, p+(i+2)*s, s);
            for (int i = 2; i+1 < k; i += 3)
                this.compRange(array, p+i*s, p+(i+1)*s, s);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        for (int k = 3; k <= sortLength; k *= 3)
            for (int i = 0; i < sortLength; i += k)
                this.merge(array, i, k);
    }
}