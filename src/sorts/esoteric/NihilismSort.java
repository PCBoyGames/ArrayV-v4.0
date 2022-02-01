package sorts.esoteric;

import java.util.ArrayList;
import java.util.Random;
import java.lang.reflect.Field;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class NihilismSort extends Sort {

    public NihilismSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Nihilism");
        this.setRunAllSortsName("Nihilism Sort");
        this.setRunSortName("Nihilism Sort");
        this.setCategory("Esoteric Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setPathogenic(true);
        this.setPathogenName("STG-22 Beta");
    }

    private static NihilismSort self;

    private static final Random r = new Random();

    private static String blank(String output) {
        int randomspaces = r.nextInt(10);
        String blankstring = "";
        for (int i = 0; i < randomspaces; i++) {
            blankstring = blankstring + " ";
        }
        return blankstring;
    }

    private static Sort[] wrapsCompare, wrapsDistr;

    public static class Nothing extends Sort {

        private static Sort wrapped;

        public Nothing(ArrayVisualizer arrayVisualizer) {
            super(arrayVisualizer);
        }

        public Nothing(ArrayVisualizer arrayVisualizer, int index, boolean compare) {
            super(arrayVisualizer);
            if(compare)
                wrapped = wrapsCompare[index];
            else
                wrapped = wrapsDistr[index];
            instantiate();
        }

        private void instantiate() {
            Object v;
            for(Field i : wrapped.getClass().getDeclaredFields()) {
                try {
                    v = i.get(wrapped);
                    i.set(this, v);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                }
            }
        }

        public String getSortListName() {
            return blank(wrapped.getSortListName());
        }

        public String getRunAllSortsName() {
            return blank(wrapped.getRunAllSortsName());
        }

        public String getRunSortName() {
            return blank(wrapped.getRunSortName());
        }

        public String getCategory() {
            return blank(wrapped.getCategory());
        }

        public String getQuestion() {
            return blank(wrapped.getQuestion());
        }

        public boolean isPathogenic() {
            return false; // They're already infected. Everything is a pathgen. Why even bother?
        }

        @Override
        public void runSort(int[] array, int currentLength, int bucketCount) {
            nihilism(array, currentLength, bucketCount);
        }
    }

    private void infect() {
        ArrayList<Sort> sorts0 = arrayVisualizer.getSortAnalyzer().comparisonSorts;
        ArrayList<Sort> sorts1 = arrayVisualizer.getSortAnalyzer().distributionSorts;
        wrapsCompare = new Sort[sorts0.size()];
        wrapsDistr = new Sort[sorts1.size()];
        for (int i = 0; i < sorts0.size(); i++) {
            Nothing s = new Nothing(arrayVisualizer);
            Nothing.wrapped = sorts0.get(i);
            s.instantiate();
            sorts0.set(i, s);
        }
        for (int i = 0; i < sorts1.size(); i++) {
            Nothing s = new Nothing(arrayVisualizer);
            Nothing.wrapped = sorts1.get(i);
            s.instantiate();
            sorts1.set(i, s);
        }
        arrayVisualizer.getSortAnalyzer().sortSorts();
        arrayVisualizer.refreshSorts();
        for (int i = 0; i < sorts0.size(); i++)
            wrapsCompare[i] = Nothing.wrapped;
        for (int i = 0; i < sorts1.size(); i++)
            wrapsDistr[i] = Nothing.wrapped;
    }

    protected void overrideLength(int n) {
        double sleepRatio = Delays.getSleepRatio();
        arrayVisualizer.setCurrentLength(n);
        Delays.setSleepRatio(sleepRatio);
    }

    protected void runNihilism() {
        overrideLength(0);
    }

    protected static void nihilism(int[] array, int currentLength, int bucketCount) {
        self.runNihilism();
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        infect();
        self = this;
        nihilism(array, currentLength, bucketCount);
    }
}