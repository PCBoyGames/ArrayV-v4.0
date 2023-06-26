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

public final class AdaptiveCreaseSort extends Sort {
    public AdaptiveCreaseSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Adaptive Crease");
        this.setRunAllSortsName("Adaptive Crease Sorting Network");
        this.setRunSortName("Adaptive Crease Sort");
        this.setCategory("Concurrent Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private boolean compSwap(int[] array, int a, int b) {
        if (Reads.compareIndices(array, a, b, 0.5, true) > 0) {
            Writes.swap(array, a, b, 0.5, false, false);
            return true;
        }
        return false;
    }

    @Override
    public void runSort(int[] array, int n, int bucketCount) {
        int m = 2;
        for (; 2*m < n; m *= 2);

        boolean swapped;

        for (int k = m; k > 0; k /= 2) {
            for (int i = 1; i < n; i += 2)
                this.compSwap(array, i-1, i);

            swapped = false;

            for (int j = m; j >= k && j > 1; j /= 2) {
                for (int i = 1; i+j-1 < n; i += 2)
                    swapped |= this.compSwap(array, i, i+j-1);

                if (!swapped && j == k) k /= 2;
            }
        }
    }
}
