package sorts.merge;

import java.util.ArrayList;
import java.util.Stack;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan
in collaboration with stentor

-----------------------------
- Sorting Algorithm Scarlet -
-----------------------------

 */

/**
 * @author Ayako-chan
 * @author stentor
 *
 */
public final class PDStackSort extends Sort {

    public PDStackSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Pattern-Defeating Stack");
        this.setRunAllSortsName("Pattern-Defeating Stack Sort");
        this.setRunSortName("Pattern-Defeating Stacksort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    final int WLEN = 3;

    protected boolean getBit(int[] bits, int idx) {
        int b = (bits[idx >> WLEN]) >> (idx & ((1 << WLEN) - 1)) & 1;
        return b == 1;
    }

    protected void flag(int[] bits, int idx) {
        Writes.write(bits, idx >> WLEN, bits[idx >> WLEN] | (1 << (idx & ((1 << WLEN) - 1))), 0, false, true);
    }

    protected int findRun(int[] array, int a, int b) {
        int i = a + 1;
        boolean dir;
        if(i < b)
            dir = Reads.compareIndices(array, i - 1, i++, 0.5, true) <= 0;
        else
            dir = true;
        if(dir)
            while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) <= 0)
                i++;
        else {
            while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) > 0)
                i++;
            if(i - a < 4)
                Writes.swap(array, a, i - 1, 1.0, true, false);
            else
                Writes.reversal(array, a, i - 1, 1.0, true, false);
        }
        Highlights.clearMark(2);
        return i;
    }

    public boolean patternDefeat(int[] array, int a, int b) {
        int i = a + 1, j = a;
        boolean noSort = true;
        while (i < b) {
            i = findRun(array, j, b);
            if (i < b)
                noSort = false;
            j = i++;
        }
        return noSort;
    }

    protected ArrayList<Stack<Integer>> buildStacks(int[] array, int[] bits, int a, int b) {
        ArrayList<Stack<Integer>> stacksBuilt = new ArrayList<>();
        int zeroed = 0;
        while (zeroed < b - a) {
            Stack<Integer> currentStack = new Stack<>();
            for (int j = a; j < b; j++) {
                if (!getBit(bits, j - a)) {
                    if (currentStack.empty() || Reads.compareValues(currentStack.peek(), array[j]) < 0) {
                        currentStack.add(array[j]);
                        Writes.changeAllocAmount(1);
                        Writes.changeAuxWrites(1);
                        Highlights.markArray(1, j);
                        Delays.sleep(1);
                        flag(bits, j - a);
                        zeroed++;
                    }
                }
            }
            stacksBuilt.add(currentStack);
        }
        return stacksBuilt;
    }

    protected int mergeWithStack(int[] array, int start, int end, Stack<Integer> stack) {
        int l = end-1, sz = stack.size(), t = end + sz - 1;
        while(l >= start && !stack.empty()) {
            if(Reads.compareValues(array[l], stack.peek()) > 0) {
                Writes.write(array, t--, array[l--], 0.5, true, false);
            } else {
                Writes.changeAllocAmount(-1);
                Writes.write(array, t--, stack.pop(), 0.5, true, false);
            }
        }
        while(!stack.empty()) {
            Writes.changeAllocAmount(-1);
            Writes.write(array, t--, stack.pop(), 0.5, true, false);
        }
        return end + sz;
    }

    protected void mergeStacks(int[] array, int start, int end, ArrayList<Stack<Integer>> stacks) {
        Stack<Integer> first = stacks.remove(0);
        int ptr = start, n = start + first.size() - 1;
        while(!first.empty()) {
            Writes.changeAllocAmount(-1);
            Writes.write(array, n--, first.pop(), 0.5, true, false);
            ptr++;
        }
        while(!stacks.isEmpty()) {
            ptr = mergeWithStack(array, start, ptr, stacks.remove(0));
        }
    }

    public void mergeSort(int[] array, int a, int b) {
        if(patternDefeat(array, a, b))
            return;
        int[] bits = Writes.createExternalArray(((b-a-1) >> WLEN) + 1);
        ArrayList<Stack<Integer>> stacks = buildStacks(array, bits, a, b);
        Writes.deleteExternalArray(bits);
        mergeStacks(array, a, b, stacks);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        mergeSort(array, 0, sortLength);

    }

}
