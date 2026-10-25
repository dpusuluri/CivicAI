package com.civicai.casework;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface CaseRepository extends JpaRepository<CivicCase,UUID> {
 Optional<CivicCase> findByTrackingCodeAndAccessToken(String trackingCode,String accessToken);
 List<CivicCase> findAllByOrderByCreatedAtDesc();
}
