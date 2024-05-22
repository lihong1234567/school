package com.example.entity;

import javax.persistence.*;

/**
 * 用户
 */
@Table(name = "user")
public class User extends Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	/** 用户名 */
	@Column(name = "username")
	private String username;
	/** 密码 */
	@Column(name = "password")
	private String password;
	/** 头像 */
	@Column(name = "avatar")
	private String avatar;
	/** 手机号 */
	@Column(name = "phone")
	private String phone;
	/** 性别 */
	@Column(name = "sex")
	private String sex;
	/** 年龄 */
	@Column(name = "age")
	private String age;
	/** 余额 */
	@Column(name = "account")
	private Double account;

	@Column(name = "studyNum")
	private String studyNum;

	@Column(name = "email")
	private String email;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public Double getAccount() {
		return account;
	}

	public void setAccount(Double account) {
		this.account = account;
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String getStudyNum() {
		return studyNum;
	}

	@Override
	public void setStudyNum(String studyNum) {
		this.studyNum = studyNum;
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public void setEmail(String email) {
		this.email = email;
	}
}
