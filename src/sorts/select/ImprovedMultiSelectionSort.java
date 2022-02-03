package sorts.select;

import main.ArrayVisualizer;

/*
 * 
MIT License

Copyright (c) 2021 Gaming32

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

public class ImprovedMultiSelectionSort extends MultiSelectionSort {
    public ImprovedMultiSelectionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Improved Multi Selection");
        this.setRunAllSortsName("Improved Multi Selection Sort");
        this.setRunSortName("Improved Multi Selectionsort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int left = 0, right = currentLength;
        boolean dir = Reads.compareIndices(array, left, left + 1, 1, true) > 0;
        if (Reads.compareIndices(array, left, left + 1, 1, true) > 0) {
            left = selectSmallest(array, left, left + 1, right);
        } else {
            right = selectLargest(array, left, left + 1, right);
        }
        boolean sameDir = dir;
        int sameDirCnt = 1;
        int sameDirMax = 1;
        while (sameDirMax * sameDirMax < currentLength) {
            sameDirMax *= 2;
        }
        while (left < right - 1) {
            int runEnd = left + 1;
            dir = Reads.compareIndices(array, left, left + 1, 1, true) > 0;
            if (dir == sameDir) {
                sameDirCnt++;
                if (sameDirCnt == sameDirMax) {
                    sameDirCnt = 0;
                    dir = !dir;
                    runEnd--;
                }
            } else {
                sameDirCnt = 0;
            }
            if (dir) {
                left = selectSmallest(array, left, runEnd, right);
            } else {
                right = selectLargest(array, left, runEnd, right);
            }
        }
    }
}
