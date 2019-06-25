package kr.co.pentacle;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
	
	private final PostRepository postRepository;

	@Override
	public List<Post> findAll() {
		return postRepository.findAll();
	}
	
	@Override
	public Optional<Post> findById(Long id) {
		return postRepository.findById(id);
	}

	@Override
	public Post save(Post post) {
		return postRepository.save(post);
	}

	@Override
	public void delete(Long id) {
		postRepository.deleteById(id);
	}

}
