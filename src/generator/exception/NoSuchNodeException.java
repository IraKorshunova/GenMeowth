package generator.exception;

/**
 * Thrown by the <code>add</code> method of an <code>Graph</code> to indicate
 * that this node is no contains in graph.
 */
@SuppressWarnings("serial")
public class NoSuchNodeException extends RuntimeException {
    /**
     * Constructs a <code> NoSuchNodeException</code> with <tt>null</tt> as its
     * error message string.
     */
    public NoSuchNodeException() {
	super();
    }

    /**
     * Constructs a <code>NoSuchNodeException</code>, saving a reference to the
     * error message string <tt>s</tt> for later retrieval by the
     * <tt>getMessage</tt> method.
     * 
     * @param s the detail message.
     */
    public NoSuchNodeException(String s) {
	super(s);
    }
}
