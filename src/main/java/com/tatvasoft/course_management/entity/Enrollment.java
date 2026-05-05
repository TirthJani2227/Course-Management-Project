package com.tatvasoft.course_management.entity;

import java.time.LocalDateTime;

import com.tatvasoft.course_management.enums.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
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
@Table(indexes = {
		@Index(name = "enrollment_idx", columnList = "user_id, course_id", unique = true) })
public class Enrollment extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "course_id", nullable = false)
	private Course course;

	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private Status status;

	@Column(name = "enrolled_at", nullable = false)
	private LocalDateTime enrolledAt;

	@Column(name = "completed_at")
	private LocalDateTime completedAt;
}