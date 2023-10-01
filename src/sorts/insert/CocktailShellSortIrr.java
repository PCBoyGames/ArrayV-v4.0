package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 *
MIT License
Copyright (c) 2020 fungamer2
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

public class CocktailShellSortIrr extends Sort {

    public CocktailShellSortIrr(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Cocktail Shell (/irr test)");
        this.setRunAllSortsName("Cocktail Shell Sort (/irr test)");
        this.setRunSortName("Cocktail Shell Sort (/irr test)");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int gap = length / 2;
        boolean dir = true;
        while (gap >= 1) {
            if (dir) {
                for (int i = gap; i < length; i++) {
                    int tmp = array[i];
                    int j = i;
                    while (j >= gap && Reads.compareValues(array[j - gap], tmp) == 1) {
                        Highlights.markArray(2, j - gap);
                        Writes.write(array, j, array[j - gap], 0.7, true, false);
                        j -= gap;
                    }

                    if (j - gap >= 0) {
                        Highlights.markArray(2, j - gap);
                    } else {
                        Highlights.clearMark(2);
                    }

                    Writes.write(array, j, tmp, 0.7, true, false);
                }
            } else {
                for (int i = length - gap; i >= 0; i--) {
                    int tmp = array[i];
                    int j = i;
                    while (j < length - gap && Reads.compareValues(array[j + gap], tmp) == -1) {
                        Highlights.markArray(2, j + gap);
                        Writes.write(array, j, array[j + gap], 0.7, true, false);
                        j += gap;
                    }

                    if (j + gap < length) {
                        Highlights.markArray(2, j + gap);
                    } else {
                        Highlights.clearMark(2);
                    }

                    Writes.write(array, j, tmp, 0.7, true, false);
                }
            }
            gap /= Math.E;
            dir = gap == 1 ? true : !dir; //Do a normal insertion sort during the last pasd
        }
    }
}