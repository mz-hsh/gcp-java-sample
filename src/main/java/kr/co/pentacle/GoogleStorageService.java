package kr.co.pentacle;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gcp.storage.GoogleStorageResource;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class GoogleStorageService implements StorageService {
	
	@Value("${gcs-resource-post-bucket}")
	private String bucketName;
	
	private final ApplicationContext applicationContext;

//	@SuppressWarnings("deprecation")
//	@Override
//	public String create(String name, String contentType, InputStream content) {
//		Storage storage = StorageOptions.getDefaultInstance().getService();
//		
//		BlobId blobId = BlobId.of(bucketName, name);
//		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(contentType).build();
//		Blob blob = storage.create(blobInfo, content);
//		
//		return blob.getMediaLink();
//	}

	@Override
	public boolean delete(String filename) throws IOException {
		GoogleStorageResource resource = getResource(filename);
		return resource.getBlob().delete();
	}
	
	@Override
	public String createSignedUrl(String filename) throws IOException {
		GoogleStorageResource resource = getResource(filename);
		return resource.createSignedUrl(TimeUnit.HOURS, 1L).toString();
	}

	private GoogleStorageResource getResource(String filename) {
		GoogleStorageResource resource;
		try {
			resource = (GoogleStorageResource) applicationContext.getResource(String.format("gs://%s/%s", bucketName, URLEncoder.encode(filename, StandardCharsets.UTF_8.name()).replaceAll("\\+", "%20")));
		} catch (UnsupportedEncodingException e) {
			throw new StorageException(e);
		}
		return resource;
	}

	@Override
	public String store(MultipartFile file) {
		log.info("originalFilename: {}", file.getOriginalFilename());
		log.info("size: {}", file.getSize());
		log.info("contentType: {}", file.getContentType());
		String filename = file.getOriginalFilename() + "_" + System.currentTimeMillis();
		GoogleStorageResource resource = getResource(filename);
		
		try (OutputStream os = resource.getOutputStream()) {
			os.write(file.getBytes());
			os.flush();
			
			
		} catch (IOException e) {
			throw new StorageException("Failed to store file " + filename);
		}
		
		// Setting contentType
		try {
			resource.getBlob().toBuilder().setContentType(file.getContentType()).build().update();
		} catch (IOException e) {
			throw new StorageException("Failed to set contentType " + filename);
		}
		
		return filename;
	}
	
}
