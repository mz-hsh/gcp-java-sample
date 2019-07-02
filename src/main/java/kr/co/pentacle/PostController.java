package kr.co.pentacle;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {
	
	private final PostService postService;
	
	private final StorageService storageService;
	
	@GetMapping
	public List<Post> list() {
		return postService.findAll();
	}
	
	@PostMapping
	public Post post(@RequestParam(name = "file", required = false) MultipartFile file, @Valid Post post) {
		if (file != null && !file.getOriginalFilename().equals("")) {
			String filename = storageService.store(file);
			
			post.setFilename(filename);
			post.setOriginalFilename(file.getOriginalFilename());
		}
		
		return postService.save(post);
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		postService.findById(id).ifPresent(post -> {
			try {
				if (post.getFilename() != null) {
					storageService.delete(post.getFilename());
				}
			} catch (IOException e) {
				log.error("Failed to delete file " + post.getFilename(), e);
			}
		});
		
		postService.delete(id);
	}
	
	@GetMapping("/download/{filename}")
	public RedirectView download(@PathVariable String filename) throws IOException {
		String signedUrl = storageService.createSignedUrl(filename);
		return new RedirectView(signedUrl);
	}

}
