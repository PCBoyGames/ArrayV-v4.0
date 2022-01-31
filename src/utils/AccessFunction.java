package utils;

import main.ArrayVisualizer;
import java.util.Map;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

// [Distray, I have no clue why this works, please help]
public class AccessFunction {
    private ArrayVisualizer arrayv;
    private int pow = 0;
    private ArrayList<String> generatedA;
    private ArrayList<BigInteger> generatedB;
    private BigDecimal last = BigDecimal.ONE;
    private static final HashMap<String, UnaryOperator<Double>> ENUM;
    private static final HashMap<String, UnaryOperator<Long>> SpecialAccesses;
    private static BigInteger factorial(int num) {
        BigInteger n = BigInteger.valueOf(num);
        int y = num-1;
        while(y>0)
            n = n.multiply(BigInteger.valueOf(y--));
        return n;
    }
    
    // prime stuff
    public static boolean isPrime(BigInteger num) {
        BigInteger two = BigInteger.valueOf(2l),
                   three = BigInteger.valueOf(3l),
                   five = BigInteger.valueOf(5l),
                   seven = BigInteger.valueOf(7l);
        return ((num.mod(two).compareTo(BigInteger.ZERO) != 0) && (num.mod(three).compareTo(BigInteger.ZERO) != 0) && (num.mod(five).compareTo(BigInteger.ZERO) != 0) && (num.mod(seven).compareTo(BigInteger.ZERO) != 0)) || 
               ((num.compareTo(two) == 0) || (num.compareTo(three) == 0) || (num.compareTo(five) == 0) || (num.compareTo(seven) == 0));
    }
    /*private static BigInteger prime(long loc) {
        long primeHits = 0;
        BigInteger n = BigInteger.valueOf(1l);
        while(true) {
            if(primeHits >= loc) {
                if(!isPrime(n))
                    n = n.subtract(BigInteger.ONE);
                return n;
            }
            n = n.add(BigInteger.ONE);
            if(isPrime(n))
                primeHits++;
        }
    }*/
    static {
        ENUM = new HashMap<String, UnaryOperator<Double>>();
        ENUM.put("", n -> 1d);
        ENUM.put(" cbrt n", n -> Math.pow(n, 1d/3d));
        ENUM.put(" log n", n -> Math.log(n));
        ENUM.put(" log2 n", n -> m.log(n, 2));
        ENUM.put(" sqrt n", n -> Math.sqrt(n));
        SpecialAccesses = new HashMap<String, UnaryOperator<Long>>();
        SpecialAccesses.put("log2 n!", n -> (long) m.flog2(factorial((int)(long)n)));
        SpecialAccesses.put("2^n", n -> (long)Math.pow(2, n));
        SpecialAccesses.put("n log3 n^2", n -> { // Distay's Access
            BigInteger bN = BigInteger.valueOf(n);
            int sz = m.flog(bN.pow(2), 3);
            return (long) bN.multiply(BigInteger.valueOf(sz)).intValue();
        });
    }
    public AccessFunction(ArrayVisualizer arrayVisualizer) {
        this.arrayv = arrayVisualizer;
        this.pow = 0;
        last = BigDecimal.ONE;
        this.generatedA = new ArrayList<>();
        this.generatedB = new ArrayList<>();
    }
    public HashMap<String, BigInteger> generateAccessFunctions(BigInteger upperBound){
        pow = 0;
        last = BigDecimal.ONE;
        generatedA.clear();
        generatedB.clear();
        Set<Map.Entry<String, UnaryOperator<Double>>> x = ENUM.entrySet();
        BigDecimal sqr = new BigDecimal(upperBound.multiply(upperBound));
        while(last.compareTo(sqr) < 0) {
            x.stream().forEach(t -> {
                int n = arrayv.getCurrentLength();
                UnaryOperator<Double> func = t.getValue();
                BigDecimal K = BigDecimal.valueOf(n).pow(pow);
                double Result = func.apply((double)n);
                BigDecimal bIntRes = new BigDecimal(Result);
                K = K.multiply(bIntRes);
                last = K;
                BigInteger k = K.toBigInteger();
                if(last.compareTo(BigDecimal.ONE) > 0) {
                    generatedA.add(((pow == 0 ? "" : pow == 1 ? "n" : ("n^"+pow)) + t.getKey()).trim());
                    generatedB.add(k);
                }
            });
            pow++;
        }
        // Due to the Ford Johnson access count (log2 n!) slowing the calculation down exponentially,
        // this is commented out.
        /*for(Map.Entry<String, UnaryOperator<Long>> Special : SpecialAccesses.entrySet()) {
            long n = arrayv.getCurrentLength();
            UnaryOperator<Long> func = Special.getValue();
            long Result = func.apply(n);
            BigInteger bIntRes = BigInteger.valueOf(Result);
            generatedA.add(Special.getKey());
            generatedB.add(bIntRes);
        }*/
        HashMap<String, BigInteger> theMap = new HashMap<String, BigInteger>();
        for(int i=0; i<generatedA.size(); i++) {
            theMap.put(generatedA.get(i), generatedB.get(i));
        }
        return theMap;
    }
}