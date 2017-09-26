package com.dian.model.sys;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;



@Entity  
@Table(name="payorder") 
public class Payorder implements Serializable{


	private static final long serialVersionUID = 1131039236L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String openid;
	private String appid;
	private String nickname;
	private String avatarurl;
	private String body;
	private String ordernum;
	private String totalfee;
	private String createip;
	private String createtime;
	private String tradetype;
	private String status;
	private String failreason;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getAvatarurl() {
		return avatarurl;
	}
	public void setAvatarurl(String avatarurl) {
		this.avatarurl = avatarurl;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getOrdernum() {
		return ordernum;
	}
	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}
	public String getTotalfee() {
		return totalfee;
	}
	public void setTotalfee(String totalfee) {
		this.totalfee = totalfee;
	}
	public String getCreateip() {
		return createip;
	}
	public void setCreateip(String createip) {
		this.createip = createip;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getTradetype() {
		return tradetype;
	}
	public void setTradetype(String tradetype) {
		this.tradetype = tradetype;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getFailreason() {
		return failreason;
	}
	public void setFailreason(String failreason) {
		this.failreason = failreason;
	}
	@Override
	public String toString() {
		return "Payorder [id=" + id + ", openid=" + openid + ", appid=" + appid
				+ ", nickname=" + nickname + ", avatarurl=" + avatarurl
				+ ", body=" + body + ", ordernum=" + ordernum + ", totalfee="
				+ totalfee + ", createip=" + createip + ", createtime="
				+ createtime + ", tradetype=" + tradetype + ", status="
				+ status + ", failreason=" + failreason + "]";
	}
	
}
