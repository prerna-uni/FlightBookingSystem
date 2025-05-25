package com.flightapp.auth.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor

@Table(name="users") 

public class User {

@Id
@GeneratedValue(strategy=GenerationType.AUTO)
private Long id;

private String password;
private String email;
private String role;
private String fullName;
public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}
public String getRole() {
	return role;
}
public void setRole(String role) {
	this.role = role;
}
public String getFullName() {
	return fullName;
}
public void setFullName(String fullName) {
	this.fullName = fullName;
}
 
	// TODO Auto-generated method stub


}
