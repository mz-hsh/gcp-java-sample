package kr.co.pentacle;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostTests {
	
	@Autowired
	private PostService postService;

	@Test
	public void findAll() {
		
		Post post = new Post();
		
		post.setTitle("제목");
		post.setContent("내용");
		
		postService.save(post);
		
		System.out.println(postService.findAll());
	}

}
