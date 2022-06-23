package info.bstancham.bsjavautil;

import java.util.Arrays;

/**
 * <p>Static methods for manipulating arrays.</p>
 *
 * <p>All methods return a new array without modifying the input arrays.</p>
 *
 * <p>Don't use these methods for high performance jobs. They will certainly be
 * much slower than the methods in java.util.Arrays.</p>
 *
 * <p>TODO: unit testing</p>
 */
public final class BSArrays {

    private BSArrays() {} // prevent instantiation

    /**
     * @return A copy of the input array, with the order of the items
     * reversed.
     */
    public static <T> T[] reverse(T[] in) {
	T[] out = Arrays.copyOf(in, in.length);
	for (int i = 0; i < in.length; i++)
	    out[i] = in[in.length - 1 - i];
	return out;
    }

    /**
     * @return A copy of the input array, with {@code item} added at index
     * {@code i}.
     */
    public static <T> T[] addItemAt(T[] input, int i, T item) {
    	T[] output = Arrays.copyOf(input, input.length + 1);
	output[i] = item;
    	System.arraycopy(input, i, output, i + 1, input.length - i);
    	return output;
    }

    /**
     * @return A copy of the input array, with the item at the
     * specified index removed.
     */
    public static <T> T[] removeItemAt(T[] input, int index) {
	T[] out = Arrays.copyOf(input, input.length - 1);
	System.arraycopy(input, index + 1, out, index, out.length - index);
	return out;
    }

    /**
     * @return A copy of the input array, with the new item added to
     * the beginning.
     */
    public static <T> T[] prepend(T item, T[] input) {
    	T[] output = Arrays.copyOf(input, input.length + 1);
	output[0] = item;
    	System.arraycopy(input, 0, output, 1, input.length);
    	return output;
    }

    /**
     * @return A copy of the input array, with the new item added to
     * the end.
     */
    public static <T> T[] append(T[] array, T item) {
	T[] result = Arrays.copyOf(array, array.length + 1);
	result[array.length] = item;
	return result;
    }

    /**
     * @return A new array made up of the two input arrays joined
     * together.
     */
    public static <T> T[] concat(T[] first, T[] second) {
	T[] result = Arrays.copyOf(first, first.length + second.length);
	System.arraycopy(second, 0, result, first.length, second.length);
	return result;
    }

    /**
     * @return A new array made up of all of the input arrays joined
     * together.
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] concat(T[] first, T[]... rest) {
    	int totalLength = first.length;
    	for (T[] array : rest) {
    	    totalLength += array.length;
    	}
    	T[] result = Arrays.copyOf(first, totalLength);
    	int offset = first.length;
    	for (T[] array : rest) {
    	    System.arraycopy(array, 0, result, offset, array.length);
    	    offset += array.length;
    	}
    	return result;
    }

}
