package kr.co.pentacle;

import java.io.InputStream;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

public class GoogleStorageService implements StorageService {
	
	private final String BUCKET_NAME = "hsh-bucket";

	@SuppressWarnings("deprecation")
	@Override
	public String create(String name, String contentType, InputStream content) {
		Storage storage = StorageOptions.getDefaultInstance().getService();
		
		BlobId blobId = BlobId.of(BUCKET_NAME, name);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(contentType).build();
		Blob blob = storage.create(blobInfo, content);
		return blob.getMediaLink();
	}

}
