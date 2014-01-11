/**
 * Examination candidate number: Y1041916
 */
package japrc2013;

import java.io.Serializable;


@SuppressWarnings("serial")
public class Topic implements TopicInterface, Serializable
{
    private String subject;
    private int duration;
    private CalendarEventInterface target;
    
    public Topic(String name, int duration)
    {
        this.subject = name;
        this.duration = duration;
    }

    @Override
    public String getSubject()
    {
        return subject;
    }

    @Override
    public int getDuration()
    {
        return duration;
    }

    @Override
    public void setTargetEvent(CalendarEventInterface target)
    {
    	if ( !target.isValidTopicTarget() ) {
    		throw new StudyPlannerException( "TargetEventType" );
    	} else {
    		this.target = target;
    	}
    }

    @Override
    public CalendarEventInterface getTargetEvent()
    {
        return target;
    }
}
