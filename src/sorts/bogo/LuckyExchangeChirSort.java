package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

Coded for ArrayV by Ayako-chan
in collaboration with PCBoy

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 * @author PCBoy
 *
 */
public final class LuckyExchangeChirSort extends BogoSorting {

    public LuckyExchangeChirSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Lucky Exchange Chir");
        this.setRunAllSortsName("Lucky Exchange Chir Sort");
        this.setRunSortName("Lucky Exchange Chirsort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(8192);
        this.setBogoSort(true);
        setQuestion("Enter the luck for this sort:", 50);
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 1 || answer > 100)
            return 50;
        return answer;
    }

    protected boolean bogoCompSwap(int[] array, int a, int b, int luck) {
        boolean anyswaps = false;
        if (BogoSorting.randBoolean()) {
            for (int i = a; i < b - 1; i++) {
                int j = BogoSorting.randInt(i, b);
                if (Reads.compareIndices(array, i, j, this.delay, true) > 0) {
                    if (BogoSorting.randInt(1, 101) <= luck) {
                        Writes.swap(array, i, j, this.delay, true, false);
                        anyswaps = true;
                    }
                }
            }
        } else {
            for (int i = b - 1; i > a; i--) {
                int j = BogoSorting.randInt(a, i);
                if (Reads.compareIndices(array, i, j, this.delay, true) < 0) {
                    if (BogoSorting.randInt(1, 101) <= luck) {
                        Writes.swap(array, i, j, this.delay, true, false);
                        anyswaps = true;
                    }
                }
            }
        }
        return anyswaps;
    }

    public boolean pull(int[] array, int a, int b, int luck) {
        boolean anyswaps = false;
        if (a < b) {
            for (int i = a; i < b; i++) {
                if (Reads.compareIndices(array, i, i + 1, this.delay, true) > 0) {
                    if (BogoSorting.randInt(1, 101) <= luck) {
                        Writes.swap(array, i, i + 1, delay, true, false);
                        anyswaps = true;
                    }
                }
            }
        } else {
            for (int i = a; i > b; i--) {
                if (Reads.compareIndices(array, i, i - 1, this.delay, true) < 0) {
                    if (BogoSorting.randInt(1, 101) <= luck) {
                        Writes.swap(array, i, i - 1, delay, true, false);
                        anyswaps = true;
                    }
                }
            }
        }
        return anyswaps;
    }

    protected boolean circle(int[] array, int left, int right, int luck) {
        int a = left;
        int b = right;
        boolean anyswap = false;
        while (a < b) {
            if (Reads.compareIndices(array, a, b, this.delay, true) > 0)
                if (BogoSorting.randInt(1, 101) <= luck) {
                    Writes.swap(array, a, b, this.delay, true, false);
                    anyswap = true;
                }
            a++;
            b--;
        }
        return anyswap;
    }

    public void sort(int[] array, int a, int b, int luck) {
        while (!this.isRangeSorted(array, a, b, false, true)) {
            boolean noswap = true;
            while (noswap) {
                boolean swapped = false;
                int choice = BogoSorting.randInt(0, 10);
                // Exchange Bogo
                if (choice == 0)
                    swapped = bogoCompSwap(array, a, b, luck);
                // Exchange Bozo
                if (choice == 1) {
                    int i1 = BogoSorting.randInt(a, b), i2 = BogoSorting.randInt(a, b);
                    int comp = Reads.compareIndices(array, i1, i2, this.delay, true);
                    if (i1 < i2 ? comp > 0 : comp < 0)
                        if (BogoSorting.randInt(1, 101) <= luck) {
                            Writes.swap(array, i1, i2, this.delay, true, false);
                            swapped = true;
                        }
                }
                // Bubble Bogo
                if (choice == 2) {
                    int i = BogoSorting.randInt(a, b - 1);
                    if (Reads.compareIndices(array, i, i + 1, this.delay, true) > 0)
                        if (BogoSorting.randInt(1, 101) <= luck) {
                            Writes.swap(array, i, i + 1, this.delay, true, false);
                            swapped = true;
                        }
                }
                // Exchange Bojo
                if (choice == 3) {
                    int i1 = BogoSorting.randInt(a, b), i2 = BogoSorting.randInt(a, b);
                    if (i1 > i2) {
                        int temp = i1;
                        i1 = i2;
                        i2 = temp;
                    }
                    if (Reads.compareIndices(array, i1, i2, this.delay, true) > 0)
                        if (BogoSorting.randInt(1, 101) <= luck) {
                            Writes.reversal(array, i1, i2, this.delay, true, false);
                            swapped = true;
                        }
                }
                // Exchange Bovo
                if (choice == 4)
                    swapped = pull(array, BogoSorting.randInt(a, b), a, luck);
                // Exchange Vogo
                if (choice == 5)
                    swapped = pull(array, BogoSorting.randInt(a, b), b - 1, luck);
                // Exchange Bomo
                if (choice == 6)
                    swapped = pull(array, BogoSorting.randInt(a, b), BogoSorting.randInt(a, b), luck);
                // Exchange Slice Bogo
                if (choice == 7) {
                    int i1 = BogoSorting.randInt(a, b), i2 = BogoSorting.randInt(a, b);
                    if (i1 > i2) {
                        int temp = i1;
                        i1 = i2;
                        i2 = temp;
                    }
                    swapped = bogoCompSwap(array, i1, i2 + 1, luck);
                }
                // Circle Bogo
                if (choice == 8) {
                    int i1 = BogoSorting.randInt(a, b), i2 = BogoSorting.randInt(a, b);
                    if (i1 > i2) {
                        int temp = i1;
                        i1 = i2;
                        i2 = temp;
                    }
                    swapped = circle(array, i1, i2, luck);
                }
                // Comb Bogo
                if (choice == 9) {
                    int gap = BogoSorting.randInt(1, b - a);
                    if (BogoSorting.randBoolean()) {
                        for (int i = a; (gap + i) < b; i++) {
                            if (Reads.compareIndices(array, i, i + gap, this.delay, true) > 0)
                                if (BogoSorting.randInt(1, 101) <= luck) {
                                    Writes.swap(array, i, i + gap, this.delay, true, false);
                                    swapped = true;
                                }
                        }
                    } else {
                        for (int i = b - 1; i >= a + gap; i--) {
                            if (Reads.compareIndices(array, i - gap, i, this.delay, true) > 0)
                                if (BogoSorting.randInt(1, 101) <= luck) {
                                    Writes.swap(array, i - gap, i, this.delay, true, false);
                                    swapped = true;
                                }
                        }
                    }
                }
                if (swapped)
                    noswap = false;
            }
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int luck) {
        sort(array, 0, sortLength, luck);

    }

}
