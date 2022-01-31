package sorts.bogo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;
import sorts.templates.Sort;

class Pair<T, U> { // wrapper
    public T a;
    public U b;
    public Pair(T a, U b) {
        this.a = a;
        this.b = b;
    }
}
final public class PathOwOgenSort extends BogoSorting {
    public PathOwOgenSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("PathOwOgen");
        this.setRunAllSortsName("PathOwOgen Sort");
        this.setRunSortName("PathOwOgen Sort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(16);
        this.setBogoSort(true);
        this.setPathogenic(true);
        this.setPathogenName("STG-22 Alpha");
    }
    private static PathOwOgenSort self;
    private static final Random r = new Random();
    private static char[] punct = new char[] {
        '!', '?', '.'
    };
    private static String[] owo = new String[] {
        "OwO", "UwU", "-w-", "OvO", "OnO", "(\u00b4\u30fb\u03c9\u30fb\uff40)" // denko??? what happened to you?!
    };
    private static HashMap<String, String> maps = new HashMap<>();
    @SuppressWarnings("unchecked")
    // it messi (todo: refactor the whole thing)
    private static Pair<Pattern, Function<MatchResult, String>>[] owos = new Pair[] {
            new Pair<Pattern, Function<MatchResult, String>>(
                    Pattern.compile("([rl])([eau]|\\s)", Pattern.CASE_INSENSITIVE),
                    x -> x.group().replaceAll("([RL])", "W$1")
                                  .replaceAll("([rl])", "w$1")
                ),
            new Pair<Pattern, Function<MatchResult, String>>(
                    Pattern.compile("([rl])(?=[iowrl]+)", Pattern.CASE_INSENSITIVE),
                    x -> x.group().replaceAll("[RL]", "W")
                                  .replaceAll("[rl]", "w")
                ),
            new Pair<Pattern, Function<MatchResult, String>>(
                    Pattern.compile("([cnmj]+)(?=[aeiou])", Pattern.CASE_INSENSITIVE),
                    x -> {
                        String g = x.group();
                        return g+(g.substring(g.length()-1) ==
                                  g.substring(g.length()-1).toUpperCase() ?
                                "Y" : "y");
                    }
                ),
            new Pair<Pattern, Function<MatchResult, String>>(
                    Pattern.compile(".th.", Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
                    x -> {
                        String g = x.group();
                        boolean left = g.matches("^\\W"),
                                right = g.matches("\\W$");
                        if(right || (!right&&!left))
                            return g.replaceFirst("t[Hh]", "f").
                                    replaceFirst("T[Hh]", "F");
                        return g.replaceFirst("t[Hh]", "d").
                                replaceFirst("T[Hh]", "D");
                    }
                ),
            new Pair<Pattern, Function<MatchResult, String>>(
                    Pattern.compile("s[ot]", Pattern.CASE_INSENSITIVE),
                    x -> {
                        String g = x.group();
                        return g.replaceFirst("s([ot])", "sh$1")
                                .replaceFirst("S([OT])", "SH$1")
                                .replaceFirst("S([ot])", "Sh$1")
                                .replaceFirst("s([OT])", "sH$1");
                    }
                ),
            new Pair<Pattern, Function<MatchResult, String>>(
                    Pattern.compile("[fg][aeiou]", Pattern.CASE_INSENSITIVE),
                    x -> {
                        String g = x.group();
                        return g.replaceFirst("([fg])", "$1w")
                                .replaceFirst("([FG])", "$1W");
                    }
                ),
            new Pair<Pattern, Function<MatchResult, String>>(
                    Pattern.compile("([eiou]t)|(ar)", Pattern.CASE_INSENSITIVE),
                    x -> {
                        String g = x.group();
                        return g.replaceFirst("([tr])", "w$1")
                                .replaceFirst("([TR])", "W$1");
                    }
                ),
            new Pair<Pattern, Function<MatchResult, String>>(
                    Pattern.compile("([\\.\\!\\?]+|$)", Pattern.MULTILINE),
                    x -> {
                        String g = x.group();
                        if(!g.matches("[\\.\\!\\?]+")) {
                            int bound = r.nextInt(3)+2,
                                punc = r.nextInt(punct.length),
                                uwu = r.nextInt(owo.length);
                            String endPunct = "";
                            for(int i=0; i<bound; i++) {
                                endPunct += punct[punc];
                            }
                            return g + endPunct + " " + owo[uwu];
                        } else {
                            int uwu = r.nextInt(owo.length);
                            return g + " " + owo[uwu];
                        }
                    }
                ),
    };
    
    private static String why(String output) {
        if(output == null) return null;
        if(maps.containsKey(output))
            return maps.get(output);
        String og = output;
        for(Pair<Pattern, Function<MatchResult, String>> i : owos) {
            // Eclipse was whining about something that shouldn't have happened
            // (tried to use the Function version of the replaceAll method,
            // but apparently, the String version is the only one that Eclipse
            // recognizes), so this is what I have to do
            Matcher stubborn = i.a.matcher(output);
            Method func;
            try {
                func = stubborn.getClass().getMethod("replaceAll", Function.class);
                output = (String) func.invoke(stubborn, i.b);
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        maps.put(og, output);
        return output;
    }
    private static Sort[] wrapsCompare, wrapsDistr;
    public static class Sowort extends Sort {
        private Sort wrapped;
        public Sowort(ArrayVisualizer arrayVisualizer) {
            super(arrayVisualizer);
        }
        public Sowort(ArrayVisualizer arrayVisualizer, int index, boolean compare) {
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
            return why(wrapped.getSortListName());
        }
        public String getRunAllSortsName() {
            return why(wrapped.getRunAllSortsName());
        }
        public String getRunSortName() {
            return why(wrapped.getRunSortName());
        }
        public String getCategory() {
            return why(wrapped.getCategory());
        }
        public String getQuestion() {
            return why(wrapped.getQuestion());
        }
        public boolean isPathogenic() {
            return false; // They're already infected. Everything is a pathgen. Why even bother?
        }
        @Override
        public void runSort(int[] array, int length, int buckets) {
            owoS(array,length,buckets);
        }
    }
    
    private void infect() {
        ArrayList<Sort> sorts0 = arrayVisualizer.getSortAnalyzer().comparisonSorts,
                        sorts1 = arrayVisualizer.getSortAnalyzer().distributionSorts;
        wrapsCompare = new Sort[sorts0.size()];
        wrapsDistr = new Sort[sorts1.size()];

        for(int i=0; i<sorts0.size(); i++) {
            Sowort s = new Sowort(arrayVisualizer);
            s.wrapped = sorts0.get(i);
            s.instantiate();
            sorts0.set(i, s);
        }
        for(int i=0; i<sorts1.size(); i++) {
            Sowort s = new Sowort(arrayVisualizer);
            s.wrapped = sorts1.get(i);
            s.instantiate();
            sorts1.set(i, s);
        }
        arrayVisualizer.getSortAnalyzer().sortSorts();
        arrayVisualizer.refreshSorts();
        for(int i=0; i<sorts0.size(); i++)
            wrapsCompare[i] = ((Sowort) sorts0.get(i)).wrapped;
        for(int i=0; i<sorts1.size(); i++)
            wrapsDistr[i] = ((Sowort) sorts1.get(i)).wrapped;
    }
    private void drunkDrunk(int[] array, int start, int end) {
        boolean invert = false;
        final int slow = 128;
        while(!isRangeSorted(array, start, end)) {
            for(int i=start, m=0, max=start; m<slow*(end-start) && i<end-1; m++) {
                if(invert ^ (Reads.compareValues(array[i], array[i+1]) == 1)) {
                    Writes.swap(array, i, i+1, 1, true, false);
                } else {
                    invert = randBoolean();
                    break;
                }
                i += randBoolean() ? 1 : -1;
                if(i < start)
                    i = max;
                if(i > max)
                    max = i;
                drunkDrunk(array, i, max);
            }
        }
    }
    public static void owoS(int[] array, int length, int buckets) {
        self.drunkDrunk(array, 0, length);
    }
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        if (this.getRunSortName() == "PathOwOgen Sort") System.err.println("Do mew wegwet it yet, nya? >w<");
        infect();
        self = this;
        owoS(array, currentLength, bucketCount);
    }
}