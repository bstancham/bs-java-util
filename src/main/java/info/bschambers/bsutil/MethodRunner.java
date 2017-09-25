package info.bschambers.bsutil;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.InvocationTargetException;

/**
 * <p>TODO: Parameters to be checked</p>
 * <p>TODO: Support for methods with return values</p>
 */
public class MethodRunner {

    private Object object;
    private Method method;

    public MethodRunner(Object o, String methodName, Class[] argsTypes) {
	object = o;
	// System.out.println("MethodRunner (CONSTRUCTOR): OBJECT=" + object + " METHOD-NAME=" + methodName + " ...");
	// get method
	try {
            method = object.getClass().getMethod(methodName, argsTypes);
	} catch (Exception e) {
            e.printStackTrace();
        }
	// this will cause an exception if method didn't initialize properly
	// System.out.println("... METHOD=" + method);
    }

    public Method getMethod() { return method; }

    /**
     * @param args Arg types must match the types passed into the constructor...
     */
    public void run(Object[] args) {
	// System.out.println(this);
	try {
	    // System.out.println("MethodRunner.run() ---> method=" + method);
	    method.invoke(object, args);
	} catch (InvocationTargetException e) {
	    e.getTargetException().printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    @Override
    public String toString() { return "<MethodRunner: " + method + ">"; }

}
