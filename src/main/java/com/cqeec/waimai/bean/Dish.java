package com.cqeec.waimai.bean;


import java.sql.Timestamp;

public class Dish {

  private long id;
  private String name;
  private long category_id;
  private double price;
  private String code;
  private String image;
  private String description;
  private long status;
  private long sort;
  private Timestamp create_time;
  private Timestamp update_time;
  private long create_user;
  private long update_user;
  private long is_deleted;

   private  Category category;  // 关联字段（关联分类数据）
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public long getCategory_id() {
    return category_id;
  }

  public void setCategory_id(long category_id) {
    this.category_id = category_id;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }


  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }


  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }


  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  public long getStatus() {
    return status;
  }

  public void setStatus(long status) {
    this.status = status;
  }


  public long getSort() {
    return sort;
  }

  public void setSort(long sort) {
    this.sort = sort;
  }


  public Timestamp getCreate_time() {
    return create_time;
  }

  public void setCreate_time(Timestamp create_time) {
    this.create_time = create_time;
  }


  public Timestamp getUpdate_time() {
    return update_time;
  }

  public void setUpdate_time(Timestamp update_time) {
    this.update_time = update_time;
  }


  public long getCreate_user() {
    return create_user;
  }

  public void setCreate_user(long create_user) {
    this.create_user = create_user;
  }


  public long getUpdate_user() {
    return update_user;
  }

  public void setUpdate_user(long update_user) {
    this.update_user = update_user;
  }


  public long getIs_deleted() {
    return is_deleted;
  }

  public void setIs_deleted(long is_deleted) {
    this.is_deleted = is_deleted;
  }

}
