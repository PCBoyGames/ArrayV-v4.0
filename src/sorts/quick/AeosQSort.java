package sorts.quick;

import main.ArrayVisualizer;
import sorts.insert.InsertionSort;
import sorts.templates.Sort;

public class AeosQSort extends Sort {
    private InsertionSort insertSorter;

    private int[] elementAux;
    private int[]   indexAux;

    private double rSleep = 0.5;
    private double wSleep = 0.5;

    public AeosQSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("AeosQsort");
        this.setRunAllSortsName("Aeos Quick Sort");
        this.setRunSortName("Aeos Quicksort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    // brief method for zeroing out an aux array for visualization
    private void clearArray(int[] array) {
        for(int i = 0; i < array.length; i++)
            array[i] = 0;
    }

    private int medianOfFewUnique(int[] array, int start, int end) {
        int i = start;
        int read = 0;

        while(read == 0) read = Reads.compareIndices(array, start, ++i, rSleep, true);

        if(read < 0) return i;
        return start;
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

    private int medianOf9(int[] array, int start, int end) {
        // anti-overflow with good rounding
        int  length = end - start;
        int    half =  length / 2;
        int quarter =    half / 2;
        int  eighth = quarter / 2;

        int[] elements0 = {start, start + eighth, start + quarter};
        int med0 = medianOf3(array, elements0);

        int[] elements1 = {start + quarter + eighth, start + half, start + half + eighth};
        int med1 = medianOf3(array, elements1);

        int[] elements2 = {start + half + quarter, start + half + quarter + eighth, end - 1};
        int med2 = medianOf3(array, elements2);

        return medianOf3(array, new int[] {med0, med1, med2});
    }

    private int mOMHelper(int[] array, int start, int length) {
        if(length == 1) return start;

        int[] meds = new int[3];
        int third = length / 3;
        meds[0] = mOMHelper(array, start, third);
        meds[1] = mOMHelper(array, start + third, third);
        meds[2] = mOMHelper(array, start + 2 * third, third);

        return medianOf3(array, meds);
    }

    private int medianOfMedians(int[] array, int start, int length) {
        if(length == 1) return start;

        int[] meds = new int[3];

        int nearPower = (int) Math.pow(3, Math.round(Math.log(length)/Math.log(3)));
        if(nearPower == length)
            return mOMHelper(array, start, length);

        nearPower /= 3;
        // uncommon but can happen with numbers slightly smaller than 2*3^k
        // (e.g., 17 < 18 or 47 < 54)
        if(2*nearPower >= length) nearPower /= 3;

        meds[0] = mOMHelper(array, start, nearPower);
        meds[2] = mOMHelper(array, start + length - nearPower, nearPower);
        meds[1] = medianOfMedians(array, start + nearPower, length - 2 * nearPower);

        return medianOf3(array, meds);
    }

    private void rotate(int[] array, int start, int leftLen, int rightLen) {
        int j = start + leftLen;
        int k = 0;
        while(k < rightLen) {
            Highlights.markArray(2, j);
            Writes.write(elementAux, k++, array[j++], wSleep, true, true);
        }

        k = start + leftLen;
        while(k > start) {
            Highlights.markArray(2, --k);
            Writes.write(array, --j, array[k], wSleep, true, false);
        }

        j = 0;
        while(j < rightLen) {
            Highlights.markArray(2, j);
            Writes.write(array, k++, elementAux[j++], wSleep, true, true);
        }
        clearArray(elementAux);
    }

    private int partition(int[] array, int start, int end, int sqrt, int pivotEle) {

        int       smalls = 0;
        int       larges = 0;
        int  smallBlocks = 0;
        int blockCounter = 0;

        for(int i = start; i < end; i++) {
            Highlights.markArray(2, i);
            Delays.sleep(rSleep);
            if(Reads.compareValues(array[i], pivotEle) < 0) {
                if(larges != 0) // usually true, but maybe false often enough to make this worth it
                    Writes.write(array, start + blockCounter * sqrt + smalls, array[i],
                            wSleep, true, false);
                if(++smalls == sqrt) {
                    smalls = 0;
                    // multiplication by sqrt is solely for visualization purposes
                    indexAux[blockCounter++] = (smallBlocks++) * sqrt;
                    Delays.sleep(wSleep);
                }
            } else {
                Writes.write(elementAux, larges, array[i], wSleep, true, true);
                if(++larges == sqrt) {
                    int j = i;
                    for(int k = i - sqrt; k >= start + blockCounter * sqrt; ) {
                        Highlights.markArray(2, k);
                        Writes.write(array, j--, array[k--], wSleep, true, false);
                    }
                    for(int k = sqrt; k > 0; ) {
                        Highlights.markArray(2, --k);
                        Writes.write(array, j--, elementAux[k], wSleep, true, false);
                    }
                    clearArray(elementAux);
                    larges = 0;
                    indexAux[blockCounter++] = -1;
                    Delays.sleep(wSleep);
                }
            }
            Highlights.clearAllMarks();
        }

        for(int j = end, k = larges; k > 0; ) {
            Highlights.markArray(2, --k);
            Writes.write(array, --j, elementAux[k], wSleep, true, false);
        }
        clearArray(elementAux);
        Highlights.clearAllMarks();

        // easy cases
        if(smallBlocks == blockCounter)
            return smallBlocks * sqrt + smalls;
        if(smallBlocks == 0) {
            if(smalls != 0)
                rotate(array, start, blockCounter * sqrt, smalls);
            return smalls;
        }

        int largeFinalPos = smallBlocks;
        for(int i = 0; i < blockCounter; i++) {
            if(indexAux[i] == -1) { // multiplication by sqrt is solely for visualization purposes
                indexAux[i] = (largeFinalPos++) * sqrt;
                Delays.sleep(wSleep);
            }
        }

        // Skip already sorted blocks
        int i = 0;
        while(i < blockCounter && indexAux[i] / sqrt == i) i++;

        while(i < blockCounter) {
            // write block to aux memory
            for(int j = start + i * sqrt, k = 0; k < sqrt; j++, k++) {
                Highlights.markArray(2, j);
                Writes.write(elementAux, k, array[j], wSleep, true, true);
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
                Writes.write(array, k, elementAux[j], wSleep, true, false);
            }
            clearArray(elementAux);
            indexAux[to] = to * sqrt;
            Delays.sleep(wSleep);
            // Skip already sorted blocks
            do{i++;} while(i < blockCounter && indexAux[i] / sqrt == i);
        }
        clearArray(indexAux);

        if(smalls != 0)
            rotate(array, start + smallBlocks * sqrt, (blockCounter - smallBlocks) * sqrt, smalls);
        return smallBlocks * sqrt + smalls;
    }

    private void sortHelper(int[] array, int start, int end, int sqrt, int badPartition) {
        while(end - start >= 16) {
            int pivotPos;
            if(badPartition == 0) {
                pivotPos = medianOf9(array, start, end);
            } else if(badPartition > 0) {
                int length = end - start;
                if((length & 1) == 0) length -= 1; // even lengths bad
                pivotPos = medianOfMedians(array, start, length);
            } else {
                pivotPos = medianOfFewUnique(array, start, end);
                badPartition = ~badPartition; // few uniques case should be over now
            }

            int pivotEle = array[pivotPos];
            pivotPos = partition(array, start, end, sqrt, pivotEle);

            int newEnd = start + pivotPos;
            int newStart = newEnd;

            // Many equal element handling
            while(newStart < end && Reads.compareValues(array[newStart], pivotEle) == 0) newStart++;

            int len1 = newEnd - start;
            int len2 = end - newStart;

            if(len1 > len2) {
                badPartition += len1 > 8 * len2 ? 1 : 0; // 8 is arbitrary; any constant is ok
                sortHelper(array, newStart, end, sqrt, badPartition);
                end = newEnd;
            } else {
                if(len2 > 8 * len1) {
                    if(len1 == 0)
                        badPartition = ~badPartition; // very few uniques handling
                    else {
                        sortHelper(array, start, newEnd, sqrt, ++badPartition);
                        start = newStart;
                    }
                }
                else {
                    sortHelper(array, start, newEnd, sqrt, badPartition);
                    start = newStart;
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

        elementAux = Writes.createExternalArray(sqrt);
        indexAux   = Writes.createExternalArray(sortLength / sqrt);

        sortHelper(array, 0, sortLength, sqrt, 0);
    }

}
