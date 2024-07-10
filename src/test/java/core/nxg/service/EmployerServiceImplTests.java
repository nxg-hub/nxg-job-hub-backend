 package core.nxg.service;

 import core.nxg.entity.Employer;
 import org.junit.jupiter.api.Test;
 import org.springframework.boot.test.context.SpringBootTest;

 import static org.junit.jupiter.api.Assertions.assertFalse;
 import static org.junit.jupiter.api.Assertions.assertTrue;


 public class EmployerServiceImplTests {

     @Test
     public void test_isVerified_returnsFalseWhenAllFieldsAreNull() {
         Employer employer = new Employer();

         boolean result = employer.isVerified();

         assertFalse(result);
     }

     @Test
     void createEmployer() {
     }

     @Test
     void getEmployer() {
     }

     @Test
     void patchEmployer() {
     }

     @Test
     void deleteEmployer() {
     }

     @Test
     void getEngagements() {
     }

     @Test
     void getJobPostings() {
     }

     @Test
     void isEmployerVerified() {
     }

     @Test
     void verifyEmployer() {
     }
 }
