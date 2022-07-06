package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 *
MIT License

Copyright (c) 2021 aphitorite

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

final public class OptimizedOutOfPlaceHeapSort extends Sort {
    public OptimizedOutOfPlaceHeapSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Optimized Out-of-Place Heap");
        this.setRunAllSortsName("Optimized Out-of-Place Heap Sort");
        this.setRunSortName("Optimized Out-of-Place Heapsort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    //implements modifications to quick heap sort from https://arxiv.org/pdf/1209.4214.pdf
    //to "out of place heap" already in arrayv

    private final int WLEN = 3;

    private boolean getBit(int[] bits, int idx) {
        int b = (bits[idx >> WLEN]) >> (idx & ((1 << WLEN) - 1)) & 1;
        return b == 1;
    }

    private void setBit(int[] bits, int idx, boolean r) {
        if (r) Writes.write(bits, idx >> WLEN, bits[idx >> WLEN] |  (1 << (idx & ((1 << WLEN) - 1))), 0, false, true);
        else  Writes.write(bits, idx >> WLEN, bits[idx >> WLEN] & ~(1 << (idx & ((1 << WLEN) - 1))), 0, false, true);
    }

    private void siftDown(int[] array, int[] comp, int[] flag, int r, int n) {
        int i = r;

        while (2*i+2 < n) {
            i = 2*i+1;
            this.setBit(comp, i/2, Reads.compareValues(array[i+1], array[i]) > 0); //comp == right?
            if (this.getBit(comp, i/2)) i++;
        }
        if (2*i+1 < n) i = 2*i+1;

        while (i > r) {
            if (Reads.compareValues(array[i], array[r]) > 0) {
                while (i > r) {
                    Writes.swap(array, r, i, 1, true, false);
                    this.setBit(flag, (i-1)/2, false);
                    i = (i-1)/2;
                }
                return;
            }
            else this.setBit(flag, (i-1)/2, true);

            i = (i-1)/2;
        }
    }

    private void findNext(int[] array, int[] bits, int[] comp, int[] flag, int b) {
        int i = 0, l = 1, r = 2;

        while (r < b && !(this.getBit(bits, l) && this.getBit(bits, r))) {
            if (this.getBit(flag, l/2)) {
                if (this.getBit(comp, l/2)) {
                    Writes.write(array, i, array[r], 1, true, false);
                    i = r;
                }
                else {
                    Writes.write(array, i, array[l], 1, true, false);
                    i = l;
                }
                this.setBit(flag, l/2, false);
            }
            else if (this.getBit(bits, l)) {
                Writes.write(array, i, array[r], 1, true, false);
                i = r;
            }
            else if (this.getBit(bits, r)) {
                Writes.write(array, i, array[l], 1, true, false);
                i = l;
            }
            else if (Reads.compareValues(array[r], array[l]) > 0) {
                Writes.write(array, i, array[r], 1, true, false);
                i = r;
            }
            else {
                Writes.write(array, i, array[l], 1, true, false);
                i = l;
            }
            l = 2*i + 1;
            r = l+1;
        }
        if (l < b && !this.getBit(bits, l)) {
            Writes.write(array, i, array[l], 1, true, false);
            i = l;
        }
        this.setBit(bits, i, true);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int[] bits = Writes.createExternalArray(((length-1) >> WLEN) + 1);
        int[] comp = Writes.createExternalArray(((length/2-1) >> WLEN) + 1);
        int[] flag = Writes.createExternalArray(((length/2-1) >> WLEN) + 1);

        for (int i = (length-1)/2; i >= 0; i--)
            this.siftDown(array, comp, flag, i, length);
        Highlights.clearMark(2);

        int[] tmp = Writes.createExternalArray(length);

        for (int i = length-1; i >= 0; i--) {
            Highlights.markArray(2, i);
            Writes.write(tmp, i, array[0], 1, true, true);
            this.findNext(array, bits, comp, flag, length);
        }
        Highlights.clearMark(2);
        Writes.arraycopy(tmp, 0, array, 0, length, 1, true, false);
        Writes.deleteExternalArray(bits);
        Writes.deleteExternalArray(comp);
        Writes.deleteExternalArray(flag);
        Writes.deleteExternalArray(tmp);
    }
}