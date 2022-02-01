package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

// #6 of Distray's Pop The Top Lineup
public final class LuckierRogueSort extends BogoSorting {
    public LuckierRogueSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Luckier Rogue");
        this.setRunAllSortsName("Luckier Rogue Sort");
        this.setRunSortName("Luckier Roguesort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Enter the luck for this sort:", 99);
    }

    private boolean qSift(int[] array, int start, int end) {
        if(start >= end)
            return false;
        int mid = start + (end-start) / 2;
        boolean f = false;
        if (Reads.compareValues(array[start],array[end]) == 1) {
            Writes.swap(array, start, end, 1, true, false);
            f = true;
        }
        if(start == mid) {
            return false;
        }
        boolean l = this.qSift(array, start, mid);
        boolean r = this.qSift(array, mid, end);
        return f||l||r;
    }

    private void fallback(int[] array, int start, int end) {
        int l = end - start;
        do {
            l--;
        } while(l > 0 && this.qSift(array, start, end-1));
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 0)
            return 0;
        if(answer > 100)
            return 100;
        return answer;
    }

    @Override
    public void runSort(int[] array, int currentLength, int luck) {
        int gap = currentLength-1;
        boolean swapped;
        do {
            do {
                swapped = false;
                for(int i=gap; i<currentLength; i++) {
                    int rand = randInt(1, 101);
                    if(Reads.compareIndices(array, i-gap, i, 0.1, true) > 0) {
                        if(rand <= luck) {
                            swapped = true;
                            Writes.swap(array, i-gap, i, 0.1, true, false);
                        }
                    } else if(rand > luck) {
                        swapped = true;
                        Writes.swap(array, i-gap, i, 0.1, true, false);
                    }
                }
            } while(swapped);
            if(gap > 1) gap--;
        } while(gap > 1 || (luck > 25 && !isArraySorted(array, currentLength)));
        if(!isArraySorted(array, currentLength)) {
            fallback(array, 0, currentLength);
        }
    }
}