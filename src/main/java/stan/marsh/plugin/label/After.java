package stan.marsh.plugin.label;

import java.lang.reflect.Method;

public interface After {
    void after(Object returnValue, Object[] args) throws Throwable;
}
