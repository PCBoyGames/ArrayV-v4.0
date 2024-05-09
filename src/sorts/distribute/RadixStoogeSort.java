package sorts.distribute;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 *
MIT License

Copyright (c) 2020 thatsOven

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

public final class RadixStoogeSort extends Sort {
    public RadixStoogeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("LSD Radix Stooge");
        this.setRunAllSortsName("Radix Stooge Sort (LSD)");
        this.setRunSortName("Radix Stooge Sort (LSD)");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(false);
        this.setBucketSort(true);
        this.setRadixSort(true);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(512);
        this.setBogoSort(false);
    }

    public void stooge(int[] array, int a, int b, int d, int ba) {
        if (b - a + 1 < 3) {
            Highlights.markArray(0, a);
            Highlights.markArray(1, b);
            Delays.sleep(0.5);
            Highlights.clearAllMarks();

            int x = Reads.getDigit(array[a], d, ba),
                y = Reads.getDigit(array[b], d, ba);

            if (Reads.compareOriginalValues(x, y) > 0)
                Writes.swap(array, a, b, 0.5, true, false);
        } else {
            int t = (b - a + 1) / 3;
            stooge(array, a    , b - t, d, ba);
            stooge(array, a + t, b    , d, ba);
            stooge(array, a    , b - t, d, ba);
        }
    }

    public void sort(int[] array, int len, int ba) {
        int h = Reads.analyzeMaxLog(array, len, ba, 0.5, true);
        for (int d = 0; d <= h; d++)
            stooge(array, 0, len - 1, d, ba);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        sort(array, length, bucketCount);
    }
}
