package com.cqeec.waimai.bean;


import java.sql.Timestamp;
import java.util.Date;

public class Category {

  private long id;
  private long type;
  private String name;
  private long sort;
  private Timestamp create_time;
  private Timestamp update_time;
  private long create_user;
  private long update_user;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public long getType() {
    return type;
  }

  public void setType(long type) {
    this.type = type;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

}
