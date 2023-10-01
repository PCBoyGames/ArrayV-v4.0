package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

public class LuckierBubbleSort extends BogoSorting {
    public LuckierBubbleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Luckier Bubble");
        this.setRunAllSortsName("Luckier Bubble Sort");
        this.setRunSortName("Luckier Bubblesort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(8192);
        this.setBogoSort(true);
        this.setQuestion("Enter the luck for this sort:", 50);
    }

    private boolean qSift(int[] array, int start, int end) {
        if (start >= end)
            return false;
        int mid = start + (end-start) / 2;
        boolean f = false;
        if (Reads.compareValues(array[start],array[end]) == 1) {
            Writes.swap(array, start, end, 1, true, false);
            f = true;
        }
        if (start == mid) {
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
        } while (l > 0 && this.qSift(array, start, end-1));
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 1 || answer > 100)
            return 50;
        return answer;
    }

    @Override
    public void runSort(int[] array, int m, int luck) {
        boolean anyswaps = true;
        long tries = 0;
        long n = m;
        while (anyswaps && tries < n*n*n) {
            anyswaps = false;
            for (int i = 0; i < n - 1; i++) {
                Highlights.markArray(1, i);
                Highlights.markArray(2, i + 1);
                Delays.sleep(0.01);
                int rand = BogoSorting.randInt(1, 101);
                if (Reads.compareValues(array[i], array[i + 1]) > 0) {
                    if (rand <= luck) {
                        anyswaps = true;
                        Writes.swap(array, i, i + 1, 0.01, true, false);
                    }
                } else if (rand > luck) {
                    anyswaps = true;
                    Writes.swap(array, i, i + 1, 0.01, true, false);
                }
            }
            tries++;
        }
        if (luck <= 50) {
            Writes.reversal(array, 0, m-1, 1, true, false);
        }
        if (tries >= n*n*n || !isRangeSorted(array, 0, m, false, false)) // cap it at O(n^4)
            this.fallback(array, 0, m);
    }
}
