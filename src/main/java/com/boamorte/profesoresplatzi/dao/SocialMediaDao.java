package com.boamorte.profesoresplatzi.dao;

import java.util.List;

import com.boamorte.profesoresplatzi.model.SocialMedia;
import com.boamorte.profesoresplatzi.model.TeacherSocialMedia;

public interface SocialMediaDao {

	void saveSocialMedia(SocialMedia socialMedia);
	
	void deleteSocialMedia(Long idSocialMedia);
	
	void deleteSocialMedia(SocialMedia SocialMedia);
	
	void updateSocialMedia(SocialMedia SocialMedia);
	
	List<SocialMedia> findAllSocialMedia();
	
	SocialMedia findById(Long idSocialMedia);
	
	SocialMedia findByName(String name);
	
	TeacherSocialMedia findByIdAndName(Long idSocialMedia, String nickname);
}
