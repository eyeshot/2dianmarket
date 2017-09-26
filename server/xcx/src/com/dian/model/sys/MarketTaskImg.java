package com.dian.model.sys;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.dian.model.sys.param.DianTypeParameter;

/**
 * QueryLog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "market_taskimg", catalog = "xiaocx")
public class MarketTaskImg extends DianTypeParameter implements
		java.io.Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1123131313131L;
	private long id;
	private long mainid;
	private byte[] img;
	private String type;
	private String temppath;
	private String imgpath;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public long getId() {
		return id;
	}
	
	
	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "mainid", unique = false, nullable = false)
	public long getMainid() {
		return mainid;
	}


	public void setMainid(long mainid) {
		this.mainid = mainid;
	}

	@Column(name = "img", unique = false, nullable = false)
	public byte[] getImg() {
		return img;
	}


	public void setImg(byte[] img) {
		this.img = img;
	}

	@Column(name = "type", unique = false, nullable = false)
	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}
	
	

	@Column(name = "temppath", unique = false, nullable = false)
	public String getTemppath() {
		return temppath;
	}


	public void setTemppath(String temppath) {
		this.temppath = temppath;
	}

	@Column(name = "imgpath", unique = false, nullable = false)
	public String getImgpath() {
		return imgpath;
	}


	public void setImgpath(String imgpath) {
		this.imgpath = imgpath;
	}


	@Override
	public String toString() {
		return "MarketTaskImg [id=" + id + ", mainid=" + mainid + ", img="
				+ Arrays.toString(img) + ", type=" + type + "]";
	}
	

}