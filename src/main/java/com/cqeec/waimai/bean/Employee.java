package com.cqeec.waimai.bean;


import java.sql.Timestamp;

public class Employee {

  private long id;
  private String name;
  private String username;
  private String password;
  private String phone;
  private String sex;
  private String id_number;
  private long status;
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


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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


  public String getId_number() {
    return id_number;
  }

  public void setId_number(String id_number) {
    this.id_number = id_number;
  }


  public long getStatus() {
    return status;
  }

  public void setStatus(long status) {
    this.status = status;
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
