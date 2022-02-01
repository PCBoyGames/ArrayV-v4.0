package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.GrailSorting;


final public class AdaptiveRemoteMergeSort extends GrailSorting {
    public AdaptiveRemoteMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Adaptive Remote Merge");
        this.setRunAllSortsName("Adaptive Remote Merge Sort");
        this.setRunSortName("Adaptive Remote Merge Sort");
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

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int t = length - (length / 3);
        for(int l=2; l<=t; l++) {
            int phase = 0;
            if(l == 2) {
                for(int i=0; i<length-1; i+=2) {
                    if(Reads.compareValues(array[i], array[i+1]) == 1) {
                        Writes.swap(array, i, i+1, 1, true, false);
                    }
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
                            this.grailMergeWithoutBuffer(array, i, l - (phase + 1), phase + 1);
                        }
                    } else if(i + l - phase <= length) {
                        this.grailMergeWithoutBuffer(array, i, l-(phase+1), next - (i + (l - (phase + 1))));
                    }
                    phase = (phase + 1) % (l - 1);
                }
            }
        }
        // halving the iteration length and merging the rest works in most cases, but it still fails
        // with a handful

        // *update 2* 2/3 iter len works well, though
        this.grailMergeWithoutBuffer(array, 0, t, length/3);
    }
}