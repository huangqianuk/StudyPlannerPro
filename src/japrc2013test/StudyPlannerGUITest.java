/**
 * Examination candidate number: Y1041916
 */
package japrc2013test;

import japrc2013.StudyBlockInterface;
import japrc2013.StudyPlanner;
import japrc2013.StudyPlannerGUI;
import japrc2013.StudyPlannerGUIInterface;
import japrc2013.StudyPlannerInterface;
import japrc2013.StudyPlannerInterface.CalendarEventType;
import japrc2013.TopicInterface;

import java.awt.Component;
import java.awt.Container;
import java.util.Calendar;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JTextField;

import junit.framework.TestCase;

public class StudyPlannerGUITest extends TestCase {
	
	private StudyPlannerGUIInterface gui;
	private StudyPlannerInterface planner;
	
	/**
	 * Constructor, initialize the StudyPlannerGUI
	 */
	public StudyPlannerGUITest() {
		planner = new StudyPlanner();
		planner.addTopic("Software Measurement and Testing", 480);
		planner.addTopic("European agricultural policy 1950-1974", 720);  
        Calendar cal = Calendar.getInstance();
        cal.set( Calendar.HOUR_OF_DAY, 13 );
        planner.addCalendarEvent( "Java file handling", cal, 60, CalendarEventType.ESSAY );
        gui = new StudyPlannerGUI((StudyPlanner)planner);
       
	}
	
	/**
	 * Test the user's actions of creating a new topic, 
	 *  including text inputting, button clicking and data saving.
	 * @throws Exception
	 */
	public void testActionOfCreateTopic() throws Exception {
		
		String topicName = "APRC";
		int topicDuration = 720;
		// Get topicsPanel
		Component topicsPanel = ( (StudyPlannerGUI)gui ).getTopicComponent();
		if ( topicsPanel instanceof Container ) {
			// Get the components in the topicsPanel
			Component[] children = ((Container) topicsPanel).getComponents();
			for ( int i = 0; i < children.length; i++ ) {
				if( children[ i ].getName() == "jtxtTopSubject" ) { // Test the input action of the name of the topic 
					( (JTextField) children[ i ] ).setText( topicName );
					//( (JTextField) com[ i ] ).postActionEvent();  // Type in a test message + ENTER
					// Test the new topic's name has been input
					assertEquals( topicName, ( (JTextField) children[ i ] ).getText());
				} else if ( children[ i ].getName() == "jtxtTopDuration" ) { // Test the input action of the duration of the topic 
					( (JTextField) children[ i ] ).setText( "" + topicDuration );
					// Test the new topic's duration has been input
					assertEquals( "" + topicDuration, ( (JTextField) children[ i ] ).getText());
				} else if ( children[ i ].getName() == "jbtTopAdd" ) { // Test the click action of the addTopic of the topic 
					( (JButton) children[ i ] ).doClick(); // Save the topic
					List<TopicInterface> topics = planner.getTopics(); // Get the topics
					// Test the new topic( "APRC", 720 ) has been saved
					assertEquals( topicName, topics.get( topics.size() - 1 ).getSubject() ); 
					assertEquals( topicDuration, topics.get( topics.size() - 1 ).getDuration() );
				}
			}
		}
	}
	
	/**
	 * Test the user's action of deleting a topic,
	 *  including selecting a topic item and deleting it from the list of topics.
	 * @throws Exception
	 */
	public void testActionOfDeleteTopic() throws Exception {
		// Get topicsPanel
		Component topicsPanel = ( (StudyPlannerGUI)gui ).getTopicComponent();
		List<TopicInterface> topicsBeforeDel = planner.getTopics(); // Get the topics before deleting
		String deleteName = topicsBeforeDel.get( 0 ).getSubject(); // The name will be deleted
		if ( topicsPanel instanceof Container ) {
			// Get the components in the topicsPanel
			Component[] children = ((Container) topicsPanel).getComponents();
			for ( int i = 0; i < children.length; i++ ) {
				if( children[ i ].getName() == "topicsJSP" ) { 
					//( (JList) children[ i ] ).getComponent(0);
					Component[] topicListCom = ((Container) children[ i ]).getComponents(); // Get JViewport is topicListCom[ 0 ]
					Component[] topicList = ((Container) topicListCom[ 0 ]).getComponents(); // Get JList is topicList[ 0 ]
					@SuppressWarnings("rawtypes")
					JList jlstTopic = (JList) topicList[ 0 ]; // JList
					gui.notifyModelHasChanged();
					jlstTopic.setSelectedIndex( 0 ); // Select an item for deleting
				} else if ( children[ i ].getName() == "jbtTopDelete" ) {
					// Delete current selected item (index = 0).
					( (JButton) children[ i ] ).doClick();
					List<TopicInterface> topicsAfterDel = planner.getTopics(); // Get the topics after deleting
					for ( int j = 0; j < topicsAfterDel.size(); j++ ) {
						// The deleted topic cannot be found
						assertNotSame( deleteName, topicsAfterDel.get( j ).getSubject() );
					}
				}
			}
		}
	}
	
	/**
	 * Test the user's action of generate study plan.
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "deprecation" })
	public void testActionOfCreatePlan() throws Exception {
		Component contenPanel = ( (StudyPlannerGUI)gui ).getContentPane(); // Get the content panel
		if ( contenPanel instanceof Container ) {
			Component[] children = ((Container) contenPanel).getComponents();
			for ( int i = 0; i < children.length; i++ ) {
				if( children[ i ].getName() == "jbtPlan" ) {
					 ((JButton)children[ i ]).doClick(); // Click button to generate study plan
					 Component planPanel = ( (StudyPlannerGUI)gui ).getPlanComponent(); // Get plan panel
					 Component[] childrenPlan = ((Container)planPanel).getComponents(); // Get the components in the plan panel
					 for ( int j = 0; j < childrenPlan.length; j++ ) {
						 if ( childrenPlan[ j ].getName() == "planJSP" ) { // Get the plan JScrollPane
							 Component[] planListCom = ((Container) childrenPlan[ j ]).getComponents(); // Get JViewport is planListCom[ 0 ]
							 Component[] planList = ((Container) planListCom[ 0 ]).getComponents(); // Get JList is planList[ 0 ]
							 JList jlstPlan = (JList) planList[ 0 ]; // Get jstPlan
							 List<StudyBlockInterface> plan = planner.getStudyPlan();
							 int hours, minutes;
							 String time = "", temp;
							 // Check every plan item has been added to the GUI.
							 for ( int s = 0; s < plan.size(); s++ ) {
								 // Select current item 
								 jlstPlan.setSelectedIndex( s );
								 hours = plan.get( s ).getStartTime().getTime().getHours();
					    		 minutes = plan.get( s ).getStartTime().getTime().getMinutes();
					    		 if ( hours < 10 ) time = "0" + hours;
					    		 else time = "" + hours;
					    		
					    		 if ( minutes < 10 ) time += ":0" + minutes;
					    		 else time += ":" + minutes;
					    		
					    		 time += " " + plan.get( s ).getStartTime().get(Calendar.DATE) + "/" 
					    					+ plan.get( s ).getStartTime().get(Calendar.MONTH)+ "/" + plan.get( s ).getStartTime().get(Calendar.YEAR);
					    		 temp =  "  " + time + "  " + plan.get( s ).getTopic() ;
					    		 // Check the contents are equal or not.
					    		 assertEquals( temp, jlstPlan.getSelectedValue() );
							 }
							 break;
						 }
					 }
					 break;
				}
			}
		}
	}
	
}