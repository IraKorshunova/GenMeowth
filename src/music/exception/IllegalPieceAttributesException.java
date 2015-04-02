package music.exception;

/**
 * Thrown by the <code>Piece constructor, and setMethods </code> of an
 * <code>Piece</code> to indicate that attributes is illegal.
 */
@SuppressWarnings("serial")
public class IllegalPieceAttributesException extends RuntimeException {
    /**
     * Constructs a <code>IllegalPieceAttributesException</code> with no detail
     * message.
     */
    public IllegalPieceAttributesException() {
	super();
    }

    /**
     * Constructs a <code>IllegalPieceAttributesException</code>, saving a reference to
     * the error message string <tt>s</tt> for later retrieval by the
     * <tt>getMessage</tt> method.
     * 
     * @param s the detail message.
     */
    public IllegalPieceAttributesException(String s) {
	super(s);
    }
}