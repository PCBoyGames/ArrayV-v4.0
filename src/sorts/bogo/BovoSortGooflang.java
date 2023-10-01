package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

/------------------/
|   SORTS GALORE   |
|------------------|
|  courtesy of     |
|  meme man        |
|  (aka gooflang)  |
/------------------/

Vm0wd2QyUXlWa1pPVldSWFYwZG9WMVl3Wkc5WFJsbDNXa1JTVjFKdGVGWlZNak
ExVmpKS1NHVkVRbUZXVjFKSVZtcEJlRll5VGtsaFJscE9ZbTFvVVZkV1pEUlpW
MUpJVm10V1VtSlZXbGhXYWtaTFUxWmFjbHBFVWxwV01VcEpWbTEwVjFWdFNrZF
hiR2hhWWtkU2RsWldXbXRXTVZaeVdrWndWMDFWY0ZsV1Z6RTBWakZWZVZOclpG
aGlWR3hXVm0xNFlWbFdjRmhsUjBaWFlrZFNlVll5ZUVOV01rVjNZMFpTVjFaV2
NGTmFSRVpEVld4Q1ZVMUVNRDA9

 */

public class BovoSortGooflang extends BogoSorting {
    public BovoSortGooflang(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Gooflang's Bovo");
        this.setRunAllSortsName("Gooflang's Bovo Sort");
        this.setRunSortName("Gooflang's Bovosort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(6);
        this.setBogoSort(true);
    }

    public void nOmegaMultiSwap(int O, int[] array, int a, int b, int depth) {
        if (a != b) {
            int temp = array[a];
            Writes.recordDepth(depth);
            if (O == 1) {
                if (b - a > 0) {
                    for (int i = 0; i < arrayVisualizer.getCurrentLength(); i++) {
                        Writes.multiSwap(array, a, b, 0.1, true, false);
                    }
                } else {
                    for (int i = 0; i < arrayVisualizer.getCurrentLength(); i++) {
                        Writes.multiSwap(array, a, b, 0.1, true, false);
                    }
                }
            } else {
                if (b - a > 0) {
                    for (int i = 0; i < arrayVisualizer.getCurrentLength(); i++) {
                        Writes.recursion();
                        nOmegaMultiSwap(O - 1, array, a, b, depth + 1);
                    }
                } else {
                    for (int i = 0; i < arrayVisualizer.getCurrentLength(); i++) {
                        Writes.recursion();
                        nOmegaMultiSwap(O - 1, array, a, b, depth + 1);
                    }
                }
            }
            while (array[b] != temp) {
                Writes.multiSwap(array, a, b, 0.1, true, false);
            }
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        do {
            nOmegaMultiSwap(currentLength, array, randInt(1, currentLength), 0, 0);
        } while (!isArraySorted(array, currentLength));
    }
}
