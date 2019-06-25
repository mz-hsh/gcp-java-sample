package kr.co.pentacle;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gcp.storage.GoogleStorageResource;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class GoogleStorageService implements StorageService {
	
	@Value("${gcs-resource-post-bucket}")
	private String bucketName;
	
	private final ApplicationContext applicationContext;

	@SuppressWarnings("deprecation")
	@Override
	public String create(String name, String contentType, InputStream content) {
		Storage storage = StorageOptions.getDefaultInstance().getService();
		
		BlobId blobId = BlobId.of(bucketName, name);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(contentType).build();
		Blob blob = storage.create(blobInfo, content);
		
		return blob.getMediaLink();
	}
	
	@Override
	public String create(String name, String contentType, byte[] bytes) throws IOException {
		String filename = name + "_" + System.currentTimeMillis();
		
		GoogleStorageResource resource = getResource(filename);
		
		try (OutputStream os = resource.getOutputStream()) {
			os.write(bytes);
		}
		
		// Setting contentType
		resource.getBlob().toBuilder().setContentType(contentType).build().update();
		
		return filename;
	}

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

	private GoogleStorageResource getResource(String filename) throws UnsupportedEncodingException {
		GoogleStorageResource resource = (GoogleStorageResource) applicationContext.getResource(String.format("gs://%s/%s", bucketName, URLEncoder.encode(filename, StandardCharsets.UTF_8.name()).replaceAll("\\+", "%20")));
		return resource;
	}
	
}
