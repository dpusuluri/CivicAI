package com.civicai.triage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class DemoTriageTest {
 @Test void classifiesPotholeAndSchoolRisk(){var r=new DemoTriage().analyze("Large pothole outside school");assertEquals("POTHOLE",r.category());assertEquals("P1",r.priority());}
 @Test void unknownRequiresReview(){var r=new DemoTriage().analyze("Something needs attention");assertEquals("OTHER",r.category());assertTrue(r.confidence()<0.5);}
}
