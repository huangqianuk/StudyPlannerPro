/**
 * Examination candidate number: Y1041916
 */
package japrc2013;

import java.io.Serializable;
import java.util.Calendar;


@SuppressWarnings("serial")
public class StudyBlock implements StudyBlockInterface, Serializable
{
    private String subject;
    private Calendar startTime;
    private int duration;
    private int hours;
    private int minutes;
    
    @SuppressWarnings("deprecation")
	public StudyBlock(String subject, Calendar startTime, int duration)
    {
        this.subject = subject;
        this.startTime = startTime;
        this.duration = duration;
        hours = startTime.getTime().getHours();
        minutes = startTime.getTime().getMinutes();
    }

    @Override
    public String getTopic()
    {
        return subject;
    }

    @Override
    public int getDuration()
    {
        return duration;
    }

    
    @Override
    public Calendar getStartTime()
    {
    	startTime.set( Calendar.HOUR_OF_DAY, hours );
    	startTime.set( Calendar.MINUTE, minutes );
        return startTime;
    }
}
