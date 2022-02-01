package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class WeavedIntrospectivePatternDefeatingIterativeRogueOmegaHyperPopsort extends Sort {
    public WeavedIntrospectivePatternDefeatingIterativeRogueOmegaHyperPopsort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Weaved Introspective Pattern Defeating Iterative Rogue Omega Hyper-Pop");
        this.setRunAllSortsName("Weaved Introspective Pattern Defeating Iterative Rogue Omega Hyper Pop Sort");
        this.setRunSortName("Weaved Introspective Pattern Defeating Iterative Rogue \u03a9 Hyperpopsort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(8);
        this.setBogoSort(false);
    }

    public void omegaPush(int[] array, int start, int end) {
        for(int i=0; i<end-start-1; i++) {
            Writes.multiSwap(array, end-1, start, 0.01, true, false);
        }
    }
    public void omegaPushBW(int[] array, int start, int end) {
        for(int i=0; i<end-start-1; i++) {
            Writes.multiSwap(array, start, end-1, 0.01, true, false);
        }
    }
    private void omegaSwap(int[] array, int start, int end) {
        if(start >= end)
            return;
        this.omegaPush(array, start, end+1);
        this.omegaPushBW(array, start, end);
        this.omegaSwap(array, start+1, end-1);
        this.omegaSwap(array, start+1, end-1);
    }

    // dead insertion
    private void gappedInsertion(int[] array, int start, int end, double sleep, int gap, int direction) {
        for(int i=start+gap; i<end; i+=gap) {
            int t=array[i], j = i-gap;
            while(j >= start) {
                int comp = Reads.compareValues(array[j], t);
                if(comp == -direction || comp == 0) {
                    break;
                }
                Writes.write(array, j+gap, array[j], sleep, true, false);
                j -= gap;
            }
            Writes.write(array, j+gap, t, sleep, true, false);
            gappedInsertion(array, start, end-gap, sleep, gap, direction);
            gappedInsertion(array, start, i-gap, sleep, gap, direction);
        }
    }

    private void gapreversal(int[] array, int start, int end, int gap) {
        int l = Math.floorDiv(end-start-1, gap) * gap;
        for(int i=0; i<l/2; i+=gap) {
            this.omegaSwap(array, start+i, end-i-gap);
        }
    }

    // we ain't making it stable
    private boolean defeatPatterns(int[] array, int start, int end, int gap, int direction) {
        int comp = Reads.compareValues(array[start], array[start+gap]) * direction,
            now = comp, tmpstart = start;
        if(comp == 0) comp = -direction;
        while(start < end-gap && (now == comp || now == 0)) {
            start+=gap;
            now = Reads.compareIndices(array, start, start+gap, 0.5, true);
        }
        if(comp == direction) {
            this.gapreversal(array, tmpstart, start+1, gap);
        }
        return start >= end-gap;
    }

    private void introBubbleDown(int[] array, int start, int end, int gap, int direction) {
        if(defeatPatterns(array, start, end, gap, direction))
            return;
        int consecutive = 1, maxConsecutive = 1, unsortedFirst = start,
            bound = (int) Math.pow((end-start)/gap, 0.66d);
        for(int j=end-gap; j>=unsortedFirst; j-=consecutive*gap) {
            int startBub = Math.max(unsortedFirst-gap, start);
            boolean firstFound = false;
            for(int i=startBub; i<j; i+=gap) {
                if(Reads.compareValues(array[i], array[i+gap]) == direction) {
                    this.omegaSwap(array, i, i+gap);
                    if(!firstFound) {
                        unsortedFirst = i;
                        firstFound = true;
                    }
                    if(consecutive > maxConsecutive) {
                        maxConsecutive = consecutive;
                    }
                    consecutive = 1;
                } else {
                    consecutive++;
                }
                this.introBubbleDown(array, i+gap, end, gap, direction);
            }
            if(maxConsecutive > bound) {
                this.gappedInsertion(array, start, j, 0.5, gap, direction);
                return;
            }
        }
    }

    // normal Introspective Pattern-defeating Iterative Pop (+ order)
    public void pdiPop(int[] array, int start, int end, int dir, int ord, int depth) {
        Writes.recordDepth(depth++);
        if(ord < 1) {
            this.introBubbleDown(array, start, end, 1, dir);
        } else {
            int gap = 2, d = dir;
            while(gap <= (end-start)/2) {
                for(int i=start; i<end; i+=gap) {
                    Writes.recursion();
                    this.pdiPop(array, i, Math.min(i+gap, end), d, ord-1, depth);
                    d = -d;
                }
                gap++;
            }
            if(d == dir) {
                Writes.reversal(array, start, end-1, 0.5, true, false);
            }
        }
    }

    public void wipdiroPop(int[] array, int start, int end, int dir, int gapq, int ord, int depth) {
        if(end-start <= gapq)
            return;
        Writes.recordDepth(depth++);
        if(ord < 1) {
            this.introBubbleDown(array, start, end, gapq, dir);
        } else {
            int gap = 2, d = dir;
            while(gap <= (end-start)/2) {
                int gapr = gapq*((end-start)/gap);
                for(int i=start; i<start+gapr; i++) {
                    Writes.recursion();
                    this.wipdiroPop(array, i, end, d, gapr, ord-1, depth);
                    d = -d;
                }
                gap++;
            }
            if(d == dir) {
                this.wipdiroPop(array, start, end, dir, gapq, ord-1, depth);
            }
        }
    }

    public void weavedIntroPatternDefeatIterRogueOmegaHyperPop(int[] array, int start, int end, int dir) {
        this.wipdiroPop(array, start, end, dir, 1, end-start, 0);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.weavedIntroPatternDefeatIterRogueOmegaHyperPop(array, 0, currentLength, 1);
    }
}