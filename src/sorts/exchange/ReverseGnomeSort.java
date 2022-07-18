package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;
/*
*
MIT License
Copyright (c) 2021-2022 Ayako-chan
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

/**
 * This is the reversed variant of Optimized Gnomesort.
 *
 * @author Ayako-chan
 *
 */
public final class ReverseGnomeSort extends Sort {

    public ReverseGnomeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Reverse Gnome");
        this.setRunAllSortsName("Reverse Gnome Sort");
        this.setRunSortName("Reverse Gnomesort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);

    }

    private void reverseGnomeSort(int[] array, int lowerBound, int upperBound, double sleep) {
        int pos = lowerBound;

        while (pos < upperBound && Reads.compareValues(array[pos], array[pos + 1]) == 1) {
            Writes.swap(array, pos, pos + 1, sleep, true, false);
            pos++;
        }
    }

    public void customSort(int[] array, int low, int high, double sleep) {
        for (int i = high - 1; i >= low; i--) {
            reverseGnomeSort(array, i, high - 1, sleep);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        for (int i = sortLength - 1; i >= 0; i--) {
            reverseGnomeSort(array, i, sortLength - 1, 0.05);
        }

    }

}