package com.civicai.casework;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.*;
@Entity @Table(name="civic_cases") public class CivicCase {
 @Id @GeneratedValue(strategy=GenerationType.UUID) public UUID id;
 @Column(nullable=false,unique=true) public String trackingCode;
 @Column(nullable=false,unique=true) public String accessToken;
 @Column(nullable=false,length=2000) public String description;
 @Column(nullable=false) public double latitude;
 @Column(nullable=false) public double longitude;
 @Column(nullable=false) public String category;
 @Column(nullable=false) public String department;
 @Column(nullable=false) public String status;
 @Column(nullable=false) public String priority;
 public String photoName;
 @Column(length=1500) public String aiObservation;
 public double aiConfidence;
 @Column(nullable=false) public Instant createdAt;
 @Column(nullable=false) public Instant updatedAt;
 @Column(length=1000) public String resolutionNote;
 @OneToMany(mappedBy="civicCase",cascade=CascadeType.ALL,orphanRemoval=true,fetch=FetchType.LAZY) @OrderBy("createdAt ASC") public List<CaseEvent> history=new ArrayList<>();
 public void event(String actor,String reason){CaseEvent e=new CaseEvent();e.civicCase=this;e.actor=actor;e.status=this.status;e.reason=reason;e.createdAt=Instant.now();history.add(e);updatedAt=Instant.now();}
}
