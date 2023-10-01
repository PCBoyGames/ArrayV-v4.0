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

public class OptimizedDualPivotStableQuickSort extends Sort {
    public OptimizedDualPivotStableQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Optimized Dual-Pivot Stable Quick");
        this.setRunAllSortsName("Optimized Dual-Pivot Stable Quick Sort");
        this.setRunSortName("Optimized Dual-Pivot Stable Quicksort");
        this.setCategory("Quick Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int MIN_INSERT = 32;

    private int[] selectPiv(int[] array, int a, int b) {
        int s = (b-a)/3;

        if (Reads.compareIndices(array, a+s, a+s+s, 1, true) > 0)
            return new int[] {a+s+s, a+s};
        else
            return new int[] {a+s, a+s+s};
    }

    private void quickSortFW(int[] array, int[] tmp, int a, int b, int pa, int pb) {
        if (b-a < this.MIN_INSERT) {
            BinaryInsertionSort smallSort = new BinaryInsertionSort(this.arrayVisualizer);
            smallSort.customBinaryInsert(array, a, b, 0.25);

            return;
        }

        int[] t = this.selectPiv(array, a, b);
        int piv1 = array[t[0]], piv2 = array[t[1]];

        int a1 = a, j = pa, k = pb;

        for (int i = a; i < b; i++) {
            if (Reads.compareIndexValue(array, i, piv1, 0.25, true) < 0) {
                Highlights.markArray(2, a1);
                Writes.write(array, a1++, array[i], 0.25, false, false);
            }
            else if (Reads.compareIndexValue(array, i, piv2, 0.25, false) > 0)
                Writes.write(tmp, --k, array[i], 0.25, false, true);

            else Writes.write(tmp, j++, array[i], 0.25, false, true);
        }
        Highlights.clearAllMarks();

        int b1 = b-(pb-k);

        this.quickSortFW(array, tmp, a, a1, j, k);
        this.quickSortBWExt(array, tmp, b1, b, k, pb);

        if (a1 == a && k == pb) {
            j = pa;

            for (int i = pa; i < pb; i++) {
                if (Reads.compareIndexValue(tmp, i, piv1, 0.25, true) == 0) {
                    Highlights.markArray(2, a1);
                    Writes.write(array, a1++, tmp[i], 0.25, false, false);
                }
                else if (Reads.compareIndexValue(tmp, i, piv2, 0.25, false) == 0) {
                    Highlights.markArray(3, b1-1);
                    Writes.write(array, --b1, tmp[i], 0.25, false, false);
                }
                else Writes.write(tmp, j++, tmp[i], 0.25, false, true);
            }
            Highlights.clearAllMarks();

            Writes.reversal(array, b1, b-1, 0.25, true, false);
            Highlights.clearAllMarks();
        }
        this.quickSortFWExt(array, tmp, a1, b1, pa, j);
    }

    private void quickSortBW(int[] array, int[] tmp, int a, int b, int pa, int pb) {
        if (b-a < this.MIN_INSERT) {
            Writes.reversal(array, a, b-1, 0.25, true, false);
            BinaryInsertionSort smallSort = new BinaryInsertionSort(this.arrayVisualizer);
            smallSort.customBinaryInsert(array, a, b, 0.25);

            return;
        }

        int[] t = this.selectPiv(array, a, b);
        int piv1 = array[t[0]], piv2 = array[t[1]];

        int b1 = b, j = pa, k = pb;

        for (int i = b-1; i >= a; i--) {
            if (Reads.compareIndexValue(array, i, piv1, 0.25, true) < 0)
                Writes.write(tmp, j++, array[i], 0.25, false, true);

            else if (Reads.compareIndexValue(array, i, piv2, 0.25, false) > 0) {
                Highlights.markArray(2, b1-1);
                Writes.write(array, --b1, array[i], 0.25, false, false);
            }
            else Writes.write(tmp, --k, array[i], 0.25, false, true);
        }
        Highlights.clearAllMarks();

        int a1 = a+(j-pa);

        this.quickSortFWExt(array, tmp, a, a1, pa, j);
        this.quickSortBW(array, tmp, b1, b, j, k);

        if (j == pa && b1 == b) {
            k = pb;

            for (int i = pb-1; i >= pa; i--) {
                if (Reads.compareIndexValue(tmp, i, piv1, 0.25, true) == 0) {
                    Highlights.markArray(2, a1);
                    Writes.write(array, a1++, tmp[i], 0.25, false, false);
                }
                else if (Reads.compareIndexValue(tmp, i, piv2, 0.25, false) == 0) {
                    Highlights.markArray(3, b1-1);
                    Writes.write(array, --b1, tmp[i], 0.25, false, false);
                }
                else Writes.write(tmp, --k, tmp[i], 0.25, false, true);
            }
            Highlights.clearAllMarks();

            Writes.reversal(array, b1, b-1, 0.25, true, false);
            Highlights.clearAllMarks();
        }
        this.quickSortBWExt(array, tmp, a1, b1, k, pb);
    }

    private void quickSortFWExt(int[] array, int[] tmp, int a, int b, int pa, int pb) {
        if (b-a < this.MIN_INSERT) {
            Writes.arraycopy(tmp, pa, array, a, b-a, 0.25, true, false);
            BinaryInsertionSort smallSort = new BinaryInsertionSort(this.arrayVisualizer);
            smallSort.customBinaryInsert(array, a, b, 0.25);

            return;
        }

        int[] t = this.selectPiv(tmp, pa, pb);
        int piv1 = tmp[t[0]], piv2 = tmp[t[1]];

        int a1 = a, b1 = b, j = pa;

        for (int i = pa; i < pb; i++) {
            if (Reads.compareIndexValue(tmp, i, piv1, 0.25, true) < 0) {
                Highlights.markArray(2, a1);
                Writes.write(array, a1++, tmp[i], 0.25, false, false);
            }
            else if (Reads.compareIndexValue(tmp, i, piv2, 0.25, false) > 0) {
                Highlights.markArray(3, b1-1);
                Writes.write(array, --b1, tmp[i], 0.25, false, false);
            }
            else Writes.write(tmp, j++, tmp[i], 0.25, false, true);
        }
        Highlights.clearAllMarks();

        int k = pb-(b-b1);

        this.quickSortFW(array, tmp, a, a1, j, k);
        this.quickSortBW(array, tmp, b1, b, k, pb);

        if (a1 == a && b1 == b) {
            j = pa;

            for (int i = pa; i < pb; i++) {
                if (Reads.compareIndexValue(tmp, i, piv1, 0.25, true) == 0) {
                    Highlights.markArray(2, a1);
                    Writes.write(array, a1++, tmp[i], 0.25, false, false);
                }
                else if (Reads.compareIndexValue(tmp, i, piv2, 0.25, false) == 0) {
                    Highlights.markArray(3, b1-1);
                    Writes.write(array, --b1, tmp[i], 0.25, false, false);
                }
                else Writes.write(tmp, j++, tmp[i], 0.25, false, true);
            }
            Highlights.clearAllMarks();

            Writes.reversal(array, b1, b-1, 0.25, true, false);
            Highlights.clearAllMarks();
        }
        this.quickSortFWExt(array, tmp, a1, b1, pa, j);
    }

    private void quickSortBWExt(int[] array, int[] tmp, int a, int b, int pa, int pb) {
        if (b-a < this.MIN_INSERT) {
            int b1 = b;
            while (pa < pb) Writes.write(array, --b1, tmp[pa++], 0.25, true, false);
            BinaryInsertionSort smallSort = new BinaryInsertionSort(this.arrayVisualizer);
            smallSort.customBinaryInsert(array, a, b, 0.25);

            return;
        }

        int[] t = this.selectPiv(tmp, pa, pb);
        int piv1 = tmp[t[0]], piv2 = tmp[t[1]];

        int a1 = a, b1 = b, k = pb;

        for (int i = pb-1; i >= pa; i--) {
            if (Reads.compareIndexValue(tmp, i, piv1, 0.25, true) < 0) {
                Highlights.markArray(2, a1);
                Writes.write(array, a1++, tmp[i], 0.25, false, false);
            }
            else if (Reads.compareIndexValue(tmp, i, piv2, 0.25, false) > 0) {
                Highlights.markArray(3, b1-1);
                Writes.write(array, --b1, tmp[i], 0.25, false, false);
            }
            else Writes.write(tmp, --k, tmp[i], 0.25, false, true);
        }
        Highlights.clearAllMarks();

        int j = pa+(a1-a);

        this.quickSortFW(array, tmp, a, a1, pa, j);
        this.quickSortBW(array, tmp, b1, b, j, k);

        if (a1 == a && b1 == b) {
            k = pb;

            for (int i = pb-1; i >= pa; i--) {
                if (Reads.compareIndexValue(tmp, i, piv1, 0.25, true) == 0) {
                    Highlights.markArray(2, a1);
                    Writes.write(array, a1++, tmp[i], 0.25, false, false);
                }
                else if (Reads.compareIndexValue(tmp, i, piv2, 0.25, false) == 0) {
                    Highlights.markArray(3, b1-1);
                    Writes.write(array, --b1, tmp[i], 0.25, false, false);
                }
                else Writes.write(tmp, --k, tmp[i], 0.25, false, true);
            }
            Highlights.clearAllMarks();

            Writes.reversal(array, b1, b-1, 0.25, true, false);
            Highlights.clearAllMarks();
        }
        this.quickSortBWExt(array, tmp, a1, b1, k, pb);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int[] tmp = Writes.createExternalArray(currentLength);
        this.quickSortFW(array, tmp, 0, currentLength, 0, currentLength);
        Writes.deleteExternalArray(tmp);
    }
}