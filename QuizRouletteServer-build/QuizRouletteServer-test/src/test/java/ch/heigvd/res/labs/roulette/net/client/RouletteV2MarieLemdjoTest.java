
package ch.heigvd.res.labs.roulette.net.client;

import ch.heigvd.res.labs.roulette.data.Student;
import ch.heigvd.res.labs.roulette.net.protocol.RouletteV2Protocol;
import ch.heigvd.schoolpulse.TestAuthor;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/*
 * This class contains automated tests to validate the client and the server
 * implementation of the Roulette Protocol (version 2)
 *
 * @author Olivier Liechti
 *
 * @author lemdjo
 */
public class RouletteV2MarieLemdjoTest {
    
  @Rule
  public ExpectedException exception = ExpectedException.none();
  
  @Rule
  public EphemeralClientServerPair roulettePair = new EphemeralClientServerPair(RouletteV2Protocol.VERSION);

  private static int STUDENT_NUMBER = 31;
  
  @Test
  @TestAuthor(githubId = "LemdjoM")
  public void theClearDataSoreShouldRemoveAllStudents() throws IOException{
      int port = roulettePair.getServer().getPort();
      IRouletteV2Client client = new RouletteV2ClientImpl();
      client.connect("localhost", port);
      
      client.loadStudent("McMoudi");
      client.loadStudent("LemdjoM");
      
      assertEquals(client.getNumberOfStudents(), 2);
      
      client.clearDataStore(); //Remove all students
      
      assertEquals(client.getNumberOfStudents(), 0);
      
      client.disconnect();
  }
  
  @Test
  @TestAuthor(githubId = "LemdjoM")
  public void theNumberOfStudentShouldBeCorrectAtTime() throws IOException{
      assertEquals(roulettePair.getClient().getNumberOfStudents(), 0);
      
      roulettePair.getClient().loadStudent("McMoudi");
       assertEquals(roulettePair.getClient().getNumberOfStudents(), 1);
       
      roulettePair.getClient().loadStudent("LemdjoM");
       assertEquals(roulettePair.getClient().getNumberOfStudents(), 2);
  } 
    
}
