package sorts.templates;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import main.ArrayVisualizer;
import utils.*;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

CHANGELOG:

MHT-1.1a (August 7, 2025)
 - Improve the documentation.
   + Add related functions to docs.
 - Add a safe swap function.
 - Add color functions. This is alongside adding color highlighting to the ArrayV fork.
 - (Hopefully) fix the stable segment reversal function.

MHT-1.1 (March 22, 2024)
 - Add center bias search.
 - Add a simple stable swap.
 - Improve and start to manage sort bugfixing efforts.
   + In-depth error messages are now displayed when certain functions are given invalid parameters.
     * Rotations of length zero are still allowed, as they are executed with no error.
   + If any compatability issues arise from this update, please report them.

MHT-1.0b (September 30, 2023)
 - Minor rewrites and fixes.
   + (Hopefully) fixed bound issues with the max-side exponential search.
 - Minor rewrites and touchups of Javadocs.
 - parX() comparisons are now counted.
 - Aux rotations are now an option.

MHT-1.0a (July 21, 2023)
 - Minor rewrites and fixes.
 - MHT now extends GrailSorting.
   + I also decided to manage the rotation options the template uses.
 - Add Rectangle algorithm functions.
 - Add Rotation call functions.
 - Add prime number functions.

MHT-1.0 (June 26, 2023)
 - Initial version.

*/
/**The Madhouse Tools template contains many useful sorting algorithm development tools and utilities, brought to you in part by the Sorting Algorithm Madhouse.<p>
 * Compiled and rewritten by PCBoy.
 * @version MHT-1.1a
 * @author Various
 */
public abstract class MadhouseTools extends GrailSorting {
    protected MadhouseTools(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    /*-----------------------\
    |SINCE MADHOUSE TOOLS 1.0|
    \-----------------------*/

    /**Copied from the {@link BogoSorting#randInt BogoSorting} template to allow random integers to work alongside MadhouseTools.
     * @param min The start of the range.
     * @param max The end of the range, exclusively.
     * @throws RuntimeException If the range is invalid, i.e. if {@code min} is greater than or equal to {@code max}.
     * @return An {@code int} with a random integer in the range.
     * @author Doug Lea
     * @since MHT-1.0
     */
    public int randInt(int min, int max) throws RuntimeException {
        if (min >= max) throw new RuntimeException("MadhouseTools: Invalid range for random integer, since min is after or at max: (" + min + ", " + max + ")");
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    /**Copied from the {@link BogoSorting#bogoSwap BogoSorting} template to allow the Fisher–Yates shuffle function to work alongside MadhouseTools.
     * @param array The input array.
     * @param start The start of the input bounds.
     * @param end The end of the input bounds, exclusively.
     * @param delay The visual time of the operation, as a factor.
     * @param mark A toggle for whether highlights are made during the operation.
     * @param aux A toggle for whether the operation is indicated in external space.
     * @throws RuntimeException If the range is invalid, i.e. if {@code start} is greater than or equal to {@code end}.
     * @see {@link #randInt(int, int)} Runs this function. Useful for custom shuffles.
     * @author w0rthy
     * @since MHT-1.0
     */
    public void shuffleArray(int[] array, int start, int end, double delay, boolean mark, boolean aux) throws RuntimeException {
        if (start >= end) throw new RuntimeException("MadhouseTools: Invalid range for random shuffle, since start is after or at end: (" + start + ", " + end + ")");
        for (int i = start; i < end; ++i) {
            int j = randInt(i, end);
            if (i != j) Writes.swap(array, i, j, delay, mark, aux);
        }
    }

    /**Defines the first unsorted position from the maximums and where the next item in line is in {@code O(n)}, given input {@code array} from bounds {@code [start, end)}.
     * @param array The input array.
     * @param start The start of the input bounds.
     * @param end The end of the input bounds, exclusively.
     * @param delay The visual time of the operation, as a factor.
     * @param mark A toggle for whether highlights are made during the operation.
     * @throws RuntimeException If the range is invalid, i.e. if {@code start} is greater than or equal to {@code end}.
     * @see {@link #maxSorted(int[], int, int, double, boolean)} Expands on this function. Does not return the next item in line.
     * @see {@link #minSortedW(int[], int, int, double, boolean)} Similar, but for minimums.
     * @see {@link #minSorted(int[], int, int, double, boolean)} Similar, but for minimums. Does not return the next item in line.
     * @return An {@code int[]} with two values:<ul><li>{@code [0]}: the index of the first unsorted maximum, exclusively.</li><li>{@code [1]}: the index of the next item in line.<ul><li>Special Case: returns {@code -1} if sorted.</li></ul></li></ul>
     * @author PCBoy
     * @since MHT-1.0
    */
    public int[] maxSortedW(int[] array, int start, int end, double delay, boolean mark) throws RuntimeException {
        if (start >= end) throw new RuntimeException("MadhouseTools: Invalid range for max sorted check, since start is after or at end: (" + start + ", " + end + ")");
        int i = end - 1;
        end--;
        boolean segment = true;
        while (segment) {
            if (i - 1 < start) return new int[] {start, -1};
            if (Reads.compareIndices(array, i - 1, i, delay, mark) > 0) segment = false;
            else i--;
        }
        int sel = i - 1;
        for (int s = i - 2; s >= start; s--) if (Reads.compareIndices(array, sel, s, delay, mark) < 0) sel = s;
        int where = sel;
        while (Reads.compareIndices(array, sel, end, delay, mark) <= 0) {
            end--;
            if (end < start) break;
        }
        return new int[] {end + 1, where};
    }

    /**Defines the first unsorted position from the maximums in {@code O(n)}, given input {@code array} from bounds {@code [start, end)}.
     * @param array The input array.
     * @param start The start of the input bounds.
     * @param end The end of the input bounds, exclusively.
     * @param delay The visual time of the operation, as a factor.
     * @param mark A toggle for whether highlights are made during the operation.
     * @throws RuntimeException If the range is invalid, i.e. if {@code start} is greater than or equal to {@code end}. Gets thrown from {@link #maxSortedW}.
     * @see {@link #maxSortedW(int[], int, int, double, boolean)} Runs this function. Returns the next item in line.
     * @see {@link #minSortedW(int[], int, int, double, boolean)} Similar, but for minimums. Returns the next item in line.
     * @see {@link #minSorted(int[], int, int, double, boolean)} Similar, but for minimums.
     * @return An {@code int} with the index of the first unsorted maximum, exclusively.
     * @author PCBoy
     * @since MHT-1.0
    */
    public int maxSorted(int[] array, int start, int end, double delay, boolean mark) {
        return maxSortedW(array, start, end, delay, mark)[0];
    }

    /**Defines the first unsorted position from the minimums and where the next item in line is in {@code O(n)}, given input {@code array} from bounds {@code [start, end)}.
     * @param array The input array.
     * @param start The start of the input bounds.
     * @param end The end of the input bounds, exclusively.
     * @param delay The visual time of the operation, as a factor.
     * @param mark A toggle for whether highlights are made during the operation.
     * @throws RuntimeException If the range is invalid, i.e. if {@code start} is greater than or equal to {@code end}.
     * @see {@link #minSorted(int[], int, int, double, boolean)} Expands on this function. Does not return the next item in line.
     * @see {@link #maxSortedW(int[], int, int, double, boolean)} Similar, but for maximums.
     * @see {@link #maxSorted(int[], int, int, double, boolean)} Similar, but for maximums. Does not return the next item in line.
     * @return An {@code int[]} with two values:<ul><li>{@code [0]}: the index of the first unsorted minimum.</li><li>{@code [1]}: the index of the next item in line.<ul><li>Special Case: returns {@code -1} if sorted.</li></ul></li></ul>
     * @author PCBoy
     * @since MHT-1.0
    */
    public int[] minSortedW(int[] array, int start, int end, double delay, boolean mark) throws RuntimeException {
        if (start >= end) throw new RuntimeException("MadhouseTools: Invalid range for min sorted check, since start is after or at end: (" + start + ", " + end + ")");
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
     * @param start The start of the input bounds.
     * @param end The end of the input bounds, exclusively.
     * @param delay The visual time of the operation, as a factor.
     * @param mark A toggle for whether highlights are made during the operation.
     * @throws RuntimeException If the range is invalid, i.e. if {@code start} is greater than or equal to {@code end}. Gets thrown from {@link #minSortedW}.
     * @see {@link #minSortedW(int[], int, int, double, boolean)} Runs this function. Returns the next item in line.
     * @see {@link #maxSortedW(int[], int, int, double, boolean)} Similar, but for maximums. Returns the next item in line.
     * @see {@link #maxSorted(int[], int, int, double, boolean)} Similar, but for maximums.
     * @return An {@code int} with the index of the first unsorted minimum.
     * @author PCBoy
     * @since MHT-1.0
    */
    public int minSorted(int[] array, int start, int end, double delay, boolean mark) {
        return minSortedW(array, start, end, delay, mark)[0];
    }

    /**Performs a Stable Segment Reversal (SSR) on {@code array} items between {@code [start, end]}.
     * @param array The input array.
     * @param start The start of the reversal.
     * @param end The end of the reversal, inclusively.
     * @param delay The visual time of the operation, as a factor.
     * @param mark A toggle for whether highlights are made during the operation.
     * @param aux A toggle for whether the operation is indicated in external space.
     * @see {@link #stableFindRun(int[], int, int, double, boolean, boolean)} Expands on this function.
     * @throws RuntimeException If the range is invalid, i.e. if {@code start} is greater than or equal to {@code end}.
     * @author PCBoy
     * @since MHT-1.0
     */
    public void stableSegmentReversal(int[] array, int start, int end, double delay, boolean mark, boolean aux) throws RuntimeException {
        if (start >= end) throw new RuntimeException("MadhouseTools: Invalid range for stable segment reversal, since start is after or at end: (" + start + ", " + end + ")");
        if (end - start < 3) Writes.swap(array, start, end, delay * 0.3, mark, aux);
        else Writes.reversal(array, start, end, delay * 0.3, mark, aux);
        for (int i = start; i < end; i++) {
            int left = i;
            while (i < end && Reads.compareIndices(array, i, i + 1, delay, mark) == 0) i++;
            int right = i;
            if (left != right) {
                if (right - left < 3) Writes.swap(array, left, right, delay * 3, mark, aux);
                else Writes.reversal(array, left, right, delay * 3, mark, aux);
            }
        }
    }

    /**Defines a run from {@code array} starting at {@code start}, and if backward, stably reverses it.
     * @param array The input array.
     * @param start The start of the input bounds.
     * @param end The end of the input bounds, exclusively.
     * @param delay The visual time of the operation, as a factor.
     * @param mark A toggle for whether highlights are made during the operation.
     * @param aux A toggle for whether the operation is indicated in external space.
     * @throws RuntimeException If the range is invalid, i.e. if {@code start} is greater than or equal to {@code end}.
     * @see {@link #stableSegmentReversal(int[], int, int, double, boolean, boolean)} Runs this function to reverse the run.
     * @see {@link #findRun(int[], int, int, double, boolean, boolean)} Similar, but unstable.
     * @see {@link #patternDefeat(int[], int, int, boolean, double, boolean, boolean)} Expands on this function, but for multiple runs.
     * @return An {@code int} with the index where the run gets cut off, the last index of the run exclusively.
     * @author Ayako-chan
     * @author PCBoy
     * @since MHT-1.0
     */
    public int stableFindRun(int[] array, int start, int end, double delay, boolean mark, boolean aux) throws RuntimeException {
        if (start >= end) throw new RuntimeException("MadhouseTools: Invalid range for stable run detection, since start is after or at end: (" + start + ", " + end + ")");
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
        } else {
            while (cmp <= 0 && i < end) {
                i++;
                if (i + 1 < end) cmp = Reads.compareIndices(array, i, i + 1, delay, mark);
            }
        }
        return Math.min(i + 1, end);
    }

    /**Defines a run from {@code array} starting at {@code start}, and if backward, reverses it.
     * @param array The input array.
     * @param start The start of the input bounds.
     * @param end The end of the input bounds, exclusively.
     * @param delay The visual time of the operation, as a factor.
     * @param mark A toggle for whether highlights are made during the operation.
     * @param aux A toggle for whether the operation is indicated in external space.
     * @throws RuntimeException If the range is invalid, i.e. if {@code start} is greater than or equal to {@code end}.
     * @see {@link #stableFindRun(int[], int, int, double, boolean, boolean)} Similar, but stable.
     * @see {@link #patternDefeat(int[], int, int, boolean, double, boolean, boolean)} Expands on this function, but for multiple runs.
     * @return An {@code int} with the index where the run gets cut off, the last index of the run exclusively.
     * @author Ayako-chan
     * @author PCBoy
     * @since MHT-1.0
     */
    public int findRun(int[] array, int start, int end, double delay, boolean mark, boolean aux) throws RuntimeException {
        if (start >= end) throw new RuntimeException("MadhouseTools: Invalid range for run detection, since start is after or at end: (" + start + ", " + end + ")");
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
        return Math.min(i, end);
    }

    /**Performs a Pattern-Defeating operation that flips reversed runs from {@code array} between {@code [start, end)}.
     * @param array The input array.
     * @param start The start of the input bounds.
     * @param end The end of the input bounds, exclusively.
     * @param stable A toggle for whether the operation is performed stably or not.
     * @param delay The visual time of the operation, as a factor.
     * @param mark A toggle for whether highlights are made during the operation.
     * @param aux A toggle for whether the operation is indicated in external space.
     * @throws RuntimeException If the range is invalid, i.e. if {@code start} is greater than or equal to {@code end}.
     * @see {@link #stableFindRun(int[], int, int, double, boolean, boolean)} Runs this function to find runs stably.
     * @see {@link #findRun(int[], int, int, double, boolean, boolean)} Runs this function to find runs unstably.
     * @return A {@code boolean}, whether the entire input range is one run.
     * @author Ayako-chan
     * @author PCBoy
     * @since MHT-1.0
     */
    public boolean patternDefeat(int[] array, int start, int end, boolean stable, double delay, boolean mark, boolean aux) throws RuntimeException {
        if (start >= end) throw new RuntimeException("MadhouseTools: Invalid range for pattern defeat, since start is after or at end: (" + start + ", " + end + ")");
        int i = start + 1, j = start;
        boolean noSort = true;
        while (i < end) {
            i = stable ? stableFindRun(array, j, end, delay, mark, aux) : findRun(array, j, end, delay, mark, aux);
            if (i < end) noSort = false;
            j = i++;
        }
        return noSort;
    }

    /**Defines the translated value of {@code value} in the array.
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
     * @param start The start of the input bounds.
     * @param end The end of the input bounds, exclusively.
     * @param delay The visual time of the operation, as a factor.
     * @param mark A toggle for whether highlights are made during the operation.
     * @throws RuntimeException If the range is invalid, i.e. if {@code start} is greater than or equal to {@code end}.
     * @return An {@code int} with the resulting disparity.
     * @author Control
     * @since MHT-1.0
     */
    public int parX(int[] array, int start, int end, double delay, boolean mark) throws RuntimeException {
        if (start >= end) throw new RuntimeException("MadhouseTools: Invalid range for disparity check, since start is after or at end: (" + start + ", " + end + ")");
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
            Highlights.markArray(1, start + j);
            int c = stableReturn(array[start + i]);
            Highlights.markArray(2, start + i);
            Reads.addComparison();
            Delays.sleep(delay);
            while (maximum <= c && i >= p) {
                i--;
                c = stableReturn(array[start + i]);
                Highlights.markArray(2, start + i);
                Delays.sleep(delay);
                Reads.addComparison();
            }
            Reads.addComparison();
            if (stableReturn(array[start + j]) > stableReturn(array[start + i]) && p < i - j) p = i - j;
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
     * @param start The start of the input bounds.
     * @param end The end of the input bounds, exclusively.
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
        Highlights.clearAllMarks();
        return start;
    }

    /**Defines an index in a sorted segment of which is or approximates {@code value} in {@code O(log n)} time using an Exponential search starting from the maximums.
     * @param array The input array.
     * @param start The start of the input bounds.
     * @param end The end of the input bounds, exclusively.
     * @param value The value to search or approximate for.
     * @param left A toggle for whether the search looks for a left-most instance of the value.
     * @param delay The visual time of the operation, as a factor.
     * @param mark A toggle for whether highlights are made during the operation.
     * @see {@link #binarySearch(int[], int, int, int, boolean, double, boolean)} Runs this function.
     * @see {@link #minExponentialSearch(int[], int, int, int, boolean, double, boolean)} Similar, but for minimums.
     * @return An {@code int} with the resulting index.
     * @author Ayako-chan
     * @author PCBoy
     * @since MHT-1.0
     */
    public int maxExponentialSearch(int[] array, int start, int end, int value, boolean left, double delay, boolean mark) {
        int i = 1;
        int cmp = -1;
        if (end - i >= start) cmp = Reads.compareValues(value, array[end - i]);
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
     * @param start The start of the input bounds.
     * @param end The end of the input bounds, exclusively.
     * @param value The value to search or approximate for.
     * @param left A toggle for whether the search looks for a left-most instance of the value.
     * @param delay The visual time of the operation, as a factor.
     * @param mark A toggle for whether highlights are made during the operation.
     * @see {@link #binarySearch(int[], int, int, int, boolean, double, boolean)} Runs this function.
     * @see {@link #maxExponentialSearch(int[], int, int, int, boolean, double, boolean)} Similar, but for maximums.
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

    /*------------------------\
    |SINCE MADHOUSE TOOLS 1.0a|
    \------------------------*/

    /**Defines the dimensions of the square-most rectangle that contains an area of exactly {@code x}.
     * @param x The area of the rectangle.
     * @throws RuntimeException If the area is not greater than 0.
     * @see {@link #isValidRectangleAction(int, int, int, int)} Checks whether actions on the rectangle are valid.
     * @return An {@code int[]} with two values:<ul><li>{@code [0]}: the longer side length.</li><li>{@code [1]}: the shorter side length.</li></ul>
     * @author PCBoy
     * @since MHT-1.0a
    */
    public int[] getRectangleDimensions(int x) throws RuntimeException {
        if (x <= 0) throw new RuntimeException("MadhouseTools: Invalid area for rectangle dimensions, since value is not greater than 0: " + x);
        int a = x;
        for (int p = (int) Math.ceil(Math.sqrt(x)); p < x; p++) {
            if (x % p == 0) {
                a = p;
                break;
            }
        }
        int b = x / a;
        return new int[] {a, b};
    }

    /**Defines whether or not actions on indexes {@code a} and {@code b} of an {@code x}-by-{@code y} Rectangle sort are valid actions.
     * @param x The length of the rectangle.
     * @param y The height of the rectangle.
     * @param a An index to check.
     * @param b Another index to check.
     * @throws RuntimeException If the dimensions are not greater than 0, or if the indexes are out of bounds of the rectangle.
     * @see {@link #getRectangleDimensions(int)} Usually a premise to this function.
     * @return A {@code boolean}, whether indexes {@code a} and {@code b} are along a straight line inside the rectangle.
     * @author PCBoy
     * @since MHT-1.0a
    */
    public boolean isValidRectangleAction(int x, int y, int a, int b) throws RuntimeException {
        if (x <= 0 || y <= 0) throw new RuntimeException("MadhouseTools: Invalid dimensions for rectangle action, since one or more dimensions are not greater than 0: (" + x + ", " + y + ")");
        else if (a >= x * y || b >= x * y || a < 0 || b < 0) throw new RuntimeException("MadhouseTools: Invalid indices for rectangle action, since one or more are out of bounds of the rectangle: (" + a + ", " + b + ")");
        else if (Math.abs(b - a) < x) {if (Math.floor(a / x) != Math.floor(b / x)) return false;}
        else if (a % x != b % x) return false;
        return true;
    }

    /**Performs an {@code array} rotation using a combination of Holy Gries-Mills and Contrev rotations.<p>
     * This uses a length-based format. For the index format, use {@link #rotateIndexed} instead.
     * @param array The input array.
     * @param pos The starting position of the rotation.
     * @param lenA The first length of the rotation, the subarray to move tailward.
     * @param lenB The second length of the rotation, the subarray to move headward.
     * @param delay The visual time of the operation, as a factor.
     * @param mark A toggle for whether highlights are made during the operation.
     * @param aux A toggle for whether the operation is indicated in external space.
     * @throws RuntimeException If either length is negative.
     * @see {@link #rotateIndexed(int[], int, int, int, double, boolean, boolean)} Index-based version of this function.
     * @see {@link Rotations#adaptableRevised(int[], int, int, int, double, boolean, boolean)} Rotation code that this function uses.
     * @author PCBoy
     * @author Team Holy Grail
     * @author David Gries
     * @author Harlan Mills
     * @author Scandum
     * @since MHT-1.0a
    */
    public void rotate(int[] array, int pos, int lenA, int lenB, double delay, boolean mark, boolean aux) throws RuntimeException {
        if (lenA < 0 || lenB < 0) throw new RuntimeException("MadhouseTools: Invalid lengths for rotation, since one or both values are not positive: (" + lenA + ", " + lenB + ")");
        Rotations.adaptableRevised(array, pos, lenA, lenB, delay, mark, aux);
    }

    /**Replacement code for {@link GrailSorting#grailRotate} with a combination of Holy Gries-Mills and Contrev rotations.<p>
     * This uses the same delay factor, mark setting, and aux setting parameters as {@link GrailSorting#grailSwap the original Grail swap code}. To customize these, use {@link #rotate} directly or {@link #rotateIndexed} instead.
     * @param array The input array.
     * @param pos The starting position of the rotation.
     * @param lenA The first length of the rotation, the subarray to move tailward.
     * @param lenB The second length of the rotation, the subarray to move headward.
     * @throws RuntimeException If either length is negative. Gets thrown from {@link #rotate}.
     * @see {@link #rotate(int[], int, int, int, double, boolean, boolean)} Runs this function.
     * @see {@link #rotateIndexed(int[], int, int, int, double, boolean, boolean)} Index-based version of the function this runs.
     * @see {@link GrailSorting#grailRotate(int[], int, int, int)} The original Grail rotate code that this function replaces.
     * @author PCBoy
     * @author Team Holy Grail
     * @author David Gries
     * @author Harlan Mills
     * @author Scandum
     * @since MHT-1.0a
    */
    public void grailRotate(int[] array, int pos, int lenA, int lenB) {
        rotate(array, pos, lenA, lenB, 1, true, false);
    }

    /**Performs an {@code array} rotation using a combination of Holy Gries-Mills and Contrev rotations.<p>
     * This uses an index-based format. For the length format, use {@link #rotate} directly instead.
     * @param array The input array.
     * @param a The starting position of the rotation.
     * @param b The second position of the rotation, indicating a split in subarrays. The index inclusively begins the second subarray, and exclusively ends the first.
     * @param c The end position of the rotation, exclusively.
     * @param delay The visual time of the operation, as a factor.
     * @param mark A toggle for whether highlights are made during the operation.
     * @param aux A toggle for whether the operation is indicated in external space.
     * @throws RuntimeException If either length (after conversion) is negative. Gets thrown from {@link #rotate}.
     * @see {@link #rotate(int[], int, int, int, double, boolean, boolean)} Length-based version of this function.
     * @see {@link Rotations#adaptableRevised(int[], int, int, int, double, boolean, boolean)} Rotation code that this function uses.
     * @author PCBoy
     * @author Team Holy Grail
     * @author David Gries
     * @author Harlan Mills
     * @author Scandum
     * @since MHT-1.0a
    */
    public void rotateIndexed(int[] array, int a, int b, int c, double delay, boolean mark, boolean aux) {
        rotate(array, a, b - a, c - b, delay, mark, aux);
    }

    /**Defines a prime number less than {@code n}.
     * @param n The initial number.
     * @see {@link #greaterPrime(int)} The opposite of this function.
     * @return An {@code int} with the first prime number that is less than the input.<ul><li>Special Case: Returns {@code 1} if {@code n <= 2}.</li></ul>
     * @author PCBoy
     * @since MHT-1.0a
     */
    public int lessPrime(int n) {
        boolean broken = false;
        if (n <= 2) return 1;
        while (!broken) {
            n--;
            for (int i = (int) Math.ceil(Math.sqrt(n)); i < n; i++) {
                if (n % i == 0) break;
                if (i == n - 1) broken = true;
            }
        }
        return n;
    }

    /**Defines a prime number greater than {@code n}.
     * @param n The initial number.
     * @see {@link #lessPrime(int)} The opposite of this function.
     * @return An {@code int} with the first prime number that is greater than the input.<ul><li>Special Case: Returns {@code 1} if {@code n <= 0}.</li></ul>
     * @author PCBoy
     * @since MHT-1.0a
     */
    public int greaterPrime(int n) {
        boolean broken = false;
        if (n <= 0) return 1;
        while (!broken) {
            n++;
            for (int i = (int) Math.ceil(Math.sqrt(n)); i < n; i++) {
                if (n % i == 0) break;
                if (i == n - 1) broken = true;
            }
        }
        return n;
    }

    /**Defines whether or not the two inputs {@code a} and {@code b} are coprime, meaning there are no prime factors in common.
     * @param a The first number to check.
     * @param b The second number to check.
     * @return A {@code boolean}, whether {@code a} and {@code b} are coprime.
     * @author PCBoy
     * @since MHT-1.0a
     */
    public boolean coprime(int a, int b) {
        for (int i = 2; i <= Math.min(a, b); i++) if (a % i == 0 && b % i == 0) return false;
        return true;
    }

    /**Defines the prime number lowest in the prime factor tree of {@code n}.
     * @param n The initial number.
     * @see {@link #highPrime(int)} The opposite of this function.
     * @see {@link #factorTree(int)} The prime factor tree of {@code n}.
     * @return An {@code int} with the first prime number that is in the factor tree.
     * @author PCBoy
     * @since MHT-1.0a
     */
    public int lowPrime(int n) {
        if (n >= 2) for (int i = 2; i < n; i++) if (n % i == 0) return i;
        return n;
    }

    /**Defines the prime number highest in the prime factor tree of {@code n}.
     * @param n The initial number.
     * @see {@link #lowPrime(int)} The opposite of this function.
     * @see {@link #factorTree(int)} The prime factor tree of {@code n}.
     * @return An {@code int} with the last prime number that is in the factor tree.
     * @author PCBoy
     * @since MHT-1.0a
     */
    public int highPrime(int n) {
        int h = lowPrime(n);
        while (n > 1) {
            n /= h;
            if (n > 1) h = lowPrime(n);
        }
        return h;
    }

    /*------------------------\
    |SINCE MADHOUSE TOOLS 1.0b|
    \------------------------*/

    /**Defines a list of prime factors of {@code n}.
     * @param n The initial number.
     * @see {@link #lowPrime(int)} The lowest prime factor of {@code n}.
     * @see {@link #highPrime(int)} The highest prime factor of {@code n}.
     * @return An {@code int[]} with all the values the make the prime factor tree of {@code n}.
     * @author PCBoy
     * @since MHT-1.0b
     */
    public int[] factorTree(int n) {
        ArrayList<int[]> tree = new ArrayList<>();
        while (n != 1) {
            int trial = lowPrime(n);
            tree.add(new int[] {trial});
            n /= trial;
        }
        int[] print = new int[tree.size()];
        for (int i = 0; i < tree.size(); i++) print[i] = tree.get(i)[0];
        tree.clear();
        return print;
    }

    /**Performs an {@code array} rotation using a Bridge rotation.<p>
     * This uses a length-based format. For the index format, use {@link #bridgeSpecifyAuxIndexed} instead.
     * @param array The input array.
     * @param ext The external space to use.
     * @param pos The starting position of the rotation.
     * @param lenA The first length of the rotation, the subarray to move tailward.
     * @param lenB The second length of the rotation, the subarray to move headward.
     * @param delay The visual time of the operation, as a factor.
     * @param mark A toggle for whether highlights are made during the operation.
     * @param aux A toggle for whether the operation is indicated in external space.
     * @throws RuntimeException If either length is negative.
     * @see {@link #bridgeSpecifyAuxIndexed(int[], int[], int, int, int, double, boolean, boolean)} Index-based version of this function.
     * @see {@link Rotations#bridge(int[], int, int, int, double, boolean, boolean)} Rotation code that this function is based on.
     * @author Scandum
     * @since MHT-1.0b
     */
    public void bridgeSpecifyAux(int[] array, int[] ext, int pos, int lenA, int lenB, double delay, boolean mark, boolean aux) throws RuntimeException {
        if (lenA < 0 || lenB < 0) throw new RuntimeException("MadhouseTools: Invalid lengths for bridge rotation, since one or both values are not positive: (" + lenA + ", " + lenB + ")");
        if (lenA < 1 || lenB < 1) return;
        int pta = pos;
        int ptb = pos + lenA;
        int ptc = pos + lenB;
        int ptd = ptb + lenB;
        if (lenA < lenB) {
            int bridge = lenB - lenA;
            if (bridge < lenA) {
                int loop = lenA;
                Writes.arraycopy(array, ptb, ext, 0, bridge, delay, mark, true);
                while (loop-- > 0) {
                    Writes.write(array, --ptc, array[--ptd], delay / 2, mark, aux);
                    Writes.write(array, ptd, array[--ptb], delay / 2, mark, aux);
                }
                Writes.arraycopy(ext, 0, array, pta, bridge, delay, mark, aux);
            } else {
                Writes.arraycopy(array, pta, ext, 0, lenA, delay, mark, true);
                Writes.arraycopy(array, ptb, array, pta, lenB, delay, mark, aux);
                Writes.arraycopy(ext, 0, array, ptc, lenA, delay, mark, aux);
            }
        } else {
            int bridge = lenA - lenB;
            if (bridge < lenB) {
                int loop = lenB;
                Writes.arraycopy(array, ptc, ext, 0, bridge, delay, mark, true);
                while (loop-- > 0) {
                    Writes.write(array, ptc++, array[pta], delay / 2, mark, aux);
                    Writes.write(array, pta++, array[ptb++], delay / 2, mark, aux);
                }
                Writes.arraycopy(ext, 0, array, ptd - bridge, bridge, delay, mark, aux);
            } else {
                Writes.arraycopy(array, ptb, ext, 0, lenB, delay, mark, true);
                while (lenA-- > 0) Writes.write(array, --ptd, array[--ptb], delay, mark, aux);
                Writes.arraycopy(ext, 0, array, pta, lenB, delay, mark, aux);
            }
        }
    }

    /**Performs an {@code array} rotation using a Bridge rotation.<p>
     * This uses an index-based format. For the length format, use {@link #bridgeSpecifyAux} directly instead.
     * @param array The input array.
     * @param ext The external space to use.
     * @param a The starting position of the rotation.
     * @param b The second position of the rotation, indicating a split in subarrays. The index inclusively begins the second subarray, and exclusively ends the first.
     * @param c The end position of the rotation, exclusively.
     * @param delay The visual time of the operation, as a factor.
     * @param mark A toggle for whether highlights are made during the operation.
     * @param aux A toggle for whether the operation is indicated in external space.
     * @throws RuntimeException If either length (after conversion) is negative. Gets thrown from {@link #bridgeSpecifyAux}.
     * @see {@link #bridgeSpecifyAux(int[], int[], int, int, int, double, boolean, boolean)} Length-based version of this function.
     * @see {@link Rotations#bridge(int[], int, int, int, double, boolean, boolean)} Rotation code that this function is based on.
     * @author Scandum
     * @since MHT-1.0b
     */
    public void bridgeSpecifyAuxIndexed(int[] array, int[] ext, int a, int b, int c, double delay, boolean mark, boolean aux) {
        bridgeSpecifyAux(array, ext, a, b - a, c - b, delay, mark, aux);
    }

    /**Performs an {@code array} rotation using a combination of Holy Gries-Mills, Bridge, and Contrev rotations.<p>
     * This uses a length-based format. For the index format, use {@link #rotateAuxIndexed} instead.
     * @param array The input array.
     * @param ext The external space to attempt Bridge using.<ul><li>Special Case: Only used if {@code Math.min(lenA, lenB) <= ext.length}.</li></ul>
     * @param pos The starting position of the rotation.
     * @param lenA The first length of the rotation, the subarray to move tailward.
     * @param lenB The second length of the rotation, the subarray to move headward.
     * @param delay The visual time of the operation, as a factor.
     * @param mark A toggle for whether highlights are made during the operation.
     * @param aux A toggle for whether the operation is indicated in external space.
     * @throws RuntimeException If either length is negative.
     * @see {@link #rotateAuxIndexed(int[], int[], int, int, int, double, boolean, boolean)} Index-based version of this function.
     * @see {@link #bridgeSpecifyAux(int[], int[], int, int, int, double, boolean, boolean)} Sister function that this runs if there is enough aux and HGM isn't perfect.
     * @author PCBoy
     * @author Team Holy Grail
     * @author David Gries
     * @author Harlan Mills
     * @author Scandum
     * @since MHT-1.0b
     */
    public void rotateAux(int[] array, int[] ext, int pos, int lenA, int lenB, double delay, boolean mark, boolean aux) throws RuntimeException {
        if (lenA < 0 || lenB < 0) throw new RuntimeException("MadhouseTools: Invalid lengths for aux rotation, since one or both values are not positive: (" + lenA + ", " + lenB + ")");
        if ((lenA % lenB == 0 && lenA / lenB < 4) || (lenB % lenA == 0 && lenB / lenA < 4)) Rotations.holyGriesMills(array, pos, lenA, lenB, delay, mark, aux);
        else if (Math.min(lenA, lenB) <= ext.length) bridgeSpecifyAux(array, ext, pos, lenA, lenB, delay, mark, aux);
        else Rotations.cycleReverse(array, pos, lenA, lenB, delay, mark, aux);
    }

    /**Performs an {@code array} rotation using a combination of Holy Gries-Mills, Bridge, and Contrev rotations.<p>
     * This uses an index-based format. For the length format, use {@link #rotateAux} directly instead.
     * @param array The input array.
     * @param ext The external space to attempt Bridge using.<ul><li>Special Case: Only used if {@code Math.min(b - a, c - b) <= ext.length}.</li></ul>
     * @param a The starting position of the rotation.
     * @param b The second position of the rotation, indicating a split in subarrays. The index inclusively begins the second subarray, and exclusively ends the first.
     * @param c The end position of the rotation, exclusively.
     * @param delay The visual time of the operation, as a factor.
     * @param mark A toggle for whether highlights are made during the operation.
     * @param aux A toggle for whether the operation is indicated in external space.
     * @throws RuntimeException If either length (after conversion) is negative. Gets thrown from {@link #rotateAux}.
     * @see {@link #rotateAux(int[], int[], int, int, int, double, boolean, boolean)} Length-based version of this function.
     * @see {@link #bridgeSpecifyAux(int[], int[], int, int, int, double, boolean, boolean)} Sister function that this runs if there is enough aux and HGM isn't perfect.
     * @see {@link #bridgeSpecifyAuxIndexed(int[], int[], int, int, int, double, boolean, boolean)} Sister function that this mimics if there is enough aux and HGM isn't perfect.
     * @author PCBoy
     * @author Team Holy Grail
     * @author David Gries
     * @author Harlan Mills
     * @author Scandum
     * @since MHT-1.0b
     */
    public void rotateAuxIndexed(int[] array, int[] ext, int a, int b, int c, double delay, boolean mark, boolean aux) {
        rotateAux(array, ext, a, b - a, c - b, delay, mark, aux);
    }

    /**Performs a linear sortedness check on {@code array}.
     * @param array The input array.
     * @param start The start of the input bounds.
     * @param end The end of the input bounds, exclusively.
     * @throws RuntimeException If the range is invalid, i.e. if {@code start} is greater than or equal to {@code end}.
     * @see {@link #isSortedRevStart(int[], int, int)} Similar, but checks in reverse.
     * @return A {@code boolean}, whether the input is sorted.
     * @author Various
     * @since MHT-1.0b
     */
    public boolean isSorted(int[] array, int start, int end) throws RuntimeException {
        if (start >= end) throw new RuntimeException("MadhouseTools: Invalid range for sortedness detection, since start is after or at end: (" + start + ", " + end + ")");
        for (int i = start; i + 1 < end; i++) if (Reads.compareValues(array[i], array[i + 1]) > 0) return false;
        return true;
    }

    /*-----------------------\
    |SINCE MADHOUSE TOOLS 1.1|
    \-----------------------*/

    /**Defines an index in a sorted segment of which is or approximates {@code value} in {@code O(log n)} time using a Center Bias hybrid search.
     * @param array The input array.
     * @param start The start of the input bounds.
     * @param end The end of the input bounds, exclusively.
     * @param value The value to search or approximate for.
     * @param left A toggle for whether the search looks for a left-most instance of the value.
     * @param delay The visual time of the operation, as a factor.
     * @param mark A toggle for whether highlights are made during the operation.
     * @see {@link #binarySearch(int[], int, int, int, boolean, double, boolean)} Runs this function if {@code end - start < 3}, or within Exponential searches.
     * @see {@link #maxExponentialSearch(int[], int, int, int, boolean, double, boolean)} Runs this function if the value is right of the center.
     * @see {@link #minExponentialSearch(int[], int, int, int, boolean, double, boolean)} Runs this function if the value is left of the center.
     * @return An {@code int} with the resulting index.
     * @author Ayako-chan
     * @author PCBoy
     * @since MHT-1.1
    */
    public int centerBiasSearch(int[] array, int start, int end, int value, boolean left, double delay, boolean mark) {
        if (end - start < 3) return binarySearch(array, start, end, value, left, delay, mark);
        int m = start + (end - start) / 2;
        int c = Reads.compareValues(value, array[m]);
        if (mark) {
            Highlights.markArray(1, start);
            Highlights.markArray(2, m);
            Highlights.markArray(3, end);
        }
        Delays.sleep(delay);
        if (mark) Highlights.clearMark(3);
        if (c == 0) {
            if (left) return maxExponentialSearch(array, start, m + 1, value, left, delay, mark);
            else return minExponentialSearch(array, m, end, value, left, delay, mark);
        } else if (c < 0) return maxExponentialSearch(array, start, m, value, left, delay, mark);
        else return minExponentialSearch(array, m + 1, end, value, left, delay, mark);
    }

    /**Performs a special type of swap that maintains stability across an entire section of an array thus spanned.
     * @param array The input array.
     * @param l The left element in the section.
     * @param r The right element in the section.
     * @param delay The visual time of the operation, as a factor.
     * @param mark A toggle for whether highlights are made during the operation.
     * @param aux A toggle for whether the operation is indicated in external space.
     * @author PCBoy
     * @since MHT-1.1
    */
    public void stableSwap(int[] array, int l, int r, double delay, boolean mark, boolean aux) {
        if (l > r) {
            int t = l;
            l = r;
            r = t;
        }
        int temp = array[r];
        boolean moved = false;
        for (int j = r; j - 1 >= l; j--) {
            if (Reads.compareIndexValue(array, j - 1, temp, delay, mark) != 0) {
                Writes.write(array, j, array[j - 1], delay, mark, aux);
                moved = true;
            } else {
                int temp2 = array[j - 1];
                Writes.write(array, j, temp, delay, mark, aux);
                moved = true;
                temp = temp2;
            }
        }
        if (moved) Writes.write(array, l, temp, delay, mark, aux);
        temp = array[l + 1];
        moved = false;
        for (int j = l + 1; j + 1 <= r; j++) {
            if (Reads.compareValueIndex(array, temp, j + 1, delay, mark) != 0) {
                Writes.write(array, j, array[j + 1], delay, mark, aux);
                moved = true;
            } else {
                int temp2 = array[j + 1];
                Writes.write(array, j, temp, delay, mark, aux);
                moved = true;
                temp = temp2;
            }
        }
        if (moved) Writes.write(array, r, temp, delay, mark, aux);
    }

    /**Performs a linear sortedness check on {@code array}, but with the check in reverse order.
     * @param array The input array.
     * @param start The start of the input bounds.
     * @param end The end of the input bounds, exclusively.
     * @throws RuntimeException If the range is invalid, i.e. if {@code start} is greater than or equal to {@code end}.
     * @see {@link #isSorted(int[], int, int)} Similar, but checks in normal order.
     * @return A {@code boolean}, whether the input is sorted.
     * @author Various
     * @since MHT-1.1
     */
    public boolean isSortedRevStart(int[] array, int start, int end) throws RuntimeException {
        if (start >= end) throw new RuntimeException("MadhouseTools: Invalid range for sortedness detection, since start is after or at end: (" + start + ", " + end + ")");
        for (int i = end - 2; i >= start; i--) if (Reads.compareValues(array[i], array[i + 1]) > 0) return false;
        return true;
    }

    /**Compares two indices in an array, but with the indices shifted by 1.
     * @param array The input array.
     * @param a The first index to compare.
     * @param b The second index to compare.
     * @param sleep The visual time of the operation, as a factor.
     * @param mark A toggle for whether highlights are made during the operation.
     * @see {@link Reads#compareIndices(int[], int, int, double, boolean)} Runs this function.
     * @return An {@code int} with the result of the comparison.
     * @author Various
     * @since MHT-1.1
     */
    public int compareIndicesI1(int[] array, int a, int b, double sleep, boolean mark) {
        return Reads.compareIndices(array, a - 1, b - 1, sleep, mark);
    }

    /**Performs a swap on two indices in an array, but with the indices shifted by 1.
     * @param array The input array.
     * @param a The first index to swap.
     * @param b The second index to swap.
     * @param sleep The visual time of the operation, as a factor.
     * @param mark A toggle for whether highlights are made during the operation.
     * @param aux A toggle for whether the operation is indicated in external space.
     * @see {@link Writes#swap(int[], int, int, double, boolean, boolean)} Runs this function.
     * @author Various
     * @since MHT-1.1
     */
    public void swapI1(int[] array, int a, int b, double sleep, boolean mark, boolean aux) {
        Writes.swap(array, a - 1, b - 1, sleep, mark, aux);
    }

    /*------------------------\
    |SINCE MADHOUSE TOOLS 1.1a|
    \------------------------*/

    /**Performs a swap only if the two indices are not equal, to prevent unnecessary writes.
     * @param array The input array.
     * @param a The first index to swap.
     * @param b The second index to swap.
     * @param sleep The visual time of the operation, as a factor.
     * @param mark A toggle for whether highlights are made during the operation.
     * @param aux A toggle for whether the operation is indicated in external space.
     * @see {@link Writes#swap(int[], int, int, double, boolean, boolean)} Runs this function.
     * @author Various
     * @since MHT-1.1a
     */
    public void safeSwap(int[] array, int a, int b, double sleep, boolean mark, boolean aux) {
        if (a != b) Writes.swap(array, a, b, sleep, mark, aux);
    }

    /**Defines a color range for highlighting purposes.
     * @param colorName The name of the color to define.
     * @param color The {@code java.awt.Color} to define.
     * @param start The start of the range, inclusively.
     * @param end The end of the range, exclusively.
     * @see {@link Highlights#defineColor(String, java.awt.Color)} Defines the color.
     * @see {@link Highlights#colorCode(String, int)} Colors a single index.
     * @author Distray
     * @author PCBoy
     * @since MHT-1.1a
     */
    public void colorRange(String colorName, java.awt.Color color, int start, int end) {
        Highlights.defineColor(colorName, color);
        for (int i = start; i < end; i++) Highlights.colorCode(colorName, i);
    }

    /**Swaps the colors and items of two indices in an array, for highlighting purposes.
     * @param array The input array.
     * @param a The first index to swap.
     * @param b The second index to swap.
     * @see {@link Highlights#swapColors(int, int)} Swaps the colors.
     * @see {@link Writes#swap(int[], int, int, double, boolean, boolean)} Swaps the indices in the array.
     * @author Distray
     * @author PCBoy
     * @since MHT-1.1a
     */
    public void colorSwap(int[] array, int a, int b) {
        Highlights.swapColors(a, b);
        Writes.swap(array, a, b, 1, true, false);
    }
}