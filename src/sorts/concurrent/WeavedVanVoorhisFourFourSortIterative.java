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

public class WeavedVanVoorhisFourFourSortIterative extends Sort {
    public WeavedVanVoorhisFourFourSortIterative(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Weaved [4,4] Van Voorhis Sort (Iterative)");
        this.setRunAllSortsName("Iterative Weaved [4,4] Van Voorhis Sorting Network");
        this.setRunSortName("Iterative Weaved [4,4] Van Voorhis Sorting Network");
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

    private void compRange(int[] array, int a, int b, int s) {
        while (s-- > 0) this.compSwap(array, a++, b++);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.end = length;
        int n = 1 << (2*((33-Integer.numberOfLeadingZeros(length-1))/2)); //4^ceil(log4 n)

        for (int k = 4; k <= n; k *= 4) {
            int g = n/k;

            //four sorters

            for (int i = 0; i < k; i += 2)
                this.compRange(array, (i  )*g, (i+1)*g, g);

            for (int i = 0; i < k; i += 4) {
                this.compRange(array, (i  )*g, (i+2)*g, g);
                this.compRange(array, (i+1)*g, (i+3)*g, g);
            }
            for (int i = 0; i < k; i += 4)
                this.compRange(array, (i+1)*g, (i+2)*g, g);

            //???

            for (int j = 16; j <= k; j *= 4) {
                int s = k/j, g1 = 4*(s-1);

                for (int i = 2; i+7 < j; i += 4) {
                    for (int m = 0; m < s; m++) {
                        this.compRange(array, ((i  )+((i  )/4)*g1+4*m)*g, ((i+6)+((i+6)/4)*g1+4*m)*g, g);
                        this.compRange(array, ((i+1)+((i+1)/4)*g1+4*m)*g, ((i+7)+((i+7)/4)*g1+4*m)*g, g);
                    }
                }
                for (int i = 1; i+5 < j; i += 4) {
                    for (int m = 0; m < s; m++) {
                        this.compRange(array, ((i  )+((i  )/4)*g1+4*m)*g, ((i+3)+((i+3)/4)*g1+4*m)*g, g);
                        this.compRange(array, ((i+2)+((i+2)/4)*g1+4*m)*g, ((i+5)+((i+5)/4)*g1+4*m)*g, g);
                    }
                }
                for (int i = 2; i+3 < j; i += 4) {
                    for (int m = 0; m < s; m++) {
                        this.compRange(array, ((i  )+((i  )/4)*g1+4*m)*g, ((i+2)+((i+2)/4)*g1+4*m)*g, g);
                        this.compRange(array, ((i+1)+((i+1)/4)*g1+4*m)*g, ((i+3)+((i+3)/4)*g1+4*m)*g, g);
                    }
                }
                if (g1 > 0) {
                    for (int i = 3; i+3 < j; i += 4)
                        for (int m = 0; m < s; m++)
                            this.compRange(array, ((i  )+((i  )/4)*g1+4*m)*g, ((i+1)+((i+1)/4)*g1+4*m)*g, g);
                    for (int i = 5; i+3 < j; i += 4)
                        for (int m = 0; m < s; m++)
                            this.compRange(array, ((i  )+((i  )/4)*g1+4*m)*g, ((i+1)+((i+1)/4)*g1+4*m)*g, g);
                }
                else {
                    for (int i = 3; i+3 < j; i += 2)
                        for (int m = 0; m < s; m++)
                            this.compRange(array, ((i  )+((i  )/4)*g1+4*m)*g, ((i+1)+((i+1)/4)*g1+4*m)*g, g);
                }
            }
        }
    }
}
