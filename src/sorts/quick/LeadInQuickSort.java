package sorts.quick;

import main.ArrayVisualizer;
import sorts.hybrid.LazicciSort;
import sorts.select.ExpliciumSort;
import sorts.templates.Sort;
import utils.IndexedRotations;

final public class LeadInQuickSort extends Sort {
    public LeadInQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Lead-In Quick");
        this.setRunAllSortsName("Quick Sort, Lead-In Recursion");
        this.setRunSortName("Lead-In Quicksort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    public enum Median {
        NONE(0),
        LOW(1),
        MEDIUM(2),
        HIGH(3),
        MAXPRACTICAL(4),
        AGGRESSIVE(5),
        OVERDRIVE(6),
        SINGULARITY(7);
        public final int medianLevel;
        Median(int level) {
            medianLevel = level;
        }
    }

    private int gear;

    private int cmpOne(int[] array, int pos0, int pos1) {
        int cmp = Reads.compareIndices(array, pos1, pos0, 0.125, true);
        return -(cmp >> 31);
    }

    private int medof3(int[] array, int loc) {
        if((gear & 2) > 0) {
            int a=loc, b=a+1, c=b+1, t;
            if(cmpOne(array, a, b) == 1) {
                t=a;a=b;b=t;
            }
            if(cmpOne(array, b, c) == 1) {
                t=b;b=c;c=t;
                if(cmpOne(array, a, b) == 1) {
                    return a;
                }
            }
            return b;
        }
        int a = cmpOne(array, loc, loc+1),
            b = cmpOne(array, loc+(a^1), loc+2);
        b += (a^1)|b;
        int c = cmpOne(array, loc+b, loc+a);
        return loc + (((c - 1) & a) | (-c & b));
    }

    private int medof3(int[] array, int l, int l1, int l2) {
        if((gear & 2) > 0) {
            int t;
            if(cmpOne(array, l, l1) == 1) {
                t=l;l=l1;l1=t;
            }
            if(cmpOne(array, l1, l2) == 1) {
                t=l1;l1=l2;l2=t;
                if(cmpOne(array, l, l1) == 1) {
                    return l;
                }
            }
            return l1;
        }
        int[] spread = new int[] {l, l1, l2};
        int a = cmpOne(array, l, l1),
            b = cmpOne(array, spread[a^1], l2);
        b += (a^1)|b;
        int c = cmpOne(array, spread[b], spread[a]);
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
        if(b-a < 27) {
            return ninther(array, a, b);
        }
        int d = (b-a+1)/8,
            m0 = ninther(array, a,a+2*d),
            m1 = ninther(array, a+3*d, a+5*d),
            m2 = ninther(array, a+6*d, b);
        return medof3(array, m0, m1, m2);
    }

    private int pseudomo81(int[] array, int a, int b) {
        if(b-a < 81) {
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
        if(b-a < 243) {
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
        if(b-a < 2187) {
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

    private Median box = Median.OVERDRIVE;

    private int rotatePartition(int[] a, int[] s, int l, int r, int p, int d, int o) {
        Writes.recursion();
        if(r <= l) {
            return Reads.compareValues(a[l], p) > 0 ? 1 : 0;
        }
        Writes.recordDepth(d++);
        int m = l+(r-l)/2,
            q = rotatePartition(a, s, l, m, p, d, o),
            t = rotatePartition(a, s, m+1, r, p, d, o);
        IndexedRotations.cycleReverse(a, m-q+1, m+1, r-t+1, 0.5, true, false);
        if(d == 1) {
            if(q+t == 0) {
                Writes.write(s, o+4, -1, 0.25, true, true);
                ExpliciumSort fallback = new ExpliciumSort(arrayVisualizer);
                fallback.Explic(a, l, r+1);
            }
            Writes.write(s, o+1, r-q-t, 0.25, true, true);
            Writes.write(s, o+2, r-q-t+1, 0.25, true, true);
        }
        return q+t;
    }

    private void partitionAndStore(int[] a, int[] rec, int l, int r, int o) {
        Writes.write(rec, o, l, 0.25, true, true);
        Writes.write(rec, o+3, r, 0.25, true, true);
        int med;
        switch(box) {
            case NONE:
                med = l+(r-l+1)/2;
                break;
            case LOW:
                med = medof3(a, l, l+(r-l+1)/2, r);
                break;
            case MEDIUM:
                med = ninther(a, l, r);
                break;
            case HIGH:
            default:
                med = pseudomo27(a, l, r);
                break;
            case MAXPRACTICAL:
                med = pseudomo81(a, l, r);
                break;
            case AGGRESSIVE:
                med = pseudomo243(a, l, r);
                break;
            case OVERDRIVE:
                med = pseudomo2187(a, l, r);
                break;
            case SINGULARITY:
                med = l;
                while(med <= r && Reads.compareIndices(a, med, med+1, 0.05, true) <= 0) {
                    med++;
                }
                if(med > r)
                    return;
                break;
        }
        Writes.write(rec, o+4, 2, 0.25, true, true);
        int i = l, j = r, p = a[med];
        if((gear & 4) > 0) {
            rotatePartition(a, rec, l, r, p, 0, o);
        } else {
            while(i <= j) {
                while(i <= j && Reads.compareValues(a[i], p) < 0) {
                    i++;
                    Highlights.markArray(1, i);
                    Delays.sleep(0.25);
                }
                while(i <= j && Reads.compareValues(a[j], p) > 0) {
                    j--;
                    Highlights.markArray(2, j);
                    Delays.sleep(0.25);
                }
                if(i <= j) {
                    Writes.swap(a, i++, j--, 1, true, false);
                }
            }
            Writes.write(rec, o+1, j, 0.25, true, true);
            Writes.write(rec, o+2, i, 0.25, true, true);
        }
    }
    public void quickSort(int[] a, int p, int q) {
        int depth = ((q-p+3)/2);
        LazicciSort fallback = new LazicciSort(arrayVisualizer);
        if((gear & 1) > 0) {
            int log = 0;
            while(depth > 0) {
                log++;
                depth/=2;
            }
            depth=2*log;
        }
        int[] record = Writes.createExternalArray(5*depth);
        partitionAndStore(a, record, p, q, 0);
        int sp = 0;
        while(true) {
            if(record[sp+3]<=record[sp]) {
                Writes.write(record, sp+4, record[sp+4]-1, 0.25, true, true);
                sp-=5;
            }
            while(sp >= 0 && record[sp+4] == 0)
                sp-=5;
            if(sp < 0)
                break;
            int o = record[sp+4] & 2;
            int l = record[sp+o], r = record[sp+o+1];
            if(l < r) {
                if(sp+9 < record.length && record[sp+4] >= 0) {
                    partitionAndStore(a, record, l, r, sp+5);
                    Writes.write(record, sp+4, record[sp+4]-1, 0.25, true, true);
                    sp+=5;
                } else {
                    fallback.lazicciStable(a, l, r+1);
                    if(record[sp+4] < 0) {
                        Writes.write(record, sp+4, 1, 0.25, true, true);
                    } else
                        Writes.write(record, sp+4, record[sp+4]-1, 0.25, true, true);
                }
            } else {
                if(record[sp+4] < 0) {
                    Writes.write(record, sp+4, 1, 0.25, true, true);
                }
                    Writes.write(record, sp+4, record[sp+4]-1, 0.25, true, true);
            }
        }
        Writes.deleteExternalArray(record);
    }

    @Override
    public void runSort(int[] array, int currentLength, int buckC) {
        // bit 0: log(n) stack
        // bit 1: compare-optimized median
        // bit 2: stable rotate partition
        // bits 3-6: median type
        gear = 1 | 2 | (6 << 3);
        box = Median.values()[(gear>>3)&7];
        this.quickSort(array, 0, currentLength - 1);
    }
}