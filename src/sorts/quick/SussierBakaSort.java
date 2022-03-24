package sorts.quick;

import main.ArrayVisualizer;
import sorts.merge.UpdatedQuadSort;
import sorts.merge.OnlinePDMSort;
import sorts.templates.Sort;

final public class SussierBakaSort extends Sort {
    public SussierBakaSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("SussierBaka");
        this.setRunAllSortsName("SussierBakasort");
        this.setRunSortName("Sussierbakasort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    private UpdatedQuadSort op0;
    private OnlinePDMSort op1;
    
    private int cmp(int[] array, int pos0, int pos1) {
        int cmpv = Reads.compareIndices(array, pos1, pos0, 0.125, true);
        return -(cmpv >> 31);
    }
    
    private int medof3(int[] array, int loc) {
        int a = cmp(array, loc, loc+1),
            b = cmp(array, loc+(a^1), loc+2);
        b += (a^1)|b;
        int c = cmp(array, loc+b, loc+a);
        return loc + (((c - 1) & a) | (-c & b));
    }
    
    private int medof3(int[] array, int l, int l1, int l2) {
        int[] spread = new int[] {l, l1, l2};
        int a = cmp(array, l, l1),
            b = cmp(array, spread[a^1], l2);
        b += (a^1)|b;
        int c = cmp(array, spread[b], spread[a]);
        return spread[((c - 1) & a) | (-c & b)];
    }
    
    private int ninther(int[] array, int a, int b) {
        if(b-a < 4) {
            return a+(b-a)/2;
        }
        if(b-a < 8) {
            return medof3(array, a+(b-a-1)/2);
        }
        int d = (b-a+1)/8,
            m0 = medof3(array, a, a+d, a+2*d),
            m1 = medof3(array, a+3*d, a+4*d, a+5*d),
            m2 = medof3(array, a+6*d, a+7*d, b);
        return medof3(array, m0, m1, m2);
    }
    
    private int pseudomo27(int[] array, int a, int b) {
        if(b-a < 256) {
            return ninther(array, a, b);
        }
        int d = (b-a+1)/8,
            m0 = ninther(array, a,a+2*d),
            m1 = ninther(array, a+3*d, a+5*d),
            m2 = ninther(array, a+6*d, b);
        return medof3(array, m0, m1, m2);
    }
    
    private int pseudomo81(int[] array, int a, int b) {
        if(b-a < 1024) {
            return pseudomo27(array, a, b);
        }
        int d = (b-a+1)/24,
            m0 = ninther(array, a, a+2*d),
            m1 = ninther(array, a+3*d, a+5*d),
            m2 = ninther(array, a+6*d, a+8*d),
            m3 = ninther(array, a+9*d, a+11*d),
            m4 = ninther(array, a+12*d, a+14*d),
            m5 = ninther(array, a+15*d, a+17*d),
            m6 = ninther(array, a+18*d, a+20*d),
            m7 = ninther(array, a+19*d, a+21*d),
            m8 = ninther(array, a+22*d, b);
        return medof3(array,
            medof3(array, m0, m1, m2),
            medof3(array, m3, m4, m5),
            medof3(array, m6, m7, m8)
        );
    }
    
    private int pseudomo243(int[] array, int a, int b) {
        if(b-a < 8192) {
            return pseudomo81(array, a, b);
        }
        int d = (b-a+1)/24,
            m0 = pseudomo27(array, a, a+2*d),
            m1 = pseudomo27(array, a+3*d, a+5*d),
            m2 = pseudomo27(array, a+6*d, a+8*d),
            m3 = pseudomo27(array, a+9*d, a+11*d),
            m4 = pseudomo27(array, a+12*d, a+14*d),
            m5 = pseudomo27(array, a+15*d, a+17*d),
            m6 = pseudomo27(array, a+18*d, a+20*d),
            m7 = pseudomo27(array, a+19*d, a+21*d),
            m8 = pseudomo27(array, a+22*d, b);
        return medof3(array,
            medof3(array, m0, m1, m2),
            medof3(array, m3, m4, m5),
            medof3(array, m6, m7, m8)
        );
    }
    
    private int pseudomo2187(int[] array, int a, int b) {
        if(b-a < 131072) {
            return pseudomo243(array, a, b);
        }
        int d = (b-a+1)/78,
            m0 = pseudomo81(array, a, a+2*d),
            m1 = pseudomo81(array, a+3*d, a+5*d),
            m2 = pseudomo81(array, a+6*d, a+8*d),
            m3 = pseudomo81(array, a+9*d, a+11*d),
            m4 = pseudomo81(array, a+12*d, a+14*d),
            m5 = pseudomo81(array, a+15*d, a+17*d),
            m6 = pseudomo81(array, a+18*d, a+20*d),
            m7 = pseudomo81(array, a+19*d, a+21*d),
            m8 = pseudomo81(array, a+22*d, a+24*d),
            m9 = pseudomo81(array, a+25*d, a+27*d),
            m10 = pseudomo81(array, a+28*d, a+30*d),
            m11 = pseudomo81(array, a+31*d, a+33*d),
            m12 = pseudomo81(array, a+34*d, a+36*d),
            m13 = pseudomo81(array, a+37*d, a+39*d),
            m14 = pseudomo81(array, a+40*d, a+42*d),
            m15 = pseudomo81(array, a+43*d, a+45*d),
            m16 = pseudomo81(array, a+46*d, a+48*d),
            m17 = pseudomo81(array, a+49*d, a+51*d),
            m18 = pseudomo81(array, a+52*d, a+54*d),
            m19 = pseudomo81(array, a+55*d, a+57*d),
            m20 = pseudomo81(array, a+58*d, a+60*d),
            m21 = pseudomo81(array, a+61*d, a+63*d),
            m22 = pseudomo81(array, a+64*d, a+66*d),
            m23 = pseudomo81(array, a+67*d, a+69*d), // nice
            m24 = pseudomo81(array, a+70*d, a+72*d),
            m25 = pseudomo81(array, a+73*d, a+75*d),
            m26 = pseudomo81(array, a+76*d, b);
        return medof3(array,
            medof3(array,
                medof3(array, m0, m1, m2),
                medof3(array, m3, m4, m5),
                medof3(array, m6, m7, m8)
            ),
            medof3(array,
                medof3(array, m9, m10, m11),
                medof3(array, m12, m13, m14),
                medof3(array, m15, m16, m17)
            ),
            medof3(array,
                medof3(array, m18, m19, m20),
                medof3(array, m21, m22, m23),
                medof3(array, m24, m25, m26)
            )
        );
    }
    
    private boolean sussyAnalyze(int[] array, int[] tmp, int start, int end) {
        int z = 0, e = 0, mid=(end-start)/2, tend = end--;
        while(--end >= start) {
            int w = Reads.compareIndices(array, end, end+1, 0.1, true);
            z += w == 1 ? 1 : 0;
            e += w == 0 ? 1 : 0;
        }
        if(z + e == tend-start-1) {
            Writes.reversal(array, start, tend-1, 1, true, false);
            return false;
        }
        if(z == 0)
            return false;
        boolean bad = Math.abs(mid-z) >= (tend-start) / 3;
        boolean worse = Math.abs(mid-(z+e)) >= mid - (tend-start) / 9;
        if(worse)
            op0.quadSortSwap(array, tmp, start, 0, tmp.length, tend-start);
        else if(bad) {
            op1.ms(array, tmp, start, tend, tend-start, 0);
        }
        return !(bad||worse);
    }
    
    // among_us function (ternary partitioning)
    private void among_us(int[] array, int[] tmp, int swapoff, int start, int end, boolean aux, int depth) {
        if(start >= end) {
            return;
        }
        Writes.recordDepth(depth++);
        int p = pseudomo2187(array, start, end), pi = array[p];
        int mama = start, mia = end, mamamia_ = swapoff, mamamia = start, now = array[mama];
        boolean allEq = true;
        innerpartition:
        while(mama <= mia) {
            Highlights.markArray(1, Math.max(mamamia-1, start));
            Highlights.markArray(2, mamamia_);
            Highlights.markArray(3, mia);
            Delays.sleep(1);
            switch(Reads.compareValues(now, pi)) {
                case -1: // Less than pivot case
                    Writes.visualClear(array, mama);
                    Writes.write(array, mamamia++, now, 0, false, aux);
                    break;
                case 0: // Equal pivot case (ternary split)
                    while(mama < mia && Reads.compareIndexValue(array, mia, pi, 0, false) == 0) {
                        mia--;
                        Highlights.markArray(3, mia);
                        Delays.sleep(1.25);
                    }
                    if(mama == mia) // Save single drips of write
                        break innerpartition;
                    int v = array[mia];
                    Writes.visualClear(array, mama);
                    Writes.write(array, mia--, now, 0, false, aux);
                    now = v;
                    continue innerpartition;
                case 1: // Greater than pivot case
                    Writes.visualClear(array, mama);
                    Writes.write(tmp, mamamia_++, now, 0.5, true, !aux);
                    break;
            }
            allEq = false;
            now = array[++mama]; // Iterate onto next number
        }
        if(allEq) {
            return;
        }
        mia = mamamia + (mamamia_ - swapoff) - 1;
        
        Writes.recursion();
        among_us(tmp, array, mamamia, swapoff, mamamia_ - 1, !aux, depth);
        
        int w = end - mia;
        
        Writes.arraycopy(array, mia + 1, array, mamamia, w, 1, true, aux);
        Writes.arraycopy(tmp, swapoff, array, mamamia + w, mamamia_ - swapoff, 1, true, aux);
        
        Writes.recursion();
        among_us(array, tmp, swapoff, start, mamamia - 1, aux, depth);
    }
    
    public void sussierBaka(int[] array, int start, int end) {
        // Analysis will use whichever of the 2 algorithms it thinks
        // are better suited for the situation
        op0 = new UpdatedQuadSort(arrayVisualizer); 
        op1 = new OnlinePDMSort(arrayVisualizer);
        int[] p = Writes.createExternalArray(end-start);
        if(sussyAnalyze(array, p, start, end))
            among_us(array, p, 0, start, end-1, false, 0);
        Writes.deleteExternalArray(p);
    }
    
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        sussierBaka(array, 0, currentLength);
    }
}