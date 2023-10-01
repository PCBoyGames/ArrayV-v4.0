package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import sorts.insert.BinaryInsertionSort;

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

public class DualPivotStableQuickSort extends Sort {
    public DualPivotStableQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Dual-Pivot Stable Quick");
        this.setRunAllSortsName("Dual-Pivot Stable Quick Sort");
        this.setRunSortName("Dual-Pivot Stable Quicksort");
        this.setCategory("Quick Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int[] partition(int[] array, int[] tmp, int a, int b, int piv1, int piv2, int pCmp) {
        int i1 = a, j = 0, k = tmp.length;
        Highlights.markArray(2, i1);

        for (int i = a; i < b; i++) {
            if (Reads.compareIndexValue(array, i, piv1, 0.25, true) == -pCmp) {
                Writes.write(array, i1++, array[i], 0.25, false, false);
                Highlights.markArray(2, i1);
            }
            else if (Reads.compareIndexValue(array, i, piv2, 0.25, false) == pCmp)
                Writes.write(tmp, --k, array[i], 0.25, false, true);

            else Writes.write(tmp, j++, array[i], 0.25, false, true);
        }
        Highlights.clearMark(2);

        if (j < b-a) {
            Writes.arraycopy(tmp, 0, array, i1, j, 0.5, true, false);
            while (k < tmp.length) Writes.write(array, --b, tmp[k++], 0.5, true, false);
        }
        return new int[] {i1, i1+j};
    }

    private void quickSort(int[] array, int[] tmp, int a, int b) {
        if (b-a <= 32) {
            BinaryInsertionSort smallSort = new BinaryInsertionSort(this.arrayVisualizer);
            smallSort.customBinaryInsert(array, a, b, 0.25);
            return;
        }

        int s = (b-a)/3;
        int piv1, piv2;

        if (Reads.compareIndices(array, a+s, a+s+s, 1, true) > 0)
            {piv1 = array[a+s+s]; piv2 = array[a+s];}
        else
            {piv1 = array[a+s]; piv2 = array[a+s+s];}

        int[] t = this.partition(array, tmp, a, b, piv1, piv2, 1);
        int m1 = t[0], m2 = t[1];

        this.quickSort(array, tmp, a, m1);
        this.quickSort(array, tmp, m2, b);

        if (a == m1 && b == m2) {
            t = this.partition(array, tmp, m1, m2, piv1, piv2, 0);
            m1 = t[0]; m2 = t[1];
        }
        this.quickSort(array, tmp, m1, m2);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int[] tmp = Writes.createExternalArray(currentLength);
        this.quickSort(array, tmp, 0, currentLength);
        Writes.deleteExternalArray(tmp);
    }
}