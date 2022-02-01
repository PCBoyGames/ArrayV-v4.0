package sorts.merge;

import java.util.ArrayList;
import java.util.Stack;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import utils.Statistics;


final public class TableTennisStackSort extends Sort {
    public TableTennisStackSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Table Tennis Stack");
        this.setRunAllSortsName("Table Tennis Stack Sort");
        this.setRunSortName("Table Tennis Stacksort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    // Credit to Stentor for the original algorithm
    final int zero = Integer.MIN_VALUE;
    private ArrayList<Stack<Integer>> buildStacks(int[] array, int start, int end) {
        ArrayList<Stack<Integer>> stacksBuilt = new ArrayList<>();
        int zeroed = 0;
        while(zeroed < end-start) {
            Stack<Integer> currentStack = new Stack<>();
            int d = -2;
            for(int j=start; j<end; j++) {
                if(array[j] != zero) {
                    int c, e = d;
                    if(d == -2 && currentStack.size() == 1)
                        d = -(-Reads.compareValues(currentStack.peek(), array[j]) | 1);
                    if(currentStack.empty() || e == -2 ||
                    (c = Reads.compareValues(currentStack.peek(), array[j])) == d || c == 0) {
                        currentStack.add(array[j]);
                        Writes.changeAllocAmount(1);
                        Writes.changeAuxWrites(1);
                        Writes.write(array, j, zero, 1, true, false);
                        zeroed++;
                    }
                }
            }
            if(d == 1) { // unstable for now
                Stack<Integer> reversed = new Stack<>();
                while(!currentStack.empty()) {
                    Writes.changeAuxWrites(1);
                    reversed.push(currentStack.pop());
                }
                currentStack = reversed;
            }
            Statistics.addStat("Stack");
            stacksBuilt.add(currentStack);
        }
        return stacksBuilt;
    }

    private int mergeWithStack(int[] array, int start, Stack<Integer> stack0, Stack<Integer> stack1) {
        if(stack1 == null) {
            int sz = stack0.size(), t = start + sz - 1;
            while(!stack0.empty()) {
                Writes.changeAllocAmount(-1);
                Writes.write(array, t--, stack0.pop(), 1, true, false);
            }
            return start + sz;
        }
        int sz0 = stack0.size(), sz1 = stack1.size(), t = start + sz0 + sz1 - 1;
        while(!stack0.empty() && !stack1.empty()) {
            Writes.changeAllocAmount(-1);
            if(Reads.compareValues(stack0.peek(), stack1.peek()) > 0) {
                Writes.write(array, t--, stack0.pop(), 1, true, false);
            } else {
                Writes.write(array, t--, stack1.pop(), 1, true, false);
            }
        }
        while(!stack0.empty()) {
            Writes.changeAllocAmount(-1);
            Writes.write(array, t--, stack0.pop(), 1, true, false);
        }
        while(!stack1.empty()) {
            Writes.changeAllocAmount(-1);
            Writes.write(array, t--, stack1.pop(), 1, true, false);
        }
        return start + sz0 + sz1;
    }

    private Stack<Integer> stackRebuild(int[] array, int start, int mid, int end) {
        int l = start, r = mid;
        Stack<Integer> merged = new Stack<>();
        while(l < mid && r < end) {
            Highlights.markArray(2, l);
            Highlights.markArray(3, r);
            Writes.changeAllocAmount(1);
            Writes.changeAuxWrites(1);
            if(Reads.compareValues(array[l], array[r]) <= 0) {
                merged.push(array[l++]);
                Writes.write(array, l-1, zero, 1, false, false);
            } else {
                merged.push(array[r++]);
                Writes.write(array, r-1, zero, 1, true, false);
            }
        }
        while(l < mid) {
            Highlights.markArray(2, l);
            Writes.changeAllocAmount(1);
            Writes.changeAuxWrites(1);
            merged.push(array[l++]);
            Writes.write(array, l-1, zero, 1, true, false);
        }
        while(r < end) {
            Highlights.markArray(3, r);
            Writes.changeAllocAmount(1);
            Writes.changeAuxWrites(1);
            merged.push(array[r++]);
            Writes.write(array, r-1, zero, 1, true, false);
        }
        Statistics.addStat("Stack");
        Highlights.clearAllMarks();
        return merged;
    }

    private void mergeStacks(int[] array, int start, int end, ArrayList<Stack<Integer>> stacks) {
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
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        Statistics.putStat("Stack");
        ArrayList<Stack<Integer>> stacks = buildStacks(array, 0, length);
        mergeStacks(array, 0, length, stacks);
    }
}