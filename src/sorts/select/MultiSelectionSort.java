package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

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

public class MultiSelectionSort extends Sort {
    public MultiSelectionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Multi Selection");
        this.setRunAllSortsName("Multi Selection Sort");
        this.setRunSortName("Multi Selectionsort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int monoboundBw(int[] array, int start, int end, int value) {
        int top, mid;

        top = end - start;

        while (top > 1) {
            mid = top / 2;

            if (Reads.compareIndexValue(array, start + mid, value, 0.5, true) > 0) {
                start += mid;
            }
            top -= mid;
        }

        if (Reads.compareIndexValue(array, start, value, 0.5, true) > 0) {
            return start + 1;
        }
        return start;
    }

    private int monoboundFw(int[] array, int start, int end, int value) {
        int top, mid;

        top = end - start;

        while (top > 1) {
            mid = top / 2;
            
            if (Reads.compareValueIndex(array, value, end - mid, 0.5, true) <= 0) {
                end -= mid;
            }
            top -= mid;
        }

        if (Reads.compareValueIndex(array, value, end - 1, 0.5, true) <= 0) {
            return end - 1;
        }
        return end;
    }

    protected void moveFront(int[] array, int start, int mid, int end) {
        int stop = Math.max(mid, start + (end - start) / 2);
        while (end > stop) {
            Writes.swap(array, start++, end--, 1, true, false);
        }
    }

    protected void moveBack(int[] array, int start, int mid, int end) {
        while (mid > start) {
            Writes.swap(array, mid--, end--, 1, true, false);
        }
    }

    protected int selectSmallest(int[] array, int left, int runEnd, int right) {
        int runStart = left;
        int i = runEnd + 1;
        Highlights.markArray(3, runStart);
        Highlights.markArray(4, runEnd);
        while (i < right) {
            if (Reads.compareIndices(array, i, runEnd, 0.5, true) <= 0) {
                if (runEnd < i - 1) {
                    runStart = i;
                    Highlights.markArray(3, runStart);
                }
                runEnd = i;
                Highlights.markArray(4, runEnd);
            }
            else if (runEnd != runStart && Reads.compareIndices(array, i, runStart, 0.5, true) < 0) {
                runStart = monoboundBw(array, runStart, runEnd + 1, array[i]);
                Highlights.markArray(3, runStart);
            }
            i++;
        }
        Highlights.clearMark(3);
        Highlights.clearMark(4);
        moveFront(array, left, runStart - 1, runEnd);
        return left + (runEnd - runStart + 1);
    }

    protected int selectLargest(int[] array, int left, int runEnd, int right) {
        int runStart = left;
        int i = runEnd + 1;
        Highlights.markArray(3, runStart);
        Highlights.markArray(4, runEnd);
        while (i < right) {
            if (Reads.compareIndices(array, i, runEnd, 0.5, true) >= 0) {
                if (runEnd < i - 1) {
                    runStart = i;
                    Highlights.markArray(3, runStart);
                }
                runEnd = i;
                Highlights.markArray(4, runEnd);
            }
            else if (runEnd != runStart && Reads.compareIndices(array, i, runStart, 0.5, true) > 0) {
                runStart = monoboundFw(array, runStart, runEnd + 1, array[i]);
                Highlights.markArray(3, runStart);
            }
            i++;
        }
        Highlights.clearMark(3);
        Highlights.clearMark(4);
        if (runEnd != right - 1) {
            moveBack(array, runStart - 1, runEnd, right - 1);
        }
        return right - (runEnd - runStart + 1);
    }
    
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int left = 0, right = currentLength;
        while (left < right - 1) {
            if (Reads.compareIndices(array, left, left + 1, 1, true) > 0) {
                left = selectSmallest(array, left, left + 1, right);
            } else {
                right = selectLargest(array, left, left + 1, right);
            }
        }
    }
}
