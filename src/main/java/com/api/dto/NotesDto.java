package com.api.dto;

import java.util.Date;
import com.api.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NotesDto {

	private Integer id;
	private String title;
	private String description;
	private CategoryDto category;
	private Integer createdBy;
	private Date createdOn;
	private Integer updatedBy;
	private Date updatedOn;

	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	@Setter
	public static class CategoryDto {
		private Integer id;
		private String name;
	}
}
