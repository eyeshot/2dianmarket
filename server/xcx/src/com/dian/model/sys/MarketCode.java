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
@Table(name = "market_code", catalog = "xiaocx")
public class MarketCode extends DianTypeParameter implements
		java.io.Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1123131313131L;
	private Long id;
	private String title;
	private String des;
	private String type;
	private String money;
	private String createtime;
	private String openid;
	private String resourceid;
	private String resourcename;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
	
	
	public void setId(Long id) {
		this.id = id;
	}


	@Column(name = "title")
	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "des")
	public String getDes() {
		return des;
	}


	public void setDes(String des) {
		this.des = des;
	}

	@Column(name = "type")
	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "money")
	public String getMoney() {
		return money;
	}


	public void setMoney(String money) {
		this.money = money;
	}


	@Column(name = "createtime")
	public String getCreatetime() {
		return createtime;
	}


	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}


	@Column(name = "openid")
	public String getOpenid() {
		return openid;
	}


	public void setOpenid(String openid) {
		this.openid = openid;
	}
	
	@Column(name = "resourceid")
	public String getResourceid() {
		return resourceid;
	}


	public void setResourceid(String resourceid) {
		this.resourceid = resourceid;
	}

	@Column(name = "resourcename")
	public String getResourcename() {
		return resourcename;
	}


	public void setResourcename(String resourcename) {
		this.resourcename = resourcename;
	}


	@Override
	public String toString() {
		return "MarketCode [id=" + id + ", title=" + title + ", des=" + des
				+ ", type=" + type + ", money=" + money + ", createtime="
				+ createtime + ", openid=" + openid + ", resourceid="
				+ resourceid + ", resourcename=" + resourcename + "]";
	}
	
	


	

}