package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

final public class LuckierStoogeSort extends BogoSorting {
    public LuckierStoogeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Luckier Stooge");
        this.setRunAllSortsName("Luckier Stooge Sort");
        this.setRunSortName("Luckier Stoogesort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(256);
        this.setBogoSort(false);
        this.setQuestion("Enter the luck for this sort:", 99);
    }

    // equivalent to (ternary) (v & (1 << s)) >> s
    public int trit(int v, int sig) {
        while(sig-- > 0) {
            v /= 3;
        }
        return v % 3;
    }

    // how many iterations it takes for a lerp to reach 0
    // (in this case, how much recursive depth it takes for stooge to hit 0-length)
    public int flerp(int v, int b) {
        int i = 0;
        while(v > 0) {
            v = ((b-1)*v)/b;
            i++;
        }
        return i;
    }

    private int getStooge(int recursion, int a, int b, int depth) {
        for(int third = (b-a+1)/3,
                depthNow = depth;;
                third=(b-a+1)/3) {
            Highlights.markArray(1, a);
            Highlights.markArray(2, b);
            Delays.sleep(0.0025);
            switch(trit(recursion, depthNow--)) {
                case 0:
                case 2:
                    b -= third; break;
                case 1:
                    a += third; break;
            }
            if((b - a == 1 && third == 1) || third < 1)
                break;
        }
        return a;
    }

    public void luckierStoogeRoutine(int[] A, int i, int j, int luck0) {
        int cube = (j-i),
            maxDepth = flerp(j-i+1, 3)-1;
        cube=cube*cube*cube;
        for(int k=0; k<cube; k++) {
            int a = getStooge(k, i, j, maxDepth),
                luck1 = randInt(1, 101);
            if(Reads.compareIndices(A, a, a+1, 0.05, true)>0) {
                if(luck1 <= luck0)
                    Writes.swap(A, a, a+1, 0.025, true, false);
            } else if(luck1 > luck0) {
                Writes.swap(A, a, a+1, 0.025, true, false);
            }
        }
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

    public void luckierStoogeSort(int[] A, int i, int j, int luck) {
        int runs = 0, cube = j-i+1;
        cube = cube * cube * cube;
        while(!isRangeSorted(A, i, j+1) && runs < cube) {
            luckierStoogeRoutine(A, i, j, luck);
            if(luck < 25)
                Writes.reversal(A, i, j, 0.125, true, false);
        }
        if(runs >= cube && !isRangeSorted(A, i, j+1)) {
            fallback(A, i, j+1);
        }
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 1)
            return 0;
        if(answer > 100)
            return 100;
        return answer;
    }

    @Override
    public void runSort(int[] array, int currentLength, int luck) {
        luckierStoogeSort(array, 0, currentLength-1, luck);
    }
}