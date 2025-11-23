package com.skupina1.accountmanagement.model;

enum Role{
    ADMIN,
    DRIVER,
    CUSTOMER
}

public class User{
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Location location;
    private String address;
    private Role role;

    public User(){}

    public User(String email, String password, String role){
        this.email = email;
        this.password = password;
        this.setRole(role);
    }

    public String getEmail() {return this.email;}
    public Location getLocation() {return this.location;}
    public String getAddress() {return this.address;}
    public String getFirstName() {return this.firstName;}
    public String getLastName() {return this.lastName;}
    public String getPassword() {return this.password;}
    public String getRole() {
        if(this.role == Role.ADMIN){
            return "ADMIN";
        }else if(this.role == Role.DRIVER){
            return "DRIVER";
        }else if(this.role == Role.CUSTOMER){
            return "CUSTOMER";
        }
        return "CUSTOMER";
    }

    public void setAddress(String address) {this.address = address;}
    public void setEmail(String email) {this.email = email;}
    public void setFirstName(String firstName) {this.firstName = firstName;}
    public void setLastName(String lastName) {this.lastName = lastName;}
    public void setLocation(Location location) {this.location = location;}
    public void setPassword(String password) {this.password = password;}
    public void setRole(String role) {
        if(role.equals("ADMIN")){
            this.role = Role.ADMIN;
        }else if(role.equals("DRIVER")){
            this.role = Role.DRIVER;
        }else
            this.role = Role.CUSTOMER;
    }
}
