package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

// #2 of Distray's Pop The Top Lineup
final public class PoptimalSort extends Sort {
    public PoptimalSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Poptimal");
        this.setRunAllSortsName("Optimal Pop Sort");
        this.setRunSortName("Poptimal Sort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void gapreversal(int[] array, int start, int end, int gap) {
        int l = end-start,
            k = start + (l - gap - (l % gap));
        for(int i=start, j=k; i<j; i+=gap, j-=gap) {
            Writes.swap(array, i, j, 0.1, true, false);
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

    private void zubble(int[] array, int start, int end, int dir, int gap) {
        if(defeatPatterns(array, start, end, gap, dir))
            return;
        int firstbound=start+gap, consecutive=1;
        for(int j=end-gap; j>=firstbound; j-=gap*consecutive) {
            boolean firstset = false;
            for(int i=Math.max(start, firstbound-gap); i<j; i+=gap) {
                int k=i;
                while(i < j && Reads.compareIndices(array, k, i+gap, 0.01, true) == dir) {
                    i+=gap;
                }
                if(k != i) {
                    Writes.swap(array, k, i, 1, true, false);
                    if(!firstset) {
                        firstbound = k;
                        firstset=true;
                    }
                    consecutive=1;
                } else {
                    consecutive++;
                }
            }

        }
    }

    private void circleRoutine(int[] array, int start, int end, int bound, int dir, int gap) {
        for(int i=start, j=end; i<j; i+=gap, j-=gap) {
            if(j < bound) {
                if(Reads.compareValues(array[i], array[j]) == dir) {
                    Writes.swap(array, i, j, 1, true, false);
                }
            }
        }
        int mid = start+Math.floorDiv(end-start, 2*gap)*gap;
        if(mid==start) return;
        circleRoutine(array, start, mid, bound, dir, gap);
        circleRoutine(array, mid+gap, end, bound, dir, gap);
    }

    private void gappedCircle(int[] array, int start, int end, int dir, int gap) {
        int ungapped = (end-start) / gap,
            closestpow = 1;
        while(closestpow < ungapped) closestpow <<= 1;
        circleRoutine(array, start, start+(closestpow-1)*gap, end, dir, gap);
    }

    public void wipdiPop(int[] array, int start, int end, int dir, int gapq, int ord, int depth) {
        if(end-start <= gapq)
            return;
        Writes.recordDepth(depth++);
        if(ord < 1) {
            this.zubble(array, start, end, dir, gapq);
        } else {
            int gap = 1, d = dir;
            while(gap <= (end-start)/(2*gapq)) {
                gap*=2;
            }
            gap/=2;
            int lasti = start,
                thresholdCirc = (int) Math.sqrt(gap);
            while(gap > 1) {
                int gapr = gap*gapq;
                d = dir;
                for(int i=start; i<start+((end-start)%gapr)+gapr; i+=gapq) {
                    Writes.recursion();
                    if(ord == 1 && gap < thresholdCirc)
                        this.gappedCircle(array, i, end, d, gapr);
                    else
                        this.wipdiPop(array, i, end, d, gapr, ord-1, depth);
                    lasti = i;
                    d = -d;
                }
                gap /= 2;
            }
            if(d == dir) {
                this.gapreversal(array, lasti, end+gapq, 2*gapq);
            } else {
                this.gapreversal(array, start, end+gapq, 2*gapq);
            }
            this.wipdiPop(array, start, end, dir, gapq, ord-1, depth);
        }
    }

    public void poptimal(int[] array, int start, int end, int dir) {
        wipdiPop(array, start, end, dir, 1, 1, 0);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        poptimal(array, 0, currentLength, 1);
    }
}