package sorts.esoteric;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

enum Push {
    from(-1),
    to(1);

    int type;

    Push(int type) {
        this.type = type;
    }
}

class Key {
    public static utils.Reads Reads;
    public static utils.Writes Writes;
    public static utils.Highlights Highlights;
    public static utils.Delays Delays;

    public static int alreadyGreatest = Integer.MIN_VALUE;

    int[] container;
    public int index;

    public Key(int[] container) {
        this.container = container;
        this.index = 0;
    }

    public Key(int[] container, int index) {
        this.container = container;
        this.index = index;
    }

    public boolean isGreaterThanAllIn(int startRange, int endRange) {
        while (endRange >= startRange && Reads.compareValues(container[endRange], container[index]) >= 0) {
            Highlights.markArray(1, endRange);
            Delays.sleep(0.75);
            endRange--;
        }

        return endRange < startRange;
    }

    public int firstIndexHigherThan_goRight(int startRange, int endRange) {
        while (startRange <= endRange && Reads.compareValues(container[startRange], container[index]) < 0) {
            Highlights.markArray(1, startRange);
            Delays.sleep(0.75);
            startRange++;
        }
        if (startRange > endRange)
            return alreadyGreatest;
        return startRange;
    }

    public static void push(Push where, Key key1, Push which, int to) {
        Writes.multiSwap(key1.container, key1.index, to, 0.1, true, false);
    }
}
public class PseudocodeSort extends BogoSorting {
    public PseudocodeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Pseudocode");
        this.setRunAllSortsName("Pseudocode Sort");
        this.setRunSortName("Pseudocode Sort");
        this.setCategory("Esoteric Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(false);
    }

    private void sort(int[] array, int start, int end) {
        for (int i = start + 1; i < end; i++) {
            Key key = new Key(array, i);
            if (Reads.compareValues(array[key.index], array[i-1]) > 0)
                continue;
            if (Reads.compareValues(array[key.index], array[start]) <= 0) {
                Key.push(Push.from, key, Push.to, start);
                continue;
            }
            while (!key.isGreaterThanAllIn(start, i - 1)) {
                int found = key.firstIndexHigherThan_goRight(start, i - 1);
                if (found == Key.alreadyGreatest)
                    break;
                Writes.swap(array, key.index, found, 1, true, false);
            }
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        Key.Writes = Writes;
        Key.Delays = Delays;
        Key.Highlights = Highlights;
        Key.Reads = Reads;
        this.sort(array, 0, currentLength);
    }
}