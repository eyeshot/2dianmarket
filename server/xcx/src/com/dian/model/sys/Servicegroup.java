package com.dian.model.sys;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.dian.model.sys.param.DianTypeParameter;

/**
 * Servicegroup entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "servicegroup", catalog = "xiaocx")
public class Servicegroup extends DianTypeParameter implements
		java.io.Serializable {

	// Fields

	private Integer id;
	private String name;
	private Integer sortnum;

	// Constructors

	/** default constructor */
	public Servicegroup() {
	}

	/** full constructor */
	public Servicegroup(String name, Integer sortnum) {
		this.name = name;
		this.sortnum = sortnum;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "name", length = 200)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "sortnum")
	public Integer getSortnum() {
		return this.sortnum;
	}

	public void setSortnum(Integer sortnum) {
		this.sortnum = sortnum;
	}

}