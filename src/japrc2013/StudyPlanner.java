/**
 * Examination candidate number: Y1041916
 */
package japrc2013;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@SuppressWarnings("serial")
public class StudyPlanner implements StudyPlannerInterface, Serializable 
{
    private ArrayList<TopicInterface> topics = new ArrayList<TopicInterface>();
    private ArrayList<StudyBlockInterface> plan = new ArrayList<StudyBlockInterface>();
    private ArrayList<CalendarEventInterface> calendarEvent = new ArrayList<CalendarEventInterface>();
    
    private StudyPlannerGUIInterface gui;


    @Override
    public void addTopic(String name, int duration)
    {
    	// Throw a StudyPlannerException is client code tries to create two topics with the same name
    	for ( int i = 0; i < topics.size(); i++ ) {
    		if ( topics.get( i ).getSubject().equals( name ) ) {
    			throw new StudyPlannerException( "Two topics with the same name" );
    		}
    	}
        topics.add(new Topic(name, duration));
    }
    

    @Override
    public List<TopicInterface> getTopics()
    {
        return topics;
    }

    @SuppressWarnings("unused")
	@Override
    public List<StudyBlockInterface> getStudyPlan()
    {         
    	int i = 0;
    	for ( StudyBlockInterface s : plan ) {
    		Calendar ca = plan.get( i ).getStartTime();
    		i++;
    	}
        return plan;
    }


    public void setGUI(StudyPlannerGUIInterface gui)
    {
        this.gui = gui;
    }

	@Override
    public void generateStudyPlan()
    {
		generateStudyPlan( Calendar.getInstance() );
    }



    @Override
    public void deleteTopic(String topic)
    {
    	for ( int i = 0; i < topics.size(); i++ ) {
    		if ( topics.get( i ).getSubject().equals( topic ) ) {
    			topics.remove( i );
    			break;
    		}
    	}
    }

	@Override
    public void generateStudyPlan( Calendar startStudy )
    {
		// Throw a StudyPlannerException if client code tries to generate a study plan when there are no topics
		if ( topics.size() == 0 ) throw new StudyPlannerException( "NullTopicException" );
        plan = new ArrayList<StudyBlockInterface>();
        addBefCalendarEvent( validCalendar( startStudy ) );
    	generatePlan( topics, validCalendar( startStudy ) );
    	    
        if(gui != null)
        {
            gui.notifyModelHasChanged();
        }    
    }
    
	/**
	 * The main method of generating study plan
	 * @param topics
	 * @param startStudy
	 */
    @SuppressWarnings("deprecation")
	public void generatePlan( List<TopicInterface> topics, Calendar startStudy ) {
    	
    	
    	ArrayList<TopicInterface> currentTopics = new ArrayList<TopicInterface>();
        int hours = startStudy.getTime().getHours()
        		, minutes = startStudy.getTime().getMinutes()
        		, blockSize = getBlockSize()
        		, breakLength = getBreakLength();
        Calendar currentCal = startStudy;
        CalendarEventInterface calEvent;
        boolean eventIsAdded = false;
        
        // add topics or events
    	for ( int i = 0; i < topics.size(); i++ ) {
    		
    		eventIsAdded = false;
    		int leftTimeLength = getTimeLength( getDailyEndStudyTime() ) - getTimeLength( currentCal );
    		int topicDuration = topics.get( i ).getDuration();
    		// If reach the end time of the dairy study, break
    		if ( leftTimeLength > blockSize || leftTimeLength > topicDuration ) {
    			if ( topicDuration > blockSize ) {
    				calEvent = hasCalendarEvent( currentCal, blockSize );
    				if (  calEvent != null ) {
    					int breforCalTimeLength = getTimeLength( calEvent.getStartTime() ) - getTimeLength( currentCal );
    					if ( breforCalTimeLength >= 10 ) {
    						plan.add( new StudyBlock( topics.get( i ).getSubject(), (Calendar)calendarToHM( currentCal, hours, minutes ).clone(), breforCalTimeLength ) );
    						currentTopics.add( new Topic( topics.get( i ).getSubject(), topicDuration - breforCalTimeLength  ) );
    					} else {
        	    			i--;
    					}
						plan.add( new StudyBlock( calEvent.getName(), calEvent.getStartTime(), calEvent.getDuration() ) );
    	    			hours = calEvent.getStartTime().getTime().getHours();
    	    			minutes = calEvent.getStartTime().getTime().getMinutes() + calEvent.getDuration();
    	    			eventIsAdded = true;
    	    			
    	    		} else {
    	    			plan.add( new StudyBlock( topics.get( i ).getSubject(), (Calendar)calendarToHM( currentCal, hours, minutes ).clone(), blockSize ) );
    	    			currentTopics.add( new Topic( topics.get( i ).getSubject(), topicDuration - blockSize  ) );
    	    			minutes += blockSize;
    	    		}
	    			
	    		} else if ( topicDuration < 10 ) {
	    			// if a block would be below minimum length because there is not enough required study time left in the topic,
	    			// make the block the minimum length anyway
	    			// make the student study a few more minutes on the topic than would normally be required. 
	    			calEvent = hasCalendarEvent( currentCal, 10 );
    				if (  calEvent != null ) {
    	    			plan.add( new StudyBlock( calEvent.getName(), calEvent.getStartTime(), calEvent.getDuration() ) );
    	    			hours = calEvent.getStartTime().getTime().getHours();
    	    			minutes = calEvent.getStartTime().getTime().getMinutes() + calEvent.getDuration();
    	    			eventIsAdded = true;
    	    			i--;
    	    		} else {
    	    			plan.add( new StudyBlock( topics.get( i ).getSubject(), (Calendar)calendarToHM( currentCal, hours, minutes ).clone(), 10 ) );	
    	    			minutes += 10;
    	    		}
	    			
	    		} else { // the duration of the topics less than standard block size
	    			calEvent = hasCalendarEvent( currentCal, topicDuration );
    				if (  calEvent != null ) {
    	    			plan.add( new StudyBlock( calEvent.getName(), calEvent.getStartTime(), calEvent.getDuration() ) );
    	    			hours = calEvent.getStartTime().getTime().getHours();
    	    			minutes = calEvent.getStartTime().getTime().getMinutes() + calEvent.getDuration();
    	    			eventIsAdded = true;
    	    			i--;
    	    		} else {
    	    			plan.add( new StudyBlock( topics.get( i ).getSubject(), (Calendar)calendarToHM( currentCal, hours, minutes ).clone(), topicDuration ) );	
    	    			minutes += topicDuration;
    	    		}
	    		}
    			
	    		// After a block-sized time have a break
	    		if ( minutes >= 60 ) {
	    			hours = hours + minutes / 60 ;
	    			minutes %= 60;
	    		}
	    		
	    		currentCal = calendarToHM( currentCal, hours, minutes );
	    		int minuteBefBreak  = minutes + breakLength, hourBefBreak = hours;
	    		if ( minuteBefBreak >= 60 ) {
	    			hourBefBreak = hours + minuteBefBreak / 60 ;
	    			minuteBefBreak %= 60;
	    		} 
	    		
	    		int lengthBefBreak = getTimeLength( calendarToHM(currentCal, hourBefBreak, minuteBefBreak) );
	    		// If break length is set to zero, there should be no gap between the end of one study block and the start of the next 
	    		if ( breakLength > 0 && !eventIsAdded ) {
	    			calEvent = hasCalendarEvent( currentCal, breakLength );
    				if (  calEvent != null ) {
    					// Truncate any breaks that would overlap with the start of an event or the end of the study day
    					if ( getTimeLength( calEvent.getStartTime() ) > getTimeLength( currentCal ) ) {
    						plan.add( new StudyBlock( "(break)", (Calendar)calendarToHM( currentCal, hours, minutes ).clone(), breakLength ) );
    					}
						plan.add( new StudyBlock( calEvent.getName(), calEvent.getStartTime(), calEvent.getDuration() ) );
    	    			hours = calEvent.getStartTime().getTime().getHours();
    	    			minutes = calEvent.getStartTime().getTime().getMinutes() + calEvent.getDuration();
    	    			
    				} else if ( getTimeLength( getDailyEndStudyTime() ) - lengthBefBreak > 10 ){ 
    					plan.add( new StudyBlock( "(break)", (Calendar)calendarToHM( currentCal, hours, minutes ).clone(), breakLength ) );
    					// Add the time of the break to the current time
    		    		minutes += breakLength; 
    				}
		    		
		    		// calculate current time
		    		if ( minutes >= 60 ) {
		    			hours = hours + minutes / 60 ;
		    			minutes %= 60;
		    		}
		    		currentCal = calendarToHM( currentCal, hours, minutes );
	    		}
    		} else if ( leftTimeLength >= 10 ) {
    			// there is not enough time left in the day for a whole study block
    			// it creates a short study block in the same way as it does when there is not enough time left in a topic. 
    			plan.add( new StudyBlock( topics.get( i ).getSubject(), (Calendar)currentCal.clone(), leftTimeLength ) );	
    			currentTopics.add( new Topic( topics.get( i ).getSubject(), topicDuration - leftTimeLength  ) );
    			minutes += leftTimeLength;
    			if ( minutes >= 60 ) {
	    			hours = hours + minutes / 60 ;
	    			minutes %= 60;
	    		}
	    		currentCal = calendarToHM( currentCal, hours, minutes );
	    		
    		} else if ( leftTimeLength >= 0 && leftTimeLength < 10 ) {
    			leftTimeLength = getTimeLength( getDailyEndStudyTime() ) - getTimeLength( getDailyStartStudyTime() );
    			currentCal.set( Calendar.DATE, currentCal.getTime().getDate() + 1 );
    			currentCal = calendarToHM( currentCal, getDailyStartStudyTime().getTime().getHours(), getDailyStartStudyTime().getTime().getMinutes() );
    			currentTopics.add( new Topic( topics.get( i ).getSubject(), topicDuration  ) );
    			ArrayList<TopicInterface> tempTopics = new ArrayList<TopicInterface>();
    			for ( int j = i; j < topics.size(); j++ )
    				tempTopics.add( new Topic( topics.get( j ).getSubject(), topics.get( j ).getDuration()  ) );
    			for ( int k = 0; k < currentTopics.size(); k++ ) {
    				tempTopics.add( currentTopics.get( k ) );
    			}
    			currentTopics.clear();
    			
    			for ( int j = 0; j < tempTopics.size() - 1; j++ ) {
    				currentTopics.add( tempTopics.get( j ) );
    			}
    			
    			break;
    		}
    			
		}
    	
    	// Fully studied until the end of the dairy study time
    	if ( currentTopics.size() > 0 ) {
    		generatePlan( currentTopics , currentCal );
    	} else {
    		 // add events that before the current time
            addAftCalendarEvent( currentCal );
    	}
    }

    /**
     * Make sure Calendar is valid
     * @param cal
     * @return cal: 9AM < cal < 5PM, 9AM: Calendar < 9AM, next day 9AM: Calendar > 17AM
     */
	private Calendar validCalendar( Calendar cal) {
		int endTimeLength = getTimeLength( getDailyEndStudyTime() )
				, startTimeLength = getTimeLength( getDailyStartStudyTime() )
				, currentTimeLength = getTimeLength( cal );
		if ( endTimeLength - currentTimeLength > 0 && currentTimeLength - startTimeLength > 0 )
			return cal;
		else if ( currentTimeLength <= startTimeLength ) {
			// cal < 9AM 
			// cal begin at 9AM
			cal.set( Calendar.HOUR_OF_DAY, 9);
			cal.set( Calendar.MINUTE, 0 );
			return cal;
		} else if ( currentTimeLength >= endTimeLength) {
			// cal > 17PM
			// cal begin at next day 9AM
			cal.set( Calendar.HOUR_OF_DAY, 9);
			cal.set( Calendar.MINUTE, 0 );
			cal.set( Calendar.DATE, cal.get( Calendar.DATE) + 1 );
			return cal;
		}
		return null;
			
    }
    
    /**
     * Format Calendar
     * @param cal
     * @return the time length(Integer)
     */
    @SuppressWarnings("deprecation")
	private int getTimeLength( Calendar cal ) {
    	int hours = cal.getTime().getHours(), minutes = cal.getTime().getMinutes();
    	return hours * 60 + minutes;
    }
    
    /**
     * Format Calendar with HH:MM
     * @param cal
     * @return
     */
	@SuppressWarnings("deprecation")
	private Calendar calendarToHM( Calendar cal, int hours, int minutes ) {
    	cal.set( Calendar.HOUR_OF_DAY, hours );
    	cal.set( Calendar.MINUTE, minutes );
    	cal.set( Calendar.YEAR, cal.get(Calendar.YEAR) );
		cal.set( Calendar.MONTH, cal.getTime().getMonth() );
		cal.set( Calendar.DATE, cal.getTime().getDate() );
    	return cal;
    }
	
	/**
	 * Calendar event 
	 * @param cal
	 * @param duration
	 * @return
	 */
    @SuppressWarnings("deprecation")
	private CalendarEventInterface hasCalendarEvent( Calendar cal, int duration ) {
    	
    	int month = cal.getTime().getMonth(), date = cal.getTime().getDate()
    			, timeRLength = getTimeLength( cal )
    			, timeLLength = timeRLength + duration;
    	int calMonth, calDate, calRTimeLength, calLTimeLength;
    	Calendar eventCal;
    	for ( int i = 0; i < calendarEvent.size(); i++ ) {
    		calMonth = calendarEvent.get( i ).getStartTime().getTime().getMonth();
    		calDate = calendarEvent.get( i ).getStartTime().getTime().getDate();
    		eventCal = calendarEvent.get( i ).getStartTime();
    		calRTimeLength = getTimeLength( eventCal );
    		calLTimeLength = calRTimeLength + calendarEvent.get( i ).getDuration();
    		if ( month == calMonth && date == calDate
    				&& ( (timeRLength <= calRTimeLength && timeLLength > calRTimeLength) 
    						|| ( timeRLength >= calRTimeLength && timeRLength < calLTimeLength )
    						|| ( timeRLength <= calRTimeLength && calLTimeLength < timeLLength ) ) ) {
    			return calendarEvent.get( i );
    		} 
    	}
    	
    	return null;
    }
    
    /**
     * there have calendar events before current time
     * @param cal
     * @return
     */
    @SuppressWarnings("unused")
	private void addBefCalendarEvent( Calendar cal ) {
    	ArrayList<CalendarEventInterface> calEventList = new ArrayList<CalendarEventInterface>();
    	int month = cal.get(Calendar.MONTH), date = cal.get(Calendar.DATE)
    			, timeLength = getTimeLength( cal )
    			, calMonth, calDate, calTimeLength;
    	for ( int i = 0; i < calendarEvent.size(); i++ ) {
    		calMonth = calendarEvent.get( i ).getStartTime().get( Calendar.MONTH );
    		calDate = calendarEvent.get( i ).getStartTime().get( Calendar.DATE );
    		calTimeLength = getTimeLength( calendarEvent.get( i ).getStartTime() ) + calendarEvent.get( i ).getDuration();
	    	if ( calDate < date || ( ( calDate == date ) && ( calTimeLength <= timeLength )  ) ) {
	    			plan.add( new StudyBlock( calendarEvent.get( i ).getName(), calendarEvent.get( i ).getStartTime(), calendarEvent.get( i ).getDuration() ) );
	    	}
    	}
    }
	
    /**
     * There have calendar events after current time
     * @param cal
     * @return
     */
    @SuppressWarnings("unused")
	private ArrayList<CalendarEventInterface> addAftCalendarEvent( Calendar cal ) {
    	ArrayList<CalendarEventInterface> calEventList = new ArrayList<CalendarEventInterface>();
    	int month = cal.get(Calendar.MONTH), date = cal.get(Calendar.DATE)
    			, timeLength = getTimeLength( cal )
    			, calMonth, calDate, calTimeLength;
    	for ( int i = 0; i < calendarEvent.size(); i++ ) {
    		calMonth = calendarEvent.get( i ).getStartTime().get( Calendar.MONTH );
    		calDate = calendarEvent.get( i ).getStartTime().get( Calendar.DATE );
    		calTimeLength = getTimeLength( calendarEvent.get( i ).getStartTime() );
    		if ( calDate > date || ( ( calDate == date ) && ( calTimeLength > timeLength ) ) ) {
     			plan.add( new StudyBlock( calendarEvent.get( i ).getName(), calendarEvent.get( i ).getStartTime(), calendarEvent.get( i ).getDuration() ) );
    		}
    	}
    	return calEventList;
    }
    
    private int blockSize = 60;
    private int breakLength = 0;
    @Override
    public void setBlockSize(int size)
    {
    	if ( size < 10 ) 
    		throw new StudyPlannerException( "BelowStandardBlockLength(10)" );
    	blockSize = size;
    }
    
    public int getBlockSize() { return blockSize; }
    @Override
    public void setBreakLength(int i)
    {
    	breakLength = i;
    }
    
    public int getBreakLength() { return breakLength; }

    private Calendar dairyStartStudyTime;
    private Calendar dairyEndStudyTime;
    @SuppressWarnings("deprecation")
	@Override
    public void setDailyStartStudyTime(Calendar startTime)
    {
    	int timeLength = getTimeLength( getDailyEndStudyTime() ) - getTimeLength( startTime );
    	// Throw a StudyPlannerException on any call of those methods if the start time is later than the end time
    	if ( timeLength > getBlockSize() ) {
    		dairyStartStudyTime = calendarToHM( startTime, startTime.getTime().getHours(), startTime.getTime().getMinutes() );
    	} else {
    		throw new StudyPlannerException( "StartTimeAndEndTimeConficts" );
    	}
    	
    }

    @SuppressWarnings("deprecation")
	@Override
    public void setDailyEndStudyTime(Calendar endTime)
    {
    	int timeLength = getTimeLength( endTime ) - getTimeLength( getDailyStartStudyTime() );
    	if ( timeLength > getBlockSize() ) {
    		dairyEndStudyTime = calendarToHM( endTime, endTime.getTime().getHours(), endTime.getTime().getMinutes() );
    	} else {
    		// Throw a StudyPlannerException on any call of those methods if the start time is later than the end time
    		throw new StudyPlannerException( "StartTimeAndEndTimeConficts" );
    	}
    }
    
    @Override
    public Calendar getDailyStartStudyTime()
    {
    	// default start time 9AM
    	if ( dairyStartStudyTime == null ) {
    		dairyStartStudyTime = calendarToHM( Calendar.getInstance(), 9, 0 );
    	}
        return dairyStartStudyTime;
    }

    @Override
    public Calendar getDailyEndStudyTime()
    {
    	// default end time 5PM
    	if ( dairyEndStudyTime == null ) {
    		dairyEndStudyTime = calendarToHM( Calendar.getInstance(), 17, 0 );
    	}
        return dairyEndStudyTime;
    }

    @Override
    public void addCalendarEvent(String eventName, Calendar startTime, int duration)
    {
    	addCalendarEvent( eventName, startTime, duration, CalendarEventType.OTHER );
    }

    @SuppressWarnings({ "deprecation" })
	@Override
    public void addCalendarEvent(String eventName, Calendar startTime, int duration, CalendarEventType type)
    {
    	if ( calendarEvent.size() != 0 ) {
    		for ( int i = 0; i < calendarEvent.size(); i++ ) {
    			// an event that overlaps an existing calendar event, throw a StudyPlannerException 
    			int startTimeLength = getTimeLength( startTime )
    					, eventTimeLength = getTimeLength( calendarEvent.get( i ).getStartTime() );
    			int year = calendarEvent.get( i ).getStartTime().getTime().getYear()
    					, month = calendarEvent.get( i ).getStartTime().getTime().getMonth()
    					, day = calendarEvent.get( i ).getStartTime().getTime().getDay();
    			if ( !( startTimeLength - eventTimeLength >= calendarEvent.get( i ).getDuration()
    					|| eventTimeLength - startTimeLength >= duration )
    					&& year == startTime.getTime().getYear()
    					&& month == startTime.getTime().getMonth()
    					&& day == startTime.getTime().getDay() ) {
    				throw new StudyPlannerException( "CalendarEventOverlap" );
    			}
    		}
			calendarEvent.add( new CalendarEvent( eventName, startTime, duration, type ) );
    	} else {
    		calendarEvent.add( new CalendarEvent( eventName, startTime, duration, type ) );
    	}
    	
    }

    @Override
    public List<CalendarEventInterface> getCalendarEvents()
    {
        return calendarEvent;
    }

    @Override
    public void saveData(OutputStream saveStream)
    {
    	ObjectOutputStream outputStream = null;
		try {
			outputStream = new ObjectOutputStream( saveStream );
		} catch (IOException e) {
			e.printStackTrace();
		}
        try {
        	outputStream.writeObject( plan );
		} catch (IOException e) {
			e.printStackTrace();
		}
        try {
        	outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
        try {
        	outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    /**
     * Write data to the document
     * @param objectOutput
     * @throws IOException
     */
    private void writeObject(ObjectOutputStream objectOutput )
        throws IOException {
    	objectOutput.defaultWriteObject();
    	objectOutput.writeInt( plan.size() );
        for (int i = 0; i < plan.size(); i++) {
        	objectOutput.writeObject( plan.get( i ) );
        }
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public void loadData(InputStream loadStream)
    {
         ObjectInputStream inputStream = null;
		try {
			inputStream = new ObjectInputStream( loadStream );
		} catch (IOException e) {
			e.printStackTrace();
		}
         try {
			plan = (ArrayList<StudyBlockInterface>) inputStream.readObject();
			if ( plan.size() == 0 ) {
				throw new StudyPlannerException( "StreamDataEmpty");
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
         
         try {
        	 inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Read data from document
     * @param objectInput
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readObject(ObjectInputStream objectInput )
        throws IOException, ClassNotFoundException  {
    	objectInput.defaultReadObject();
        int  planSize = objectInput.readInt();
        plan = new ArrayList<StudyBlockInterface>();
        for (int i = 0; i < planSize; i++) {
        	plan.add( (StudyBlockInterface) objectInput.readObject() );
        }
    }
}
