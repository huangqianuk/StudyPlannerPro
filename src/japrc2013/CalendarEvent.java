/**
 * Examination candidate number: Y1041916
 */
package japrc2013;
import japrc2013.StudyPlannerInterface.CalendarEventType;

import java.util.Calendar;

public class CalendarEvent implements CalendarEventInterface {

	private String name;
	private Calendar startTime;
	private int duration;
	private CalendarEventType type;
	
	public CalendarEvent( String name, Calendar startTime, int duration ) {
		this.name = name;
		this.startTime = startTime;
		this.duration = duration;
	}
	
	public CalendarEvent( String name, Calendar startTime, int duration, CalendarEventType type ) {
		this.name = name;
		this.startTime = startTime;
		this.duration = duration;
		this.type = type;
	}
	@Override
	public String getName() {
		return name;
	}

	@Override
	public Calendar getStartTime() {
		return startTime;
	}

	@Override
	public int getDuration() {
		return duration;
	}

	@Override
	public boolean isValidTopicTarget() {
	
		if ( type == CalendarEventType.ESSAY || type == CalendarEventType.EXAM )
			return true;
		return false;
	}
	
	public CalendarEventType getType() {
		return type;
	}

}
