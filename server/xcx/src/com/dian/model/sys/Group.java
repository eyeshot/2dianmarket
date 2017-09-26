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
@Table(name = "group", catalog = "xiaocx")
public class Group extends DianTypeParameter implements
		java.io.Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1123131313131L;
	private int id;
	private String name;
	private String order;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return id;
	}
	
	
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name = "name", nullable = false, length = 50)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "order", nullable = false, length = 150)
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}


	@Override
	public String toString() {
		return "Group [id=" + id + ", name=" + name + ", order=" + order + "]";
	}


}