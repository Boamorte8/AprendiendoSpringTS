package com.boamorte.profesoresplatzi.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="teacher_social_media")
public class TeacherSocialMedia implements Serializable {
	
	@Id
	@Column(name="id_teacher_social_media")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="id_teacher")
	private Teacher teacher;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="id_social_media")
	private SocialMedia socialmedia;
	
	@Column(name="nickname")			
	private String nickname;

	public TeacherSocialMedia() {
		super();
	}

	public TeacherSocialMedia(Teacher teacher, SocialMedia socialmedia, String nickname) {
		super();
		this.teacher = teacher;
		this.socialmedia = socialmedia;
		this.nickname = nickname;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public SocialMedia getSocialmedia() {
		return socialmedia;
	}

	public void setSocialmedia(SocialMedia socialmedia) {
		this.socialmedia = socialmedia;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	
}
