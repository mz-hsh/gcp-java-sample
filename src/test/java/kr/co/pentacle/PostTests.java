package kr.co.pentacle;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class PostTests {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private PostService postService;
	
	@MockBean
	private StorageService storageService;

	@Test
	public void store() throws Exception {
		String title = "제목";
		String content = "내용";
		String originalFilename = "test.txt";
		String filename = originalFilename + "_" + System.currentTimeMillis();
		
		Post post = new Post();
		post.setId(1L);
		post.setTitle(title);
		post.setContent(content);
		post.setFilename(filename);
		post.setOriginalFilename(originalFilename);
		
		given(postService.save(any())).willReturn(post);
		given(storageService.store(any())).willReturn(filename);
		
		MockMultipartFile file = new MockMultipartFile("file", originalFilename, "text/plain", "test".getBytes());
		
		ResultActions resultActions = mvc.perform(
				MockMvcRequestBuilders.multipart("/posts")
					.file(file)
					.param("title", title)
					.param("content", content));
		
		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("id").value(1L))
			.andExpect(jsonPath("title").value(title))
			.andExpect(jsonPath("content").value(content))
			.andExpect(jsonPath("filename").value(filename))
			.andExpect(jsonPath("originalFilename").value(originalFilename));
		
		then(storageService).should().store(file);
		
	}

}
