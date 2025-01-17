package sorts.bogo;

import java.util.ArrayList;
import java.util.Random;
import java.lang.reflect.Field;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class PandemicSort extends BogoSorting {

    public PandemicSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Pandemic");
        this.setRunAllSortsName("Pandemic Sort");
        this.setRunSortName("Pandemic Sort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(false);
        this.setPathogenic(true);
        this.setPathogenName("STG-22 Delta");
    }

    private static PandemicSort self;

    private static Random r = new Random();

    private static String blank() {
        int randomspaces = r.nextInt(10);
        String blankstring = "";
        for (int i = 0; i < randomspaces; i++) blankstring = blankstring + " ";
        return blankstring;
    }

    private static Sort[] wrapsCompare, wrapsDistr;

    public static class Pandemic extends Sort {

        private static Sort wrapped;

        public Pandemic(ArrayVisualizer arrayVisualizer) {
            super(arrayVisualizer);
        }

        public Pandemic(ArrayVisualizer arrayVisualizer, int index, boolean compare) {
            super(arrayVisualizer);
            if (compare) wrapped = wrapsCompare[index];
            else wrapped = wrapsDistr[index];
            instantiate();
        }

        private void instantiate() {
            Object v;
            for (Field i : wrapped.getClass().getDeclaredFields()) {
                try {
                    v = i.get(wrapped);
                    i.set(this, v);
                } catch (IllegalArgumentException | IllegalAccessException e) {}
            }
        }

        public String getSortListName() {
            return "Pandemic" + blank();
        }

        public String getRunAllSortsName() {
            return "Pandemic Sort";
        }

        public String getRunSortName() {
            return "Pandemic Sort";
        }

        public String getCategory() {
            return "Pandemic Sorts" + blank();
        }

        public String getQuestion() {
            return null;
        }

        public boolean isPathogenic() {
            return false; // They're already infected. Everything is a pathogen. Why even bother?
        }

        @Override
        public void runSort(int[] array, int currentLength, int bucketCount) {
            pandemic(array, currentLength, bucketCount);
        }
    }

    private void infect() {
        ArrayList<Sort> sorts0 = arrayVisualizer.getSortAnalyzer().comparisonSorts;
        ArrayList<Sort> sorts1 = arrayVisualizer.getSortAnalyzer().distributionSorts;
        wrapsCompare = new Sort[sorts0.size()];
        wrapsDistr = new Sort[sorts1.size()];
        for (int i = 0; i < sorts0.size(); i++) {
            Pandemic s = new Pandemic(arrayVisualizer);
            Pandemic.wrapped = sorts0.get(i);
            s.instantiate();
            sorts0.set(i, s);
        }
        for (int i = 0; i < sorts1.size(); i++) {
            Pandemic s = new Pandemic(arrayVisualizer);
            Pandemic.wrapped = sorts1.get(i);
            s.instantiate();
            sorts1.set(i, s);
        }
        arrayVisualizer.getSortAnalyzer().sortSorts();
        arrayVisualizer.refreshSorts();
        for (int i = 0; i < sorts0.size(); i++) wrapsCompare[i] = Pandemic.wrapped;
        for (int i = 0; i < sorts1.size(); i++) wrapsDistr[i] = Pandemic.wrapped;
    }

    protected void runPandemic(int[] array, int currentLength) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < currentLength; i++) {
            if (array[i] < min) min = array[i];
            if (array[i] > max) max = array[i];
        }
        int random = array[randInt(0, currentLength)];
        boolean all = false;
        while (!all) {
            all = true;
            for (int j = 0; j < currentLength && all; j++) if (Reads.compareValues(array[j], random) != 0) all = false;
            if (all) break;
            boolean change = false;
            while (!change) {
                int i = randInt(0, currentLength);
                Highlights.markArray(1, i);
                Delays.sleep(0.01);
                if (Reads.compareValues(array[i], random) != 0) Writes.write(array, i, randInt(min, max + 1), 0.1, change = true, false);
            }
        }
    }

    protected static void pandemic(int[] array, int currentLength, int bucketCount) {
        self.runPandemic(array, currentLength);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        infect();
        self = this;
        pandemic(array, currentLength, bucketCount);
    }
}