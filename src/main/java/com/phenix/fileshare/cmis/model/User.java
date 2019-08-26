package com.phenix.fileshare.cmis.model;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="user")
public class User{


    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "user_name")
    private String userName;

    @NaturalId
    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "manager_id")
    private Long managerId;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "createdBy",  cascade = CascadeType.ALL)
    private Set<Document> documents = new HashSet<Document>();


    @ManyToMany(mappedBy = "shared_users")
    private Set<Document> shared_docs = new HashSet<Document>();

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "user_property" , joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "property_id")})
    private Set<UserProperty> properties = new HashSet<UserProperty>();

    public User(){

    }
    public User(String userName,String password){
        this.userName = userName;
        this.password = password;
    }
    public User(String userName,String email,String password){
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    @Override
    public boolean equals(Object obj) {
        User user = (User)obj;
        return userName.equals(user.getUserName())&&password.equals(user.getPassword());
    }

    public User(String userName, String password, Long managerId, Role role) {
        this.userName = userName;
        this.password = password;
        this.managerId = managerId;
        this.role = role;
    }

    public Set<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Set<UserProperty> getProperties() {
        return properties;
    }

    public Set<Document> getShared_docs() {
        return shared_docs;
    }

    public void setShared_docs(Set<Document> shared_docs) {
        this.shared_docs = shared_docs;
    }

    public void setProperties(Set<UserProperty> properties) {
        this.properties = properties;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
