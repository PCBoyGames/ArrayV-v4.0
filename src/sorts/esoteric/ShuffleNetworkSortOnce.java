package sorts.esoteric;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import main.ArrayVisualizer;
import sorts.insert.InsertionSort;
import sorts.templates.BogoSorting;
import utils.IndexedRotations;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public final class ShuffleNetworkSortOnce extends BogoSorting {

    boolean changes = false;
    boolean changesthis = false;
    int type;

    public ShuffleNetworkSortOnce(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Shuffle as Network (Inverted)");
        this.setRunAllSortsName("Shuffle as Network Sort");
        this.setRunSortName("Shuffle as Networksort");
        this.setCategory("Esoteric Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Enter shuffle:\n1: Inverted Random\n2: Inverted Reversed\n3: Inverted Almost Sorted\n4: Inverted Scrambled End\n5: Inverted Scrambled Start\n6: Inverted Both Sides Scrambled\n7: Inverted Low Disparity\n8: Inverted Scrambled Odds\n9: Inverted Final Merge\n10: Inverted Real Final Merge\n11: Inverted Sawtooth\n12: Inverted Real Sawtooth\n13: Inverted Scrambled Back Half\n14: Inverted Partitioned\n15: Inverted Quick Partitions\n16: Inverted Evens Up, Odds Down\n17: Inverted Evens Reversed\n18: Inverted Final Radix\n19: Inverted Recursive Final Radix\n20: Inverted Binary Search Tree\n21: Inverted Logpile\n22: Inverted Heap\n23: Inverted Circle Pass\n24: Inverted Final Pairwise\n25: Inverted Recursive Reversal\n26: Inverted Triangular\n27: Inverted Quick Killer\n28: Inverted Grail Killer\n29: Inverted Shuffle Killer\n(Default is 1)", 1);
    }

    protected int stablereturn(int a) {
        return arrayVisualizer.doingStabilityCheck() ? arrayVisualizer.getStabilityValue(a) : a;
    }

    protected int par(int[] array, int len) {
        boolean[] max = new boolean[len];
        int maximum = stablereturn(array[0]);
        for (int i = 1; i < len; i++) {
            if (stablereturn(array[i]) > maximum) {
                maximum = stablereturn(array[i]);
                max[i] = true;
            }
        }
        int i = len - 1;
        int p = 1;
        int j = len - 1;
        while (j >= 0 && i >= p) {
            while (!max[j] && j > 0) j--;
            maximum = stablereturn(array[j]);
            while (maximum <= stablereturn(array[i]) && i >= p) i--;
            if (stablereturn(array[j]) > stablereturn(array[i]) && p < i - j) p = i - j;
            j--;
        }
        return p;
    }

    protected void networksort(int[] array, int[] indexnetwork, int start, int length) {
        for (int i = 1; i < length; i += 2) {
            Highlights.markArray(3, start + i - 1);
            Highlights.markArray(4, start + i);
            pairsort(array, start + indexnetwork[i - 1], start + indexnetwork[i]);
        }
    }

    protected void pairsort(int[] array, int i, int j) {
        if (i > j) {
            int temp = i;
            i = j;
            j = temp;
        }
        if (Reads.compareIndices(array, i, j, 0.025, true) > 0) {
            Writes.swap(array, i, j, 0.025, true, false);
            changes = true;
            changesthis = true;
        }
    }

    protected void initializeNetwork(int[] array, int currentLen, int type) {
        for (int i = 0; i < currentLen; i++) Writes.write(array, i, i, 0.1, true, true);
        int seventh = Math.max(1, currentLen / 7);
        Random random = new Random();

        // Random
        if (type == 1) {
            bogoSwap(array, 0, currentLen, true);
        }

        // Reversed
        if (type == 2) {
            Writes.reversal(array, 0, currentLen, 0.1, true, true);
        }

        // Almost Sorted
        if (type == 3) {
            for (int i = 0; i < Math.max(currentLen / 20, 1); i++) {
                int a = random.nextInt(currentLen);
                int b = random.nextInt(currentLen);
                if (a != b) Writes.swap(array, a, b, 0.1, true, true);
                else i--;
            }
        }

        // Scrambled End
        if (type == 4) {
            bogoSwap(array, 0, currentLen, true);
            sortAux(array, 0, currentLen - seventh, 0.1);
        }

        // Scrambled Start
        if (type == 5) {
            bogoSwap(array, 0, currentLen, true);
            sortAux(array, seventh, currentLen, 0.1);
        }

        // Both Sides Scrambled
        if (type == 6) {
            bogoSwap(array, 0, currentLen, true);
            sortAux(array, seventh, currentLen - seventh, 0.1);
        }

        // Low Disparity
        if (type == 7) {
            int i, size = Math.max(4, (int) (Math.sqrt(currentLen) / 2));
            for (i = 0; i + size <= currentLen; i += random.nextInt(size - 1) + 1) bogoSwap(array, i, i+size, true);
            bogoSwap(array, i, currentLen, true);
        }

        // Scrambled Odds
        if (type == 8) {
            for (int i = 1; i < currentLen; i += 2) {
                int randomIndex = (((random.nextInt(currentLen - i) / 2)) * 2) + i;
                if (i != randomIndex) Writes.swap(array, i, randomIndex, 0.1, true, true);
            }
        }

        // Final Merge
        if (type == 9) {
            int count = 2;
            int k = 0;
            int[] temp = new int[currentLen];
            for (int j = 0; j < count; j++) for (int i = j; i < currentLen; i += count) Writes.write(temp, k++, array[i], 0, false, true);
            for (int i = 0; i < currentLen; i++) Writes.write(array, i, temp[i], 0.1, true, true);
        }

        // Real Final Merge
        if (type == 10) {
            bogoSwap(array, 0, currentLen, true);
            Highlights.clearMark(2);
            sortAux(array, 0, currentLen / 2, 0.1);
            sortAux(array, currentLen / 2, currentLen, 0.1);
        }

        // Sawtooth
        if (type == 11) {
            int count = 4;
            int k = 0;
            int[] temp = new int[currentLen];
            for (int j = 0; j < count; j++) for (int i = j; i < currentLen; i += count) Writes.write(temp, k++, array[i], 0, false, true);
            for (int i = 0; i < currentLen; i++) Writes.write(array, i, temp[i], 0.1, true, true);
        }

        // Real Sawtooth
        if (type == 12) {
            bogoSwap(array, 0, currentLen, true);
            Highlights.clearMark(2);
            sortAux(array, 0, currentLen / 4, 0.1);
            sortAux(array, currentLen / 4, currentLen / 2, 0.1);
            sortAux(array, currentLen / 2, 3 * currentLen / 4, 0.1);
            sortAux(array, 3 * currentLen / 4, currentLen, 0.1);
        }

        // Scrambled Back Half
        if (type == 13) {
            bogoSwap(array, 0, currentLen, true);
            Highlights.clearMark(2);
            sortAux(array, 0, currentLen / 2, 0.1);
        }

        // Partitioned
        if (type == 14) {
            bogoSwap(array, 0, currentLen / 2, true);
            bogoSwap(array, currentLen / 2, currentLen, true);
        }

        // Quick Partitions
        if (type == 15) {
            for (int point = currentLen; point >= 1; point /= 2) bogoSwap(array, (point / 2), point, true);
        }

        // Evens Up, Odds Down
        if (type == 16) {
            int[] referenceArray = new int[currentLen];
            for (int i = 0; i < currentLen; i++) referenceArray[i] = array[i];
            int leftIndex = 1;
            int rightIndex = currentLen - 1;
            for (int i = 1; i < currentLen; i++) {
                if (i % 2 == 0) Writes.write(array, i, referenceArray[leftIndex++], 0.1, true, true);
                else Writes.write(array, i, referenceArray[rightIndex--], 0.1, true, true);
            }
        }

        // Evens Reversed
        if (type == 17) {
            for (int i = 0; i < currentLen / 2; i += 2) Writes.swap(array, i, currentLen - i - 1, 0.1, true, true);
        }

        // Final Radix
        if (type == 18) {
            currentLen -= currentLen % 2;
            int mid = currentLen / 2;
            int[] temp = new int[mid];
            for (int i = 0; i < mid; i++) Writes.write(temp, i, array[i], 0, false, true);
            for (int i = mid, j = 0; i < currentLen; i++, j+=2) {
                Writes.write(array, j, array[i], 0.1, true, true);
                Writes.write(array, j + 1, temp[i-mid], 0.1, true, true);
            }
        }

        // Recursive Final Radix
        if (type == 19) {
            weaveRec(array, 0, currentLen, 1, 0.1);
        }

        // Binary Search Tree
        if (type == 20) {
            int[] temp = Arrays.copyOf(array, currentLen);
            class Subarray {
                private int start;
                private int end;
                Subarray(int start, int end) {
                    this.start = start;
                    this.end = end;
                }
            }
            Queue<Subarray> q = new LinkedList<Subarray>();
            q.add(new Subarray(0, currentLen));
            int i = 0;
            while (!q.isEmpty()) {
                Subarray sub = q.poll();
                if (sub.start != sub.end) {
                    int mid = (sub.start + sub.end) / 2;
                    Writes.write(array, i, temp[mid], 0.1, true, true);
                    i++;
                    q.add(new Subarray(sub.start, mid));
                    q.add(new Subarray(mid + 1, sub.end));
                }
            }
        }

        // Logpile
        if (type == 21) {
            int[] temp = new int[currentLen];
            for (int i = 0; i < currentLen; i++) Writes.write(temp, i, array[i], 0, false, true);
            Writes.write(array, 0, 0, 0.1, true, true);
            for (int i = 1; i < currentLen; i++) {
                int log = (int) (Math.log(i) / Math.log(2));
                int power = (int) Math.pow(2, log);
                int value = temp[2 * (i - power) + 1];
                Writes.write(array, i, value, 0.1, true, true);
            }
        }

        // Heap
        if (type == 22) {
            heapify(array, 0, currentLen, 0.1, true);
        }

        // If you want Smooth, Poplar, or Triangle Heaps, well, too bad.
        // I can't properly implement them without the hassle of aux write conversion.

        // Circle Pass
        if (type == 23) {
            bogoSwap(array, 0, currentLen, true);
            int n = 1;
            for (; n < currentLen; n *= 2);
            circleSortRoutine(array, 0, n - 1, currentLen);
        }

        // Final Pairwise
        if (type == 24) {
            bogoSwap(array, 0, currentLen, true);
            for (int i = 1; i < currentLen; i += 2) if (Reads.compareIndices(array, i - 1, i, 0.1, true) > 0) Writes.swap(array, i-1, i, 0.1, true, true);
            Highlights.clearMark(2);
            int[] temp = new int[currentLen];
            for (int m = 0; m < 2; m++) {
                for (int k = m; k < currentLen; k += 2) Writes.write(temp, array[k], temp[array[k]] + 1, 0, false, true);
                int i = 0, j = m;
                while (true) {
                    while (i < currentLen && temp[i] == 0) i++;
                    if (i >= currentLen) break;
                    Writes.write(array, j, i, 0.1, true, true);
                    j += 2;
                    Writes.write(temp, i, temp[i] - 1, 0, false, true);
                }
            }
        }

        // Recursive Reversal
        if (type == 25) {
            reversalRec(array, 0, currentLen, 0.1);
        }

        // Triangular
        if (type == 26) {
            int[] triangle = new int[currentLen];
            int j = 0, k = 2;
            int max = 0;
            for (int i = 1; i < currentLen; i++, j++) {
                if (i == k) {
                    j = 0;
                    k *= 2;
                }
                triangle[i] = triangle[j]+1;
                if (triangle[i] > max) max = triangle[i];
            }
            int[] cnt = new int[max+1];
            for (int i = 0; i < currentLen; i++) cnt[triangle[i]]++;
            for (int i = 1; i < cnt.length; i++) cnt[i] += cnt[i-1];
            for (int i = currentLen-1; i >= 0; i--) triangle[i] = --cnt[triangle[i]];
            int[] temp = Arrays.copyOf(array, currentLen);
            for (int i = 0; i < currentLen; i++) Writes.write(array, i, temp[triangle[i]], 0.1, true, true);
        }

        // Quick Killer
        if (type == 27) {
            for (int j = currentLen - currentLen % 2 - 2, i = j - 1; i >= 0; i -= 2, j--) Writes.swap(array, i, j, 0.1, true, true);
        }

        // Can't do the Pattern Quick one yet. Wish I could.

        // Grail Killer
        if (type == 28) {
            if (currentLen <= 16) Writes.reversal(array, 0, currentLen - 1, 0.1, true, true);
            else {
                int blockLen = 1;
                while (blockLen * blockLen < currentLen) blockLen *= 2;
                int numKeys = (currentLen - 1) / blockLen + 1;
                int keys = blockLen + numKeys;
                bogoSwap(array, 0, currentLen, true);
                sortAux(array, 0, keys, 0.1);
                Writes.reversal(array, 0, keys-1, 0.1, true, true);
                Highlights.clearMark(2);
                sortAux(array, keys, currentLen, 0.1);
                push(array, keys, currentLen, blockLen, 0.1);
            }
        }

        // Shuffle Killer
        if (type == 29) {
            int[] tmp = new int[currentLen];
            int d = 2, end = 1 << (int) (Math.log(currentLen - 1) / Math.log(2) + 1);
            while (d <= end) {
                int i = 0, dec = 0;
                while (i < currentLen) {
                    int j = i;
                    dec += currentLen;
                    while (dec >= d) {
                        dec -= d;
                        j++;
                    }
                    int k = j;
                    dec += currentLen;
                    while (dec >= d) {
                        dec -= d;
                        k++;
                    }
                    shuffleMergeBad(array, tmp, i, j, k, 0.1);
                    i = k;
                }
                d *= 2;
            }
        }
    }

    protected void weaveRec(int[] array, int pos, int length, int gap, double delay) {
        if (length < 2) return;
        int mod2 = length % 2;
        length -= mod2;
        int mid = length / 2;
        int[] temp = new int[mid];
        for (int i = pos, j = 0; i < pos + gap * mid; i += gap, j++) Writes.write(temp, j, array[i], 0, false, true);
        for (int i = pos + gap * mid, j = pos, k = 0; i < pos + gap * length; i += gap, j += 2 * gap, k++) {
            Writes.write(array, j, array[i], delay, true, true);
            Writes.write(array, j + gap, temp[k], delay, true, true);
        }
        weaveRec(array, pos, mid + mod2, 2 * gap, delay / 2);
        weaveRec(array, pos + gap, mid, 2 * gap, delay / 2);
    }

    private void siftDown(int[] array, int root, int dist, int start, double sleep, boolean isMax) {
        int compareVal;
        if (isMax) compareVal = -1;
        else compareVal = 1;
        while (root <= dist / 2) {
            int leaf = 2 * root;
            if (leaf < dist && Reads.compareValues(array[start + leaf - 1], array[start + leaf]) == compareVal) leaf++;
            Highlights.markArray(1, start + root - 1);
            Highlights.markArray(2, start + leaf - 1);
            Delays.sleep(sleep);
            if (Reads.compareValues(array[start + root - 1], array[start + leaf - 1]) == compareVal) {
                Writes.swap(array, start + root - 1, start + leaf - 1, 0, true, true);
                root = leaf;
            }
            else break;
        }
    }

    protected void heapify(int[] arr, int low, int high, double sleep, boolean isMax) {
        int length = high - low;
        for (int i = length / 2; i >= 1; i--) {
            siftDown(arr, i, length, low, sleep, isMax);
        }
    }

    protected void circleSortRoutine(int[] array, int lo, int hi, int end) {
        if (lo == hi) return;
        int high = hi;
        int low = lo;
        int mid = (hi - lo) / 2;
        while (lo < hi) {
            if (hi < end && Reads.compareIndices(array, lo, hi, 0.1, true) > 0) Writes.swap(array, lo, hi, 0.1, true, false);
            lo++;
            hi--;
        }
        circleSortRoutine(array, low, low + mid, end);
        if (low + mid + 1 < end) circleSortRoutine(array, low + mid + 1, high, end);
    }

    protected void reversalRec(int[] array, int a, int b, double sleep) {
        if (b - a < 2) return;
        Writes.reversal(array, a, b - 1, sleep, true, true);
        int m = (a + b) / 2;
        this.reversalRec(array, a, m, sleep / 2);
        this.reversalRec(array, m, b, sleep / 2);
    }

    public void rotate(int[] array, int a, int m, int b, double sleep) {
        IndexedRotations.cycleReverse(array, a, m, b, 0.1, true, true);
    }

    public void push(int[] array, int a, int b, int bLen, double sleep) {
        int len = b - a,
            b1 = b - len % bLen, len1 = b1 - a;
        if (len1 <= 2 * bLen) return;
        int m = bLen;
        while (2 * m < len) m *= 2;
        m += a;
        if (b1 - m < bLen) push(array, a, m, bLen, sleep);
        else {
            m = a + b1 - m;
            rotate(array, m - (bLen - 2), b1 - (bLen - 1), b1, sleep);
            Writes.multiSwap(array, a, m, sleep / 2, true, true);
            rotate(array, a, m, b1, sleep);
            m = a + b1 - m;
            push(array, a, m, bLen, sleep / 2);
            push(array, m, b, bLen, sleep / 2);
        }
    }

    protected void shuffleMergeBad(int[] array, int[] tmp, int a, int m, int b, double sleep) {
        if ((b-a)%2 == 1) {
            if (m-a > b-m) a++;
            else          b--;
        }
        shuffleBad(array, tmp, a, b, sleep);
    }

    protected void shuffleBad(int[] array, int[] tmp, int a, int b, double sleep) {
        if (b - a < 2) return;
        int m = (a + b) / 2;
        int s = (b - a - 1) / 4 + 1;
        a = m - s;
        b = m + s;
        int j = a;
        for (int i = a + 1; i < b; i += 2) Writes.write(tmp, j++, array[i], sleep, true, true);
        for (int i = a; i < b; i += 2) Writes.write(tmp, j++, array[i], sleep, true, true);
        Writes.arraycopy(tmp, a, array, a, b-a, sleep, true, true);
    }

    protected void sortAux(int[] array, int start, int end, double sleep) {
        int min = array[start], max = min;
        for (int i = start + 1; i < end; i++) {
            if (array[i] < min) min = array[i];
            else if (array[i] > max) max = array[i];
        }
        int size = max - min + 1;
        int[] holes = new int[size];
        for (int i = start; i < end; i++) Writes.write(holes, array[i] - min, holes[array[i] - min] + 1, 0, true, true);
        for (int i = 0, j = start; i < size; i++) {
            while (holes[i] > 0) {
                Writes.write(holes, i, holes[i] - 1, 0, false, true);
                Writes.write(array, j, i + min, sleep, true, true);
                j++;
            }
        }
    }

    protected void linearInvert(int[] array, int currentLen) {
        int[] tmp = new int[currentLen];
        tableinvert(array, tmp, currentLen);
        Highlights.clearAllMarks();
        Writes.arraycopy(tmp, 0, array, 0, currentLen, 0.1, true, true);
    }

    protected void siftDown(int[] array, int[] keys, int r, int len, int a, int t) {
        int j = r;
        while (2*j + 1 < len) {
            j = 2*j + 1;
            if (j+1 < len) {
                int cmp = Reads.compareOriginalIndices(array, a+keys[j+1], a+keys[j], 0, true);
                if (cmp > 0 || (cmp == 0 && Reads.compareOriginalValues(keys[j+1], keys[j]) > 0))
                    j++;
            }
        }
        for (int cmp = Reads.compareOriginalIndices(array, a+t, a+keys[j], 0, true);
            cmp > 0 || (cmp == 0 && Reads.compareOriginalValues(t, keys[j]) > 0);
            j = (j-1)/2,
            cmp = Reads.compareOriginalIndices(array, a+t, a+keys[j], 0, true));
        for (int t2; j > r; j = (j-1)/2) {
            t2 = keys[j];
            Highlights.markArray(3, j);
            Writes.write(keys, j, t, 0, false, true);
            t = t2;
        }
        Highlights.markArray(3, r);
        Writes.write(keys, r, t, 0, false, true);
    }

    protected void tableSort(int[] array, int[] keys, int a, int b) {
        int len = b-a;
        for (int i = (len-1)/2; i >= 0; i--)
            this.siftDown(array, keys, i, len, a, keys[i]);
        for (int i = len-1; i > 0; i--) {
            int t = keys[i];
            Highlights.markArray(3, i);
            Writes.write(keys, i, keys[0], 0, false, true);
            this.siftDown(array, keys, 0, i, a, t);
        }
        Highlights.clearAllMarks();
    }

    protected void tableinvert(int[] array, int[] table, int currentLength) {
        for (int i = 0; i < currentLength; i++) Writes.write(table, i, i, 0, false, true);
        tableSort(array, table, 0, currentLength);
    }

    protected void prepareIndexes(int[] array, int length) {
        initializeNetwork(array, length, type);
        linearInvert(array, length);
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 1) return 1;
        if (answer > 999) return 14;
        return answer;
    }

    @Override
    public void runSort(int[] array, int currentLength, int input) {
        type = input;
        delay = 0.1;
        int indexeslen = par(array, currentLength);
        int lastlen = currentLength;
        int[] indexes = Writes.createExternalArray(indexeslen);
        boolean lenchange = true;
        int firstpos = 0;
        int nextlast = currentLength;
        int lastpos = currentLength;
        boolean firstfound = false;
        while (lastlen > 1) {
            changes = false;
            Highlights.clearAllMarks();
            if (lenchange && indexeslen > 1) prepareIndexes(indexes, indexeslen);
            else if (lenchange) break;
            lenchange = false;
            firstfound = false;
            Highlights.clearAllMarks();
            for (int i = firstpos > 0 ? firstpos - 1 : 0; i + indexeslen <= (lastpos + 1 < currentLength ? lastpos + 1 : currentLength); i++) {
                changesthis = false;
                networksort(array, indexes, i, indexeslen);
                if (changes && !firstfound) {
                    firstpos = i;
                    firstfound = true;
                }
                if (changesthis) nextlast = i + indexeslen;
            }
            Highlights.clearAllMarks();
            lastpos = nextlast;
            if (!changes) {
                if (!isArraySorted(array, currentLength)) {
                    firstpos = 0;
                    lastpos = currentLength;
                    lastlen = indexeslen;
                    indexeslen = par(array, currentLength);
                    while (indexeslen >= lastlen && indexeslen >= 2) indexeslen--;
                    lenchange = true;
                    Writes.deleteExternalArray(indexes);
                    indexes = Writes.createExternalArray(indexeslen);
                } else break;
            }
        }
        Highlights.clearAllMarks();
        InsertionSort clean = new InsertionSort(arrayVisualizer);
        clean.customInsertSort(array, 0, currentLength, 10, false);
    }
}