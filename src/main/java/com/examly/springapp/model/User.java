
package com.examly.springapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

@Entity
public class User {
@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
private long userId;

private String email;
private String password;
private String username;
private String mobileNumber;
private String userRole;//(Manager or Co-ordinator)
}
