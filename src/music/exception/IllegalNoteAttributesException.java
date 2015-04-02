package music.exception;

/**
 * Thrown by the <code>Note constructor, and setMethods </code> of an
 * <code>Note</code> to indicate that attributes is illegal.
 */
@SuppressWarnings("serial")
public class IllegalNoteAttributesException extends RuntimeException {
    /**
     * Constructs a <code>IllegalNoteAttributesException</code> with no detail
     * message.
     */
    public IllegalNoteAttributesException() {
	super();
    }

    /**
     * Constructs a <code>IllegalNoteAttributesException</code>, saving a
     * reference to the error message string <tt>s</tt> for later retrieval by
     * the <tt>getMessage</tt> method.
     * 
     * @param s the detail message.
     */
    public IllegalNoteAttributesException(String s) {
	super(s);
    }
}