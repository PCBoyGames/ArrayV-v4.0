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

public class VanVoorhisFourFourSortIterative extends Sort {
    public VanVoorhisFourFourSortIterative(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("[4,4] Van Voorhis Sort (Iterative)");
        this.setRunAllSortsName("Iterative [4,4] Van Voorhis Sorting Network");
        this.setRunSortName("Iterative [4,4] Van Voorhis Sorting Network");
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

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        for (int k = 4; k <= sortLength; k *= 4) {
            int f = k/4;

            for (int i = 0; i < sortLength; i += k) {
                this.compRange(array, i,     i+f,     f);
                this.compRange(array, i+f+f, i+f+f+f, f);
            }
            for (int i = 0; i < sortLength; i += k) {
                this.compRange(array, i,     i+f+f,   f);
                this.compRange(array, i+f,   i+f+f+f, f);
            }
            for (int i = 0; i < sortLength; i += k)
                this.compRange(array, i+f,   i+f+f,   f);

            for (int m = 16; m <= k; m *= 4) {
                int s = k/m;

                for (int i = 0; i < sortLength; i += k) {
                    for (int j = 2; j+7 < m; j += 4) {
                        this.compRange(array, i+j*s,     i+(j+6)*s, s);
                        this.compRange(array, i+(j+1)*s, i+(j+7)*s, s);
                    }
                }
                for (int i = 0; i < sortLength; i += k) {
                    for (int j = 1; j+5 < m; j += 4) {
                        this.compRange(array, i+j*s,     i+(j+3)*s, s);
                        this.compRange(array, i+(j+2)*s, i+(j+5)*s, s);
                    }
                }
                for (int i = 0; i < sortLength; i += k) {
                    for (int j = 2; j+3 < m; j += 4) {
                        this.compRange(array, i+j*s,     i+(j+2)*s, s);
                        this.compRange(array, i+(j+1)*s, i+(j+3)*s, s);
                    }
                }
                for (int i = 0; i < sortLength; i += k)
                    for (int j = 3; j+3 < m; j += 2)
                        this.compRange(array, i+j*s,     i+(j+1)*s, s);
            }
        }
    }
}