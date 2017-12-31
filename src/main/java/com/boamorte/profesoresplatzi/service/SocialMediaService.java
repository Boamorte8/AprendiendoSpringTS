package com.boamorte.profesoresplatzi.service;

import java.util.List;

import com.boamorte.profesoresplatzi.model.SocialMedia;
import com.boamorte.profesoresplatzi.model.TeacherSocialMedia;

public interface SocialMediaService {

	void saveSocialMedia(SocialMedia socialMedia);
	
	void deleteSocialMedia(Long idSocialMedia);
	
	void deleteSocialMedia(SocialMedia socialMedia);
	
	void updateSocialMedia(SocialMedia socialMedia);
	
	List<SocialMedia> findAllSocialMedia();
	
	SocialMedia findById(Long idSocialMedia);
	
	SocialMedia findByName(String name);
	
	TeacherSocialMedia findByIdAndName(Long idSocialMedia, String nickname);
}
