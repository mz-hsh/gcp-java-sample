package kr.co.pentacle;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gcp.storage.GoogleStorageResource;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {
	
	private final PostService postService;
	
	private final ApplicationContext applicationContext;
	
	@Value("${gcs-resource-post-bucket}")
	private String bucketName;
	
	@GetMapping
	public List<Post> list() {
		return postService.findAll();
	}
	
	@PostMapping
	public Post post(@RequestParam(name = "file", required = false) MultipartFile file, @Valid Post post) throws IOException {
		
		if (file != null) {
			String filename = file.getOriginalFilename() + "_" + System.currentTimeMillis();
			log.info("originalFilename: {}", file.getOriginalFilename());
			log.info("filename: {}", file.getOriginalFilename());
			log.info("size: {}", file.getSize());
			log.info("contentType: {}", file.getContentType());
			
			GoogleStorageResource resource = (GoogleStorageResource) applicationContext.getResource(String.format("gs://%s/%s", bucketName, URLEncoder.encode(filename, StandardCharsets.UTF_8.name()).replaceAll("\\+", "%20")));
			
			try (OutputStream os = resource.getOutputStream()) {
				os.write(file.getBytes());
			}
			
			// Setting contentType
			resource.getBlob().toBuilder().setContentType(file.getContentType()).build().update();
			
			post.setFilename(filename);
			post.setOriginalFilename(file.getOriginalFilename());
		}
		
		postService.save(post);
		
		return post;
	}

}
