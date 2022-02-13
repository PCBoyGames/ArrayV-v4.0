package sorts.quick;


import main.ArrayVisualizer;
import sorts.templates.QuadSorting;

final public class FluxSort extends QuadSorting {
    public FluxSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Flux");
        this.setRunAllSortsName("Flux Sort");
        this.setRunSortName("Fluxsort");
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

    private byte cmpOne_IV(int[] array, int pos0, int pos1) {
        int cmp = Reads.compareIndexValue(array, pos1, pos0, 0.125, true);
        return (byte) (-(cmp >> 31));
    }

    private boolean fluxAnalyze(int[] array, int start, int length) {
        int balance = 0, pos = start, cnt = length;
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
        if(balance <= sixth || balance >= length - sixth) {
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

    private int medianOf15(int[] array, int pos, int len) {
        int div = len / 16,
    m0 = medianOf3(array, pos+2*div, pos+div, pos+3*div),
    m1 = medianOf3(array, pos+5*div, pos+4*div, pos+6*div),
    m2 = medianOf3(array, pos+8*div, pos+7*div, pos+9*div),
    m3 = medianOf3(array, pos+11*div, pos+10*div, pos+12*div),
    m4 = medianOf3(array, pos+14*div, pos+13*div, pos+15*div);
        return array[medianOf5(array, m2, m0, m1, m3, m4)];
    }
    final int fluxOut = 24;
    private void fluxPart(int[] array, int[] swap, int[] partitionOn, int offsMain, int len) {
        byte cmp;
        int size1 = 0, size2 = 0, offs = partitionOn == swap ? 0 : offsMain, ptx = offs;
        int piv;
        if(len > 1024) {
            piv = medianOf15(partitionOn, ptx, len);
        } else {
            piv = ninther(partitionOn, ptx, len);
        }
        while(ptx + 8 < len + offs) {
            // Distray note: Really cacheable, but also really unreadable.
            cmp = cmpOne_IV(partitionOn, ptx, piv);
            Writes.write(array, size1+offsMain, partitionOn[ptx], 0.25, true, false); size1 += cmp^1;
            Writes.write(swap, size2, partitionOn[ptx], 0.25, true, true); size2 += cmp;
            cmp = cmpOne_IV(partitionOn, ptx+1, piv);
            Writes.write(array, size1+offsMain, partitionOn[ptx+1], 0.25, true, false); size1 += cmp^1;
            Writes.write(swap, size2, partitionOn[ptx+1], 0.25, true, true); size2 += cmp;
            cmp = cmpOne_IV(partitionOn, ptx+2, piv);
            Writes.write(array, size1+offsMain, partitionOn[ptx+2], 0.25, true, false); size1 += cmp^1;
            Writes.write(swap, size2, partitionOn[ptx+2], 0.25, true, true); size2 += cmp;
            cmp = cmpOne_IV(partitionOn, ptx+3, piv);
            Writes.write(array, size1+offsMain, partitionOn[ptx+3], 0.25, true, false); size1 += cmp^1;
            Writes.write(swap, size2, partitionOn[ptx+3], 0.25, true, true); size2 += cmp;
            cmp = cmpOne_IV(partitionOn, ptx+4, piv);
            Writes.write(array, size1+offsMain, partitionOn[ptx+4], 0.25, true, false); size1 += cmp^1;
            Writes.write(swap, size2, partitionOn[ptx+4], 0.25, true, true); size2 += cmp;
            cmp = cmpOne_IV(partitionOn, ptx+5, piv);
            Writes.write(array, size1+offsMain, partitionOn[ptx+5], 0.25, true, false); size1 += cmp^1;
            Writes.write(swap, size2, partitionOn[ptx+5], 0.25, true, true); size2 += cmp;
            cmp = cmpOne_IV(partitionOn, ptx+6, piv);
            Writes.write(array, size1+offsMain, partitionOn[ptx+6], 0.25, true, false); size1 += cmp^1;
            Writes.write(swap, size2, partitionOn[ptx+6], 0.25, true, true); size2 += cmp;
            cmp = cmpOne_IV(partitionOn, ptx+7, piv);
            Writes.write(array, size1+offsMain, partitionOn[ptx+7], 0.25, true, false); size1 += cmp^1;
            Writes.write(swap, size2, partitionOn[ptx+7], 0.25, true, true); size2 += cmp;
            ptx += 8;
        }
        while(ptx < len + offs) {
            cmp = cmpOne_IV(partitionOn, ptx, piv);
            Writes.write(array, size1+offsMain, partitionOn[ptx], 0.25, true, false); size1 += cmp^1;
            Writes.write(swap, size2, partitionOn[ptx], 0.25, true, true); size2 += cmp;
            ptx++;
        }
        if(size2 <= size1 / 16 || size2 <= fluxOut) {
            Writes.arraycopy(swap, 0, array, offsMain + size1, size2, 1, true, false);
            this.quadSortSwap(array, swap, offsMain + size1, size2);
        } else {
            fluxPart(array, swap, swap, offsMain + size1, size2);
        }
        if(size1 <= size2 / 16 || size1 <= fluxOut) {
            this.quadSortSwap(array, swap, offsMain, size1);
        } else {
            fluxPart(array, swap, array, offsMain, size1);
        }
    }

    public void fluxSort(int[] array, int start, int end) {
        int len = end - start;
        if(len < 32) {
            this.tailSwap(array, start, len);
        } else if(!fluxAnalyze(array, start, len)) {
            int[] swap = Writes.createExternalArray(len);
            fluxPart(array, swap, array, start, len);
            Writes.deleteExternalArray(swap);
        }
    }

    public void fluxSort_swapDef(int[] array, int[] swap, int start, int end) {
        int len = (end - start);
        if(len < 32) {
            this.tailSwap(array, start, len);
        } else if(!fluxAnalyze(array, start, len)) {
            fluxPart(array, swap, array, start, len);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        fluxSort(array, 0, currentLength);
    }
}