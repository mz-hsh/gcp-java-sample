package kr.co.pentacle;

import java.util.List;

public interface PostService {

	List<Post> findAll();
	
	Post save(Post post);
}
