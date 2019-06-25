package kr.co.pentacle;

import java.util.List;
import java.util.Optional;

public interface PostService {

	List<Post> findAll();
	
	Optional<Post> findById(Long id);
	
	Post save(Post post);

	void delete(Long id);
}
