package com.example.entity;

import javax.persistence.*;

/**
 * 商品管理
 */
@Table(name = "goods")
public class Goods {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	/** 商品图片 */
	@Column(name = "img")
	private String img;
	/** 商品名称 */
	@Column(name = "name")
	private String name;
	/** 商品分类 */
	@Transient
	@Column(name = "type")
	private String type;
	/** 出售状态 */
	@Column(name = "status")
	private String status;
	/** 商品价格 */
	@Column(name = "price")
	private Double price;
	/** 商品描述 */
	@Column(name = "description")
	private String description;
	/** 商品分类Id */
	@Column(name = "typeId")
	private Integer typeId;
	/** 发布人Id */
	@Column(name = "userId")
	private Integer userId;
	/** 发布人姓名 */
	@Column(name = "userName")
	private String userName;
	/** 发布人电话 */
	@Column(name = "telePhone")
	private String telePhone;
	/** 收藏数 */
	@Column(name = "collection")
	private Integer collection;

	/** 成色 */
	@Column(name = "almostNew")
	private String almostNew;


	/** 品牌 */
	@Column(name = "brand")
	private String brand;


	/** 上架状态 */
	@Column(name = "onStatus")
	private String onStatus;

	/** 规格 */
	@Column(name = "specifications")
	private String specifications;

	/** 商品数量 */
	@Column(name = "num")
	private Integer num;
	/** 商品视频 */
	@Column(name = "goodsVideo")
	private String goodsVideo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTelePhone() {
		return telePhone;
	}

	public void setTelePhone(String telePhone) {
		this.telePhone = telePhone;
	}

	public Integer getCollection() {
		return collection;
	}

	public void setCollection(Integer collection) {
		this.collection = collection;
	}

	public String getAlmostNew() {
		return almostNew;
	}

	public void setAlmostNew(String almostNew) {
		this.almostNew = almostNew;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getOnStatus() {
		return onStatus;
	}

	public void setOnStatus(String onStatus) {
		this.onStatus = onStatus;
	}

	public String getSpecifications() {
		return specifications;
	}

	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getGoodsVideo() {
		return goodsVideo;
	}

	public void setGoodsVideo(String goodsVideo) {
		this.goodsVideo = goodsVideo;
	}
}
