package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 *
MIT License

Copyright (c) 2022 Meme Man, implemented by aphitorite

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

public final class DragSort extends Sort {
    public DragSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Drag");
        this.setRunAllSortsName("Drag Sort");
        this.setRunSortName("Dragsort");
        this.setCategory("Exchange Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(512);
        this.setBogoSort(false);
    }

    private boolean isSorted(int[] array, int a, int b, int idx) {
        int c = a, ce = c+1;

        for (int i = a; i < b; i++) {
            if (i == idx) continue;

            int cmp = Reads.compareIndices(array, i, idx, 0.001, true);

            c  += cmp <  0 ? 1 : 0;
            ce += cmp <= 0 ? 1 : 0;
        }
        return idx >= c && idx < ce;
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int a = 0, b = length;

        while (true) {
            int i = a;

            while (i < b && this.isSorted(array, a, b, i)) i++;

            if (i == b) break;

            for (int j = i++; i < b; i++) {
                if (!this.isSorted(array, a, b, i)) {
                    Writes.swap(array, j, i, 0.001, true, false);
                    j = i;
                }
            }
        }
    }
}
