package sorts.quick;

import main.ArrayVisualizer;
import sorts.insert.InsertionSort;
import sorts.templates.Sort;

public class AeosDPQSort extends Sort {
    private InsertionSort insertSorter;

    private int[] smallsAux;
    private int[] middleAux;
    private int[] largesAux;
    private int[]  indexAux;

    private double rSleep = 0.5;
    private double wSleep = 0.5;

    public AeosDPQSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("AeosDPQsort");
        this.setRunAllSortsName("Aeos Dual Pivot Quick Sort");
        this.setRunSortName("Aeos Dual-Pivot Quicksort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    // brief method for zeroing out an aux array (for visualization)
    private void clearArray(int[] array) {
        for(int i = 0; i < array.length; i++)
            array[i] = 0;
    }

    private int medianOf3(int[] array, int[] indices) {
        // small length cases

        // maybe an error would be better but w/e
        if(indices.length == 0) return -1;

        // median of 1 or 2 elements can just be the first
        if(indices.length < 3) return indices[0];

        // 3 element case (common)
        // only first 3 elements are considered if given an array of 4+ indices
        if(Reads.compareIndices(array, indices[0], indices[1], rSleep, true) <= 0) {
            if(Reads.compareIndices(array, indices[1], indices[2], rSleep, true) <= 0)
                return indices[1];
            if(Reads.compareIndices(array, indices[0], indices[2], rSleep, true) < 0)
                return indices[2];
            return indices[0];
        }
        if(Reads.compareIndices(array, indices[1], indices[2], rSleep, true) >= 0) {
            return indices[1];
        }
        if(Reads.compareIndices(array, indices[0], indices[2], rSleep, true) <= 0) {
            return indices[0];
        }
        return indices[2];
    }

    private long medianOf5(int[] array, int[] indices) { // 7-depth decision tree
        if(indices.length < 5) return -1; // maybe an error better

        int smallMed, largeMed;

        // first 3 steps of decision tree
        int temp; // Note that these are all index moves, not element moves
        if(Reads.compareIndices(array, indices[0], indices[1], rSleep, true) > 0) {
            temp = indices[0];
            indices[0] = indices[1];
            indices[1] = temp;
        }
        if(Reads.compareIndices(array, indices[2], indices[3], rSleep, true) > 0) {
            temp = indices[2];
            indices[2] = indices[3];
            indices[3] = temp;
        }
        if(Reads.compareIndices(array, indices[0], indices[2], rSleep, true) > 0) {
            temp = indices[0];
            indices[0] = indices[2];
            indices[2] = temp;
            temp = indices[1];
            indices[3] = indices[1];
            indices[3] = temp;
        }
        // Now have 0 <= 1, 0 <= 2, and 2 <= 3

        // last 4 steps of decision tree (or 3 in exactly 1 branch: 2^7 - 5! = 128 - 120 = 8
        // permutations can be determined faster, and there are 2^3 = 8 ways to get this stage,
        // meaning there is 8 / 8 = 1 branch that skip a comparison)

        // I usually use >, but I sometimes use >= to fast-track the all-equal case to the
        // short branch. But regardless, it's arbitrary.
        if(Reads.compareIndices(array, indices[2], indices[4], rSleep, true) >= 0) {
            if(Reads.compareIndices(array, indices[0], indices[4], rSleep, true) >= 0) {
                smallMed = indices[0];
                if(Reads.compareIndices(array, indices[1], indices[2], rSleep, true) > 0) {
                    if(Reads.compareIndices(array, indices[1], indices[3], rSleep, true) > 0)
                        largeMed = indices[3];
                    else
                        largeMed = indices[1];
                } else
                    largeMed = indices[2]; // Congratulations! This is the short branch.
            } else {
                if(Reads.compareIndices(array, indices[1], indices[2], rSleep, true) > 0) {
                    smallMed = indices[4];
                    if(Reads.compareIndices(array, indices[1], indices[3], rSleep, true) > 0)
                        largeMed = indices[3];
                    else
                        largeMed = indices[1];
                 } else {
                     largeMed = indices[2];
                     if(Reads.compareIndices(array, indices[1], indices[4], rSleep, true) > 0)
                         smallMed = indices[4];
                     else
                         smallMed = indices[1];
                 }
            }
        } else {
            if(Reads.compareIndices(array, indices[3], indices[4], rSleep, true) > 0) {
                if(Reads.compareIndices(array, indices[1], indices[4], rSleep, true) > 0) {
                    smallMed = indices[2];
                    if(Reads.compareIndices(array, indices[1], indices[3], rSleep, true) > 0)
                        largeMed = indices[3];
                    else
                        largeMed = indices[1];
                } else {
                    largeMed = indices[4];
                    if(Reads.compareIndices(array, indices[1], indices[2], rSleep, true) > 0)
                        smallMed = indices[2];
                    else
                        smallMed = indices[1];
                }
            } else {
                if(Reads.compareIndices(array, indices[1], indices[3], rSleep, true) > 0) {
                    smallMed = indices[2];
                    if(Reads.compareIndices(array, indices[1], indices[4], rSleep, true) > 0)
                        largeMed = indices[4];
                    else
                        largeMed = indices[1];
                } else {
                    largeMed = indices[3];
                    if(Reads.compareIndices(array, indices[1], indices[2], rSleep, true) > 0)
                        smallMed = indices[2];
                    else
                        smallMed = indices[1];
                }
            }
        }

         return ((long) smallMed) << 32 | (long) largeMed;
    }

    private long medianOf15(int[] array, int start, int end) {
        int    length = end - start;
        int      half =  length / 2;
        int   quarter =    half / 2;
        int    eighth = quarter / 2;
        int sixteenth =  eighth / 2;

        // Provides good rounding without possibility of int overflow
        int[] samples0 = new int[] {start + sixteenth, start + quarter,
                start + quarter + eighth + sixteenth, start + half + eighth,
                start + half + quarter + sixteenth};
        int[] samples1 = new int[] {start + eighth, start + quarter + sixteenth, start + half,
                start + half + eighth + sixteenth, start + half + quarter + eighth};
        int[] samples2 = new int[] {start + sixteenth + eighth, start + quarter + eighth,
                start + half + sixteenth, start + half + quarter,
                start + half + quarter + eighth + sixteenth};

        long meds0 = medianOf5(array, samples0);
        long meds1 = medianOf5(array, samples1);
        long meds2 = medianOf5(array, samples2);

        int smallMed0 = (int) (meds0 >> 32);
        int largeMed0 = (int)  meds0;
        int smallMed1 = (int) (meds1 >> 32);
        int largeMed1 = (int)  meds1;
        int smallMed2 = (int) (meds2 >> 32);
        int largeMed2 = (int)  meds2;

        int smallMed = medianOf3(array, new int[] {smallMed0, smallMed1, smallMed2});
        int largeMed = medianOf3(array, new int[] {largeMed0, largeMed1, largeMed2});

        return ((long) smallMed) << 32 | (long) largeMed;
    }

    private long medianOf5Consec(int[] array, int start) {
        int[] samples = new int[] {start++, start++, start++, start++, start};
        return medianOf5(array, samples);
    }

    private long mOMHelper(int[] array, int start, int length) {
        if(length == 5) return medianOf5Consec(array, start);

        int  third = length / 3;
        long meds0 = mOMHelper(array, start, third);
        long meds1 = mOMHelper(array, start + third, third);
        long meds2 = mOMHelper(array, start + 2 * third, third);

        int smallMed0 = (int) (meds0 >> 32);
        int largeMed0 = (int)  meds0;
        int smallMed1 = (int) (meds1 >> 32);
        int largeMed1 = (int)  meds1;
        int smallMed2 = (int) (meds2 >> 32);
        int largeMed2 = (int)  meds2;

        int smallMed = medianOf3(array, new int[] {smallMed0, smallMed1, smallMed2});
        int largeMed = medianOf3(array, new int[] {largeMed0, largeMed1, largeMed2});

        return ((long) smallMed) << 32 | (long) largeMed;
    }

    private long medianOfMedians(int[] array, int start, int length) {
        if(length == 5) return medianOf5Consec(array, start);

        length    /= 5; // because base case is 5, not 1
        int nearPower = (int) Math.pow(3, Math.round(Math.log(length)/Math.log(3)));
        if(nearPower == length)
            return mOMHelper(array, start, length * 5);
        length    *= 5;
        nearPower *= 5;

        nearPower /= 3;
        // uncommon but can happen with numbers slightly smaller than 2*3^k, or here 10*3^k
        // (e.g., 17 < 18 and 47 < 54, or here 85 < 90 and 235 < 270)
        if(2*nearPower >= length) nearPower /= 3;

        long meds0 = mOMHelper(array, start, nearPower);
        long meds2 = mOMHelper(array, start + length - nearPower, nearPower);
        long meds1 = medianOfMedians(array, start + nearPower, length - 2 * nearPower);

        int smallMed0 = (int) (meds0 >> 32);
        int largeMed0 = (int)  meds0;
        int smallMed1 = (int) (meds1 >> 32);
        int largeMed1 = (int)  meds1;
        int smallMed2 = (int) (meds2 >> 32);
        int largeMed2 = (int)  meds2;

        int smallMed = medianOf3(array, new int[] {smallMed0, smallMed1, smallMed2});
        int largeMed = medianOf3(array, new int[] {largeMed0, largeMed1, largeMed2});

        return ((long) smallMed) << 32 | (long) largeMed;
    }

    // not at all a true rotation method, but the concept is similar
    private void rotate(int[] array, int start, int smalls,
            int middleBlocks, int middle, int largesBlocks, int blockLen) {

        int i = start + (middleBlocks + largesBlocks) * blockLen;
        int j = i + smalls + middle;

        // kind of weird exit condition for this first one but arguably best
        for(int k = largesBlocks * blockLen; k > 0; k--) {
            Highlights.markArray(2, --i);
            Writes.write(array, --j, array[i], wSleep, true, false);
        }
        for(int k = middle; k > 0; ) {
            Highlights.markArray(2, --k);
            Writes.write(array, --j, middleAux[k], wSleep, true, false);
        }
        clearArray(middleAux);
        while(i > start) {
            Highlights.markArray(2, --i);
            Writes.write(array, --j, array[i], wSleep, true, false);
        }
        for(int k = smalls; k > 0; ) {
            Highlights.markArray(2, --k);
            Writes.write(array, --j, smallsAux[k], wSleep, true, false);
        }
        clearArray(smallsAux);
    }

    private long partition(int[] array, int start, int end, int sqrt, int pivot1, int pivot2) {
        int smalls = 0;
        int middle = 0;
        int larges = 0;
        int smallsBlocks = 0;
        int largesBlocks = 0;
        int blockCounter = 0;

        int i = start;
        for(; i < end; i++) {
            if(Reads.compareIndexValue(array, i, pivot1, rSleep, true) < 0) {
                Writes.write(smallsAux, smalls++, array[i], wSleep, true, true);
                if(smalls == sqrt) {
                    // multiplication by sqrt is solely for visualization purposes
                    indexAux[blockCounter] = 1 * sqrt;
                    int j = start + sqrt * ++blockCounter;
                    while(smalls > 0) {
                        Highlights.markArray(2, --smalls);
                        Writes.write(array, --j, smallsAux[smalls], wSleep, true, false);
                    }
                    clearArray(smallsAux);
                    smallsBlocks++;
                }
            } else if(Reads.compareIndexValue(array, i, pivot2, rSleep, true) <= 0) {
                Writes.write(middleAux, middle++, array[i], wSleep, true, true);
                if(middle == sqrt) {
                    // multiplication by sqrt is solely for visualization purposes
                    indexAux[blockCounter] = 2 * sqrt;
                    int j = start + sqrt * ++blockCounter;
                    while(middle > 0) {
                        Highlights.markArray(2, --middle);
                        Writes.write(array, --j, middleAux[middle], wSleep, true, false);
                    }
                    clearArray(middleAux);
                }
            } else {
                Writes.write(largesAux, larges++, array[i], wSleep, true, true);
                if(larges == sqrt) {
                    // multiplication by sqrt is solely for visualization purposes
                    indexAux[blockCounter] = 3 * sqrt;
                    int j = start + sqrt * ++blockCounter;
                    while(larges > 0) {
                        Highlights.markArray(2, --larges);
                        Writes.write(array, --j, largesAux[larges], wSleep, true, false);
                    }
                    clearArray(largesAux);
                    largesBlocks++;
                }
            }
        }


        for(int j = larges; j > 0; ) {
            Highlights.markArray(2, --j);
            Writes.write(array, --i, largesAux[j], wSleep, true, false);
        }
        clearArray(largesAux);

        // easy cases
        if(smallsBlocks == blockCounter) {
            // rotate but it's easy
            for(int j = middle; j > 0; ) {
                Highlights.markArray(2, --j);
                Writes.write(array, --i, middleAux[j], wSleep, true, false);
            }
            clearArray(middleAux);
            for(int j = smalls; j > 0; ) {
                Highlights.markArray(2, --j);
                Writes.write(array, --i, smallsAux[j], wSleep, true, false);
            }
            clearArray(smallsAux);
            // no need to do so with larges as there must be 0 larges blocks
            smalls += smallsBlocks * sqrt;
            return ((long) smalls) << 32 | (long) larges;
        }
        if(smallsBlocks == 0) {
            if(largesBlocks == blockCounter) {
                rotate(array, start, smalls, 0, middle, largesBlocks, sqrt);
                // no need to do so with smalls as there must be 0 smalls blocks
                larges += largesBlocks * sqrt;
                return ((long) smalls) << 32 | (long) larges;
            }
            if(largesBlocks == 0) { // middleBlocks == blockCounter
                rotate(array, start, smalls, blockCounter, middle, 0, sqrt);
                // no smalls blocks or larges blocks
                return ((long) smalls) << 32 | (long) larges;
            }
        }

        // Figure out each block's sorted position
        int smallsPos = 0;
        int middlePos = smallsBlocks;
        int largesPos = blockCounter - largesBlocks;
        for(int j = 0; j < blockCounter; j++) {
            int type = indexAux[j] / sqrt;
            // multiplication by sqrt is solely for visualization purposes
            if(type == 1)
                indexAux[j] = smallsPos++ * sqrt;
            else if(type == 2)
                indexAux[j] = middlePos++ * sqrt;
            else
                indexAux[j] = largesPos++ * sqrt;
        }

        // Skip already sorted blocks
        i = 0;
        while(i < blockCounter && indexAux[i] / sqrt == i) i++;
        // Cycle sort the blocks into place
        while(i < blockCounter) {
            // write block to aux memory, which is the largesAux because its elements
            // were already placed at the end of the array
            for(int j = start + i * sqrt, k = 0; k < sqrt; j++, k++) {
                Highlights.markArray(2, j);
                Writes.write(largesAux, k, array[j], wSleep, true, true);
            }

            int to = indexAux[i] / sqrt;
            int current = i;
            int next = i;
            while(indexAux[next] / sqrt != current) next++;

            while(next != to) {
                for(int j = start + next * sqrt, k = start + current * sqrt;
                        j < start + (next + 1) * sqrt; j++, k++) {
                    Highlights.markArray(2, j);
                    Writes.write(array, k, array[j], wSleep, true, false);
                }
                indexAux[current] = current * sqrt;
                current = next;
                next = i;
                Delays.sleep(wSleep);
                while(indexAux[next] / sqrt != current) next++;
            }

            for(int j = start + next * sqrt, k = start + current * sqrt;
                    j < start + (next + 1) * sqrt; j++, k++) {
                Highlights.markArray(2, j);
                Writes.write(array, k, array[j], wSleep, true, false);
            }
            indexAux[current] = current * sqrt;
            Delays.sleep(wSleep);


            for(int j = 0, k = start + to * sqrt; j < sqrt; j++, k++) {
                Highlights.markArray(2, j);
                Writes.write(array, k, largesAux[j], wSleep, true, false);
            }
            clearArray(largesAux);
            indexAux[to] = to * sqrt;
            Delays.sleep(wSleep);
            // Skip already sorted blocks
            do{i++;} while(i < blockCounter && indexAux[i] / sqrt == i);
        }
        clearArray(indexAux);

        rotate(array, start + smallsBlocks * sqrt, smalls,
                blockCounter - smallsBlocks - largesBlocks, middle, largesBlocks, sqrt);
        smalls += smallsBlocks * sqrt;
        larges += largesBlocks * sqrt;
        return ((long) smalls) << 32 | (long) larges;
    }

    private void sortHelper(int[] array, int start, int end, int sqrt, int badPartition) {
        while(end - start >= 16) {
            long pivotPos;
            if(badPartition == 1) {
                int length = ((end - start) / 5) * 5; // round down to multiple of 5
                if((length & 1) == 0) length -= 5; // even length bad
                pivotPos = medianOfMedians(array, start, length);
            } else if(badPartition == 0)
                pivotPos = medianOf15(array, start, end);
            else {
                pivotPos = ((long) start) << 32 | (long) start; // bit of a cheese but effective
                badPartition = ~badPartition;
            }

            // java pls add tuples
            int pivot1 = array[(int)(pivotPos >> 32)];
            int pivot2 = array[(int) pivotPos];
            pivotPos = partition(array, start, end, sqrt, pivot1, pivot2);
            int len1 = (int)(pivotPos >> 32);
            int len3 = (int) pivotPos;

            if(Reads.compareValues(pivot1, pivot2) == 0) {
                if(len1 > len3) {
                    if(len1 / 8 > len3) badPartition = 1; // 8 is arbitrary; any constant is ok
                    int newStart = end - len3;
                    sortHelper(array, newStart, end, sqrt, badPartition);
                    end = start + len1;
                } else {
                    if(len3 / 8 > len1) badPartition = 1;
                    int newEnd = start + len1;
                    sortHelper(array, start, newEnd, sqrt, badPartition);
                    start = end - len3;
                }
            } else {
                int len2 = end - start - len1 - len3;
                int mid1 = start + len1;
                int mid2 = mid1  + len2;
                if(len1 > len2) { // tail recursion
                    if(len1 > len3) { // 1 > 2, 3
                        if(len1 / 8 > len2 + len3) badPartition = 1;
                        sortHelper(array,  mid2,  end, sqrt, badPartition); // larges
                        sortHelper(array,  mid1, mid2, sqrt, badPartition); // middle
                        end = mid1; // smalls
                    } else { // 3 >= 1 > 2
                        if(len3 / 8 > len1 + len2) badPartition = 1;
                        sortHelper(array,  mid1, mid2, sqrt, badPartition); // middle
                        sortHelper(array, start, mid1, sqrt, badPartition); // smalls
                        start = mid2;
                    }
                } else {
                    if(len3 > len2) { // 3 > 2 >= 1
                        if(len3 / 8 > len1 + len2) badPartition = 1;
                        sortHelper(array, start, mid1, sqrt, badPartition); // smalls
                        sortHelper(array,  mid1, mid2, sqrt, badPartition); // middle
                        start = mid2; // larges
                    } else { // 2 >= 1, 3
                        // infinite recursion otherwise possible here
                        if (len1 == 0 && len3 == 0) badPartition = ~badPartition;
                        else {
                            if(len2 / 8 > len1 + len3) badPartition = 1;
                            sortHelper(array, start, mid1, sqrt, badPartition); // smalls
                            sortHelper(array,  mid2,  end, sqrt, badPartition); // larges
                            start = mid1; // middle
                            end   = mid2;
                        }
                    }
                }
            }
        }

        insertSorter.customInsertSort(array, start, end, rSleep, false);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        insertSorter = new InsertionSort(arrayVisualizer);

        if(sortLength < 16)
            insertSorter.customInsertSort(array, 0, sortLength, 3, true);

        int lgSqrt = 1;
        while(1 << (++lgSqrt << 1) < sortLength);
        int sqrt = 1 << lgSqrt;

        smallsAux = Writes.createExternalArray(sqrt);
        middleAux = Writes.createExternalArray(sqrt);
        largesAux = Writes.createExternalArray(sqrt);
        indexAux  = Writes.createExternalArray(sortLength / sqrt);

        sortHelper(array, 0, sortLength, sqrt, 0);
    }

}
