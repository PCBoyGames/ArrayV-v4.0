package sorts.golf;

import java.util.ArrayList;
import java.util.Collection;

import main.ArrayVisualizer;
import sorts.insert.BranchlessBinaryInsertionSort;
import sorts.templates.GrailSorting;
import utils.IndexedRotations;

public class CaiSortMkII extends GrailSorting {
    public CaiSortMkII(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Cai Mk. II");
        this.setRunAllSortsName("Cai Sort Mark II");
        this.setRunSortName("Caisort Mk. II");
        this.setCategory("Golf Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setQuestion("Set the base for this sort:", 3);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int buf, bufsz;
    private static final int minBinsert = 8;
    private BranchlessBinaryInsertionSort binserter;

    private class Buffer {
        public int pstart, start, mid, end;
        public Buffer(int start, int mid, int end) {
            this.pstart = this.start = start;
            this.mid = mid;
            this.end = end;
        }
        public int sortedLength() {
            if(end-mid <= 0) return -1;
            return end-mid;
        }
        public boolean oob() {
            return mid>=end || start>=end || start>mid;
        }
        public int bufferLength() {
            return mid-start;
        }
        public String toString() {
            return String.format("<%d, %d, %d>", start, mid, end);
        }
    }

    protected void grailRotate(int[] array, int pos, int lena, int lenb) {
        IndexedRotations.neon(array, pos, pos+lena, pos+lena+lenb, 1, true, false);
    }

    // may switch to Tournamerge
    // minKey0: get minimum key, assuming you still have a sorted subarray in current merge
    protected int minKey0(int[] array, ArrayList<Buffer> buffers, int exclude) {
        int min=exclude;
        if(buffers.get(min).sortedLength() <= 0 || buffers.get(min).oob())
            return -1;
        for(int i=0; i<buffers.size(); i++) {
            if(buffers.get(i).sortedLength() <= 0 || buffers.get(min).oob()) continue;
            if(i != exclude && Reads.compareIndices(array, buffers.get(i).mid, buffers.get(min).mid, 0.1, true) < 0)
                min=i;
        }
        return min;
    }
    // minKey1: get minimum key outside of current merge
    protected int minKey1(int[] array, ArrayList<Buffer> buffers, int exclude) {
        int min=0;
        while(min < buffers.size() && (min==exclude || buffers.get(min).sortedLength() <= 0))
            min++;
        if(min == buffers.size())
            return -1;
        for(int i=0; i<buffers.size(); i++) {
            if(buffers.get(i).sortedLength() <= 0) continue;
            if(i != min && Reads.compareIndices(array, buffers.get(i).mid, buffers.get(min).mid, 0.1, true) < 0)
                min=i;
        }
        return min;
    }

    // *still in desperate need of cleanup*
    protected void caiMerge(int[] array, int... ptrs) {
        ArrayList<Buffer> buffers = new ArrayList<>();
        for(int i=0; i<ptrs.length-1; i++) {
            buffers.add(new Buffer(ptrs[i], ptrs[i], ptrs[i+1]));
        }
        buffers.get(0).start = buffers.get(0).pstart = buf;
        int to = buf;
        while(true) {
            int maxBuffer = 0;
            boolean oob = true;
            for(int i=1; i<buffers.size(); i++) {
                if(buffers.get(maxBuffer).bufferLength() < buffers.get(i).bufferLength()) {
                    maxBuffer = i;
                }
                oob = oob && buffers.get(i).oob();
            }
            Buffer now = buffers.get(maxBuffer);
            if(oob) { // nope out (ensure you don't get stuck)
                break;
            }
            while(now.bufferLength() > 0) { // merge the values while buffer remains
                int j = minKey0(array, buffers, maxBuffer);
                if(j == -1)
                    break;
                Writes.swap(array, now.start++, buffers.get(j).mid++, 1, true, false);
            }
            System.out.println(buffers);
            if(now.bufferLength() > 0) { // still buffer remaining,
                while(now.bufferLength() > 0) { // merge outside of the subarray
                    int j = minKey1(array, buffers, maxBuffer);
                    if(j == -1)
                        break;
                    Writes.swap(array, now.start++, buffers.get(j).mid++, 1, true, false);
                }
            }
            if(maxBuffer > 0) { // push merged section back, if required
                int e = now.pstart;
                IndexedRotations.neon(array, to, e, now.start, 1, true, false);
                for(int i=maxBuffer-1; i>=0; i--) { // adjust all the subarrays behind accordingly
                    buffers.get(i).end += now.start-e;
                    buffers.get(i).mid += now.start-e;
                    buffers.get(i).start += now.start-e;
                    buffers.get(i).pstart += now.start-e;
                }
                to += now.start - e;
                now.pstart = now.start;
            } else { // just change the variables, nothing else needs to be done here
                to = now.pstart = now.start;
            }
        }
        // push remaining buffers back (Caisort can't handle its buffer, apparently)
        for(Buffer i : buffers) {
            if(i.sortedLength() > 0) {
                for(int j=i.mid; j<i.end; j++) {
                    Writes.swap(array, to++, j, 1, true, false);
                }
            }
        }
        buf = ptrs[ptrs.length-1]-bufsz;
    }

    private void caiMerge(int[] array, Collection<Integer> ptrs) {
        Integer[] norm = ptrs.toArray(new Integer[0]);
        int[] prim = new int[ptrs.size()];
        for(int i=0; i<ptrs.size(); i++) {
            prim[i] = norm[i];
        }
        caiMerge(array, prim);
    }

    private void binsertruns(int[] array, int start, int end) {
        int m=Math.max(2*minBinsert, bufsz);
        for(int i=start; i<end; i+=m) {
            binserter.binaryInsertSort(array, i, Math.min(i+m, end), 0.025, 0.05);
        }
    }

    public void runCai(int[] array, int start, int end, int base) {
        buf = start;
        bufsz = (int) Math.pow(end-start, 0.5d);
        binserter = new BranchlessBinaryInsertionSort(arrayVisualizer);
        if(bufsz < 4) {
            binserter.binaryInsertSort(array, start, end, 0.5, 0.5);
            return;
        }
        binsertruns(array, buf+bufsz, end);
        for(int j=Math.max(bufsz, 2*minBinsert); j<end-start; j*=base) {
            for(int i=buf+bufsz; i<end; i+=base*j) {
                if(i+j >= end)
                    break;
                ArrayList<Integer> ptrs = new ArrayList<>();
                int l = i;
                for(int k = 0; k < base && l < end; k++) {
                    ptrs.add(l);
                    l += j;
                }
                ptrs.add(Math.min(l, end));
                caiMerge(array, ptrs);
            }
            IndexedRotations.neon(array, start, buf, buf+bufsz, 1, true, false);
            buf=start;
        }
        binserter.binaryInsertSort(array, buf, buf+bufsz, 0.25, 0.05);
        grailMergeWithoutBuffer(array, buf, bufsz, end-(buf+bufsz));
    }

    @Override
    public int validateAnswer(int val) {
        if(val < 2) return 2;
        return val;
    }

    @Override
    public void runSort(int[] array, int len, int base) {
        runCai(array, 0, len, base);
    }
}