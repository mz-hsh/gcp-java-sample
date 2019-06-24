package kr.co.pentacle;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

@Entity(name = "Post")
@EntityListeners(AuditingEntityListener.class)
@Data
public class Post {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	private String title;
	
	private String content;
	
	private String filename;
	
	private String originalFilename;
	
	@CreatedDate
	private LocalDateTime createdAt;
	
//	private String createdBy;
	
}
