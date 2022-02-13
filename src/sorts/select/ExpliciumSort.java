package sorts.select;

import java.util.Arrays;

import main.ArrayVisualizer;
import sorts.templates.Sort;


final public class ExpliciumSort extends Sort {
    public ExpliciumSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Explicium");
        this.setRunAllSortsName("Explicium Sort");
        this.setRunSortName("Expliciumsort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void make(int[] array, int[] tree, int start, int end, int index) {
        if(end <= start) {
            Highlights.markArray(1, start);
            Writes.write(tree, index, start, 1, true, true);
            return;
        }
        int next = 2*index+1, // *Yup, we're doing it dirty*
            mid = start + (end - start) / 2;
        make(array, tree, start, mid, next);
        make(array, tree, mid+1, end, next+1);
        if(Reads.compareIndices(array, tree[next], tree[next+1], 0.5, true) <= 0) {
            Writes.write(tree, index, tree[next], 1, true, true);
        } else {
            Writes.write(tree, index, tree[next+1], 1, true, true);
        }
    }
    private void makeTree(int[] array, int[] tree, int start, int end) {
        make(array, tree, start, end, 0); // wrapper function
    }
    private void removeWinner(int[] array, int[] tree) {
        int now = 0, neg;
        int l = (tree.length - 1) / 2;
        while(now < l && tree[now] != -1) {
            // took a page out of classic tournament's book
            neg = (2*(tree[2*now+1] >> 31)) + (tree[2*now+2] >> 31);
            if(neg < 0) {
                if(neg == -3)
                    break;
                // slightly faster this way, as there's no need to invert the value into the correct result
                now = 2*now-neg;
            } else if(tree[now] == tree[2*now+1]) {
                // if we had 6n tree memory for child storage, we could do this without direct comparison
                now = 2*now+1;
            } else {
                now = 2*now+2;
            }
        }
        Writes.write(tree, now, -1, 1, true, true);
        while(now > 0) {
            now = (now - 1) / 2;
            neg = (2*(tree[2*now+1] >> 31)) + (tree[2*now+2] >> 31);
            if(neg < 0) {
                if(neg == -3) {
                    Writes.write(tree, now, -1, 1, true, true);
                } else
                    Writes.write(tree, now, tree[2*now-neg], 1, true, true);
            } else if(Reads.compareIndices(array, tree[2*now+1], tree[2*now+2], 0.5, true) <= 0) {
                Writes.write(tree, now, tree[2*now+1], 1, true, true);
            } else {
                Writes.write(tree, now, tree[2*now+2], 1, true, true);
            }
        }
    }
    public void Explic(int[] array, int start, int end) {
        int trL = 2*(end-start)-1;
        trL|=trL>>1; trL|=trL>>2; trL|=trL>>4;
        trL|=trL>>8; trL|=trL>>16; // make this a power of two
        int[] tree = Writes.createExternalArray(trL), // tree of indices
              out = Writes.createExternalArray(end-start); // sorted result
        Arrays.fill(tree, -1);
        makeTree(array, tree, start, end-1);
        for(int i=trL/2; i<trL; i++) {
            // negpad the leftovers, only record what was left unchanged
            // (don't know a good way to do this without incrementing the index)
            if(tree[i] < 0)
                Writes.changeAuxWrites(1);
        }
        int t = 0;
        while(true) {
            Writes.write(out, t++, array[tree[0]], 1, true, true);
            if(t < end-start)
                removeWinner(array, tree);
            else
                break;
        }
        Writes.arraycopy(out, 0, array, start, end-start, 1, true, false);
        Writes.deleteExternalArrays(out, tree);
    }
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        Explic(array, 0, length);
    }
}