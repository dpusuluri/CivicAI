package com.civicai.casework;
import com.civicai.media.PhotoStore;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
@RestController @RequestMapping("/api/cases") public class PublicCaseController {
 private final CaseService service;private final PhotoStore photos;
 public PublicCaseController(CaseService s,PhotoStore p){service=s;photos=p;}
 @PostMapping(consumes=MediaType.MULTIPART_FORM_DATA_VALUE) public ResponseEntity<CaseService.CaseView> submit(@Valid @RequestPart("case") CaseService.CreateRequest input,@RequestPart(value="photo",required=false) MultipartFile photo) throws IOException {return ResponseEntity.status(HttpStatus.CREATED).body(service.submit(input,photo));}
 @GetMapping("/track/{code}") public CaseService.CaseView track(@PathVariable String code,@RequestParam String token){return service.track(code,token);}
 @GetMapping("/track/{code}/photo") public ResponseEntity<byte[]> photo(@PathVariable String code,@RequestParam String token)throws IOException {CivicCase c=service.lookup(code,token);if(c.photoName==null)return ResponseEntity.notFound().build();return ResponseEntity.ok().contentType(MediaType.parseMediaType(photos.contentType(c.photoName))).cacheControl(CacheControl.noStore()).body(photos.read(c.photoName));}
}
