package com.civicai.casework;
import com.civicai.media.PhotoStore;
import com.civicai.triage.DemoTriage;
import jakarta.validation.constraints.*;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
@Service public class CaseService {
 private final CaseRepository repo;private final PhotoStore photos;private final DemoTriage triage;
 public CaseService(CaseRepository repo,PhotoStore photos,DemoTriage triage){this.repo=repo;this.photos=photos;this.triage=triage;}
 public record CreateRequest(@NotBlank @Size(max=2000) String description,@NotNull @DecimalMin("-90") @DecimalMax("90") Double latitude,@NotNull @DecimalMin("-180") @DecimalMax("180") Double longitude){}
 public record EventView(String status,String actor,String reason,Instant createdAt){}
 public record CaseView(UUID id,String trackingCode,String accessToken,String description,double latitude,double longitude,String category,String department,String status,String priority,String aiObservation,double aiConfidence,boolean hasPhoto,String resolutionNote,Instant createdAt,List<EventView> history){}
 public CaseView view(CivicCase c,boolean revealToken){return new CaseView(c.id,c.trackingCode,revealToken?c.accessToken:null,c.description,c.latitude,c.longitude,c.category,c.department,c.status,c.priority,c.aiObservation,c.aiConfidence,c.photoName!=null,c.resolutionNote,c.createdAt,c.history.stream().map(e->new EventView(e.status,e.actor,e.reason,e.createdAt)).toList());}
 @Transactional public CaseView submit(CreateRequest request,MultipartFile photo) throws IOException {
  DemoTriage.Result result=triage.analyze(request.description());CivicCase c=new CivicCase();c.trackingCode="CIV-"+UUID.randomUUID().toString().substring(0,8).toUpperCase(Locale.ROOT);c.accessToken=UUID.randomUUID().toString();c.description=request.description().trim();c.latitude=request.latitude();c.longitude=request.longitude();c.category=result.category();c.department=result.department();c.priority=result.priority();c.aiConfidence=result.confidence();c.aiObservation=result.observation();c.status="VALIDATION";c.createdAt=Instant.now();c.photoName=photos.save(photo);c.event("SYSTEM","Submitted; demo triage suggestion awaits officer verification");repo.save(c);return view(c,true);
 }
 @Transactional(readOnly=true) public CaseView track(String code,String token){return view(lookup(code,token),false);}
 @Transactional(readOnly=true) public CivicCase lookup(String code,String token){return repo.findByTrackingCodeAndAccessToken(code,token).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Case not found"));}
 @Transactional(readOnly=true) public List<CaseView> all(){return repo.findAllByOrderByCreatedAtDesc().stream().map(c->view(c,false)).toList();}
 @Transactional(readOnly=true) public CivicCase officerCase(UUID id){return repo.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));}
 public record UpdateRequest(@NotBlank String status,@Size(max=1000) String reason,@Size(max=1000) String resolutionNote,@Size(max=80) String department){}
 private static final Map<String,Set<String>> TRANSITIONS=Map.of(
  "VALIDATION",Set.of("ROUTED","REJECTED"),"ROUTED",Set.of("ACKNOWLEDGED","REJECTED"),"ACKNOWLEDGED",Set.of("ASSIGNED"),"ASSIGNED",Set.of("IN_PROGRESS"),"IN_PROGRESS",Set.of("RESOLVED","ESCALATED"),"ESCALATED",Set.of("IN_PROGRESS"),"RESOLVED",Set.of("CLOSED","REOPENED"),"REOPENED",Set.of("ASSIGNED"),"REJECTED",Set.of(),"CLOSED",Set.of("REOPENED"));
 @Transactional public CaseView update(UUID id,UpdateRequest change,String officer){CivicCase c=officerCase(id);String next=change.status().toUpperCase(Locale.ROOT);if(!TRANSITIONS.getOrDefault(c.status,Set.of()).contains(next))throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid status transition from "+c.status+" to "+next);if(next.equals("RESOLVED")&&(change.resolutionNote()==null||change.resolutionNote().isBlank()))throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Resolution note is required");c.status=next;if(change.department()!=null&&!change.department().isBlank())c.department=change.department().trim();if(change.resolutionNote()!=null&&!change.resolutionNote().isBlank())c.resolutionNote=change.resolutionNote();c.event(officer,change.reason()==null?"Status updated":change.reason());return view(repo.save(c),false);}
}
