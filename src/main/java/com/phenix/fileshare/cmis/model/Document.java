package com.phenix.fileshare.cmis.model;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.hibernate.annotations.NaturalId;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Indexed
@Analyzer(impl = StandardAnalyzer.class)
@Table(name="document")
public class Document implements Serializable {
    @Id
    @Column(name = "document_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long documentId;

    @NaturalId
    @Column(name = "cmis_id")
    private String cmisId;

    @Column(name = "type")
    private int type;


    @Field(index= Index.YES, analyze= Analyze.NO, store= Store.NO)
    private String name;

    @ManyToOne
    @JoinColumn(name = "createdBy")
    private User createdBy;

    @Column(name = "createdAt")
    private Date createdAt;

    @Column(name = "updatedAt")
    private Date updatedAt;



    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "user_shared_document" , joinColumns = {@JoinColumn(name = "document_id")}, inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private Set<User> shared_users = new HashSet<User>();

    public Document(){

    }

    public Document(String cmisId, int type, String documentName, User createdBy) {
        this.cmisId = cmisId;
        this.type = type;
        this.name = documentName;
        this.createdBy = createdBy;
        //this.createdAt = createdAt;
        //this.updatedAt = updatedAt;
    }

    public Set<User> getShared_users() {
        return shared_users;
    }

    public void setShared_users(Set<User> shared_users) {
        this.shared_users = shared_users;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getCmisId() {
        return cmisId;
    }

    public void setCmisId(String cmisId) {
        this.cmisId = cmisId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String documentName) {
        this.name = documentName;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
