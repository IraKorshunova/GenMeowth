package generator.exception;

/**
 * Thrown by the <code>setWeight, getWeight, addWeight</code> methods of an
 * <code>Graph</code> to indicate that this edge is no contains in graph.
 */
@SuppressWarnings("serial")
public class NoSuchEdgeException extends RuntimeException {
    /**
     * Constructs a <code>NoSuchEdgeException</code> with <tt>null</tt> as its
     * error message string.
     */
    public NoSuchEdgeException() {
	super();
    }

    /**
     * Constructs a <code>NoSuchEdgeException</code>, saving a reference to the
     * error message string <tt>s</tt> for later retrieval by the
     * <tt>getMessage</tt> method.
     * 
     * @param s the detail message.
     */
    public NoSuchEdgeException(String s) {
	super(s);
    }
}