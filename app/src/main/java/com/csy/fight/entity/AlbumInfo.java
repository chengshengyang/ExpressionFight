package com.csy.fight.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 相册对象实体类的封装
 * @author chengsy
 *
 */
public class AlbumInfo implements Serializable {

	private String sAlbumName = "";
	private List<PhotoInfo> mPhotoList;
	
	public String getAlbumName() {
		return sAlbumName;
	}
	
	public void setAlbumName(String name) {
		this.sAlbumName = name;
	}
	
	public List<PhotoInfo> getPhotoList() {
		return mPhotoList;
	}
	
	public void setPhotoList(List<PhotoInfo> photoList) {
		this.mPhotoList = photoList;
	}
	
}
