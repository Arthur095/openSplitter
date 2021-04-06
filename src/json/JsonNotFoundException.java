package json;

public class JsonNotFoundException extends Exception {

	private static final long serialVersionUID = 3596061170291285686L;

	public JsonNotFoundException(String message, Throwable cause) {
    	super(message, cause);
    }
    
    public JsonNotFoundException(String message) {
    	super(message);
    }

}
