package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.MergeSorting;

/*
 *
MIT License

Copyright (c) 2019 w0rthy

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

final public class nlognStoogeSort extends MergeSorting {
    public nlognStoogeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("n log n Stooge");
        this.setRunAllSortsName("O(n log n) Stooge Sort");
        this.setRunSortName("nlogn-Stoogesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void mergeNR(int[] array, int[] tmp, int start, int mid, int end) {
        if(start == mid) return;

        int low = start;
        int high = mid;

        for(int nxt = 0; nxt < end - start; nxt++){
            if(low >= mid && high >= end) break;

            Highlights.markArray(1, low);
            Highlights.markArray(2, high);

            if(low < mid && high >= end){
                Highlights.clearMark(2);
                Writes.write(tmp, nxt, array[low++], 1, false, true);
            }
            else if(low >= mid && high < end){
                Highlights.clearMark(1);
                Writes.write(tmp, nxt, array[high++], 1, false, true);
            }
            else if(Reads.compareValues(array[low], array[high]) <= 0){
                Writes.write(tmp, nxt, array[low++], 1, false, true);
            }
            else{
                Writes.write(tmp, nxt, array[high++], 1, false, true);
            }
        }
        Highlights.clearMark(2);

        for(int i = 0; i < end - start; i++){
            Writes.write(array, start + i, tmp[i], 1, true, false);
        }
    }

    private void NLNStooge(int[] array, int[] tmp, int start, int end) {
        if(start == end)
            return;

        int third = (end-start+1) / 3;

        if(third == 0)
            return;

        this.NLNStooge(array, tmp, start, start+third);
        this.NLNStooge(array, tmp, start+third, end-third);
        this.NLNStooge(array, tmp, end-third, end);

        this.mergeNR(array, tmp, start, start+third, end-third);
        this.mergeNR(array, tmp, start+third, end-third, end);
        this.mergeNR(array, tmp, start, start+third, end-third);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int[] t = Writes.createExternalArray(length);
        this.NLNStooge(array, t, 0, length);
        Writes.deleteExternalArray(t);
    }
}