package com.dian.model.sys;

import static javax.persistence.GenerationType.IDENTITY;

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
@Table(name = "type", catalog = "xiaocx")
public class Type extends DianTypeParameter implements
		java.io.Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1123131313131L;
	private Long id;
	private String name;
	private String imageurl;
	private String groupid;
	private String groupname;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
	
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "name", nullable = false, length = 50)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "imageurl", nullable = false, length = 150)
	public String getImageurl() {
		return imageurl;
	}
	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
	
	@Column(name = "groupid", nullable = false, length = 20)
	public String getGroupid() {
		return groupid;
	}
	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}
	
	


	public String getGroupname() {
		return groupname;
	}


	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}


	@Override
	public String toString() {
		return "Type [id=" + id + ", name=" + name + ", imageurl=" + imageurl
				+ ", groupid=" + groupid + ", groupname=" + groupname + "]";
	}


	




}