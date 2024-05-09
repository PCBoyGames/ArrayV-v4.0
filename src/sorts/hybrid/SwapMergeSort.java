package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 *
MIT License

Copyright (c) 2023 Gaming32, fixed by Ayako-chan

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

public class SwapMergeSort extends Sort {

    public SwapMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("SwapMerge");
        this.setRunAllSortsName("SwapMerge Sort");
        this.setRunSortName("SwapMergeSort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);
        if (a != b) {
            int temp = array[a];
            int d = (a > b) ? -1 : 1;
            for (int i = a; i != b; i += d)
                Writes.write(array, i, array[i + d], 0.5, true, false);
            Writes.write(array, b, temp, 0.5, true, false);
        }
    }

    protected int expSearch(int[] array, int a, int b, int val) {
        int i = 1;
        while (b - i >= a && Reads.compareValues(val, array[b - i]) < 0) i *= 2;
        int a1 = Math.max(a, b - i + 1), b1 = b - i / 2;
        while (a1 < b1) {
            int m = a1 + (b1 - a1) / 2;
            Highlights.markArray(2, m);
            Delays.sleep(0.25);
            if (Reads.compareValues(val, array[m]) < 0) b1 = m;
            else a1 = m + 1;
        }
        return a1;
    }

    protected void insertSort(int[] array, int a, int b) {
        for (int i = a + 1; i < b; i++)
            insertTo(array, i, expSearch(array, a, i, array[i]));
    }

    public void merge(int[] array, int a, int m, int b) {
        int i = a, j = m;
        while (i < j && j < b) {
            if (Reads.compareValues(array[i], array[j]) > 0)
                Writes.multiSwap(array, j++, i, 0.025, true, false);
            i++;
        }
    }

    public void mergeSort(int[] array, int a, int b) {
        if (b - a < 32) {
            insertSort(array, a, b);
            return;
        }
        int m = a + (b - a) / 2;
        mergeSort(array, a, m);
        mergeSort(array, m, b);
        merge(array, a, m, b);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        mergeSort(array, 0, length);
    }
}
