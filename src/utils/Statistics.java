package utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import main.ArrayVisualizer;

public class Statistics {
    private String sortCategory;
    private String sortHeading;
    private String sortExtraHeading;
    private String arrayLength;

    private String sortDelay;
    private String visualTime;
    private String estSortTime;

    private String comparisonCount;
    private String swapCount;
    private String recCount;
    private String recDepth;
    private static HashMap<String, Long> addedCounts;
    private String reversalCount;
    private String bigO;

    private String mainWriteCount;
    private String auxWriteCount;

    private String auxAllocAmount;

    private String segments;

    private DecimalFormat formatter;

    private AccessFunction access;
    private ArrayVisualizer arrayv;
    public boolean bigo;

    public Statistics(ArrayVisualizer ArrayVisualizer) {
        arrayv = ArrayVisualizer;
        this.formatter = ArrayVisualizer.getNumberFormat();
        this.access = new AccessFunction(ArrayVisualizer);
        addedCounts = new HashMap<>();
        bigo = false;
        this.updateStats(ArrayVisualizer);
    }
    public static void putStat(String name) {
        addedCounts.putIfAbsent(name, 0l);
    }
    public static void putStats(String... names) {
        Arrays.stream(names).forEachOrdered(Statistics::putStat);
    }
    public static void resetStat(String name) {
        addedCounts.put(name, 0l);
    }
    public static void addStat(String name) {
        addedCounts.put(name, addedCounts.get(name)+1l);
    }
    public static void addStat(String name, long amount) {
        addedCounts.put(name, addedCounts.get(name)+amount);
    }
    public int[] findSegments(int[] array, int length, boolean reversed) {
        int runs = 1;
        int correct = 0;
        for (int i = 0; i < length-1; i++) {
            if (!reversed && array[i] > array[i+1]) runs++;
            else if (reversed && array[i] < array[i+1]) runs++;
            else correct++;
        }
        int[] result = new int[2];
        result[0] = runs;
        result[1] = (int) ((((double) correct) / (length - 1)) * 100);
        return result;
    }

    public void updateStats(ArrayVisualizer ArrayVisualizer) {
        this.sortCategory = ArrayVisualizer.getCategory();
        this.sortHeading = ArrayVisualizer.getHeading();
        this.sortExtraHeading = ArrayVisualizer.getExtraHeading();
        int showUnique = Math.min(ArrayVisualizer.getUniqueItems(), ArrayVisualizer.getCurrentLength());
        this.arrayLength = this.formatter.format(ArrayVisualizer.getCurrentLength()) + " Numbers"
        + ", " + this.formatter.format(showUnique) + " Unique";

        this.sortDelay = "Delay: " + ArrayVisualizer.getDelays().displayCurrentDelay();
        this.visualTime = "Visual Time: " + ArrayVisualizer.getTimer().getVisualTime();
        this.estSortTime = "Sort Time: " + ArrayVisualizer.getTimer().getRealTime();

        this.comparisonCount = ArrayVisualizer.getReads().getStats();
        this.swapCount = ArrayVisualizer.getWrites().getSwaps();
        this.reversalCount = ArrayVisualizer.getWrites().getReversals();
        this.recCount = ArrayVisualizer.getWrites().getRecursions();
        this.recDepth = ArrayVisualizer.getWrites().getRecursionDepth();
        if (!this.bigo)
            this.bigO = "";

        this.mainWriteCount = ArrayVisualizer.getWrites().getMainWrites();
        this.auxWriteCount = ArrayVisualizer.getWrites().getAuxWrites();

        this.auxAllocAmount = ArrayVisualizer.getWrites().getAllocAmount();

        int[] shadowarray    = ArrayVisualizer.getArray();
        int[] rawSegments    = this.findSegments(shadowarray, ArrayVisualizer.getCurrentLength(), ArrayVisualizer.reversedComparator());
        String plural = rawSegments[0] == 1 ? "" : "s";
        this.segments        = String.valueOf(rawSegments[1]) + "% Sorted (" + String.valueOf(rawSegments[0]) + " Segment" + plural + ")";
    }

    public void calculateBigO(BigInteger comps) {
        try {
            this.bigO = "Calculating Big O...";
            this.bigo = true;
            arrayv.updateNow();
            HashMap<String, BigInteger> x = access.generateAccessFunctions(comps);
            List<Map.Entry<String, BigInteger>> lst = new ArrayList<>();
            Set<Map.Entry<String, BigInteger>> y = x.entrySet();
            y.stream().forEach(lst::add);
            Collections.sort(lst, (a,b)->a.getValue().compareTo(b.getValue()));
            // System.out.println(lst);
            int j=0;
            while (j < lst.size() && lst.get(j).getValue().compareTo(comps) < 0) {
                j++;
            }
            if (j < lst.size() && comps.compareTo(lst.get(j).getValue().divide(BigInteger.TEN)) < 0) {
                j--;
            }
            MathContext n = new MathContext(4);
            BigDecimal i = new BigDecimal(comps);
            i = i.divide(new BigDecimal(lst.get(j).getValue()), n);
            i = i.round(n);
            this.bigO = "Big O \u2248 "+(i.toString())+"x O("+lst.get(j).getKey()+")";
            arrayv.updateNow();
        } catch(Exception dont) {
            this.bigO = "Big O \u2248 ---";
            arrayv.updateNow();
            return;
        }
    }

    public String getSortIdentity() {
        return this.sortCategory + ": " + this.sortHeading;
    }
    public static void resetAuxStats() {
        addedCounts.clear();
    }
    public ArrayList<String> parseMap() {
        ArrayList<String> strs = new ArrayList<>();
        for (Map.Entry<String, Long> stat : addedCounts.entrySet()) {
            long val = stat.getValue();
            strs.add(formatter.format(val) + " " + stat.getKey() + (val==1?"":"s"));
        }
        return strs;
    }
    public String getArrayLength() {
        return this.arrayLength + this.sortExtraHeading;
    }
    public String getBigO() {
        return this.bigO;
    }
    public String getSortDelay() {
        return this.sortDelay;
    }
    public String getVisualTime() {
        return this.visualTime;
    }
    public String getEstSortTime() {
        return this.estSortTime;
    }
    public String getComparisonCount() {
        return this.comparisonCount;
    }
    public String getSwapCount() {
        return this.swapCount;
    }
    public String getRecursionCount() {
        return this.recCount;
    }
    public String getRecursionDepth() {
        return this.recDepth;
    }
    public String getReversalCount() {
        return this.reversalCount;
    }
    public String getMainWriteCount() {
        return this.mainWriteCount;
    }
    public String getAuxWriteCount() {
        return this.auxWriteCount;
    }
    public String getAuxAllocAmount() {
        return this.auxAllocAmount;
    }

    public String getSegments() {
        return this.segments;
    }
}