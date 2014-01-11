/**
 * Examination candidate number: Y1041916
 */
package japrc2013test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import japrc2013.StudyBlockInterface;
import japrc2013.StudyPlanner;
import japrc2013.StudyPlannerException;
import japrc2013.StudyPlannerGUIInterface;
import japrc2013.StudyPlannerInterface;
import japrc2013.TopicInterface;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class StudyPlannerBasicTest
{
    private static final class MockPlannerGUI implements StudyPlannerGUIInterface
    {
        public int changeCount;
        
        @Override
        public void notifyModelHasChanged()
        {
            changeCount++;
        }
    }
    
    private StudyPlannerInterface planner;
    
    @Before
    public void setUp() throws Exception
    {
        planner = new StudyPlanner();
    }
    
    @Test
    public final void testAddTopic()
    {
        planner.addTopic("Java file handling", 480);
    }
    
    @Test
    public final void getTopics()
    {
        planner.addTopic("Java file handling", 480);
        Collection<TopicInterface> topics = planner.getTopics();
        assertEquals(1, topics.size());
    }
    
    @Test
    public final void testGetStudyPlan()
    {
        //minimal test to check this functionality exists
        //behaviour of this method will change a lot as levels progress, but this minimal test should still pass
        //on your final version
        
        planner.addTopic("Java file handling", 480);
        planner.generateStudyPlan();
        List<StudyBlockInterface> events = planner.getStudyPlan();
        assertNotNull(events);
        assertTrue(events.size() != 0);
        assertEquals("Java file handling", events.get(0).getTopic());
        assertTrue(events.get(0).getDuration() > 0);
    }    
    
    @Test
    public void testNotifiesGUIOnNewPlan() {
        planner.addTopic("Java file handling", 480);
        
        MockPlannerGUI gui = new MockPlannerGUI();        
        planner.setGUI(gui);      
        assertEquals(0, gui.changeCount);
        
        planner.generateStudyPlan();
        assertEquals(1, gui.changeCount);
        
        planner.generateStudyPlan();
        assertEquals(2, gui.changeCount);        
    }
    
    /**
     * Test Level 1 StudyPlannerException:
     *  no topic exception,
     *  create two same topics exception.
     */
    @Test (expected = StudyPlannerException.class)
    public void testLevelOneException() {
    	
    	// Throw a StudyPlannerException if client code tries to generate a study plan when there are no topics
    	planner.addTopic( "Java file handling", 480);
        planner.addTopic("European agricultural policy 1950-1974", 720);  
        planner.deleteTopic( "Java file handling" );
        planner.deleteTopic( "European agricultural policy 1950-1974" );
    	planner.generateStudyPlan();
    	
    }
    
    /**
     * Test  default block length and break length,
     *  and set block length and break length, test as well
     */
    @SuppressWarnings("deprecation")
	@Test
    public final void testStudyAndBreakLength()
    {
    	// Using a fixed block length of 60 minutes, study one block of each topic in turn, 
    	// in the order in which the topics were added to the planner
    	// Test default blockLength and breakLength
    	planner.addTopic("Java file handling", 480);
        planner.addTopic("European agricultural policy 1950-1974", 720);  
        planner.generateStudyPlan();
        List<StudyBlockInterface> plan = planner.getStudyPlan();
        Date date;
        int firstTimeLength, secondTimeLength, firstDate, secondDate;
        date = plan.get( 0 ).getStartTime().getTime();
        firstDate = date.getDate();
        firstTimeLength = date.getHours() * 60 + date.getMinutes();
        for ( int i = 1; i < plan.size(); i++ ) {
        	date = plan.get( i ).getStartTime().getTime();
        	secondDate = date.getDate();
        	secondTimeLength = date.getHours() * 60 + date.getMinutes();
        	// on the same day, check the default study length is 60 and break length is 0
        	if ( firstDate == secondDate ) {
            	assertTrue( secondTimeLength - firstTimeLength <= 60 );
        	}
        	
        	firstTimeLength = secondTimeLength;
        	firstDate = secondDate;
        }
        
    	// Set blockLength and breakLength, test
        planner.setBlockSize( 40 );
        planner.setBreakLength( 10 );
        planner.generateStudyPlan();
        plan = planner.getStudyPlan();
        date = plan.get( 0 ).getStartTime().getTime();
        firstDate = date.getDate();
        firstTimeLength = date.getHours() * 60 + date.getMinutes();
        for ( int i = 2; i < plan.size(); i = i + 2 ) {
        	date = plan.get( i ).getStartTime().getTime();
        	secondDate = date.getDate();
        	secondTimeLength = date.getHours() * 60 + date.getMinutes();
        	// on the same day, check the study length is 40 and break length is 10
        	if ( firstDate == secondDate ) {
        		assertTrue( secondTimeLength - firstTimeLength <= 50 );
        	}
        	firstTimeLength = secondTimeLength;
        	firstDate = secondDate;
        }
    }
    
    /**
     * Test generate plan, all topics have been fully studied 
     */
	@Test
    public void testGeneratePlan() {

    	
        planner.addTopic("Java file handling", 480);
        planner.addTopic("European agricultural policy 1950-1974", 720);  
        planner.generateStudyPlan();
        List<StudyBlockInterface> plan = planner.getStudyPlan();
        // Study one block of each topic in turn,
    	//  in the order in which the topics were added to the planner.
    	// Then repeat until all topics have been fully studied.
        int firstLength =0, secondLength = 0;
        for ( int i = 0; i < plan.size(); i++ ) {
        	if ( plan.get( i ).getTopic().equals( "Java file handling" ) ) {
        		firstLength += plan.get( i ).getDuration();
        	} else {
        		secondLength += plan.get( i ).getDuration();
        	}
        }
        // At the end of day, if the left time < 10, it will set the set block length to 10 
        assertTrue( firstLength >= 480 );
        assertTrue( secondLength >= 720);
        
        // set study length and break length 
        planner.setBlockSize( 40 );
        planner.setBreakLength( 10 );
        planner.generateStudyPlan();
        plan = planner.getStudyPlan();
        firstLength = secondLength = 0;
        for ( int i = 0; i < plan.size(); i++ ) {
        	if ( plan.get( i ).getTopic().equals( "Java file handling" ) ) {
        		firstLength += plan.get( i ).getDuration();
        	} else if ( plan.get( i ).getTopic().equals( "European agricultural policy 1950-1974" ) ) {
        		secondLength += plan.get( i ).getDuration();
        	}
        }
        // At the end of day, if the left time < 10, it will set the set block length to 10 
        assertTrue( firstLength >= 480 );
        assertTrue( secondLength >= 720);
    }
	
	/**
	 * Test if client code tries to supply another type of event, it should throw a StudyPlannerException
	 */
	@Test (expected = StudyPlannerException.class)
	public void testOtherTypeEventTarget() {
	
		Calendar cal = Calendar.getInstance();
		cal.set( Calendar.HOUR_OF_DAY, 11 );
		cal.set( Calendar.MINUTE, 0 );
		planner.addCalendarEvent( "CSYA", cal, 60); // time 15:41, duration 60
		planner.addTopic("Java file handling", 480);
		planner.generateStudyPlan();
		List<TopicInterface> topics = planner.getTopics();
		topics.get( 0 ).setTargetEvent( planner.getCalendarEvents().get( 0 ) );
		
	}
	
	/**
	 * Test if not enough time left in the day for a whole study block,
	 *  a short study block should be created.
	 * And test a block below minimum length, make the block the minimum length anyway
	 */
	@Test
	public void testShortStudyBlock() {
		planner.setBlockSize( 50 ); // study block 50
		Calendar cal = Calendar.getInstance();
		cal.set( Calendar.HOUR_OF_DAY, 16 );
		cal.set( Calendar.MINUTE, 30 ); 
	    planner.addTopic("European agricultural policy 1950-1974", 5);  
		planner.addTopic("Java file handling", 480);
	    planner.generateStudyPlan( cal ); 
	    cal.set( Calendar.HOUR_OF_DAY, 17 );
		cal.set( Calendar.MINUTE, 0 ); 
		planner.setDailyEndStudyTime( cal ); // time 16:30 - 17:00 => 30 < 50 study block
	    List<StudyBlockInterface> plan = planner.getStudyPlan();
	    assertEquals( 10, plan.get( 0 ).getDuration() ); // topic length < minimum study block length 10
	    assertEquals( 20, plan.get( 1 ).getDuration() ); // not enough time left in the day for a whole study block
		
	}
	
	/**
	 * Test If an study block ends with an event, it doesn¡¯t schedule a break at all.
	 * Truncate any breaks the end of the study day.
	 */
	@Test
	public void testEventAndBreak() {
		Calendar cal = Calendar.getInstance();
		cal.set( Calendar.HOUR_OF_DAY, 11 );
		cal.set( Calendar.MINUTE, 0 );
		planner.addCalendarEvent( "CSYA", cal, 60); // time 15:41, duration 60
		planner.addTopic( "Java file handling", 480);
		planner.addTopic("European agricultural policy 1950-1974", 150); 
		planner.setBreakLength( 10 ); // break length 10
		planner.setBlockSize( 50 ); // block length 50
		cal.set( Calendar.HOUR_OF_DAY, 16 );
		cal.set( Calendar.MINUTE, 5 );
		planner.generateStudyPlan( cal ); // today study time 16:05 - 17:00, break length 10, 
		List<StudyBlockInterface> plan = planner.getStudyPlan();
		for ( int i = 0; i < plan.size(); i++ ) {
			if ( plan.get( i ).getTopic().equals( "CSYA" ) && i < plan.size() - 1 ) {
				assertTrue( !plan.get( i + 1 ).getTopic().equals( "(break)" ) ); // no break after event
				assertEquals( 5, plan.get( 2 ).getDuration() ); // no enough time left for break, truncate the time
			}
		}
	}
}
