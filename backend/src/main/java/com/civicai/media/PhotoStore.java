package com.civicai.media;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.nio.file.*;
import java.io.IOException;
import java.util.UUID;
@Service public class PhotoStore {
 private final Path base;
 public PhotoStore(@Value("${civicai.storage-path}") String path) throws IOException {base=Paths.get(path).toAbsolutePath().normalize();Files.createDirectories(base);}
 public String save(MultipartFile image) throws IOException {
  if(image==null||image.isEmpty())return null;
  if(image.getSize()>5_000_000L)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Image exceeds 5 MB");
  byte[] data=image.getBytes();String ext;
  if(data.length>=3&&(data[0]&255)==0xff&&(data[1]&255)==0xd8&&(data[2]&255)==0xff)ext="jpg";
  else if(data.length>=8&&(data[0]&255)==137&&(data[1]&255)==80&&(data[2]&255)==78&&(data[3]&255)==71)ext="png";
  else if(data.length>=12&&new String(data,0,4,java.nio.charset.StandardCharsets.US_ASCII).equals("RIFF")&&new String(data,8,4,java.nio.charset.StandardCharsets.US_ASCII).equals("WEBP"))ext="webp";
  else throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Only JPEG, PNG or WebP allowed");
  String filename=UUID.randomUUID()+"."+ext;Files.write(base.resolve(filename),data,StandardOpenOption.CREATE_NEW);return filename;
 }
 public byte[] read(String filename) throws IOException {if(filename==null)throw new ResponseStatusException(HttpStatus.NOT_FOUND);Path p=base.resolve(filename).normalize();if(!p.getParent().equals(base))throw new ResponseStatusException(HttpStatus.BAD_REQUEST);return Files.readAllBytes(p);}
 public String contentType(String filename){if(filename.endsWith(".png"))return "image/png";if(filename.endsWith(".webp"))return "image/webp";return "image/jpeg";}
}
