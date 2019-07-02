package kr.co.pentacle;

@SuppressWarnings("serial")
public class StorageException extends RuntimeException {
	
	public StorageException(String message) {
		super(message);
	}
	
	public StorageException(Throwable cause) {
		super(cause);
	}
	
}
