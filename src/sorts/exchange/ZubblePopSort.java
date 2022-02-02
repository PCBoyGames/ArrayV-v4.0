package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

// #1 of Distray's Pop The Top Lineup
final public class ZubblePopSort extends Sort {
    public ZubblePopSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Zubble Pop");
        this.setRunAllSortsName("Zubble Pop Sort");
        this.setRunSortName("Zubble Popsort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setQuestion("Set Pop order:", 1);
        this.setBogoSort(false);
    }

    protected void bubble(int[] array, int start, int end, int dir) {
        int cons=1, first=start+1;
        for(int j=end-1; j>=first; j-=cons) {
            boolean firsts=false;
            for(int i=Math.max(first-1, start); i<j; i++) {
                int k=i;
                while(i<j && Reads.compareIndices(array, k, i+1, 0.1, true) == dir) {
                    i++;
                }
                if(i!=k) {
                    Writes.swap(array, k, i, 1, true, false);
                    if(!firsts) {
                        first=k;
                        firsts=true;
                    }
                    cons=1;
                } else {
                    cons++;
                }
            }
        }
    }

    protected void pop(int[] array, int start, int end, int dir, int order, int depth) {
        Writes.recordDepth(depth++);
        Writes.recursion();
        if(order < 1 || end-start < 2) {
            bubble(array, start, end, dir);
            return;
        }
        int quart = (end-start+1)/4,
            half = (end-start+1)/2;
        --order;
        pop(array, start, start+quart, -dir, order, depth);
        pop(array, start+quart, start+half, dir, order, depth);
        pop(array, start+half, end-quart, -dir, order, depth);
        pop(array, end-quart, end, dir, order, depth);

        pop(array, start, start+half, -dir, order, depth);
        pop(array, start+half, end, dir, order, depth);
        pop(array, start, end, dir, order, depth);
    }

    @Override
    public void runSort(int[] array, int currentLength, int ord) {
        arrayVisualizer.setExtraHeading(String.format(", Order %,d", ord));
        pop(array, 0, currentLength, 1, ord, 0);
        arrayVisualizer.setExtraHeading("");
    }
}