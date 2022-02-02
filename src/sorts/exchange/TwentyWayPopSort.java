package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class TwentyWayPopSort extends Sort {
    public TwentyWayPopSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("20 Way Pop");
        this.setRunAllSortsName("20 Way Pop Sort");
        this.setRunSortName("20 Way Popsort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setQuestion("Set Pop order:", 1);
        this.setBogoSort(false);
    }
    protected void bubbleSort(int[] array, int start, int end, boolean right) {
        int swap = end, comp = right ? 1 : -1;
        while(swap > start) {
            int lastSwap = start;
            for(int i=start; i<swap-1; i++) {
                if(Reads.compareValues(array[i], array[i+1]) == comp) {
                    Writes.swap(array, i, i+1, 0.025, true, false);
                    lastSwap = i+1;
                }
            }
            swap = lastSwap;
        }
    }
    protected void bubblePop(int[] array, int start, int end, boolean right) {
        int swap = end, comp = right ? 1 : -1;
        while(swap > start) {
            int lastSwap = start;
            for(int i=start; i<swap-1; i++) {
                if(Reads.compareValues(array[i], array[i+1]) == comp) {
                    Writes.swap(array, i, i+1, 0.025, true, false);
                    lastSwap = i+1;
                } else if(lastSwap > start)
                    break;
            }
            swap = lastSwap;
        }
    }

    // **YES, THIS IS VARIABLE ORDER 20-WAY POPSORT**
    protected void pop(int[] array, int start, int end, int order, boolean invert) {
        if(start >= end)
            return;
        if(end-start <= 20) {
            this.bubbleSort(array, start, end, !invert);
            return;
        }
        int twenty = (end - start + 1) / 20, ten = (end - start + 1) / 10,
            fifth = (end - start + 1) / 5;
        if(order < 1) {
            this.bubbleSort(array, start, end, !invert);
        } else if(order == 1) {
            Writes.recursion(39);
            this.bubbleSort(array, start, start + twenty, invert);
            this.bubbleSort(array, start + twenty, start + ten, !invert);
            this.bubbleSort(array, start + ten, start + ten + twenty, invert);
            this.bubbleSort(array, start + ten + twenty, start + fifth, !invert);
            this.bubbleSort(array, start + fifth, start + fifth + twenty, invert);
            this.bubbleSort(array, start + fifth + twenty, start + fifth + ten, !invert);
            this.bubbleSort(array, start + fifth + ten, start + fifth + ten + twenty, invert);
            this.bubbleSort(array, start + fifth + ten + twenty, start + 2 * fifth, !invert);
            this.bubbleSort(array, start + 2 * fifth, start + 2 * fifth + twenty, invert);
            this.bubbleSort(array, start + 2 * fifth + twenty, start + 2 * fifth + ten, !invert);
            this.bubbleSort(array, start + 2 * fifth + ten, start + 2 * fifth + ten + twenty, invert);
            this.bubbleSort(array, start + 2 * fifth + ten + twenty, end - 2 * fifth, !invert);
            this.bubbleSort(array, end - 2 * fifth, end - 2 * fifth + twenty, invert);
            this.bubbleSort(array, end - 2 * fifth + twenty, end - 2 * fifth + ten, !invert);
            this.bubbleSort(array, end - 2 * fifth + ten, end - 2 * fifth + ten + twenty, invert);
            this.bubbleSort(array, end - 2 * fifth + ten + twenty, end - fifth, !invert);
            this.bubbleSort(array, end - fifth, end - fifth + twenty, invert);
            this.bubbleSort(array, end - fifth + twenty, end - fifth + ten, !invert);
            this.bubbleSort(array, end - fifth + ten, end - fifth + ten + twenty, invert);
            this.bubbleSort(array, end - fifth + ten + twenty, end, !invert);

            this.bubblePop(array, start, start+ten, invert);
            this.bubblePop(array, start+ten, start+fifth, !invert);
            this.bubblePop(array, start+fifth, start+fifth+ten, invert);
            this.bubblePop(array, start+fifth+ten, start+2*fifth, !invert);
            this.bubblePop(array, start+2*fifth, start+2*fifth+ten, invert);
            this.bubblePop(array, start+2*fifth+ten, end-2*fifth, !invert);
            this.bubblePop(array, end-2*fifth, end-2*fifth+ten, invert);
            this.bubblePop(array, end-2*fifth+ten, end-fifth, !invert);
            this.bubblePop(array, end-fifth, end-fifth+ten, invert);
            this.bubblePop(array, end-fifth+ten, end, !invert);

            this.bubblePop(array, start, start+fifth, !invert);
            this.bubblePop(array, start+fifth, start+2*fifth, !invert);
            this.bubblePop(array, start+2*fifth, end-2*fifth, invert);
            this.bubblePop(array, end-2*fifth, end-fifth, !invert);
            this.bubblePop(array, end-fifth, end, invert);

            this.bubblePop(array, end-2*fifth, end, invert);
            this.bubblePop(array, start+fifth, end-2*fifth, !invert);
            this.bubblePop(array, start+fifth, end, invert);

            this.bubblePop(array, start, end, !invert);
            // it keeps getting worse
        } else {
            Writes.recordDepth(order-1);
            Writes.recursion(39);
            this.pop(array, start, start + twenty, order-1, !invert);
            this.pop(array, start + twenty, start + ten, order-1, invert);
            this.pop(array, start + ten, start + ten + twenty, order-1, !invert);
            this.pop(array, start + ten + twenty, start + fifth, order-1, invert);
            this.pop(array, start + fifth, start + fifth + twenty, order-1, !invert);
            this.pop(array, start + fifth + twenty, start + fifth + ten, order-1, invert);
            this.pop(array, start + fifth + ten, start + fifth + ten + twenty, order-1, !invert);
            this.pop(array, start + fifth + ten + twenty, start + 2 * fifth, order-1, invert);
            this.pop(array, start + 2 * fifth, start + 2 * fifth + twenty, order-1, !invert);
            this.pop(array, start + 2 * fifth + twenty, start + 2 * fifth + ten, order-1, invert);
            this.pop(array, start + 2 * fifth + ten, start + 2 * fifth + ten + twenty, order-1, !invert);
            this.pop(array, start + 2 * fifth + ten + twenty, end - 2 * fifth, order-1, invert);
            this.pop(array, end - 2 * fifth, end - 2 * fifth + twenty, order-1, !invert);
            this.pop(array, end - 2 * fifth + twenty, end - 2 * fifth + ten, order-1, invert);
            this.pop(array, end - 2 * fifth + ten, end - 2 * fifth + ten + twenty, order-1, !invert);
            this.pop(array, end - 2 * fifth + ten + twenty, end - fifth, order-1, invert);
            this.pop(array, end - fifth, end - fifth + twenty, order-1, !invert);
            this.pop(array, end - fifth + twenty, end - fifth + ten, order-1, invert);
            this.pop(array, end - fifth + ten, end - fifth + ten + twenty, order-1, !invert);
            this.pop(array, end - fifth + ten + twenty, end, order-1, invert);

            this.pop(array, start, start+ten, order-1, !invert);
            this.pop(array, start+ten, start+fifth, order-1, invert);
            this.pop(array, start+fifth, start+fifth+ten, order-1, !invert);
            this.pop(array, start+fifth+ten, start+2*fifth, order-1, invert);
            this.pop(array, start+2*fifth, start+2*fifth+ten, order-1, !invert);
            this.pop(array, start+2*fifth+ten, end-2*fifth, order-1, invert);
            this.pop(array, end-2*fifth, end-2*fifth+ten, order-1, !invert);
            this.pop(array, end-2*fifth+ten, end-fifth, order-1, invert);
            this.pop(array, end-fifth, end-fifth+ten, order-1, !invert);
            this.pop(array, end-fifth+ten, end, order-1, invert);

            this.pop(array, start, start+fifth, order-1, invert);
            this.pop(array, start+fifth, start+2*fifth, order-1, invert);
            this.pop(array, start+2*fifth, end-2*fifth, order-1, !invert);
            this.pop(array, end-2*fifth, end-fifth, order-1, invert);
            this.pop(array, end-fifth, end, order-1, !invert);

            this.pop(array, end-2*fifth, end, order-1, !invert);
            this.pop(array, start+fifth, end-2*fifth, order-1, invert);
            this.pop(array, start+fifth, end, order-1, !invert);

            this.pop(array, start, end, order-1, invert);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int pop) {
        this.pop(array, 0, currentLength, pop, false);
    }
}