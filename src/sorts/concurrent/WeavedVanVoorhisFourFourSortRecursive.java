package sorts.concurrent;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 *
MIT License

Copyright (c) 2022 aphitorite

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

public class WeavedVanVoorhisFourFourSortRecursive extends Sort {
    public WeavedVanVoorhisFourFourSortRecursive(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Weaved [4,4] Van Voorhis Sort (Recursive)");
        this.setRunAllSortsName("Recursive Weaved [4,4] Van Voorhis Sorting Network");
        this.setRunSortName("Recursive Weaved [4,4] Van Voorhis Sorting Network");
        this.setCategory("Concurrent Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int end;

    private void compSwap(int[] array, int a, int b) {
        if (b < this.end && Reads.compareIndices(array, a, b, 0.5, true) > 0)
            Writes.swap(array, a, b, 0.5, true, false);
    }

    private void merge(int[] array, int p, int n, int g, int g1) {
        if (n == 4) {
            this.compSwap(array, p,     p+g);
            this.compSwap(array, p+2*g, p+3*g);
            this.compSwap(array, p,     p+2*g);
            this.compSwap(array, p+g,   p+3*g);
            this.compSwap(array, p+g,   p+2*g);

            return;
        }
        this.merge(array, p,            n/4, g, 4*(g1+3));
        this.merge(array, p+(g1+4)*g,   n/4, g, 4*(g1+3));
        this.merge(array, p+(g1+4)*g*2, n/4, g, 4*(g1+3));
        this.merge(array, p+(g1+4)*g*3, n/4, g, 4*(g1+3));

        for (int i = 2; i+7 < n; i += 4) {
            this.compSwap(array, p+((i  )+((i  )/4)*g1)*g, p+((i+6)+((i+6)/4)*g1)*g);
            this.compSwap(array, p+((i+1)+((i+1)/4)*g1)*g, p+((i+7)+((i+7)/4)*g1)*g);
        }
        for (int i = 1; i+3 < n; i += 2)
            this.compSwap(array, p+((i  )+((i  )/4)*g1)*g, p+((i+3)+((i+3)/4)*g1)*g);

        for (int i = 2; i+3 < n; i += 4) {
            this.compSwap(array, p+((i  )+((i  )/4)*g1)*g, p+((i+2)+((i+2)/4)*g1)*g);
            this.compSwap(array, p+((i+1)+((i+1)/4)*g1)*g, p+((i+3)+((i+3)/4)*g1)*g);
        }
        for (int i = 3; i+3 < n; i += 2)
            this.compSwap(array, p+((i  )+((i  )/4)*g1)*g, p+((i+1)+((i+1)/4)*g1)*g);
    }

    private void sort(int[] array, int p, int n, int g) {
        int f = n/4;

        if (f > 1) {
            this.sort(array, p,     f, g*4);
            this.sort(array, p+g,   f, g*4);
            this.sort(array, p+2*g, f, g*4);
            this.sort(array, p+3*g, f, g*4);
        }
        this.merge(array, p, n, g, 0);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.end = length;
        int n = 1 << (2*((33-Integer.numberOfLeadingZeros(length-1))/2));
        this.sort(array, 0, n, 1);
    }
}
