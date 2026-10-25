package com.civicai.casework;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
@Entity public class CaseEvent {
 @Id @GeneratedValue(strategy=GenerationType.UUID) public UUID id;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="case_id",nullable=false) public CivicCase civicCase;
 public String actor;
 public String status;
 @Column(length=1000) public String reason;
 public Instant createdAt;
}
