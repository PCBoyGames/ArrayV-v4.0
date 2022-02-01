package sorts.select;

import main.ArrayVisualizer;
import sorts.insert.InsertionSort;
import sorts.templates.GrailSorting;

// Distray stuff
final public class OutOfPlaceLazyHeapSort extends GrailSorting {
    public OutOfPlaceLazyHeapSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("OOP Lazy Heap");
        this.setRunAllSortsName("Out-Of-Place Lazy Heap Sort");
        this.setRunSortName("OOP Lazy Heapsort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    // I initially had plans that would have made this O(n + n^0.5) memory stable,
    // but I wanted to have linear memory, so I decided to do this.

    // Doing this shenanigan cuts the memory down to O(n), but it makes it unstable
    // at the very end.
    private boolean max2Temp(int[] array, int[] tmp, int a, int b, int ptr, int nop) {
        int max = a, maxVal = array[a];


        for(int i = a+1; i < b; i++) {
            if(array[i] == nop)
                continue;
            if(Reads.compareIndices(array, i, max, 0.1, true) > 0) {
                max = i;
                maxVal = array[i];
            }
        }

        if(maxVal == nop)
            return false;

        Writes.write(array, max, nop, 0.1, true, false);
        Writes.write(tmp, ptr, maxVal, 0.1, true, true);

        return true;
    }

    private int findLastMax(int[] array, int start, int end, int nop) {
        int max = start;

        for(int i=start+1; i<end; i++) {
            if(array[i] == nop)
                continue;
            if(Reads.compareIndices(array, i, max, 0.5, true) > 0) {
                max = i;
            }
        }

        if(array[max] == nop)
            return -1;
        return max;
    }

    private void pushRemainder(int[] from, int[] to, int offset, int start, int end, int buffer, int nop) {
        int j=offset;
        for(int i=start; i<end; i++) {
            if(from[i] != nop) {
                while(to[j] != nop)
                    j++;
                Writes.write(to, j++, from[i], 0.5, true, true);
            }
        }
        InsertionSort s = new InsertionSort(arrayVisualizer);
        int target = Math.max(buffer, j);
        s.customInsertSort(to, offset, target, 0.125, true);
        // this.merge(to, from, start, j, end);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int s = (int)Math.sqrt(length-1)+1,
            blocks = (length - 1) / s + 1,
            nop = Reads.analyzeMin(array, length, 0.1, true) - 1;

        int[] tmp = Writes.createExternalArray(length);

        for(int i=0; i<length; i+=s) {
            this.max2Temp(array, tmp, i, Math.min(i+s, length), i/s, nop);
        }
        int i=length-1;
        for(; i>=blocks; i--) {
            int max = this.findLastMax(tmp, 0, blocks, nop);
            if(max == -1)
                break;
            Writes.write(tmp, i, tmp[max], 1, true, true);
            int loc = max * s, loc2 = Math.min((max+1)*s, length);
            if(!max2Temp(array, tmp, loc, loc2, max, nop)) {
                Writes.write(tmp, max, nop, 0.5, true, true);
            }
        }
        this.pushRemainder(array, tmp, 0, 0, length, blocks, nop);
        Writes.arraycopy(tmp, 0, array, 0, length, 0.5, true, false);
        Writes.deleteExternalArray(tmp);
    }
}