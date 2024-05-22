package com.example.entity;

import javax.persistence.*;

/**
 * 角色用户父类
 */
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /** 昵称 */
    @Column(name = "username")
    private String username;
    /** 学号 */
    @Transient
    private String studyNum;
    /** 手机号 */
    @Column(name = "phone")
    private String phone;
    /** 邮箱 */
    @Transient
    private String email;
    /** 性别 */
    @Column(name = "sex")
    private String sex;
    /** 密码 */
    @Column(name = "password")
    private String password;
    /** 角色标识 */
    @Column(name = "role")
    private Integer role;
    /** 新密码 */
    @Transient
    private String nmm;
    /** 头像 */
    @Column(name = "avatar")
    private String avatar;
    /** 验证码 */
    @Transient
    private String verCode;
    public String getVerCode() {
        return verCode;
    }

    public void setVerCode(String verCode) {
        this.verCode = verCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getNmm() {
        return nmm;
    }

    public void setNmm(String nmm) {
        this.nmm = nmm;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getStudyNum() {
        return studyNum;
    }

    public void setStudyNum(String studyNum) {
        this.studyNum = studyNum;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
