package sorts.hybrid;

import java.util.ArrayList;
import java.util.function.Function;

import main.ArrayVisualizer;
import sorts.insert.BinaryInsertionSort;
import sorts.templates.Sort;

public class K_itaSort extends Sort {

    public K_itaSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("K-Way Kita (K-ita)");
        this.setRunAllSortsName("K-ita Sort");
        this.setRunSortName("K-itasort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Set the base for this sort:", 3);
    }
    private int ceilLog(int val) {
        return 32 - Integer.numberOfLeadingZeros(val - 1);
    }
    private class Sublist {
        private int array[], tags[], offsetArray, endTag, blocksize, ctrMerge = 0, ctrBuf = 0, normalizedBuf = 0, normalizedMerge = 0;
        public int offsetTag, tagNoMerge = 0, tagNoBuf = 0;
        private Sublist left, right, winner, parent;
        public boolean disqualified = false;
        public Sublist() { // blank slate used for building

        }
        public Sublist(int[] array, int[] tags, int offsArr, int offsTag, int endTag, int blocksize) {
            this.array = array;
            this.tags = tags;
            this.offsetArray = offsArr;
            this.offsetTag = offsTag;
            this.endTag = endTag;
            this.blocksize = blocksize;
        }
        public int getBufferIndex() {
            return offsetArray + ctrBuf + tags[offsetTag + tagNoBuf] * blocksize;
        }
        public int getBufferTag() {
            return offsetTag + tagNoBuf;
        }
        public int getMergeIndex() {
            return offsetArray + ctrMerge + tags[offsetTag + tagNoMerge] * blocksize;
        }
        public boolean oob() {
            return disqualified || offsetTag + tagNoMerge >= endTag;
        }
        public void build() {
            if (left == null || left.oob()) {
                if (right == null || right.oob()) {
                    this.disqualified = true;
                    return;
                } else {
                    winner = right;
                }
            } else if (right == null || right.oob()) {
                winner = left;
            } else if (this.compare(left, right) <= 0) {
                winner = left;
            } else {
                winner = right;
            }
            copyWinner();
        }
        private void copyWinner() {
            this.tags = winner.tags;
            this.array = winner.array;
            this.offsetArray = winner.offsetArray;
            this.offsetTag = winner.offsetTag;
            this.endTag = winner.endTag;
            this.ctrMerge = winner.ctrMerge;
            this.tagNoMerge = winner.tagNoMerge;
            this.blocksize = winner.blocksize;
        }
        public void rebuild() {
            Sublist now = this;
            while (true) {
                now = now.parent;
                if (now == null) break;
                now.build();
            }
        }
        public Sublist winner() {
            if (winner == null)
                return this;
            if (oob()) {
                Sublist p = this.parent;
                while (p.oob()) {
                    p = p.parent;
                }
                return p;
            }
            return winner.winner();
        }
        public void incrementMerge() {
            ++normalizedMerge;
            if (++ctrMerge == blocksize) {
                tagNoMerge++;
                ctrMerge = 0;
            }
        }
        public void incrementBuffer() {
            ++normalizedBuf;
            if (++ctrBuf == blocksize) {
                tagNoBuf++;
                ctrBuf = 0;
            }
        }
        public int bufferSize() {
            return normalizedMerge - normalizedBuf;
        }
        private int compare(Sublist a, Sublist b) {
            return Reads.compareIndices(a.array, a.getMergeIndex(), b.getMergeIndex(), 1, true);
        }
    }

    private <T> boolean some(T[] array, Function<? super T, Boolean> func) {
        for (T i : array) {
            if (func.apply(i)) {
                return true;
            }
        }
        return false;
    }

    private Sublist build(Sublist[] buffers, int start, int end) {
        if (start >= end)
            return buffers[start];
        int mid = start + (end - start) / 2;
        Sublist left = build(buffers, start, mid);
        Sublist right = build(buffers, mid+1, end);
        Sublist root = new Sublist();
        root.left = left;
        root.right = right;
        left.parent = root;
        right.parent = root;
        root.build();
        return root;
    }

    private void blockmerge(int[] array, int[] buf, int[] tags, int[] tagstemp, int start, int sqrt, int... ptrs) {
        int aTmp = (ptrs[0] - start) / sqrt, a = aTmp;
        int tagNum = 0;
        Sublist[] buffers = new Sublist[ptrs.length-1];
        for (int i=0; i<ptrs.length-1; i++) {
            int m = (ptrs[i] - start) / sqrt, b = (ptrs[i+1] - start) / sqrt;
            buffers[i] = new Sublist(array, tags, ptrs[i], m, b, sqrt);
        }
        Sublist root = build(buffers, 0, ptrs.length-2);
        for (int i=0; i<buf.length; i++) {
            Sublist winner = root.winner();
            int w = winner.getMergeIndex();
            Writes.write(buf, i, array[w], 1, true, true);
            winner.incrementMerge();
            winner.rebuild();
        }
        while (some(buffers, lst -> !lst.oob())) {
            Sublist maxBuf = buffers[0];
            for (int i = 1; i < buffers.length; i++) {
                if (buffers[i].bufferSize() > maxBuf.bufferSize())
                    maxBuf = buffers[i];
            }
            for (int c = 0; c < sqrt; c++) {
                Sublist winner = root.winner();
                int w = winner.getMergeIndex();
                Writes.write(array, maxBuf.getBufferIndex(), array[w], 1, true, false);
                winner.incrementMerge();
                maxBuf.incrementBuffer();
                winner.rebuild();
            }
            Writes.write(tagstemp, tagNum++, tags[maxBuf.getBufferTag() - 1] + maxBuf.offsetTag - a, 1, true, true);
        }
        for (int i=0, j=0; i<buffers.length; i++) {
            Sublist now = buffers[i];
            while (now.bufferSize() > 0) {
                for (int c = 0; c < sqrt; c++) {
                    Writes.write(array, now.getBufferIndex(), buf[j++], 1, true, false);
                    now.incrementBuffer();
                }
                Writes.write(tags, aTmp++, tags[now.getBufferTag() - 1] + now.offsetTag - a, 1, true, true);
            }
        }
        Writes.arraycopy(tagstemp, 0, tags, aTmp, tagNum, 1, true, true);
    }
    private void blockmerge(int[] array, int[] buf, int[] tags, int[] tagstemp, int start, int sqrt, ArrayList<Integer> ptrs) {
        int[] z = new int[ptrs.size()];
        for (int i = 0; i < z.length; i++) {
            z[i] = ptrs.get(i);
        }
        blockmerge(array, buf, tags, tagstemp, start, sqrt, z);
    }
    private void standardMerge(int[] array, int[] buf, int a, int m, int b, int t, boolean aux) {
        int l = a, r = m;
        while (l < m && r < b) {
            if (Reads.compareIndices(array, l, r, 1, true) <= 0) {
                Writes.write(buf, t++, array[l++], 1, true, aux);
            } else {
                Writes.write(buf, t++, array[r++], 1, true, aux);
            }
        }
        while (l < m)
            Writes.write(buf, t++, array[l++], 1, true, aux);
        while (r < b)
            Writes.write(buf, t++, array[r++], 1, true, aux);
    }
    private void pingPongMerge(int[] array, int[] buf, int a, int b, int c, int d, int e) {
        standardMerge(array, buf, a, b, c, 0, true);
        standardMerge(array, buf, c, d, e, c - a, true);
        standardMerge(buf, array, 0, c - a, e - a, a, false);
    }
    private void tailMerge(int[] array, int[] buf, int start, int mid, int end) {
        Writes.arraycopy(array, mid, buf, 0, end-mid, 1, true, true);
        int l=mid-1, r=end-mid-1, t=end;
        while (l >= start && r >= 0) {
            t--;
            if (Reads.compareValues(array[l], buf[r]) > 0)
                Writes.write(array, t, array[l--], 1, true, false);
            else
                Writes.write(array, t, buf[r--], 1, true, false);
        }
        while (r >= 0)
            Writes.write(array, --t, buf[r--], 1, true, false);
    }

    private void index(int[] array, int[] tmp, int[] table, int a, int b, int block) {
        for (int i=a, j=0; i<b; i+=block, j++) {
            if (Reads.compareOriginalValues(j, table[j]) == 0) continue;
            Writes.arraycopy(array, i, tmp, 0, block, 0, true, true);
            int k=j, n=table[j];
            do {
                Writes.arraycopy(array, a+n*block, array, a+k*block, block, 1, true, false);
                Writes.write(table, k, k, 1, true, false);
                k = n; n = table[n];
            } while (Reads.compareOriginalValues(j, n) != 0);
            Writes.arraycopy(tmp, 0, array, a+k*block, block, 1, true, false);
            Writes.write(table, k, k, 1, true, false);
        }
    }

    public void kita(int[] array, int a, int c, int base) {
        if (c - a <= 32) {
            BinaryInsertionSort b = new BinaryInsertionSort(arrayVisualizer);
            b.customBinaryInsert(array, a, c, 0.5);
            return;
        }
        int sl = (31 - Integer.numberOfLeadingZeros(c-a)) / 2, s = 1 << sl, bl = ceilLog(base);
        int b = c - (c - a) % s, k = (c - a) / s, B[] = Writes.createExternalArray(s<<bl),
            t0[] = Writes.createExternalArray(k), t1[] = Writes.createExternalArray(k), j = 1;
        for (; j<=B.length/4; j*=4) {
            for (int i=a; i+j<b; i+=4*j) {
                pingPongMerge(array, B, i, i+j, Math.min(i+2*j, b), Math.min(i+3*j, b), Math.min(i+4*j, b));
            }
        }
        for (; j<=B.length; j*=2) {
            for (int i=a; i+j<b; i+=2*j) {
                tailMerge(array, B, i, i+j, Math.min(i+2*j, b));
            }
        }
        for (int i=0; i<t0.length; i++) {
            Writes.write(t0, i, i%(1<<(bl+1)), 0, true, true);
        }
        for (; j<b-a; j*=base) {
            for (int i=a; i+j<b; i+=base*j) {
                ArrayList<Integer> ptrs = new ArrayList<>();
                int l = i;
                for (int m = 0; m < base && l < b; m++) {
                    ptrs.add(l);
                    l += j;
                }
                ptrs.add(Math.min(l, b));
                blockmerge(array, B, t0, t1, a, s, ptrs);
            }
        }
        index(array, B, t0, a, b, s);
        Writes.deleteExternalArrays(t0, t1);
        if (b < c) {
            kita(array, b, c, base);
            tailMerge(array, B, a, b, c);
        }
        Writes.deleteExternalArray(B);
    }

    @Override
    public int validateAnswer(int val) {
        if (val < 2) return 2;
        return val;
    }
    @Override
    public void runSort(int[] array, int currentLength, int base) {
        kita(array, 0, currentLength, base);
    }
}