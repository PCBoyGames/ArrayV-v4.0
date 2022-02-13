package sorts.merge;

import main.ArrayVisualizer;
import sorts.insert.BinaryInsertionSort;
import sorts.templates.GrailSorting;


final public class ExtraAdaptiveRemoteMergeSort extends GrailSorting {
    public ExtraAdaptiveRemoteMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Extra-Adaptive Remote Merge");
        this.setRunAllSortsName("Extra-Adaptive Remote Merge Sort");
        this.setRunSortName("Extra-Adaptive Remote Merge Sort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    private void firstPhase(int[] array, int start, int end) {
        int pos = end - 1, keyTemp = array[end];

        while (pos >= start && Reads.compareValues(array[pos], keyTemp) == 1) {
            Writes.write(array, pos + 1, array[pos], 1, true, false);
            pos--;
        }
        Writes.write(array, pos + 1, keyTemp, 1, true, false);
    }

    private void lastPhase(int[] array, int start, int end) {
        int pos = start + 1, keyTemp = array[pos-1];

        while (pos < end && Reads.compareValues(array[pos], keyTemp) == -1) {
            Writes.write(array, pos - 1, array[pos], 1, true, false);
            pos++;
        }
        Writes.write(array, pos - 1, keyTemp, 1, true, false);
    }

    private void lrMerge(int[] array, int[] tmp, int start, int mid, int end) {
        if(start == mid || mid == end) return;

        if(mid-start < end-mid) {
            Writes.arraycopy(array, start, tmp, 0, mid-start, 0.5, true, true);

            int low = 0, high = mid, nxt = start;

            while(low < mid-start && high < end) {
                Highlights.markArray(1, low+start);
                Highlights.markArray(2, high);
                if(Reads.compareValues(tmp[low], array[high]) <= 0){
                    Writes.write(array, nxt++, tmp[low++], 1, false, false);
                } else {
                    Writes.write(array, nxt++, array[high++], 1, false, false);
                }
            }

            while(low < mid-start) {
                Highlights.markArray(1, low+start);
                Writes.write(array, nxt++, tmp[low++], 1, false, false);
            }

            Highlights.clearMark(1);
            Highlights.clearMark(2);
            return;
        }

        Writes.arraycopy(array, mid, tmp, 0, end-mid, 0.5, true, true);

        int low = mid-1, high = end-mid-1, nxt = end-1;

        while(low >= start && high >= 0) {
            Highlights.markArray(1, low);
            Highlights.markArray(2, high+mid);
            if(Reads.compareValues(array[low], tmp[high]) > 0){
                Writes.write(array, nxt--, array[low--], 1, false, false);
            } else {
                Writes.write(array, nxt--, tmp[high--], 1, false, false);
            }
        }

        while(high >= 0) {
            Writes.write(array, nxt--, tmp[high--], 1, false, false);
        }

        Highlights.clearMark(1);
        Highlights.clearMark(2);

    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int t = (length + 1) / 2,
            min = 32;
        BinaryInsertionSort a = new BinaryInsertionSort(arrayVisualizer);
        if(t <= min) {
            a.customBinaryInsert(array, 0, length, 0.25);
            return;
        }
        int[] tmp = Writes.createExternalArray(Math.max(min, (int) Math.sqrt(t)));
        for(int l=min; l<=t; l++) {
            int phase = 0;
            if(l == min) {
                for(int i=0; i<length; i+=l) {
                    a.customBinaryInsert(array, i, Math.min(i+l, length), 0.25);
                }
            } else {
                for(int i=0; i<length; i+=l) {
                    int next = Math.min(i+l, length);
                    if(i + l <= length) {
                        if(phase == 0) {
                            this.firstPhase(array, i, next-1);
                        } else if(phase == l-2) {
                            this.lastPhase(array, i, next);
                        } else {
                            this.lrMerge(array, tmp, i, i+l-(phase+1), i+l);
                        }
                    } else if(i + l - phase <= length) {
                        this.lrMerge(array, tmp, i, i+l-(phase+1), next);
                    }
                    phase = (phase + 1) % (l - 1);
                }
            }
        }
        this.grailMergeWithoutBuffer(array, 0, t, length-t);
        Writes.deleteExternalArray(tmp);
    }
}