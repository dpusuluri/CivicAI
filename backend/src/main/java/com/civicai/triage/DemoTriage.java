package com.civicai.triage;
import org.springframework.stereotype.Service;
import java.util.Locale;
/** TEXT KEYWORD DEMONSTRATION, NOT A REAL IMAGE OR MULTIMODAL AI MODEL. */
@Service public class DemoTriage {
 public record Result(String category,String department,String priority,double confidence,String observation){}
 public Result analyze(String text) {
  String d=text.toLowerCase(Locale.ROOT);
  String category="OTHER",department="Citizen Services";
  if(d.matches("(?s).*(pothole|road crack|road damage).*")){category="POTHOLE";department="Public Works";}
  else if(d.matches("(?s).*(garbage|trash|dumping|waste).*")){category="GARBAGE";department="Sanitation";}
  else if(d.matches("(?s).*(streetlight|street light|lamp).*")){category="STREETLIGHT";department="Public Works";}
  else if(d.matches("(?s).*(water leak|burst pipe|leaking water).*")){category="WATER_LEAK";department="Water Services";}
  else if(d.matches("(?s).*(blocked drain|flood|drainage).*")){category="DRAINAGE";department="Public Works";}
  boolean hazard=d.matches("(?s).*(school|danger|injury|accident|flooding|blocked road).*" );
  String priority=hazard?"P1":"P3";
  double confidence=category.equals("OTHER")?0.2:0.55;
  return new Result(category,department,priority,confidence,"Demo keyword-based suggestion from the written description only. Image was not analyzed. Officer verification required.");
 }
}
