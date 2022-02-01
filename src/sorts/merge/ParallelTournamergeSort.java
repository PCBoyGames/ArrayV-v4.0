package sorts.merge;

import main.ArrayVisualizer;
import sorts.hybrid.LazixioSort;
import sorts.templates.ParallelSort;


final public class ParallelTournamergeSort extends ParallelSort {
    public ParallelTournamergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Tournamerge (Parallel)");
        this.setRunAllSortsName("Parallel Tournamerge Sort");
        this.setRunSortName("Parallel Tournamergesort");
        this.setCategory("Merge Sorts");
        this.setQuestion("Set the base of this sort:", 4);
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        Reads = arrayVisualizer.getReads();
    }

    public static utils.Reads Reads;
    LazixioSort fallback;

    static class Mode {
        public int[] array;
        private int[] ptrs;
        public boolean stem;
        public int ptr, offset;
        public Mode parent, left, right, winner;
        public Mode(int[] ptrs, int index) {
            ptr = index;
            offset = 0;
            this.ptrs = ptrs;
            stem = false;
        }
        public Mode(int[] ptrs, boolean stem) {
            ptr = -1;
            offset = 0;
            this.ptrs = ptrs;
            this.stem = stem;
        }
        public int index() {
            if(ptrs == null || ptr < 0) return -1;
            return ptrs[ptr] + offset;
        }
        public void build() {
            if(left == null || left.ptr < 0) {
                if(right == null || right.ptr < 0) {
                    ptr = -1;
                    stem = false;
                    left = right = winner = null;
                    ptrs = null;
                    return;
                }
                winner=right;
                ptr = right.ptr;
                offset = right.offset;
            } else if(right == null || right.ptr < 0) {
                winner=left;
                ptr = left.ptr;
                offset = left.offset;
            } else if(Reads.compareIndices(array, left.index(), right.index(), 1, true) > 0) {
                winner=right;
                ptr = right.ptr;
                offset = right.offset;
            } else {
                winner=left;
                ptr = left.ptr;
                offset = left.offset;
            }
        }
        public void increase() {
            offset++;
            if(index() >= ptrs[ptr+1])
                ptr = -1;
        }
    }

    private Mode build(Mode l, Mode r) {
        Mode t = new Mode(l.ptrs, true);
        l.parent=r.parent=t;
        t.left=l;t.right=r;
        t.array=l.array;
        t.build();return t;
    }

    private Mode deepbuild(int[] array, int[] ptrs, int start, int end) {
        if(end<=start) {
            Mode m = new Mode(ptrs, start);
            m.array=array;
            return m;
        }
        int mid = start+(end-start)/2;
        return build(deepbuild(array, ptrs, start, mid), deepbuild(array, ptrs, mid+1, end));
    }
    private void incwinner(Mode root) {
        if(root.winner == null)
            return;
        if(root.winner.stem) {
            incwinner(root.winner);
        } else {
            root.winner.increase();
            do {
                root.build();
                root=root.parent;
            } while(root!=null);
        }
    }
    private void merge(int[] array, int[] tmp, boolean aux, int... ptrs) {
        Mode root = deepbuild(array, ptrs, 0, ptrs.length-2);
        int t = ptrs[0];
        while(root.winner != null) {
            Highlights.markArray(1, root.index());
            Writes.write(tmp, t++, array[root.index()], 1, true, aux);
            incwinner(root);
        }
    }
    public void mergeSort(int[] array, int[] tmp, boolean aux, int start, int end, int base) {
        if(end-start <= base*base && !aux) {
            fallback.mergeRuns(array, start, end);
            return;
        }
        int[] locs = new int[base+1];
        for(int i=0; i<base; i++) {
            locs[i] = start+(i*(end-start)/base);
        }
        locs[base] = end;
        Func[] threads = new Func[base];
        for(int i=0; i<base; i++) {
            threads[i] = new Func(tmp, array, !aux, locs[i], locs[i+1], base)
                         .setConsumer(this::threadMS);
            threads[i].start();
        }
        for(int i=0; i<base; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        merge(tmp, array, !aux, locs);
    }
    private Void threadMS(Object... data) {
        run("mergeSort", data);
        return null;
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 2) return 2;
        return answer;
    }

    @Override
    public void runSort(int[] array, int length, int b) {
        fallback = new LazixioSort(arrayVisualizer);
        int[] tmp = Writes.createExternalArray(length);
        this.mergeSort(array, tmp, false, 0, length, b);
        Writes.deleteExternalArray(tmp);
    }
}