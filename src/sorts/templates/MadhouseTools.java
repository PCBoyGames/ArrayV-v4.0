package sorts.templates;

import java.util.concurrent.ThreadLocalRandom;

import main.ArrayVisualizer;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
/**The Madhouse Tools template contains many useful sorting algorithm development tools and utilities, brought to you in part by the Sorting Algorithm Madhouse.<p>
 * Compiled and rewritten by PCBoy.
 * @version MHT-1.0
 * @author Various
 */
public abstract class MadhouseTools extends Sort {
    protected MadhouseTools(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    /**Copied from the {@link BogoSorting} template to allow random integers to work alongside MadhouseTools.
     * @param min The start of the range, inclusively.
     * @param max The end of the range, exclusively.
     * @return An {@code int} with a random integer in the range.
     * @author Doug Lea
     * @since MHT-1.0
     */
    public int randInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    /**Copied from the {@link BogoSorting} template to allow the Fisher–Yates shuffle function to work alongside MadhouseTools.
     * @param array The input array.
     * @param start The start of the range, inclusively.
     * @param end The end of the range, exclusively.
     * @param delay The visual time of the operation, as a factor.
     * @param mark A toggle for whether highlights are made during the operation.
     * @param aux A toggle for whether the operation is indicated in external space.
     * @author w0rthy
     * @since MHT-1.0
     */
    public void shuffleArray(int[] array, int start, int end, double delay, boolean mark, boolean aux) {
        for (int i = start; i < end; ++i) {
            int j = randInt(i, end);
            if (i != j) Writes.swap(array, i, j, delay, mark, aux);
        }
    }

    /**Defines the first unsorted position from the maximums and where the next item in line is in {@code O(n)}, given input {@code array} from bounds {@code [start, end)}.
     * @param array The input array.
     * @param start The start of the input bounds, inclusively.
     * @param end The end of the input bounds, exclusively.
     * @param delay The visual time of the operation, as a factor.
     * @param mark A toggle for whether highlights are made during the operation.
     * @return An {@code int[]} with two values:<ul><li>{@code [0]}: the index of the first unsorted maximum, exclusively.</li><li>{@code [1]}: the index of the next item in line.<ul><li>Special Case: returns {@code -1} if sorted.</li></ul></li></ul>
     * @author PCBoy
     * @since MHT-1.0
    */
    public int[] maxSortedW(int[] array, int start, int end, double delay, boolean mark) {
        int a = end - 1;
        int b = end - 1;
        boolean segment = true;
        while (segment) {
            if (b - 1 < start) return new int[] {start, -1};
            if (Reads.compareIndices(array, b - 1, b, delay, mark) > 0) segment = false;
            else b--;
        }
        int sel = b - 1;
        for (int s = b - 2; s >= start; s--) if (Reads.compareIndices(array, sel, s, delay, mark) < 0) sel = s;
        int where = sel;
        while (Reads.compareIndices(array, sel, a, delay, mark) <= 0) {
            a--;
            if (a < start) break;
        }
        return new int[] {a + 1, where};
    }

    /**Defines the first unsorted position from the maximums in {@code O(n)}, given input {@code array} from bounds {@code [start, end)}.
     * @param array The input array.
     * @param start The start of the input bounds, inclusively.
     * @param end The end of the input bounds, exclusively.
     * @param delay The visual time of the operation, as a factor.
     * @param mark A toggle for whether highlights are made during the operation.
     * @return An {@code int} with the index of the first unsorted maximum, exclusively.
     * @author PCBoy
     * @since MHT-1.0
    */
    public int maxSorted(int[] array, int start, int end, double delay, boolean mark) {
        return maxSortedW(array, start, end, delay, mark)[0];
    }

    /**Defines the first unsorted position from the minimums and where the next item in line is in {@code O(n)}, given input {@code array} from bounds {@code [start, end)}.
     * @param array The input array.
     * @param start The start of the input bounds, inclusively.
     * @param end The end of the input bounds, exclusively.
     * @param delay The visual time of the operation, as a factor.
     * @param mark A toggle for whether highlights are made during the operation.
     * @return An {@code int[]} with two values:<ul><li>{@code [0]}: the index of the first unsorted minimum, inclusively.</li><li>{@code [1]}: the index of the next item in line.<ul><li>Special Case: returns {@code -1} if sorted.</li></ul></li></ul>
     * @author PCBoy
     * @since MHT-1.0
    */
    public int[] minSortedW(int[] array, int start, int end, double delay, boolean mark) {
        int i = start;
        boolean segment = true;
        while (segment) {
            if (i + 1 > end) return new int[] {end, -1};
            if (Reads.compareIndices(array, i, i + 1, delay, mark) > 0) segment = false;
            else i++;
        }
        int sel = i + 1;
        for (int s = i + 2; s < end; s++) if (Reads.compareIndices(array, sel, s, delay, mark) > 0) sel = s;
        int where = sel;
        while (Reads.compareIndices(array, sel, start, delay, mark) >= 0) {
            start++;
            if (start >= end) break;
        }
        return new int[] {start, where};
    }

    /**Defines the first unsorted position from the minimums in {@code O(n)}, given input {@code array} from bounds {@code [start, end)}.
     * @param array The input array.
     * @param start The start of the input bounds, inclusively.
     * @param end The end of the input bounds, exclusively.
     * @param delay The visual time of the operation, as a factor.
     * @param mark A toggle for whether highlights are made during the operation.
     * @return An {@code int} with the index of the first unsorted minimum, inclusively.
     * @author PCBoy
     * @since MHT-1.0
    */
    public int minSorted(int[] array, int start, int end, double delay, boolean mark) {
        return minSortedW(array, start, end, delay, mark)[0];
    }

    /**Performs a Stable Segment Reversal (SSR) on {@code array} items between {@code [start, end]}.
     * @param array The input array.
     * @param start The start of the reversal, inclusively.
     * @param end The end of the reversal, inclusively.
     * @param delay The visual time of the operation, as a factor.
     * @param mark A toggle for whether highlights are made during the operation.
     * @param aux A toggle for whether the operation is indicated in external space.
     * @author PCBoy
     * @since MHT-1.0
     */
    public void stableSegmentReversal(int[] array, int start, int end, double delay, boolean mark, boolean aux) {
        if (end - start <= 0) return;
        if (end - start < 3) Writes.swap(array, start, end, delay * 0.3, mark, aux);
        else Writes.reversal(array, start, end, delay * 0.3, mark, aux);
        for (int i = start; i < end; i++) {
            int left = i;
            while (Reads.compareIndices(array, i, i + 1, delay, mark) == 0 && i < end) i++;
            int right = i;
            if (left != right) {
                if (right - left < 3) Writes.swap(array, left, right, delay * 3, mark, aux);
                else Writes.reversal(array, left, right, delay * 3, mark, aux);
            }
        }
    }

    /**Defines a run from {@code array} starting at {@code start}, and if backward, stably reverses it.
     * @param array The input array.
     * @param start The start of the input bounds, inclusively.
     * @param end The end of the input bounds, exclusively.
     * @param delay The visual time of the operation, as a factor.
     * @param mark A toggle for whether highlights are made during the operation.
     * @param aux A toggle for whether the operation is indicated in external space.
     * @return An {@code int} with the index where the run gets cut off.
     * @author Ayako-chan
     * @author PCBoy
     * @since MHT-1.0
     */
    public int stableFindRun(int[] array, int start, int end, double delay, boolean mark, boolean aux) {
        int i = start;
        boolean lessunique = false;
        boolean different = false;
        int cmp = Reads.compareIndices(array, i, i + 1, delay, mark);
        while (cmp == 0 && i + 1 < end) {
            lessunique = true;
            i++;
            if (i + 1 < end) cmp = Reads.compareIndices(array, i, i + 1, delay, mark);
        }
        if (cmp > 0) {
            while (cmp >= 0 && i + 1 < end) {
                if (cmp == 0) lessunique = true;
                else different = true;
                i++;
                if (i + 1 < end) cmp = Reads.compareIndices(array, i, i + 1, delay, mark);
            }
            if (i > start && different) {
                if (lessunique) stableSegmentReversal(array, start, i, delay / 2, mark, aux);
                else if (i < start + 3) Writes.swap(array, start, i, delay * 2, mark, aux);
                else Writes.reversal(array, start, i, delay * 2, mark, aux);
            }
            return i + 1;
        } else {
            while (cmp <= 0 && i < end) {
                i++;
                if (i + 1 < end) cmp = Reads.compareIndices(array, i, i + 1, delay, mark);
            }
            return i + 1;
        }
    }

    /**Defines a run from {@code array} starting at {@code start}, and if backward, reverses it.
     * @param array The input array.
     * @param start The start of the input bounds, inclusively.
     * @param end The end of the input bounds, exclusively.
     * @param delay The visual time of the operation, as a factor.
     * @param mark A toggle for whether highlights are made during the operation.
     * @param aux A toggle for whether the operation is indicated in external space.
     * @return An {@code int} with the index where the run gets cut off.
     * @author Ayako-chan
     * @author PCBoy
     * @since MHT-1.0
     */
    public int findRun(int[] array, int start, int end, double delay, boolean mark, boolean aux) {
        int i = start + 1;
        if (i == end) return i;
        int cmp = Reads.compareIndices(array, i - 1, i++, delay, mark);
        while (cmp == 0 && i < end) cmp = Reads.compareIndices(array, i - 1, i++, delay, mark);
        if (cmp == 1) {
            while (i < end && Reads.compareIndices(array, i - 1, i, delay, mark) >= 0) i++;
            if (i - start < 4) Writes.swap(array, start, i - 1, delay * 2, mark, aux);
            else Writes.reversal(array, start, i - 1, delay * 2, mark, aux);
        }
        else while (i < end && Reads.compareIndices(array, i - 1, i, delay, mark) <= 0) i++;
        Highlights.clearMark(2);
        return i;
    }

    /**Performs a Pattern-Defeating operation that flips reversed runs from {@code array} between {@code (start, end]}.
     * @param array The input array.
     * @param start The start of the input bounds, inclusively.
     * @param end The end of the input bounds, exclusively.
     * @param stable A toggle for whether the operation is performed stably or not.
     * @param delay The visual time of the operation, as a factor.
     * @param mark A toggle for whether highlights are made during the operation.
     * @param aux A toggle for whether the operation is indicated in external space.
     * @return A {@code boolean}, whether the entire input range is one run.
     * @author Ayako-chan
     * @author PCBoy
     * @since MHT-1.0
     */
    public boolean patternDefeat(int[] array, int start, int end, boolean stable, double delay, boolean mark, boolean aux) {
        int i = start + 1, j = start;
        boolean noSort = true;
        while (i < end) {
            i = stable ? stableFindRun(array, j, end, delay, mark, aux) : findRun(array, j, end, delay, mark, aux);
            if (i < end) noSort = false;
            j = i++;
        }
        return noSort;
    }

    /**Defines the translated value of {@code val} in the array.
     * @param value The value to translate.
     * @return An {@code int} with the real value translated from the input.
     * @author Gaming32
     * @author aphitorite
     * @since MHT-1.0
     */
    public int stableReturn(int value) {
        return arrayVisualizer.doingStabilityCheck() ? arrayVisualizer.getStabilityValue(value) : value;
    }

    /**Defines the maximum disparity of {@code array} section {@code [start, end)} in {@code O(n)} time.
     * @param array The input array.
     * @param start The start of the input bounds, inclusively.
     * @param end The end of the input bounds, exclusively.
     * @param delay The visual time of the operation, as a factor.
     * @return An {@code int} with the resulting disparity.
     * @author Control
     * @since MHT-1.0
     */
    public int parX(int[] array, int start, int end, double delay, boolean mark) {
        boolean[] max = new boolean[end - start];
        Writes.changeAllocAmount(end - start);
        int maximum = stableReturn(array[start]);
        for (int i = 1; i < end - start; i++) {
            if (mark) Highlights.markArray(1, start + i);
            if (stableReturn(array[start + i]) > maximum) {
                maximum = stableReturn(array[start + i]);
                max[i] = true;
                Writes.changeAuxWrites(1);
            }
            Delays.sleep(delay);
        }
        int p = 1;
        for (int i = end - start - 1, j = end - start - 1; j >= 0 && i >= p; j--) {
            while (!max[j] && j > 0) j--;
            maximum = stableReturn(array[start + j]);
            while (maximum <= stableReturn(array[start + i]) && i >= p) i--;
            if (stableReturn(array[start + j]) > stableReturn(array[start + i]) && p < i - j) p = i - j;
            j--;
        }
        Writes.changeAllocAmount(-(end - start));
        return p;
    }

    /**Performs a permutation check between the {@code original} and an {@code input} to compare with.<p>
     * The minimum length of the two is used overall in order to prevent differences in lengths.
     * @param original The original input.
     * @param input The potentially modified version of the input.
     * @param delay The visual time of the operation, as a factor.
     * @param mark A toggle for whether highlights are made during the operation.
     * @return A {@code boolean}, whether the {@code original} and the {@code input}, bounded by the minimum length of the two, are permutations of each other.
     * @author PCBoy
     * @since MHT-1.0
     */
    public boolean isPerm(int[] original, int[] input, double delay, boolean mark) {
        int length = Math.min(original.length, input.length);
        int empty = Integer.MAX_VALUE;
        for (int i = 0; i < length; i++) if (stableReturn(original[i]) < empty) empty = stableReturn(original[i]);
        empty--;
        boolean perm = true;
        int[] test = Writes.createExternalArray(length);
        Writes.arraycopy(input, 0, test, 0, length, delay, mark, true);
        for (int i = 0; i < length; i++) {
            int select = 0;
            boolean any = false;
            for (int j = 0; j < length; j++) {
                if (test[j] != empty) {
                    if (mark) {
                        Highlights.markArray(1, i);
                        Highlights.markArray(2, j);
                    }
                    Delays.sleep(delay);
                    if (Reads.compareValues(original[i], test[j]) == 0) {
                        select = j;
                        any = true;
                        break;
                    }
                }
            }
            Highlights.clearAllMarks();
            if (any) Writes.write(test, select, empty, delay, mark, true);
            else {
                perm = false;
                break;
            }
        }
        Writes.deleteExternalArray(test);
        return perm;
    }

    /**Defines an index in a sorted segment of which is or approximates {@code value} in {@code O(log n)} time using a Binary search.
     * @param array The input array.
     * @param start The start of the input bounds, inclusively.
     * @param end The end of the input bounds, inclusively.
     * @param value The value to search or approximate for.
     * @param left A toggle for whether the search looks for a left-most instance of the value.
     * @param delay The visual time of the operation, as a factor.
     * @param mark A toggle for whether highlights are made during the operation.
     * @return An {@code int} with the resulting index.
     * @author w0rthy
     * @author Ayako-chan
     * @since MHT-1.0
     */
    public int binarySearch(int[] array, int start, int end, int value, boolean left, double delay, boolean mark) {
        while (start < end) {
            int m = start + (end - start) / 2;
            if (mark) {
                Highlights.markArray(1, start);
                Highlights.markArray(2, m);
                Highlights.markArray(3, end);
            }
            Delays.sleep(delay);
            int c = Reads.compareValues(value, array[m]);
            if (c < 0 || (left && c == 0)) end = m;
            else start = m + 1;
        }
        return start;
    }

    /**Defines an index in a sorted segment of which is or approximates {@code value} in {@code O(log n)} time using an Exponential search starting from the maximums.
     * @param array The input array.
     * @param start The start of the input bounds, inclusively.
     * @param end The end of the input bounds, inclusively.
     * @param value The value to search or approximate for.
     * @param left A toggle for whether the search looks for a left-most instance of the value.
     * @param delay The visual time of the operation, as a factor.
     * @param mark A toggle for whether highlights are made during the operation.
     * @return An {@code int} with the resulting index.
     * @author Ayako-chan
     * @author PCBoy
     * @since MHT-1.0
     */
    public int maxExponentialSearch(int[] array, int start, int end, int value, boolean left, double delay, boolean mark) {
        int i = 1;
        int cmp = Reads.compareValues(value, array[end - i]);
        while (end - i >= start && (cmp < 0 || (left && cmp == 0))) {
            if (mark) {
                Highlights.markArray(1, end - i / 2);
                Highlights.markArray(2, end - i);
            }
            Delays.sleep(delay);
            i *= 2;
            if (end - i >= start) cmp = Reads.compareValues(value, array[end - i]);
        }
        int a1 = Math.max(start, end - i + 1);
        int b1 = end - i / 2;
        return binarySearch(array, a1, b1, value, left, delay, mark);
    }

    /**Defines an index in a sorted segment of which is or approximates {@code value} in {@code O(log n)} time using an Exponential search starting from the minimums.
     * @param array The input array.
     * @param start The start of the input bounds, inclusively.
     * @param end The end of the input bounds, inclusively.
     * @param value The value to search or approximate for.
     * @param left A toggle for whether the search looks for a left-most instance of the value.
     * @param delay The visual time of the operation, as a factor.
     * @param mark A toggle for whether highlights are made during the operation.
     * @return An {@code int} with the resulting index.
     * @author Ayako-chan
     * @author PCBoy
     * @since MHT-1.0
     */
    public int minExponentialSearch(int[] array, int start, int end, int value, boolean left, double delay, boolean mark) {
        int i = 1;
        int cmp = Reads.compareValues(value, array[start - 1 + i]);
        while (start - 1 + i < end && (cmp > 0 || (!left && cmp == 0))) {
            if (mark) {
                Highlights.markArray(1, start + i / 2);
                Highlights.markArray(2, start - 1 + i);
            }
            Delays.sleep(delay);
            i *= 2;
            if (start - 1 + i < end) cmp = Reads.compareValues(value, array[start - 1 + i]);
        }
        int a1 = start + i / 2;
        int b1 = Math.min(end, start - 1 + i);
        return binarySearch(array, a1, b1, value, left, delay, mark);
    }
}