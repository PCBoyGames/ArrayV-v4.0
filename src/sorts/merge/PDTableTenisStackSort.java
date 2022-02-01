package sorts.merge;

import java.util.ArrayList;
import java.util.Stack;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan
in collaboration with Distray, PCBoy and stentor

-----------------------------
- Sorting Algorithm Scarlet -
-----------------------------

 */

/**
 * @author Ayako-chan
 * @author Distray
 * @author PCBoy
 * @author stentor
 *
 */
public final class PDTableTenisStackSort extends Sort {

    public PDTableTenisStackSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Pattern-Defeating Table Tennis Stack");
        this.setRunAllSortsName("Pattern-Defeating Table Tennis Stack Sort");
        this.setRunSortName("Pattern-Defeating Table Tennis Stacksort");
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

    protected int mergeWithStack(int[] array, int a, Stack<Integer> stackA, Stack<Integer> stackB) {
        if (stackB == null) {
            int sz = stackA.size(), t = a + sz;
            while (!stackA.empty()) {
                Writes.changeAllocAmount(-1);
                Writes.write(array, --t, stackA.pop(), 1, true, false);
            }
            return a + sz;
        }
        int szA = stackA.size(), szB = stackB.size(), t = a + szA + szB;
        while (!stackA.empty() && !stackB.empty()) {
            Writes.changeAllocAmount(-1);
            if (Reads.compareValues(stackA.peek(), stackB.peek()) > 0) {
                Writes.write(array, --t, stackA.pop(), 1, true, false);
            } else {
                Writes.write(array, --t, stackB.pop(), 1, true, false);
            }
        }
        while (!stackA.empty()) {
            Writes.changeAllocAmount(-1);
            Writes.write(array, --t, stackA.pop(), 1, true, false);
        }
        while (!stackB.empty()) {
            Writes.changeAllocAmount(-1);
            Writes.write(array, --t, stackB.pop(), 1, true, false);
        }
        return a + szA + szB;
    }

    protected Stack<Integer> stackRebuild(int[] array, int start, int mid, int end) {
        int l = start, r = mid;
        Stack<Integer> merged = new Stack<>();
        while(l < mid && r < end) {
            Highlights.markArray(2, l);
            Highlights.markArray(3, r);
            Writes.changeAllocAmount(1);
            Writes.changeAuxWrites(1);
            if(Reads.compareValues(array[l], array[r]) <= 0) {
                merged.push(array[l++]);
            } else {
                merged.push(array[r++]);
            }
        }
        while(l < mid) {
            Highlights.markArray(2, l);
            Writes.changeAllocAmount(1);
            Writes.changeAuxWrites(1);
            merged.push(array[l++]);
        }
        while(r < end) {
            Highlights.markArray(3, r);
            Writes.changeAllocAmount(1);
            Writes.changeAuxWrites(1);
            merged.push(array[r++]);
        }
        Highlights.clearAllMarks();
        return merged;
    }

    protected void mergeStacks(int[] array, int start, int end, ArrayList<Stack<Integer>> stacks) {
        while(!stacks.isEmpty()) {
            int ptr0, ptr1 = start, ptr2 = start, s = 0, ms = stacks.size();
            do {
                ptr0 = ptr1;
                ptr1 = ptr2;
                ms--;
                ptr2 = mergeWithStack(array, ptr2, stacks.remove(0), ms < 1 ? null : stacks.remove(0));
                if(s++ == 1) {
                    stacks.add(stackRebuild(array, ptr0, ptr1, ptr2));
                    s = 0;
                }
                ms--;
            } while(ptr2 < end && ms > 0);
            if(s > 0 && !stacks.isEmpty()) {
                stacks.add(stackRebuild(array, ptr1, ptr1, ptr2));
            }
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
