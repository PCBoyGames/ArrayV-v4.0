package sorts.quick;


import java.util.Random;

import main.ArrayVisualizer;
import sorts.templates.QuadSorting;

final public class FluxSortLoadDamnit extends QuadSorting {
    public FluxSortLoadDamnit(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Updated Flux");
        this.setRunAllSortsName("Updated Flux Sort");
        this.setRunSortName("Updated Fluxsort");
        this.setCategory("Quick Sorts");
    // patch in constants to make this work
        // this.setConstant("n log n");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    // Distray note: outputs 0 | 1 branchlessly,
    // allowing the same Flux optimizations to be made normally
    // without ternary ops
    private byte cmpOne(int[] array, int pos0, int pos1) {
        int cmp = Reads.compareIndices(array, pos1, pos0, 0.125, true);
        return (byte) (-(cmp >> 31));
    }

    private byte cmpOne_IV(int[] array, int pos, int value) {
        int cmp = Reads.compareIndexValue(array, pos, value, 0.125, true);
        return (byte) (-(cmp >> 31));
    }

    private byte cmpOne_VI(int[] array, int value, int pos) {
        int cmp = Reads.compareValueIndex(array, value, pos, 0.125, true);
        return (byte) (-(cmp >> 31));
    }

    // Distray note: Using this for more branchless shenanigans.
    private int equ(int a, int b) {
        return ((a-b)>>31)+((b-a)>>31)+1;
    }

    private boolean fluxAnalyze(int[] array, int start, int length) {
        int balance = 0, streaks = 0,
            dist, loop, last = -1, pos = start,
            cnt = length;
        while(cnt > 16) {
            for(dist=loop=0; loop<16; loop++) {
                dist += cmpOne(array, pos, pos+1);
                pos++;
            }
            streaks += equ(dist, last); last = dist;
            balance += dist;
            cnt -= 16;
        }
        while(--cnt > 0) {
            balance += cmpOne(array, pos, pos+1);
            pos++;
        }
        if(balance == 0)
            return true;
        if(balance == length - 1) {
            Writes.reversal(array, start, start+length-1, 1, true, false);
            return true;
        }
        int sixth = length / 6;
        if(streaks > length / 20 || balance <= sixth || balance >= length - sixth) {
            this.quadSort(array, start, length);
            return true;
        }
        return false;
    }

    private int medianOf3(int[] array, int pos0, int pos1, int pos2) {
        byte[] tiers = new byte[2];
        byte val;
        val = cmpOne(array, pos0, pos1); tiers[0] = val; tiers[1] = (byte)(val ^ 1);
        val = cmpOne(array, pos0, pos2); tiers[0] += val;
        if(tiers[0] == 1) return pos0;
        val = cmpOne(array, pos1, pos2); tiers[1] += val;
        return tiers[1] == 1 ? pos1 : pos2;
    }

    private int medianOf5(int[] array, int pos0, int pos1, int pos2, int pos3, int pos4) {
        byte[] tiers = new byte[4];
        byte val;
        val = cmpOne(array, pos0, pos1); tiers[0] = val; tiers[1] = (byte)(val ^ 1);
        val = cmpOne(array, pos0, pos2); tiers[0] += val; tiers[2] = (byte)(val ^ 1);
        val = cmpOne(array, pos0, pos3); tiers[0] += val; tiers[3] = (byte)(val ^ 1);
        val = cmpOne(array, pos0, pos4); tiers[0] += val;
        if(tiers[0] == 2) return pos0;
        val = cmpOne(array, pos1, pos2); tiers[1] += val; tiers[2] += (byte)(val ^ 1);
        val = cmpOne(array, pos1, pos3); tiers[1] += val; tiers[3] += (byte)(val ^ 1);
        val = cmpOne(array, pos1, pos4); tiers[1] += val;
        if(tiers[1] == 2) return pos1;
        val = cmpOne(array, pos2, pos3); tiers[2] += val; tiers[3] += (byte)(val ^ 1);
        val = cmpOne(array, pos2, pos4); tiers[2] += val;
        if(tiers[2] == 2) return pos2;
        val = cmpOne(array, pos3, pos4); tiers[3] += val;
        return tiers[3] == 2 ? pos3 : pos4;
    }

    private int ninther(int[] array, int pos, int len) {
        int div = len / 16,
    m0 = medianOf3(array, pos+2*div, pos+div, pos+5*div),
    m1 = medianOf3(array, pos+8*div, pos+6*div, pos+10*div),
    m2 = medianOf3(array, pos+14*div, pos+12*div, pos+15*div);
        return array[medianOf3(array, m1, m0, m2)];
    }

    private int medianOf25(int[] array, int pos, int len) {
        int div = len / 64,
    m0 = medianOf5(array, pos+4*div, pos+div, pos+2*div, pos+8*div, pos+10*div),
    m1 = medianOf5(array, pos+16*div, pos+12*div, pos+14*div, pos+18*div, pos+20*div),
    m2 = medianOf5(array, pos+32*div, pos+24*div, pos+30*div, pos+34*div, pos+38*div),
    m3 = medianOf5(array, pos+48*div, pos+42*div, pos+44*div, pos+50*div, pos+52*div),
    m4 = medianOf5(array, pos+60*div, pos+54*div, pos+56*div, pos+62*div, pos+63*div);
        return array[medianOf5(array, m2, m0, m1, m3, m4)];
    }

    private int medianOfSqrt(int[] array, int[] swap, int bucket, int offsMain, int offsSwap, int len) {
        int sqrt, div;
        for(sqrt = 256; sqrt*sqrt*4 > len; sqrt/=2);
        int[] pushTo = bucket == 0 ? swap : array, pushFrom = bucket == 0 ? array : swap;
        div = len / sqrt;
        int offset0 = (new Random().nextInt(sqrt)),
            offset1 = bucket==1?offsMain:offsSwap,
            offset2 = bucket==1?offsSwap:offsMain;
        for(int i=0; i<sqrt; i++) {
            Writes.write(pushTo, i+offset1, pushFrom[i*div+offset2+offset0], 0.5, true, bucket==0);
        }
        fluxSort_selfSwap(pushTo, offset1, offset1+sqrt);
        return pushTo[offset1+sqrt/2];
    }
    final int fluxOut = 24;


    // Change: Array definitions changed to "buckets", to ensure functionality with a lack of proper pointers

    private void flux_ReversePart(int[] array, int[] swap, int pivotI, int offsMain, int offsSwap, int len) {
        byte cmp;
        int rSize = 0, loop = len / 8, gtr = 0,
            ptx = offsMain;
        for(; loop > 0; loop--) {
            cmp = cmpOne_VI(array, swap[pivotI], ptx);
            Writes.write(swap, offsSwap + rSize++ - gtr, array[ptx], 0.25, true, true);
            Writes.write(array, offsMain + gtr, array[ptx], 0.25, true, false); gtr += cmp; ptx++;
            cmp = cmpOne_VI(array, swap[pivotI], ptx);
            Writes.write(swap, offsSwap + rSize++ - gtr, array[ptx], 0.25, true, true);
            Writes.write(array, offsMain + gtr, array[ptx], 0.25, true, false); gtr += cmp; ptx++;
            cmp = cmpOne_VI(array, swap[pivotI], ptx);
            Writes.write(swap, offsSwap + rSize++ - gtr, array[ptx], 0.25, true, true);
            Writes.write(array, offsMain + gtr, array[ptx], 0.25, true, false); gtr += cmp; ptx++;
            cmp = cmpOne_VI(array, swap[pivotI], ptx);
            Writes.write(swap, offsSwap + rSize++ - gtr, array[ptx], 0.25, true, true);
            Writes.write(array, offsMain + gtr, array[ptx], 0.25, true, false); gtr += cmp; ptx++;
            cmp = cmpOne_VI(array, swap[pivotI], ptx);
            Writes.write(swap, offsSwap + rSize++ - gtr, array[ptx], 0.25, true, true);
            Writes.write(array, offsMain + gtr, array[ptx], 0.25, true, false); gtr += cmp; ptx++;
            cmp = cmpOne_VI(array, swap[pivotI], ptx);
            Writes.write(swap, offsSwap + rSize++ - gtr, array[ptx], 0.25, true, true);
            Writes.write(array, offsMain + gtr, array[ptx], 0.25, true, false); gtr += cmp; ptx++;
            cmp = cmpOne_VI(array, swap[pivotI], ptx);
            Writes.write(swap, offsSwap + rSize++ - gtr, array[ptx], 0.25, true, true);
            Writes.write(array, offsMain + gtr, array[ptx], 0.25, true, false); gtr += cmp; ptx++;
            cmp = cmpOne_VI(array, swap[pivotI], ptx);
            Writes.write(swap, offsSwap + rSize++ - gtr, array[ptx], 0.25, true, true);
            Writes.write(array, offsMain + gtr, array[ptx], 0.25, true, false); gtr += cmp; ptx++;
        }
        for(loop = len % 8; loop > 0; loop--) {
            cmp = cmpOne_VI(array, swap[pivotI], ptx);
            Writes.write(swap, offsSwap + rSize++ - gtr, array[ptx], 0.25, true, true);
            Writes.write(array, offsMain + gtr, array[ptx], 0.25, true, false); gtr += cmp; ptx++;
        }
        rSize -= gtr;
        Writes.arraycopy(swap, offsSwap, array, offsMain + gtr, rSize, 0.5, true, false);
        if(rSize <= gtr / 16 || gtr <= fluxOut) {
             if(offsSwap > 0)
                 this.quadSort(array, offsMain, gtr);
             else
                 this.quadSortSwap(array, swap, offsMain, gtr);
             return;
        }
        this.fluxPart(array, swap, 0, pivotI, offsMain, offsSwap, gtr);
    }

    private int fluxDefaultPart(int[] array, int[] swap, int bucket, int pivot, int offsMain, int offsSwap, int len) {
        byte cmp;
        int[] partitionOn = bucket == 1 ? swap : array;
        int gtr = 0, rSize = 0, loop, ptx = bucket == 1 ? offsSwap : offsMain;
        for(loop = len / 4; loop > 0; loop--) {
            cmp = cmpOne_IV(partitionOn, ptx, pivot);
            Writes.write(swap, offsSwap + rSize++ - gtr, partitionOn[ptx], 0.25, true, true);
            Writes.write(array, offsMain + gtr, partitionOn[ptx], 0.25, true, false); gtr += cmp; ptx++;
            cmp = cmpOne_IV(partitionOn, ptx, pivot);
            Writes.write(swap, offsSwap + rSize++ - gtr, partitionOn[ptx], 0.25, true, true);
            Writes.write(array, offsMain + gtr, partitionOn[ptx], 0.25, true, false); gtr += cmp; ptx++;
            cmp = cmpOne_IV(partitionOn, ptx, pivot);
            Writes.write(swap, offsSwap + rSize++ - gtr, partitionOn[ptx], 0.25, true, true);
            Writes.write(array, offsMain + gtr, partitionOn[ptx], 0.25, true, false); gtr += cmp; ptx++;
            cmp = cmpOne_IV(partitionOn, ptx, pivot);
            Writes.write(swap, offsSwap + rSize++ - gtr, partitionOn[ptx], 0.25, true, true);
            Writes.write(array, offsMain + gtr, partitionOn[ptx], 0.25, true, false); gtr += cmp; ptx++;
        }
        for(loop = len % 4; loop > 0; loop--) {
            cmp = cmpOne_IV(partitionOn, ptx, pivot);
            Writes.write(swap, offsSwap + rSize++ - gtr, partitionOn[ptx], 0.25, true, true);
            Writes.write(array, offsMain + gtr, partitionOn[ptx], 0.25, true, false); gtr += cmp; ptx++;
        }
        return gtr;
    }

    private void fluxPart(int[] array, int[] swap, int bucket, int pivot, int offsMain, int offsSwap, int len) {
        int lSize, rSize;
        while(true) {
            --pivot;
            int[] partitioningOn = bucket == 1 ? swap : array;
            int offset = bucket == 1 ? offsSwap : offsMain;
            if(len <= 2048) {
                Writes.write(swap, pivot, ninther(partitioningOn, offset, len), 0.25, true, array!=swap);
            } else if(len <= 65536) {
                Writes.write(swap, pivot, medianOf25(partitioningOn, offset, len), 0.25, true, array!=swap);
            } else {
                Writes.write(swap, pivot, medianOfSqrt(array, swap, bucket, offsMain, offsSwap, len), 0.25, true, array!=swap);
            }
            if(bucket == 0 && offsSwap + len < pivot && Reads.compareValues(swap[pivot], swap[pivot+1]) >= 0) {
                flux_ReversePart(array, swap, pivot, offsMain, offsSwap, len);
                return;
            }
            lSize = fluxDefaultPart(array, swap, bucket, swap[pivot], offsMain, offsSwap, len);
            rSize = len - lSize;
            if(lSize <= rSize / 16 || rSize <= fluxOut) {
                if(rSize == 0) {
                    flux_ReversePart(array, swap, pivot, offsMain, offsSwap, lSize);
                    return;
                }
                Writes.arraycopy(swap, offsSwap, array, offsMain + lSize, rSize, 0.5, true, false);
                if(offsSwap > 0) {
                    quadSort(array, offsMain + lSize, rSize); // pointers can't really work here, fall back
                } else
                    quadSortSwap(array, swap, offsMain + lSize, rSize);
            } else {
                fluxPart(array, swap, 1, pivot, offsMain + lSize, offsSwap, rSize);
            }
            if(rSize <= lSize / 16 || lSize <= fluxOut) {
                if(offsSwap > 0) {
                    quadSort(array, offsMain, lSize);
                } else
                    quadSortSwap(array, swap, offsMain, lSize);
                return;
            }
            len = lSize;
            bucket = 0;
        }
    }

    public void fluxSort(int[] array, int start, int end) {
        int len = end - start;
        if(len < 32) {
            this.tailSwap(array, start, len);
        } else if(!fluxAnalyze(array, start, len)) {
            int[] swap = Writes.createExternalArray(len);
            fluxPart(array, swap, 0, len, start, 0, len);
            Writes.deleteExternalArray(swap);
        }
    }

    public void fluxSort_swapDef(int[] array, int[] swap, int start, int end) {
        int len = (end - start);
        if(len < 32) {
            this.tailSwap(array, start, len);
        } else if(!fluxAnalyze(array, start, len)) {
            fluxPart(array, swap, 0, len, start, 0, len);
        }
    }

    // special case exclusively for MOSQRT
    public void fluxSort_selfSwap(int[] array, int start, int end) {
        int len = (end - start);
        if(len < 32) {
            this.tailSwap(array, start, len);
        } else if(!fluxAnalyze(array, start, len)) {
            fluxPart(array, array, 0, start+2*len+1, start, start+len+1, len);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        fluxSort(array, 0, currentLength);
    }
}