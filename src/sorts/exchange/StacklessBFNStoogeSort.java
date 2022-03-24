package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.BestForNSorting;
import utils.Statistics;

final public class StacklessBFNStoogeSort extends BestForNSorting {

    int maxtouse;

    public StacklessBFNStoogeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Stackless Best For N Stooge");
        this.setRunAllSortsName("Stackless Best For N Stooge Sort");
        this.setRunSortName("Stackless Best For N Stooge Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Enter the run length for this sort:\n(Non-2*n will be rounded up)", 64);
    }
    
    protected int digit(int val, int power, int radix) {
    	while(power-- > 0 && val > 0) {
    		val /= radix;
    	}
    	return val % radix;
    }
    
    protected int log2(int val) {
    	int log = -1;
    	while(val > 0) {
    		log++;
    		val /= 2;
    	}
    	return log;
    }
    
    protected void bestforN(int[] array, int start, int end) {
    	if(end-start < maxtouse) {
    		initNetwork(array, start, end-start);
    		return;
    	}
    	int depth = log2(end-start+1) - log2(maxtouse) - 1,
    		iterations = 6, i = depth;
    	while(i-- > 0) {
    		iterations *= 6;
    	}
    	for(int k=0; k<iterations; k++) {
    		int a = start, b = end;
    		for(int digit = depth; b-a > maxtouse && digit >= 0; digit--) {
    			Highlights.markArray(1, a);
    			Highlights.markArray(2, b);
    			Delays.sleep(2.5);
    			switch(digit(k, digit, 6)) {
    			case 0: case 3:
    				b = a + (b - a) / 2;
    				break;
    			case 1: case 4:
    				a += (b - a) / 2;
    				break;
    			case 2: case 5:
    				int half = (b - a) / 2;
    				a += (b - a) / 4;
    				b = a + half;
    				break;
    			}
    		}
			Highlights.markArray(1, a);
			Highlights.markArray(2, b);
			Delays.sleep(2.5);
    		initNetwork(array, a, b - a);
    	}
    }
    
    public void bestForNWrapper(int[] array, int start, int end, int base) {
        maxtouse = base;
    	if(end-start < maxtouse) {
    		initNetwork(array, start, end-start);
    		return;
    	}
        int test = Integer.MIN_VALUE >>> 1;
        int len = end - start, offset = 0;
        while(test > 0 && len > maxtouse) {
        	if((len & test) > 0) {
            	if(test < maxtouse) {
            		initNetwork(array, start + offset, len - offset);
            		break;
            	}
        		bestforN(array, start + offset, start + offset + test);
        		offset += test;
        		len -= test;
        	}
        	test >>>= 1;
        }
        int reversal = Math.max(test, 1);
        while(reversal <= Integer.MIN_VALUE >>> 1) {
        	if((offset & reversal) > 0) {
        		int offset2 = len - reversal;
        		bestforN(array, start + offset + offset2 / 2, start + offset + reversal + offset2 / 2);
        		bestforN(array, start + offset, start + offset + reversal);
        		bestforN(array, start + offset + offset2, start + offset + len + reversal);
        		bestforN(array, start + offset + offset2 / 2, start + offset + reversal + offset2 / 2);
        		len += reversal;
        		offset -= reversal;
        	}
        	reversal <<= 1;
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
        bestForNWrapper(array, 0, currentLength, base);
    }
}
