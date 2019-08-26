package com.phenix.fileshare.cmis.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="property")
public class UserProperty {
    @Id
    @Column(name = "property_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long propertyId;

    @Column(name = "property_name")
    private String propertyName;

    @Column(name = "property_description")
    private String propertyDescription;

    @ManyToMany(mappedBy = "properties")
    private Set<User> users = new HashSet<User>();

    @ManyToMany(mappedBy = "properties")
    private Set<Role> roles = new HashSet<Role>();

    public UserProperty(String propertyName, String propertyDescription) {
        this.propertyName = propertyName;
        this.propertyDescription = propertyDescription;
    }

    public UserProperty() {
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyDescription() {
        return propertyDescription;
    }

    public void setPropertyDescription(String propertyDescription) {
        this.propertyDescription = propertyDescription;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

}
