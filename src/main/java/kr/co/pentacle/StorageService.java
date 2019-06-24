package kr.co.pentacle;

import java.io.InputStream;

public interface StorageService {

	String create(String name, String contentType, InputStream content);
}
