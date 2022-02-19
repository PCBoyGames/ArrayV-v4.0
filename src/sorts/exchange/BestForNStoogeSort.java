package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.BestForNSorting;
import utils.Statistics;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class BestForNStoogeSort extends BestForNSorting {

    int maxtouse;

    public BestForNStoogeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Best For N Stooge");
        this.setRunAllSortsName("Best For N Stooge Sort");
        this.setRunSortName("Best For N Stooge Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Enter the run length for this sort:\n(Non-2*n will be rounded up)", 64);
    }

    protected void bestNStooge(int[] array, int s, int l, int depth) {
        Writes.recordDepth(depth);
        if (l <= 2 * maxtouse) {
            Statistics.addStat("Network Use");
            initNetwork(array, s, l / 2);
            Statistics.addStat("Network Use");
            initNetwork(array, s + l / 2, l / 2);
            Statistics.addStat("Network Use");
            initNetwork(array, s + l / 4, l / 2);
            Statistics.addStat("Network Use");
            initNetwork(array, s, l / 2);
            Statistics.addStat("Network Use");
            initNetwork(array, s + l / 2, l / 2);
            Statistics.addStat("Network Use");
            initNetwork(array, s + l / 4, l / 2);
        } else {
            Writes.recursion();
            bestNStooge(array, s, l / 2, depth + 1);
            Writes.recursion();
            bestNStooge(array, s + l / 2, l / 2, depth + 1);
            Writes.recursion();
            bestNStooge(array, s + l / 4, l / 2, depth + 1);
            Writes.recursion();
            bestNStooge(array, s, l / 2, depth + 1);
            Writes.recursion();
            bestNStooge(array, s + l / 2, l / 2, depth + 1);
            Writes.recursion();
            bestNStooge(array, s + l / 4, l / 2, depth + 1);
        }
    }

    public void bestNStoogeMain(int[] array, int start, int length, int base, int depth, double del) {
        delay = del;
        Statistics.putStat("Network Use");
        Writes.recordDepth(depth);
        if (length <= maxtouse) {
            Statistics.addStat("Network Use");
            initNetwork(array, start, length);
        } else {
            int effectivelen = base;
            while (effectivelen <= length) effectivelen *= 2;
            effectivelen /= 2;
            bestNStooge(array, start, effectivelen, depth);
            if (effectivelen != length) {
                Writes.recursion();
                bestNStoogeMain(array, start + effectivelen, length - effectivelen, base, depth + 1, del);
                bestNStooge(array, start + (length - effectivelen) / 2, effectivelen, depth);
                bestNStooge(array, start, effectivelen, depth);
                bestNStooge(array, start + length - effectivelen, effectivelen, depth);
                bestNStooge(array, start + (length - effectivelen) / 2, effectivelen, depth);
            }
        }
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer % 2 == 1) answer++;
        if (answer < 2) return 2;
        if (answer > 64) return 64;
        return answer;
    }

    @Override
    public void runSort(int[] array, int currentLength, int base) {
        maxtouse = base;
        bestNStoogeMain(array, 0, currentLength, base, 0, 0.125);
    }
}