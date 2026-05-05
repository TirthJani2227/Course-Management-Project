package com.tatvasoft.course_management.entity;

import com.tatvasoft.course_management.enums.Department;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(indexes = { @Index(name = "course_idx", columnList = "id, name, department", unique = true) })
public class Course extends BaseEntity {
	@Column(nullable = false,length = 150)
	private String name;
	
	@Column(nullable = false,length = 255)
	private String content;

	@Column(nullable = false)
	private Integer credits;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 50)
	private Department department;
}
