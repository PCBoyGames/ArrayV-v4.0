package sorts.insert;

import sorts.templates.Sort;
import main.ArrayVisualizer;


public class BlockShellSort extends Sort {

    public BlockShellSort(ArrayVisualizer arrayVisualizer)  {
        super(arrayVisualizer);

        this.setSortListName("Block Shell");
        this.setRunAllSortsName("Block Shell Sort");
        this.setRunSortName("Block Shell Sort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int gappedBinary(int[] A, int P, int l, int K, int G, boolean i) {
        int L=-1, R=l, C, M;
        while (L<R-1) {
            M = L + ((R - L) >> 1);
            C = Reads.compareValues(A[P+M*G], A[K]);
            if (C == 1 || (i && C == 0)) {
                R = M;
            } else {
                L = M;
            }
        }
        return R;
    }
    private void GSFW(int[] A, int P, int L, int G) {
        int t=A[P];
        for (int i=0; i<L; i++) {
            Writes.write(A, P+i*G, A[P+(i+1)*G], 1, true, false);
        }
        Writes.write(A, P+L*G, t, 1, true, false);
    }
    private void GSBW(int[] A, int P, int L, int G) {
        int t=A[P+L*G];
        for (int i=L; i>0; i--) {
            Writes.write(A, P+i*G, A[P+(i-1)*G], 1, true, false);
        }
        Writes.write(A, P, t, 1, true, false);
    }
    private void GMSFW(int[] A, int lA, int lB, int L, int G) {
        for (int i=0; i<L; i++) {
            Writes.swap(A, lA+i*G, lB+i*G, 1, true, false);
        }
    }
    private void GMSBW(int[] A, int lA, int lB, int L, int G) {
        for (int i=0; i<L; i++) {
            Writes.swap(A, lA+i*G, lB+i*G, 1, true, false);
        }
    }
    private void rotate(int[] A, int P, int L, int R, int G) {
        while (L > 1 && R > 1) {
            if (L <= R) {
                GMSFW(A, P, P + L * G, L, G);
                P += L * G;
                R -= L;
            } else {
                GMSBW(A, P + (L - R) * G, P + L * G, R, G);
                L -= R;
            }
        }
        if (L>0&&R>0) {
            if (L==1)
                GSFW(A,P,R,G);
            else if (R==1)
                GSBW(A,P,L,G);
        }
    }
    private void merge(int[] array, int P, int L0, int L1, int G) {
        int S;
        if (L0 < L1) {
            while (L0 != 0) {
                S = gappedBinary(array, P + L0 * G, L1, P, G, true);
                if (S != 0) {
                    rotate(array, P, L0, S, G);
                    P += S * G;
                    L1 -= S;
                }
                if (L1 == 0)
                    break;
                do {
                    P += G;
                    L0--;
                } while (L0 != 0 && Reads.compareValues(array[P], array[P+L0*G]) <= 0);
            }
        } else {
            while (L1 != 0) {
                S = gappedBinary(array, P, L0, P + (L0+L1-1) * G, G, false);
                if (S != L0) {
                    rotate(array, P+S*G, L0-S, L1, G);
                    L0 = S;
                }
                if (L0 == 0)
                    break;
                do {
                    L1--;
                } while (L1 != 0 && Reads.compareValues(array[P+(L0-1)*G], array[P+(L0+L1-1)*G]) <= 0);
            }
        }
    }

    private void gappedReverse(int[] array, int start, int end, int gap) {
        while (start <= end - gap) {
            Writes.swap(array, start, end, 1, true, false);
            start+=gap; end-=gap;
        }
    }

    private int getRun(int[] array, int start, int end, int gap) {
        int t = start,
            iD = -Reads.compareValues(array[start], array[start+gap]),
            len = 1;
        if (end-start<gap)
            return 1;
        if (iD == 0) iD = 1;
        do {
            len++;
            start+=gap;
        } while (start <= end - gap && Reads.compareValues(array[start], array[start+gap]) != iD);
        if (iD == -1) {
            gappedReverse(array, t, start, gap);
        }
        return len;
    }

    int[] gaps = {1, 4, 10, 23, 57, 132, 301, 701};

    private int ciura(int n) {
        if (n <= gaps.length) {
            return gaps[n - 1];
        }
        return (int)Math.pow(2.25, n);
    }

    public void shellPass(int[] array, int start, int end, int gap) {
        if (end-start < gap)
            return;
        int[] starts = new int[gap], lens = new int[gap], ends = new int[gap];
        for (int i=0; i<gap; i++) {
            starts[i] = start+i;
            lens[i] = 0;
            ends[i] = (end-(end%gap))+i;
            if (ends[i] >= end) {
                ends[i]-=gap;
            }
        }
        boolean done;
        do {
            done = true;
            for (int i=0; i<gap; i++) {
                int v=starts[i]+lens[i]*gap;
                if (v > ends[i] || ends[i] == -1)
                    continue;
                done=false;
                int r=getRun(array, v, ends[i], gap);
                merge(array, starts[i], lens[i], r, gap);
                Writes.write(lens, i, lens[i]+r, 0, false, true);
            }

        } while (!done);
    }

    public void shellSort(int[] array, int start, int end) {
        int k=1;
        while (ciura(k++) < end - start);
        while (--k > 1)
            shellPass(array, start, end, ciura(k-1));
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        shellSort(array, 0, length);
    }
}