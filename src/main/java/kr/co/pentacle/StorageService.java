package kr.co.pentacle;

import java.io.IOException;
import java.io.InputStream;

public interface StorageService {

	String create(String name, String contentType, InputStream content);
	
	String create(String name, String contentType, byte[] bytes) throws IOException;
	
	boolean delete(String filename) throws IOException;
	
	String createSignedUrl(String filename) throws IOException;
}
