package com.civicai.officer;
import com.civicai.casework.*;
import com.civicai.media.PhotoStore;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.*;
@RestController @RequestMapping("/api/officer") public class OfficerController {
 private final CaseService service;private final PhotoStore photos;
 public OfficerController(CaseService s,PhotoStore p){service=s;photos=p;}
 @GetMapping("/cases") public List<CaseService.CaseView> cases(){return service.all();}
 @PutMapping("/cases/{id}") public CaseService.CaseView update(@PathVariable UUID id,@Valid @RequestBody CaseService.UpdateRequest input,Authentication auth){return service.update(id,input,auth.getName());}
 @GetMapping("/cases/{id}/photo") public ResponseEntity<byte[]> photo(@PathVariable UUID id)throws IOException {CivicCase c=service.officerCase(id);if(c.photoName==null)return ResponseEntity.notFound().build();return ResponseEntity.ok().contentType(MediaType.parseMediaType(photos.contentType(c.photoName))).cacheControl(CacheControl.noStore()).body(photos.read(c.photoName));}
 @GetMapping(value="/cases/{id}/report",produces=MediaType.TEXT_PLAIN_VALUE) public String report(@PathVariable UUID id){CivicCase c=service.officerCase(id);return "CivicAI - Authority Report (DEMO)\nCase: "+c.trackingCode+"\nDescription: "+c.description+"\nLocation: "+c.latitude+", "+c.longitude+"\nSuggested Category: "+c.category+"\nDepartment (demo rule): "+c.department+"\nPriority (demo rule): "+c.priority+"\nAssessment: "+c.aiObservation+"\nStatus: "+c.status+"\nResolution: "+(c.resolutionNote==null?"Pending":c.resolutionNote)+"\n\nThis is a demo. Officer must verify the evidence, location, routing and severity. No email has been sent.";}
}
