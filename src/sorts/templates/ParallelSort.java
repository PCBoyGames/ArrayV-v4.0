package sorts.templates;

import java.lang.reflect.Method;
import java.util.function.Function;

import main.ArrayVisualizer;

public abstract class ParallelSort extends Sort {
    protected ParallelSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }
    public class Func extends Thread {
        private Function<Object[],?>  z;
                Object[] n;
        public Func(Object... r) {
            n = r;
        }
        
        public Func setConsumer(Function<Object[],?> c) {
            z = c;
            return this;
        }
        
        public void run() {
            z.apply(n);
        }
    }
    public Object getMethod(String name, int param) {
        Method[] methodsInCls = this.getClass().getDeclaredMethods();
        for(Method i : methodsInCls) {
            if(i.getName() == name) {
                Class<?>[] params = i.getParameterTypes();
                if(params.length == param) {
                    return i;
                }
            }
        }
        return null;
    }
    public Object run(String name, Object[] data) {
        Method m = (Method) getMethod(name, data.length);
        if(m != null) {
            try {
                return m.invoke(this, data);
            } catch(Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}