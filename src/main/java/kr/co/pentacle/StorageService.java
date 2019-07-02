package kr.co.pentacle;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

	String store(MultipartFile file);
	
	boolean delete(String filename) throws IOException;
	
	String createSignedUrl(String filename) throws IOException;
	
}
