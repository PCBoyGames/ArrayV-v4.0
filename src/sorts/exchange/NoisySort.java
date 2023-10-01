package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class NoisySort extends Sort {
    public NoisySort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Noisy");
        this.setRunAllSortsName("Noisy Sort");
        this.setRunSortName("Noisesort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(false);
        this.setQuestion("Enter the noise intensity for this sort:", 16);
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 1) return 1;
        return answer;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int verifyi = 1;
        boolean verifypass = false;
        while (!verifypass) {
            for (int right = verifyi + 1; right <= currentLength; right += bucketCount) {
                int left = verifyi;
                while (left <= right && right <= currentLength) {
                    if (Reads.compareIndices(array, left - 1, right - 1, 0.005, true) > 0) {
                        Writes.swap(array, left - 1, right - 1, 0.005, true, false);
                        if (right - 1 > verifyi) right--;
                        left = verifyi;
                    } else left++;
                }
            }
            if (verifyi - 1 > 0) verifyi--;
            verifypass = true;
            while (verifyi < currentLength && verifypass) {
                if (Reads.compareIndices(array, verifyi - 1, verifyi, 0.005, true) <= 0) verifyi++;
                else verifypass = false;
            }
        }
    }
}