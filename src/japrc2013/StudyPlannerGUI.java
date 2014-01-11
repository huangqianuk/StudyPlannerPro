/**
 * Examination candidate number: Y1041916
 */
package japrc2013;
import japrc2013.StudyPlannerInterface.CalendarEventType;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public final class StudyPlannerGUI extends JFrame implements StudyPlannerGUIInterface, ActionListener
{
	// main components
	private JPanel mainPanel, menuPanel, topicsPanel, eventsPanel, planPanel, targetPanel;
	
	private JButton jbtTopics, jbtEvents, jbtPlan, jbtCalDisplay, jbtLoad, jbtSave, jbtExit;
	
	@SuppressWarnings("rawtypes")
	private DefaultListModel topicsModel, eventsModel, planModel;
	
	@SuppressWarnings("rawtypes")
	private JList jstTopics, jstEvents, jstPlan;
	private JScrollPane topicsJSP, eventsJSP, planJSP;
	
	// topics components
	private JButton jbtTopDelete, jbtTopAdd;
	private JLabel jlblTopSubject, jlblTopDuration, jlblPlan, jlblTopAddWrong, jlblTopDeleteWrong;
	private JTextField jtxtTopSubject, jtxtTopDuration;
	
	// event components
	private JLabel jlblEveName, jlblEveDuration, jlblEveType, jlblEveDate, jlblEveTime;
	private JLabel jlblHour, jlblMinute, jlblYear, jlblMonth, jlblDay, jlblEventWrong;
	private JTextField jtxtEveName, jtxtEveDuration;
	@SuppressWarnings("rawtypes")
	private JList jstEveHour, jstEveMinute, jstEveDay, jstEveYear, jstEveMonth, jstEveType, jstEvent;
	@SuppressWarnings("rawtypes")
	private DefaultListModel hourModel, minuteModel, yearModel, monthModel, dayModel, typeModel, eventModel;
	private JScrollPane hourJSP, minuteJSP, yearJSP, monthJSP, dayJSP, typeJSP, eventJSP;
	private JButton jbtEveAdd, jbtSetTarget;
	private String monthS[] = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug","Sep", "Oct", "Nov", "Dec" };
	private JLabel[] weekLabel, timeLabel;
	
	// target components
	@SuppressWarnings("rawtypes")
	private JList jstTarTopic, jstTarEvent;
	@SuppressWarnings("rawtypes")
	private DefaultListModel jstTarTopModel, jstTarEveModel;
	private JScrollPane tarTopicJSP, tarEventJSP;
	private JLabel jlblTarTopic, jlblTarEvent, jlblTarWrong;
	private JButton jbtTarSave;
	
	// calendar event and play as google calendar
	private JFrame tableJFrame;
	private JPanel btnTblPanel;
	private JButton jbtnNextWeek, jbtnPreWeek;
	private JScrollPane tableJSP;
	private JPanel tablePanel = new JPanel( new GridLayout( 9, 8 ) );
	private int[] dateValue = new int[ 8 ];
	private int[] timeValue = new int[ 9 ];
    private StudyPlanner planner;
    private final String TOPICS = "topicsPanel";
    private final String EVENTS = "eventsPanel";
    private final String PLAN = "planPanel";
    private final String TARGET = "targetPanel";
    private final String[] TYPE_VALUE = {"Exam", "Essay hand in", "Other" };
    private Calendar googleCalendar = Calendar.getInstance();
    
    // google calendar add event  
    private JFrame addEventFrame;
	private JPanel addEventPanel;
	private JLabel jlblAEName, jlblAEDuration, jlblAEType, jlblTime, jlblAEWrong;
	private JTextField jtxtAEName, jtxtAEDuration;
	@SuppressWarnings("rawtypes")
	private DefaultListModel addEventModel;
	@SuppressWarnings("rawtypes")
	private JList jstAEType;
	private JScrollPane addEventJSP;
	private JButton jbtAESave, jbtAECancel;
	// popup menu
	private JPopupMenu popupMenu;
	private JMenuItem popupItem;
	// popup item click new window
	private JFrame timeFrame = new JFrame();;
	private JPanel timePanel = new JPanel( new GridLayout( 1, 2 ) ); 
	private JLabel jlblTimer, jlblTimeCount;
	private int timeCount = 0;
	private TimeCountDown timeThread = new TimeCountDown();
	private int timeFrameCount = 0;
	// store JPanel for lisetening click event
    private ArrayList<CalendarEventList> calendarAndTypeList = new ArrayList<CalendarEventList>();
    private ArrayList<EventPanelList> eventPanelList = new ArrayList<EventPanelList>();
    private ArrayList<TopicPanelList> topicPanelList = new ArrayList<TopicPanelList>();
	
    public StudyPlannerGUI(StudyPlanner simToUse)
    {
    	this.planner= simToUse;
    	initComponents();
    }
     
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private void initComponents() {
    	// panels
    	mainPanel = new JPanel( new CardLayout() );
    	menuPanel = new JPanel();
    	menuPanel.setName( "menuPanel" );
    	topicsPanel = new JPanel();
    	topicsPanel.setName( "topicsPanel" );
    	eventsPanel = new JPanel();
    	planPanel = new JPanel();
    	planPanel.setName( "planPanel" );
    	targetPanel = new JPanel();
    	
    	mainPanel.setBackground( new Color( 238, 232, 231 ) );
    	menuPanel.setBackground( new Color( 233, 204, 231 ) );
    	topicsPanel.setBackground( new Color( 204, 204, 255 ) );
    	eventsPanel.setBackground( new Color( 204, 235, 204 ) );
    	planPanel.setBackground( new Color( 218, 240, 236 ) );
    	targetPanel.setBackground( new Color( 196, 215, 255 ) );
    	
    	mainPanel.setLayout( new CardLayout() );
    	mainPanel.add( topicsPanel, TOPICS );
    	mainPanel.add( eventsPanel, EVENTS );
    	mainPanel.add( planPanel, PLAN );
    	mainPanel.add( targetPanel, TARGET );
    	
    	
    	// buttons
    	jbtTopics = new JButton( "Topics" );
    	jbtEvents = new JButton( "Events" );
    	jbtPlan = new JButton( "Plan" );
    	jbtPlan.setName( "jbtPlan" );
    	jbtCalDisplay = new JButton( "Calender" );
    	jbtLoad = new JButton( "Load Data" );
    	jbtSave = new JButton( "Save Data" );
    	jbtExit = new JButton( "Exit Program" );
    	jbtTopics.addActionListener( this );
    	jbtEvents.addActionListener( this );
    	jbtCalDisplay.addActionListener( this );
    	jbtLoad.addActionListener( this );
    	jbtSave.addActionListener( this );
    	jbtExit.addActionListener( this );
    	
    	// instantiate the list models to be initially empty
    	topicsModel = new DefaultListModel();
    	eventsModel = new DefaultListModel();
    	planModel = new DefaultListModel();
    	
    	// jList
    	jstTopics = new JList( topicsModel );
    	jstTopics.setName( "jstTopics" );
    	jstEvents = new JList( eventsModel );
    	jstPlan = new JList( planModel );
    	
    	// scroll panes
    	topicsJSP = new JScrollPane();
    	topicsJSP.setName( "topicsJSP" );
    	eventsJSP = new JScrollPane();
    	planJSP = new JScrollPane();
    	topicsJSP.setViewportView( jstTopics );
    	eventsJSP.setViewportView( jstEvents );
    	planJSP.setViewportView( jstPlan );
    	planJSP.setName( "planJSP" );
    	
    	// topics components
    	jbtTopDelete = new JButton( "Delete a topic" );
    	jbtTopDelete.setName( "jbtTopDelete" );
    	jbtTopAdd = new JButton( "Create a topic" );
    	jbtTopAdd.setName( "jbtTopAdd" );
    	jlblTopSubject = new JLabel( "Subject" );
    	jlblTopSubject.setFont( new Font( "Tahoma", 1, 12) );
    	jlblTopDuration = new JLabel( "Duration (minutes) " );
    	jlblTopDuration.setFont( new Font( "Tahoma", 1, 12) );
    	jlblPlan = new JLabel( "Study Plan" );
    	jlblPlan.setFont( new Font( "Tahoma", 1, 18 ) );
    	jlblPlan.setHorizontalAlignment( SwingConstants.CENTER );
    	jlblTopAddWrong = new JLabel();
    	jlblTopAddWrong.setFont( new Font( "Tahoma", 1, 14 ) ); 
    	jlblTopAddWrong.setForeground( Color.RED );
    	jlblTopDeleteWrong = new JLabel();
    	jlblTopDeleteWrong.setFont( new Font( "Tahoma", 1, 14 ) ); 
    	jlblTopDeleteWrong.setForeground( Color.RED );
    	jtxtTopSubject = new JTextField( 5 );
    	jtxtTopSubject.setName( "jtxtTopSubject" );
    	jtxtTopDuration = new JTextField( 5 );
    	jtxtTopDuration.setName( "jtxtTopDuration" );
    	jbtTopDelete.addActionListener( this );
    	jbtTopAdd.addActionListener( this );
    	jbtPlan.addActionListener( this );
    	
    	// Event components
    	jlblEventWrong = new JLabel();
    	jlblEventWrong.setFont( new Font( "Tahoma", 1, 14 ) ); 
    	jlblEventWrong.setForeground( Color.RED );
    	jlblEveName = new JLabel( "Name:" );
    	jlblEveName.setFont( new Font("Tahoma", 1, 14) );
    	jlblEveDuration = new JLabel( "Duration:" );
    	jlblEveDuration.setFont( new Font("Tahoma", 1, 14) );
    	jlblEveType = new JLabel( "Type:" );
    	jlblEveType.setFont( new Font( "Tahoma", 1, 14) );
    	jlblEveDate = new JLabel( "Date:" );
    	jlblEveDate.setFont( new Font( "Tahoma", 1, 14) );
    	jlblEveTime = new JLabel( "Time:" );
    	jlblEveTime.setFont( new Font("Tahoma", 1, 14) );
    	jlblHour = new JLabel( "hour" );
    	jlblHour.setFont( new Font( "Tahoma", 1, 12 ) );
    	jlblMinute = new JLabel( "minute" );
    	jlblMinute.setFont( new Font( "Tahoma", 1, 12 ) );
    	jlblYear = new JLabel( "year" );
    	jlblYear.setFont( new Font("Tahoma", 1, 12 ) );
    	jlblMonth = new JLabel( "month" );
    	jlblMonth.setFont( new Font( "Tahoma", 1, 12 ) );
    	jlblDay = new JLabel( "day" );
    	jlblDay.setFont( new Font("Tahoma", 1, 12 ) );
    	jtxtEveName = new JTextField( 5 );
    	jtxtEveDuration = new JTextField( 5 );
    	yearModel = new DefaultListModel();
    	monthModel = new DefaultListModel();
    	dayModel = new DefaultListModel();
    	hourModel = new DefaultListModel();
    	minuteModel = new DefaultListModel();
    	typeModel = new DefaultListModel();
    	eventModel = new DefaultListModel();
    	for ( int i = 2013; i < 2016; i++ )
    		yearModel.addElement( i );
    	
    	for ( int i = 0; i < 12; i++ )
    		monthModel.addElement( monthS[ i ] );
    	
    	for ( int i = 1; i < 32; i++ )
    		dayModel.addElement( i );
    	
    	for( int i = 9; i < 18; i++ )
    		hourModel.addElement( i );
    	
    	for ( int i = 0; i <= 60; i++ )
    		minuteModel.addElement( i );
    	
    	for ( int i = 0; i < TYPE_VALUE.length; i++ ) 
    		typeModel.addElement( TYPE_VALUE[ i ] );
    	
    	jstEveYear = new JList( yearModel );
    	jstEveMonth = new JList( monthModel );
    	jstEveDay = new JList( dayModel );
    	jstEveHour = new JList( hourModel );
    	jstEveMinute = new JList( minuteModel );
    	jstEveType = new JList( typeModel );
    	jstEvent = new JList( eventModel );
    	yearJSP = new JScrollPane( jstEveYear );
    	monthJSP = new JScrollPane( jstEveMonth );
    	dayJSP = new JScrollPane( jstEveDay );
    	hourJSP = new JScrollPane( jstEveHour );
    	minuteJSP = new JScrollPane( jstEveMinute );
    	typeJSP = new JScrollPane( jstEveType );
    	eventJSP = new JScrollPane( jstEvent );
    	jbtEveAdd = new JButton( "Create a calendar event" );
    	jbtEveAdd.addActionListener( this );
    	jbtSetTarget = new JButton( "Set target" );
    	jbtSetTarget.addActionListener( this );
    	
    	// target components
    	jlblTarWrong = new JLabel();
    	jlblTarWrong.setFont( new Font( "Tahoma", 1, 14 ) ); 
    	jlblTarWrong.setForeground( Color.RED );
    	jlblTarTopic = new JLabel( "Topics:" );
		jlblTarEvent = new JLabel( "Event:" );
		jstTarTopModel = new DefaultListModel();
		jstTarEveModel = new DefaultListModel();
		jstTarTopic = new JList( jstTarTopModel );
		jstTarEvent = new JList( jstTarEveModel );
		tarTopicJSP = new JScrollPane( jstTarTopic );
		tarEventJSP = new JScrollPane( jstTarEvent );
		jbtTarSave = new JButton( "Save target" );
		jbtTarSave.addActionListener( this );
		
		// Google Calendar components
		btnTblPanel = new JPanel();
		jbtnNextWeek = new JButton( "Next Week" );
		jbtnPreWeek = new JButton( "Previous Week" );
		jbtnNextWeek.setFont( new Font( "Tahoma", 1, 16 ) ); 
		jbtnPreWeek.setFont( new Font( "Tahoma", 1, 16 ) ); 
		tableJFrame = new JFrame();
		tableJFrame.add( btnTblPanel );
		tableJSP = new JScrollPane();
		jbtnNextWeek.addActionListener( this );
		jbtnPreWeek.addActionListener( this );
	    		
    	// topics panel
    	GroupLayout topicsPanelLayout = new GroupLayout( topicsPanel );
    	topicsPanel.setLayout( topicsPanelLayout );
    	topicsPanelLayout.setHorizontalGroup(
    		topicsPanelLayout.createSequentialGroup()
    		.addGap( 5 )
    		.addGroup( topicsPanelLayout.createSequentialGroup()
    			.addGroup( topicsPanelLayout.createParallelGroup()
    				.addComponent( topicsJSP, GroupLayout.PREFERRED_SIZE, 357, GroupLayout.PREFERRED_SIZE ).addGap( 5 )
    				.addGroup( topicsPanelLayout.createSequentialGroup()
    						.addComponent( jbtTopDelete ).addGap( 10 )
    						.addComponent( jlblTopDeleteWrong, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE ).addGap( 10 )) )
    			.addGap( 10 )
	    		.addGroup( topicsPanelLayout.createParallelGroup( GroupLayout.Alignment.LEADING )
	    			.addComponent( jlblTopAddWrong, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE ).addGap( 10 )
    				.addComponent( jlblTopSubject, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE ).addGap( 10 )
    				.addComponent( jtxtTopSubject, GroupLayout.PREFERRED_SIZE, 220, GroupLayout.PREFERRED_SIZE ).addGap( 20 )
    				.addComponent( jlblTopDuration, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE ) .addGap( 10 )
    				.addComponent( jtxtTopDuration, GroupLayout.PREFERRED_SIZE, 220, GroupLayout.PREFERRED_SIZE )
    				.addComponent( jbtTopAdd ).addGap( 10 ) ) 
    	) );
    	topicsPanelLayout.setVerticalGroup(
    		topicsPanelLayout.createSequentialGroup()
    		.addGap( 10 )
    		.addGroup( topicsPanelLayout.createSequentialGroup()
    			.addGroup( topicsPanelLayout.createParallelGroup()	
    			.addComponent( topicsJSP, GroupLayout.PREFERRED_SIZE, 357, GroupLayout.PREFERRED_SIZE)
    			
	    		.addGroup( topicsPanelLayout.createSequentialGroup()
	    			.addGap( 200 )
	    			.addComponent( jlblTopAddWrong ).addGap( 10 )
	    			.addComponent( jlblTopSubject ).addGap( 10 )
	    			.addComponent( jtxtTopSubject, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE ).addGap( 20 )
	    			.addComponent( jlblTopDuration )
	    			.addComponent( jtxtTopDuration, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE ) ) )
	    		.addGap( 10 )
	    		.addGroup( topicsPanelLayout.createParallelGroup()
	        		.addComponent( jbtTopDelete).addGap( 10 ) 
	        		.addComponent( jlblTopDeleteWrong).addGap( 10 ) 
	    			.addComponent( jbtTopAdd)).addGap( 10 )
	    			)
    		);
    	
    	// plan panel
    	GroupLayout planPanelLayout = new GroupLayout( planPanel );
    	planPanel.setLayout( planPanelLayout );
    	planPanelLayout.setHorizontalGroup(
    		planPanelLayout.createSequentialGroup()
    			.addGroup( planPanelLayout.createParallelGroup() 
    				.addComponent( jlblPlan )
    				.addComponent( planJSP, GroupLayout.PREFERRED_SIZE, 650, GroupLayout.PREFERRED_SIZE ) ) );
    	planPanelLayout.setVerticalGroup(
    		planPanelLayout.createParallelGroup()
    			.addGap( 50 )
    			.addGroup(planPanelLayout.createSequentialGroup()
	    			.addGap( 20 )
	    			.addComponent( jlblPlan ).addGap( 10 )
	    			.addComponent( planJSP, GroupLayout.PREFERRED_SIZE, 380, GroupLayout.PREFERRED_SIZE ) ) );
    	
    	// event panel
    	GroupLayout eventsPanelLayout = new GroupLayout( eventsPanel );
    	eventsPanel.setLayout( eventsPanelLayout );
    	eventsPanelLayout.setHorizontalGroup(
    		eventsPanelLayout.createSequentialGroup()
    		.addGap( 40 )
    		.addGroup( eventsPanelLayout.createParallelGroup()
    			.addComponent( jlblEveName )
    			.addComponent( jlblEveDuration )
    			.addComponent( jlblEveType )
    			.addComponent( jlblEveDate )
    			.addComponent( jlblEveTime ) )
    		.addGap( 30 )
    		.addGroup( eventsPanelLayout.createParallelGroup()
    			.addComponent( jtxtEveName, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE )
    			.addComponent( jtxtEveDuration, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE )
    			.addComponent( typeJSP, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE )
    			.addGroup( eventsPanelLayout.createSequentialGroup()
    				.addComponent( jlblDay ).addGap( 10 )
    				.addComponent( dayJSP, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE ).addGap( 20 ) )
    			.addGroup( eventsPanelLayout.createSequentialGroup()
    				.addComponent( jlblHour ).addGap( 10 )
    				.addComponent( hourJSP, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE  ).addGap( 20 ) ) )
    		.addGroup( eventsPanelLayout.createParallelGroup() 
        			.addComponent( jlblEventWrong ).addGap( 10 )
    				.addComponent( eventJSP, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE )
    				.addGroup( eventsPanelLayout.createSequentialGroup()
						.addComponent( jlblMonth ).addGap( 10 )
	    				.addComponent( monthJSP, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE ).addGap( 20 )
	    				.addComponent( jlblYear ).addGap( 10 )
	    				.addComponent( yearJSP, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE  ) )
	    			.addGroup( eventsPanelLayout.createSequentialGroup()	 
	    				.addComponent( jlblMinute ).addGap( 10 )
	    				.addComponent( minuteJSP, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE  ).addGap( 10 ) 
	    				.addGroup( eventsPanelLayout.createParallelGroup()
	    						.addComponent( jbtEveAdd ).addGap( 10 )
	    						.addComponent( jbtSetTarget ) ) )
	    			 )
    	);
    	eventsPanelLayout.setVerticalGroup(
    		eventsPanelLayout.createSequentialGroup()
    		.addGap( 20 )
    		.addGroup( eventsPanelLayout.createParallelGroup()
    			.addGroup( eventsPanelLayout.createSequentialGroup()
		    		.addGroup( eventsPanelLayout.createParallelGroup()
		    			.addComponent( jlblEveName ).addGap( 20 )
		    			.addComponent( jtxtEveName, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE  ).addGap( 50 ) ) 
		    		.addGroup( eventsPanelLayout.createParallelGroup()
		    			.addComponent( jlblEveDuration ).addGap( 20 ) 
		    			.addComponent( jtxtEveDuration, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE  ).addGap( 50 ) )
		    		.addGroup( eventsPanelLayout.createParallelGroup()
		    			.addComponent( jlblEveType ).addGap( 20 )
		    			.addComponent( typeJSP, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE ).addGap( 70 ) ) )
		    	
				.addGroup( eventsPanelLayout.createSequentialGroup()
    					.addComponent( jlblEventWrong ).addGap( 10 ) 
		    			.addComponent( eventJSP, GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE ) ).addGap( 20 ) ).addGap( 10 )
    		.addGroup( eventsPanelLayout.createParallelGroup()
    			.addComponent( jlblEveDate ).addGap( 20 )
    			.addGroup( eventsPanelLayout.createParallelGroup()
    				.addComponent( jlblDay )
    				.addComponent( dayJSP, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE ).addGap( 40) 
    				.addComponent( jlblMonth )
    				.addComponent( monthJSP, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE ).addGap( 40)
    				.addComponent( jlblYear )
    				.addComponent( yearJSP, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE ).addGap( 40)) ).addGap( 20) 
			.addGroup( eventsPanelLayout.createParallelGroup()
	    			.addComponent( jlblEveTime ).addGap( 20 )
	    			.addGroup( eventsPanelLayout.createParallelGroup()
	    				.addComponent( jlblHour )
	    				.addComponent( hourJSP, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE ).addGap( 50)
	    				.addComponent( jlblMinute )
	    				.addComponent( minuteJSP, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE ).addGap( 50 )
	    				.addGroup( eventsPanelLayout.createSequentialGroup()
	    						.addComponent( jbtEveAdd ).addGap( 15 )
	    						.addComponent( jbtSetTarget ) )
	    				 ) )
    	);
    	
    	// target panel
    	GroupLayout targetLayout = new GroupLayout( targetPanel );
		targetPanel.setLayout( targetLayout );
		
		targetLayout.setHorizontalGroup(
				targetLayout.createParallelGroup().addGap( 30 )
				.addGroup( targetLayout.createSequentialGroup()
						.addGap( 30 ).addComponent( jlblTarWrong, GroupLayout.PREFERRED_SIZE, 350, GroupLayout.PREFERRED_SIZE ))
				.addGroup( targetLayout.createSequentialGroup()
						.addGap( 30 ).addComponent( jlblTarTopic ).addGap( 10 )
						.addComponent( tarTopicJSP, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE ).addGap( 40 )
						.addComponent( jlblTarEvent ).addGap( 10 )
						.addComponent( tarEventJSP, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE ).addGap( 40 )
						.addComponent( jbtTarSave ).addGap( 10 )
						) );
		targetLayout.setVerticalGroup( 
				targetLayout.createParallelGroup()
				.addGroup( targetLayout.createSequentialGroup()
				.addGap( 60 )
				.addGroup( targetLayout.createSequentialGroup() 
				.addGroup( targetLayout.createParallelGroup()
					.addComponent( jlblTarWrong, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE )).addGap( 10 )
				.addGroup( targetLayout.createParallelGroup()
					.addGroup( targetLayout.createParallelGroup()
							.addComponent( jlblTarTopic ) )
					.addGroup( targetLayout.createParallelGroup()
							.addComponent( tarTopicJSP, GroupLayout.PREFERRED_SIZE, 255, GroupLayout.PREFERRED_SIZE ) ).addGap( 10 )
					.addGroup( targetLayout.createParallelGroup()
							.addComponent( jlblTarEvent ) )
					.addGroup( targetLayout.createParallelGroup()
							.addComponent( tarEventJSP, GroupLayout.PREFERRED_SIZE, 255, GroupLayout.PREFERRED_SIZE ) ).addGap( 10 ) 
					.addGroup( targetLayout.createParallelGroup()
							.addComponent( jbtTarSave ) ) ) ) ) );
    	

		// Google Calendar panel
		GroupLayout btnTblPanelLayout = new GroupLayout( btnTblPanel );
		btnTblPanel.setLayout( btnTblPanelLayout );
		btnTblPanelLayout.setHorizontalGroup(
				btnTblPanelLayout.createSequentialGroup()
				.addGroup(btnTblPanelLayout.createParallelGroup()
					.addGroup(btnTblPanelLayout.createSequentialGroup()
						.addComponent( jbtnPreWeek, GroupLayout.PREFERRED_SIZE, 600, GroupLayout.PREFERRED_SIZE  )
						.addComponent( jbtnNextWeek, GroupLayout.PREFERRED_SIZE, 600, GroupLayout.PREFERRED_SIZE )) 
					.addGroup(btnTblPanelLayout.createSequentialGroup()
						.addComponent( tableJSP, GroupLayout.PREFERRED_SIZE, 1185, GroupLayout.PREFERRED_SIZE ) )
				));
		btnTblPanelLayout.setVerticalGroup(
				btnTblPanelLayout.createSequentialGroup()
				.addGroup(btnTblPanelLayout.createSequentialGroup()
					.addGroup(btnTblPanelLayout.createParallelGroup()
							.addComponent( jbtnPreWeek, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE  )
							.addComponent( jbtnNextWeek, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE )
						) 
					.addGroup(btnTblPanelLayout.createSequentialGroup()
							.addComponent( tableJSP, GroupLayout.PREFERRED_SIZE, 600, GroupLayout.PREFERRED_SIZE ) ) ) );
	    		
		
    	// main Panel
    	GroupLayout layout = new GroupLayout( getContentPane() );
    	getContentPane().setLayout( layout );
    	layout.setHorizontalGroup(
			layout.createSequentialGroup( )
				.addGap( 5 )
				.addGroup( layout.createParallelGroup( GroupLayout.Alignment.LEADING )
					.addGap( 5 )
					.addGroup( layout.createSequentialGroup()
						.addComponent( jbtTopics ).addGap( 10 )
						.addComponent( jbtEvents ).addGap( 10 )
						.addComponent( jbtPlan ).addGap( 10 )
						.addComponent( jbtCalDisplay ).addGap( 10 )
						.addComponent( jbtLoad ).addGap( 10 )
						.addComponent( jbtSave ).addGap( 10 )
						.addComponent( jbtExit ).addGap( 5 )
					)
				.addComponent( mainPanel, GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE) )
				.addGap( 5 )
    	);
    	
    	layout.setVerticalGroup(
    		layout.createSequentialGroup()
    		.addGap( 10 )
    		.addGroup( layout.createParallelGroup()
    				.addGroup( layout.createParallelGroup()
    					.addComponent( jbtTopics ) )
					.addGroup( layout.createParallelGroup()
	    					.addComponent( jbtEvents ) )
					.addGroup( layout.createParallelGroup()
					.addComponent( jbtPlan ) )
					.addGroup( layout.createParallelGroup()
	    					.addComponent( jbtCalDisplay ) )
					.addGroup( layout.createParallelGroup()
	    					.addComponent( jbtLoad ) )
					.addGroup( layout.createParallelGroup()
	    					.addComponent( jbtSave ) )
					.addGroup( layout.createParallelGroup()
	    					.addComponent( jbtExit ) ) 
	    		)
	    		.addGap( 10 )
	    		.addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
	    );
    	
    	pack();
    }
    @Override
    public void notifyModelHasChanged()
    {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                updateDisplay();
            }
        });
    }
    
    private void updateDisplay()
    {       
    	if(planner == null)
        {
            //nothing to update from, so do nothing
        }
        else
        {
        	displayTopics();
        	displayEvents();
        }
    }
    
    /**
     * add topics to JLIst
     */
    @SuppressWarnings("unchecked")
	public void displayTopics() {
    	ArrayList<TopicInterface> topic = (ArrayList<TopicInterface>) planner.getTopics();
    	topicsModel.removeAllElements();
    	for ( TopicInterface t : topic ) {
    		topicsModel.addElement( t.getSubject() + " ( " + t.getDuration() + " )" );
    	}
    	jstTopics.setSelectedIndex( 0 );
    }
    
    /**
     * delete a topic
     * @param topic keyword
     */
    private void deleteTopic() {

		Object topic = jstTopics.getSelectedValue();
		if ( topic == null ) 
			jlblTopDeleteWrong.setText( "Please select a topic!" );
		else {
	    	String subject = topic.toString().replace( " (", ";").split( ";")[ 0 ]; 
			planner.deleteTopic( subject );
			updateDisplay(); 
			jlblTopDeleteWrong.setText( "" );
		}
    }
    
    private void addTopic() {
    	String subject = jtxtTopSubject.getText()
				, dur = jtxtTopDuration.getText().toString();
		if ( subject.isEmpty() || dur.isEmpty() ) {
			jlblTopAddWrong.setText( "Please input the values!" );
		} else {
			try {
				int duration = Integer.parseInt( dur );
				jlblTopAddWrong.setText("");
				planner.addTopic( subject, duration);
				updateDisplay(); 
				jtxtTopSubject.setText( "" );
				jtxtTopDuration.setText( "" );
				jtxtTopSubject.grabFocus();
			} catch ( NumberFormatException e1 ) {
				jlblTopAddWrong.setText( "Wrong time length value e.g. 60." );
				jtxtTopDuration.grabFocus();
			} catch ( StudyPlannerException e1 ) {
				jlblTopAddWrong.setText( "Two topics with the same name!" );
			}
		}
    }
    
    /**
     * display plan
     */
	@SuppressWarnings({ "unchecked", "deprecation" })
	private void displayPlan() {
		try {
			planner.generateStudyPlan();
		} catch ( StudyPlannerException e ) {
			
		}

		ArrayList<StudyBlockInterface> plan = (ArrayList<StudyBlockInterface>) planner.getStudyPlan();
    	planModel.removeAllElements();
    	int hours, minutes;
    	String time;
    	for ( StudyBlockInterface s : plan ) {
    		hours = s.getStartTime().getTime().getHours();
    		minutes = s.getStartTime().getTime().getMinutes();
    		if ( hours < 10 ) time = "0" + hours;
    		else time = "" + hours;
    		
    		if ( minutes < 10 ) time += ":0" + minutes;
    		else time += ":" + minutes;
    		
    		time += " " + s.getStartTime().get(Calendar.DATE) + "/" + s.getStartTime().get(Calendar.MONTH)+ "/" + s.getStartTime().get(Calendar.YEAR);
    		planModel.addElement( "  " + time + "  " + s.getTopic() );
    	}
    }
	
	/**
	 * display calendar event
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	private void displayEvents() {
		ArrayList<CalendarEventInterface> event = (ArrayList<CalendarEventInterface>)planner.getCalendarEvents();
		eventModel.removeAllElements();
		if( event.size() != 0 ) {
			String time;
			for ( CalendarEventInterface c : event ) {
				time =  c.getStartTime().getTime().getHours() + ":" + c.getStartTime().getTime().getMinutes();
				eventModel.addElement( " " + c.getName() + "  (start at: " + time + " last: "+ c.getDuration() + "min)");
			}
		}
	}
	
	/**
	 * create a calendar event
	 */
	private void addCalendarEvent() {
		String name = jtxtEveName.getText();
		String dur = jtxtEveDuration.getText();
		String typeS = (String)jstEveType.getSelectedValue();
		Object yearO =  jstEveYear.getSelectedValue()
				, monthO = jstEveMonth.getSelectedValue()
				, dayO = jstEveDay.getSelectedValue()
				, hourO = jstEveHour.getSelectedValue()
				, minuteO = jstEveMinute.getSelectedValue(); 
		if ( name.isEmpty() || dur.isEmpty() ) {
			jlblEventWrong.setText( "Please input the values!" );
		} else if ( typeS == null || yearO == null || monthO == null || dayO == null || hourO == null || minuteO == null ) {
			jlblEventWrong.setText( "Please Select date and time!" );
		} else {
			int duration = 0, year = 0, month = 0, day = 0, hour = 0, minute = 0;
			CalendarEventType type;
			if ( typeS.equals( "Exam" ) ) {
				type = CalendarEventType.EXAM;
			} else if ( typeS.equals( "Essay hand in") ) {
				type = CalendarEventType.ESSAY;
			} else {
				type = CalendarEventType.OTHER;
			}
			try {
				duration = Integer.parseInt( dur );
				year = (int)yearO;
				for ( int i = 0; i < 12; i++ ) {
					if ( monthO.equals( monthS[ i ] ) )
						month = i;
				}
				day = (int)dayO;
				hour = (int)hourO;
				minute = (int)minuteO;
				Calendar cal = Calendar.getInstance();
				cal.set( Calendar.YEAR, year );
				cal.set( Calendar.MONTH, month );
				cal.set( Calendar.DAY_OF_MONTH, day );
				cal.set( Calendar.HOUR_OF_DAY, hour );
				cal.set( Calendar.MINUTE, minute );
				try {
					// to store type and calendar event
					calendarAndTypeList.add( new CalendarEventList( new CalendarEvent( name, cal, duration, type ), type ) );
					planner.addCalendarEvent( name, cal, duration, type );	
					jlblEventWrong.setText( "" );
					jtxtEveName.grabFocus();
				} catch ( StudyPlannerException e ) {
					jlblEventWrong.setText( "Calendat event has overlaped!!" );
				}
			} catch ( NumberFormatException e ) {
				jlblEventWrong.setText( "Wrong time length value e.g. 60." );
			}
						
		}
	}
	
	
	/**
	 * display google calendar
	 */
	@SuppressWarnings("deprecation")
	public void dispalyGoogleCal( Calendar ca ) { 
		tablePanel.removeAll();
		String[] weekString = { "", "Sun", "Mon", "Tues", "Wed", "Thu", "Fri", "Sat" };
		int weekNumb = ca.get( Calendar.DAY_OF_WEEK );
		int date = ca.getTime().getDate();
		// get plan
		weekLabel = new JLabel[ 8 ];
		weekLabel[ 0 ] = new JLabel( "" );
		weekLabel[ 0 ].setBackground( new Color( 123, 231, 234 ) );
		timeLabel = new JLabel[ 9 ];
		// create week labels
		for ( int i = 1; i < 8; i++ ) {
			if ( i != weekNumb ) {
				int dayLength = i - weekNumb;
				ca.add( Calendar.DATE, dayLength);
				date = ca.getTime().getDate();
				ca.add( Calendar.DATE, -dayLength);
			} else {
				date = ca.getTime().getDate();
			}
			dateValue[ i ] = date;
			if ( date < 10 ) {
				weekString[ i ] = weekString[ i ] + " 0" + date;
			} else {
				weekString[ i ] = weekString[ i ] + " " + date;
			}
			weekLabel[ i ] = new JLabel( weekString[ i ] );
		}
		// create time labels
		for ( int i = 0; i < 9; i++ ) {
			if ( i == 0 ) timeLabel[ i ] = new JLabel( " 0" + ( 9 + i ) + ":00" );
			else timeLabel[ i ] = new JLabel( " " + ( 9 + i ) + ":00" );
			timeValue[ i ] = i + 9;
			
		}
		tableJSP.setPreferredSize( new Dimension( 700, 400 ) );
		//tablePanel.setPreferredSize( new Dimension( 680, 1250 ) );
		tableJSP.setViewportView( tablePanel );
		tableJFrame.setVisible( true );
		tableJFrame.setSize( 1200, 700 );
		tableJFrame.setLocation( 100, 10 );
		for ( int i = 0; i < 72; i++ ) {
			if ( i < 8 ) {
				// put week value in the tablePanel
				JPanel weekPanel = new JPanel();
				weekPanel.setSize( 30, 10 );
				weekPanel.setBackground( new Color( 223, 237, 209 ) );
				weekPanel.add( weekLabel[ i ] );
				weekLabel[ i ].setFont( new Font("Tahoma", 1, 16 ) );
				tablePanel.add( weekPanel );
			} else if ( i % 8 == 0 ) {
				// put time value in the tablePanel
				JPanel timePanel = new JPanel();
				//timePanel.setPreferredSize( new Dimension( 5, 35 ) );
				timePanel.setBackground( new Color( 223, 237, 209 ) );
				timePanel.add( timeLabel[ i / 8 - 1 ] );
				timeLabel[ i / 8 - 1 ] .setFont( new Font("Tahoma", 1, 16 ) );
				tablePanel.add( timePanel );
			} else {
				createGoogleContent( timeValue[ i / 8 - 1 ], dateValue[ i % 8 ], i );
			}
			
		}
		
	}
	
	/**
	 * the content of the google calendar
	 */
	int planIndex = 0;
	private ArrayList<PanelList> panelList = new ArrayList<PanelList>();
	@SuppressWarnings("deprecation")
	private void createGoogleContent( int time, int date, int id ) {
		planner.generateStudyPlan();
		ArrayList<StudyBlockInterface> plan = (ArrayList<StudyBlockInterface>) planner.getStudyPlan();
		ArrayList<Integer> indexList = new ArrayList<Integer>();
		// get events or topics in the specified time and date
		for ( int i = planIndex; i < plan.size(); i++ ) {
			if ( plan.get( i ).getStartTime().getTime().getDate() == date 
					&& plan.get( i ).getStartTime().getTime().getHours() == time && ( !plan.get( i ).getTopic().equals( "(break)") ) ) {
				indexList.add( i );
			}
		}
		planIndex = indexList.size();
		if ( indexList.size() == 0 ) {
			// there is no event or target, the calendar input JPanel
			JPanel contentPanel = new JPanel();
			contentPanel.setBackground( new Color(  121, 132, 100 ) );
			contentPanel.setBorder (BorderFactory.createLineBorder(Color.white, 1));
			tablePanel.add( contentPanel );
			// class PanelList to store JPanel and id for listening click event
			PanelList p = new PanelList( contentPanel, id );
			panelList.add( p );
		} else {
			//there have envents or topics.
			// JLabel -> time, JTextField -> name => childJPanel => contentPanel
			JPanel contentPanel = new JPanel( new GridLayout( indexList.size(), 1 ) );
			contentPanel.setBorder (BorderFactory.createLineBorder(Color.white, 5));
			for ( int k = 0; k < indexList.size(); k++ ) {
				JPanel conChildPanel = new JPanel( new GridLayout( 2, 1 ) );
				// time JLabel
				JLabel contenJL = new JLabel();
				int hours = plan.get( indexList.get( k ) ).getStartTime().getTime().getHours()
						, minutes = plan.get( indexList.get( k ) ).getStartTime().getTime().getMinutes()
						, duration = plan.get( indexList.get( k )  ).getDuration();

				String timeJL = hours + ":" + minutes + " - ";
				if ( minutes < 10 ) timeJL = hours + ":0" + minutes + " - ";
				minutes += duration;
				if ( minutes >= 60 ) {
					hours += minutes / 60;
					minutes = minutes % 60;
				}
				if ( minutes < 10 ) timeJL += hours + ":0" + minutes;
				else timeJL += hours + ":" + minutes;
				contenJL.setText( timeJL );
				conChildPanel.add( contenJL );
				conChildPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2 ));
				
				boolean isEventFlag = false;
				int eventAndTypeIndex = 0;
				// the name of an event or topic
				String currentName = plan.get( indexList.get( k ) ).getTopic();
				for ( int j = 0; j < calendarAndTypeList.size(); j++ ) {
					// the currentName is an event, so need to display its type
					if ( calendarAndTypeList.get( j ).getCalName().equals( currentName ) ) {
						currentName += " (" + calendarAndTypeList.get( j ).getType() + ")";
						isEventFlag = true;
						eventAndTypeIndex = j;
						break;
					} 
				}
				// title JTextField
				final JTextField contenJTF = new JTextField();
				contenJTF.setEditable( false );
				contenJTF.setText( currentName );
				contenJTF.setBackground( Color.WHITE );
				// contenJTF.setBackground( new Color( 123, 114, 123 ) );
				// conChildPanel.setBackground( new Color( 123, 114, 123 ) );
				// tooltip
				contenJTF.addMouseListener(new MouseAdapter() {
		               @Override
		                public void mouseEntered(MouseEvent event)
		               {
		            	   contenJTF.setToolTipText( contenJTF.getText() );
		                }
		        });
				conChildPanel.add( contenJTF );
				conChildPanel.setBackground( Color.WHITE );
				if ( isEventFlag ) {
					eventPanelList.add( new EventPanelList( contenJL, contenJTF, calendarAndTypeList.get( eventAndTypeIndex ).getCalendarEvent() ) );
				} else {
					// TopicInterface topic = new Topic( plan.get( indexList.get( k ) ).getTopic(), plan.get( indexList.get( k ) ).getStartTime(), plan.get( indexList.get( k ) ).getDuration() );
					 topicPanelList.add( new TopicPanelList( conChildPanel, contenJTF, plan.get( indexList.get( k ) ).getTopic() ) );
				}
				//conChildPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
				contentPanel.add( conChildPanel );
				contentPanel.setPreferredSize( new Dimension( 100, 100 ) ); 
				contentPanel.setBackground( new Color(  122, 122, 100 ) );
				tablePanel.add( contentPanel );
			}		
		}
		
	}
	
	//private class Panel
	
	/**
	 * In the google calendar, add event 
	 * @param location
	 * @param id
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
	public void addEvent( Point location, int id ) {
		
		// time & date
		final Calendar cal = Calendar.getInstance();
		int time = timeValue[ id / 8 - 1 ]
				,date = dateValue[ id % 8 ];
		int currentDate = cal.getTime().getDate();
		cal.add( Calendar.DATE,  date - currentDate );
		cal.set( Calendar.HOUR_OF_DAY, time );
    	cal.set( Calendar.MINUTE, 0 );

		String dateTimeString = " " + time + ":00 " + cal.get(Calendar.DATE) + "/" + ( cal.get(Calendar.MONTH) + 1 ) + "/" + cal.get(Calendar.YEAR);

		addEventFrame = new JFrame();
		addEventFrame.dispose(); // click another JPanel
		
		// initiate the components
    	jlblAEWrong = new JLabel();
    	jlblAEWrong.setFont( new Font( "Tahoma", 1, 12 ) ); 
    	jlblAEWrong.setForeground( Color.RED );
		jlblTime = new JLabel();
		jlblTime.setText( dateTimeString );
		jlblTime.setFont( new Font("Tahoma", 1, 12) );
		addEventPanel = new JPanel( new GridLayout( 4, 1 ) );
		jlblAEName = new JLabel( "Event Name:" );
		jlblAEName.setFont( new Font("Tahoma", 1, 12) );
		jlblAEDuration = new JLabel( "Event Duration:" );
		jlblAEDuration.setFont( new Font("Tahoma", 1, 12) );
		jlblAEType = new JLabel( "Event Type: " );
		jlblAEType.setFont( new Font("Tahoma", 1, 12) );
		jtxtAEName = new JTextField( 10 );
		jtxtAEDuration = new JTextField( 10 );
		addEventModel = new DefaultListModel();
		jstAEType = new JList( addEventModel );
		addEventJSP = new JScrollPane( jstAEType );
		for ( int i = 0; i < TYPE_VALUE.length; i++ ) 
			addEventModel.addElement( TYPE_VALUE[ i ] );
		jbtAESave = new JButton( "save" );
		jbtAECancel = new JButton( "cancel" );

		// set the layout of the add event
		GroupLayout addEventLayout = new GroupLayout( addEventPanel );
		addEventPanel.setLayout( addEventLayout );
		addEventLayout.setHorizontalGroup(
				addEventLayout.createParallelGroup()
				.addGap( 10 ).addGroup( addEventLayout.createParallelGroup()
						.addComponent( jlblAEWrong, GroupLayout.PREFERRED_SIZE, 240, GroupLayout.PREFERRED_SIZE ) )
				.addGroup( addEventLayout.createSequentialGroup().addGap( 15 )
				.addGroup( addEventLayout.createParallelGroup()
						.addComponent( jlblTime, GroupLayout.PREFERRED_SIZE, 155, GroupLayout.PREFERRED_SIZE )
						.addComponent( jlblAEName )
						.addComponent( jlblAEDuration )
						.addComponent( jlblAEType )
						.addComponent( jbtAECancel ))
				.addGroup( addEventLayout.createParallelGroup()
						.addComponent( jtxtAEName, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE )
						.addComponent( jtxtAEDuration, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE )
						.addComponent( addEventJSP, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE )
						.addComponent( jbtAESave ))) );
		addEventLayout.setVerticalGroup( 
				addEventLayout.createSequentialGroup().addGap( 10 )
				.addGroup( addEventLayout.createParallelGroup()
						.addComponent( jlblAEWrong, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE ) ).addGap( 10 )
				.addGroup( addEventLayout.createParallelGroup()
						.addComponent( jlblTime ) ).addGap( 20 )
				.addGroup( addEventLayout.createParallelGroup()
						.addComponent( jlblAEName )
						.addComponent( jtxtAEName, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE ).addGap( 10 ) ).addGap( 20 )
				.addGroup( addEventLayout.createParallelGroup()
						.addComponent( jlblAEDuration )
						.addComponent( jtxtAEDuration, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE ) ).addGap( 20 )
				.addGroup( addEventLayout.createParallelGroup()
						.addComponent( jlblAEType )
						.addComponent( addEventJSP, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE ) ).addGap( 20 )
				.addGroup( addEventLayout.createParallelGroup()
						.addComponent( jbtAECancel )
						.addComponent( jbtAESave ) )
						);
		
		addEventFrame.add( addEventPanel );
		addEventFrame.setSize( 330, 300 );
		// change the position in the case of click events occur in the conrner
		double x = location.getX();
		double y = location.getY();
		if ( x > 1000 ) x = 800;
		if ( y > 450 ) y = 400;
		location.setLocation(x, y);
		addEventFrame.setLocation( location );
		addEventFrame.setUndecorated( true ); // delete the auto border of the JFrame
		addEventPanel.setBorder((BorderFactory.createLineBorder(Color.GRAY, 5 ) ) );
		// cancel adding event
		jbtAECancel.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				addEventFrame.dispose();
			}
			
		});
		// save event
		jbtAESave.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				addEvenInCalendar( cal );
			}
			
		});
		addEventFrame.setVisible( true );
		
	}
	
	/**
	 * In the google calendar, add event listener method
	 * @param cal
	 */
	private void addEvenInCalendar( Calendar cal ) {
		String eventName = jtxtAEName.getText()
				, eventDuration = jtxtAEDuration.getText();
		String typeS = (String)jstAEType.getSelectedValue();
		if ( eventName.isEmpty() || eventName.isEmpty() ) {
			jlblAEWrong.setText( "  Please input the values" );
		} else if ( typeS == null ) {
			jlblAEWrong.setText( "  Please select the target type!" );
		} else {
			CalendarEventType type;
			if ( typeS.equals( "Exam" ) ) {
				type = CalendarEventType.EXAM;
			} else if ( typeS.equals( "Essay hand in") ) {
				type = CalendarEventType.ESSAY;
			} else {
				type = CalendarEventType.OTHER;
			}
			try {
				int dur = Integer.parseInt( eventDuration );
				// to store type and calendar event
				try {
					calendarAndTypeList.add( new CalendarEventList( new CalendarEvent( eventName, cal, dur, type ), type ) );
					planner.addCalendarEvent( eventName, cal, dur, type );	
					getTargetContentList();
					jlblAEWrong.setText( "" );
					jtxtAEName.grabFocus();
					addEventFrame.dispose();
					tableJFrame.dispose();
					dispalyGoogleCal( googleCalendar );
				} catch ( StudyPlannerException e ) {
						jlblAEWrong.setText( "  Calendat event has overlaped!!" );
				}
			} catch ( NumberFormatException e ) {
				jlblAEWrong.setText( "  Wrong time length value e.g. 60." );
			}
		}
	}
	
	/**
	 * get the name of the topics and events
	 */
	@SuppressWarnings("unchecked")
	public void getTargetContentList() {
		ArrayList<TopicInterface> topics = (ArrayList<TopicInterface>) planner.getTopics();
		ArrayList<CalendarEventInterface> events =  (ArrayList<CalendarEventInterface>) planner.getCalendarEvents();
		jstTarTopModel.removeAllElements();
		jstTarEveModel.removeAllElements();
		for ( int i = 0; i < topics.size(); i++ ) {
			//jstTarTopModel.addElement();
			jstTarTopModel.addElement( topics.get( i ).getSubject() );
		}
		for ( int i = 0; i < events.size(); i++ ) {
			jstTarEveModel.addElement( events.get( i ).getName() );
		}
	}
	
	/**
	 * topics set a target event
	 */
	public void saveTarget() {
		String topicName = (String) jstTarTopic.getSelectedValue()
				, eventName = (String) jstTarEvent.getSelectedValue();
		if ( topicName == null ) {
			jlblTarWrong.setText( "Please select the name of the topic!" );
		} else if ( eventName == null ) {
			jlblTarWrong.setText( "Please select the name of the event!" );
		} else {
			// get topic list and event list
			int topicIndex = 0, eventIndex = 0;
			ArrayList<TopicInterface> topicList = (ArrayList<TopicInterface>) planner.getTopics();
			ArrayList<CalendarEventInterface> eventList = (ArrayList<CalendarEventInterface>) planner.getCalendarEvents();
			for ( int i = 0; i < topicList.size(); i++ ) 
				if ( topicList.get( i ).getSubject().equals( topicName ) ) topicIndex = i;
			for ( int i = 0; i < eventList.size(); i++ )
				if ( eventList.get( i ).getName().equals( eventName ) ) eventIndex = i;
			// set target event
			try {
				topicList.get( topicIndex ).setTargetEvent( eventList.get( eventIndex ) );
				jlblTarWrong.setText( "" );
				JOptionPane.showMessageDialog( this, "The target has been set" );
			} catch (StudyPlannerException e ) {
				jlblTarWrong.setText( "Other type of event cannot be set as target!!!!" );
			}
			
		}
	}
	
	/**
	 * event JPanel click
	 * @param calendarEvent
	 */
	public void hightTargetTopics( CalendarEventInterface calendarEvent ) {
		
		for ( int k = 0; k < topicPanelList.size(); k++ )
			topicPanelList.get( k ).getPanel().setBackground( Color.WHITE );
			
		String eventName = calendarEvent.getName()
				, targetEventName = null;
		Calendar eventCal = calendarEvent.getStartTime();
		for ( int i = 0; i < calendarAndTypeList.size(); i++ ) {
			// When the user clicks on an exam or essay event
			if ( calendarAndTypeList.get( i ).getCalName().equals( eventName ) 
					&& calendarAndTypeList.get( i ).getCalendarEvent().getStartTime().equals( eventCal )
					&& ( !calendarAndTypeList.get( i ).getType().equals( CalendarEventType.OTHER) ) ) {
				targetEventName = eventName;
				break;
			}
		}
		
		if ( targetEventName != null ) {
			ArrayList<TopicInterface> topicsList = (ArrayList<TopicInterface>) planner.getTopics();
			ArrayList<String> topicNameList = new ArrayList<String>();
			for ( int i = 0; i < topicsList.size(); i++ ) {
				if( topicsList.get( i ).getTargetEvent() != null ) {
					if ( topicsList.get( i ).getTargetEvent().getName().equals( targetEventName ) ) {
						topicNameList.add(  topicsList.get( i ).getSubject() );
					}
				}
			}
			
			if ( topicNameList.size() > 0 ) {
				for ( int j = 0; j < topicNameList.size(); j++ ) {
					for ( int k = 0; k < topicPanelList.size(); k++ ) {
						if ( topicNameList.get( j ).equals( topicPanelList.get( k ).getTopic() ) ) {
							topicPanelList.get( k ).getPanel().setBackground( new Color( 233, 233, 123 ) );
						}
					}
				}
			} else {
				JOptionPane.showConfirmDialog( this, "No topic has been set target to this event", "Error", JOptionPane.WARNING_MESSAGE );
			}
		} else {
			JOptionPane.showConfirmDialog( this, "No topic has been set target to this event", "Error", JOptionPane.WARNING_MESSAGE );
		}
		
	}
	
	/**
	 * The user should be prompted by a dialog box warning that this will happen,
	 *  and giving them a choice of whether to continue.
	 */
	public void isTimeCountDown() {
		if ( timeFrameCount == 1 ) {
			int option = JOptionPane.showConfirmDialog( this, "Time is counting now!!!", "Waring", JOptionPane.WARNING_MESSAGE);
			if ( option == JOptionPane.OK_OPTION ) {
				timeCountDown();
			}
		} else {
			timeCountDown();
			timeFrameCount++;
		}
	}
	
	/**
	 * The timer should start at the duration of the study block and count down to zero. 
	 * Once it reaches zero, a dialog box should appear which is modal to the main planner GUI window.
	 * While a timer is running, the study planner and its GUI should behave completely normally.
	 */
	public void timeCountDown() {
		timeFrame.remove( timePanel );
		
		jlblTimer = new JLabel( "Timer:" );
		jlblTimeCount = new JLabel();
		jlblTimeCount.setBackground( Color.BLACK );
		jlblTimeCount.setFont(new Font("Tahoma", 0, 14));
		jlblTimeCount.setForeground(new java.awt.Color(0, 255, 0));
		jlblTimeCount.setOpaque(true);
		GroupLayout timePanelLayout = new GroupLayout( timePanel );
		timePanel.setLayout( timePanelLayout );
		timePanelLayout.setHorizontalGroup(
				timePanelLayout.createSequentialGroup()
				.addGap( 20 ).addGroup( timePanelLayout.createSequentialGroup()
						.addComponent( jlblTimer ).addGap( 10 )
						.addComponent( jlblTimeCount, GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE ) ).addGap( 20 ) );
		timePanelLayout.setVerticalGroup(
				timePanelLayout.createParallelGroup()
				.addGroup( timePanelLayout.createSequentialGroup()
					.addGroup( timePanelLayout.createParallelGroup() ).addGap( 20 )
					.addGroup( timePanelLayout.createParallelGroup()
						.addGroup( timePanelLayout.createParallelGroup() 
								.addComponent( jlblTimer ) )
						.addGroup( timePanelLayout.createParallelGroup() 
								.addComponent( jlblTimeCount, GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE ) ) ).addGap( 20 )
					)
				);
		timeFrame.add( timePanel );
		timeCount = planner.getBlockSize();
		if (timeThread != null && !timeThread.isAlive()) {
			 timeThread.start(); 
		}
		timeFrame.setVisible( true );
		timeFrame.pack();
	}
	
	/**
	 * Load data
	 */
	private void loadData() {
		JFileChooser c = new JFileChooser();
		String fileName = null;
		// Demonstrate "Open" dialog:
		int rVal = c.showOpenDialog( StudyPlannerGUI.this );
		if (rVal == JFileChooser.APPROVE_OPTION) {
			fileName = c.getSelectedFile().getAbsolutePath();
		}
		try {
			planner.loadData( new FileInputStream( fileName ) );
			@SuppressWarnings("unused")
			ArrayList<StudyBlockInterface> plan = (ArrayList<StudyBlockInterface>) planner.getStudyPlan();
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog( this, "File not found");
		} catch ( StudyPlannerException e2 ) {
			JOptionPane.showMessageDialog( this, "Stream data empty");
		}catch ( NullPointerException e ) {}
		
	}
	
	/**
	 * Save data (the value of plan)
	 */
	private void saveData() {
		JFileChooser c = new JFileChooser();
		String fileName = null;
		// Demonstrate "Open" dialog:
		int rVal = c.showOpenDialog(StudyPlannerGUI.this);
		if (rVal == JFileChooser.APPROVE_OPTION) {
			fileName = c.getSelectedFile().getAbsolutePath();
		}
	      
		try {
			planner.saveData( new FileOutputStream( fileName ));
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog( this, "File not found");
		} catch ( NullPointerException e ) {}
	}
	
    /**
     * Change mainPanel components
     * @param name
     */
  	private void changeCard( String name ) {
  		CardLayout c = (CardLayout)( mainPanel.getLayout() );
  		c.show( mainPanel, name );
  	}
  	
  	/**
  	 * Get topicsPanel
  	 * @return
  	 */
  	public Component getTopicComponent() {
  		return topicsPanel;
  	}
  	
  	/**
  	 * Get planPanel
  	 * @return
  	 */
  	public Component getPlanComponent() {
  		return planPanel;
  	}
  	/**
  	 * Main method
  	 * @param args
  	 */
    public static void main(String[] args)
    {
        StudyPlanner planner = new StudyPlanner(); 
        planner.addTopic("Software Measurement and Testing", 480);
        planner.addTopic("European agricultural policy 1950-1974", 720);  
        StudyPlannerGUI gui = new StudyPlannerGUI(planner);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setLocation( 300, 150 );
        
        planner.setGUI(gui);
                
        gui.setVisible(true);
        gui.updateDisplay();
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if ( source == jbtTopics ) {
			updateDisplay();
			changeCard( TOPICS );	
			
		} else if ( source == jbtEvents ) {
			changeCard( EVENTS );
		} else if ( source == jbtSetTarget ) {
			getTargetContentList();
			changeCard( TARGET );
		} else if ( source == jbtCalDisplay ) {
			dispalyGoogleCal( googleCalendar );
		} else if ( source == jbtLoad ) {
			loadData();
		} else if ( source == jbtSave ) {
			saveData();
		} else if ( source == jbtExit ) {
			 System.exit(0);      
		} else if ( source == jbtPlan ) {
			displayPlan();
			changeCard( PLAN );
		} else if ( source == jbtTopDelete ) {
			deleteTopic();
		} else if ( source == jbtTopAdd ) {
			addTopic();
		} else if ( source == jbtEveAdd ) {
			addCalendarEvent();
			updateDisplay();
		} else if ( source == jbtTarSave ) {
			saveTarget();
		} else if ( source == jbtnNextWeek ) {
			googleCalendar.add( Calendar.DATE, 7 );
			dispalyGoogleCal( googleCalendar );
		} else if ( source == jbtnPreWeek ) {
			googleCalendar.add( Calendar.DATE, -7 );
			dispalyGoogleCal( googleCalendar );
		}
		
	}
	

	
	/**
	 * Class PanelList to store JPanel and id
	 *  id for seraching current JPanel's time and date.
	 *  time = id / 8 - 1, date = id % 8.
	 *  when user click on the black spaces on the google calendar
	 *  the add event JPanel will display with time and date on the top.
	 * @author 
	 *
	 */
	private class PanelList {
		private JPanel panel;
		private int id; // record the date and time, id is the number of the JPanel
		public PanelList( JPanel p, int i ) {
			panel = p;
			id = i;
			panel.addMouseListener( new MouseAdapter() {
				@Override
	            public void mouseClicked (MouseEvent event)
	           {	
					for ( int k = 0; k < topicPanelList.size(); k++ )
						topicPanelList.get( k ).getPanel().setBackground( Color.WHITE );
					addEvent( event.getLocationOnScreen(), id );
	           }
			});
		}
	}
	
	/**
	 * Class CalendarEventList to store Calendar event and type
	 * @author 
	 *
	 */
	private class CalendarEventList {
		private CalendarEventInterface calendarEvent;
		private CalendarEventType type;
		public CalendarEventList( CalendarEventInterface calendarEvent, CalendarEventType type ) {
			this.calendarEvent = calendarEvent;
			this.type = type;
		}
		
		private String getCalName() { return calendarEvent.getName(); }
		private CalendarEventType getType() { return type; }
		private CalendarEventInterface getCalendarEvent() { return calendarEvent; }
	}
	
	/**
	 * Class EventPanelList to store event¡£
	 *  When click these panel related topics will be highlighted
	 * @author
	 *
	 */
	private class EventPanelList {
		@SuppressWarnings("unused")
		private JLabel label;
		@SuppressWarnings("unused")
		private JTextField textField;
		private CalendarEventInterface calendarEvent;
		public EventPanelList( JLabel label, JTextField textField, CalendarEventInterface event ) {
			this.label = label;
			this.textField = textField;
			this.calendarEvent = event;
			label.addMouseListener( new MouseAdapter() {
				@Override
	            public void mouseClicked (MouseEvent event)
	           {	
					hightTargetTopics( calendarEvent );
	           }
			});
			
			textField.addMouseListener( new MouseAdapter() {
				@Override
	            public void mouseClicked (MouseEvent event)
	           {	
					hightTargetTopics( calendarEvent );
	           }
			});
		}
	}
	
	/**
	 * Class TopicPanelList to store topics information.
	 *  When left click these panels, if the background is highlighted, it will change back.
	 *  When right click these panels, it will popup a small time window.
	 * @author
	 *
	 */
	private class TopicPanelList {
		private JPanel panel;
		private JTextField textField;
		private String topicName;
		public TopicPanelList( JPanel p, JTextField t, String topicName ) {
			this.panel = p;
			this.topicName = topicName;
			this.textField = t;

			popupMenu = new JPopupMenu();
			popupItem = new JMenuItem("Start Time");
			popupMenu.add( popupItem );
			// time count down
			popupItem.addMouseListener( new MouseAdapter() {
				
				@Override
			    public void mousePressed (MouseEvent event)
			   {
					isTimeCountDown();
			   }
				
			});

			// JPanel click event
 			panel.addMouseListener( new MouseAdapter() {
				@Override
	            public void mouseClicked (MouseEvent event)
	           {	
					for ( int k = 0; k < topicPanelList.size(); k++ )
						topicPanelList.get( k ).getPanel().setBackground( Color.WHITE );
	           }
				
				@Override
				public void mousePressed(MouseEvent e)
				{
					if (e.getModifiers() == MouseEvent.BUTTON3_MASK) {
						popupMenu.show( panel, e.getX(), e.getY() );
					}

				}
			});
			
 			// JTextField click event
			textField.addMouseListener( new MouseAdapter() {
				@Override
	            public void mouseClicked (MouseEvent event)
	           {	
					for ( int k = 0; k < topicPanelList.size(); k++ )
						topicPanelList.get( k ).getPanel().setBackground( Color.WHITE );
	           }
				
				@Override
				public void mousePressed(MouseEvent e)
				{
					if (e.getModifiers() == MouseEvent.BUTTON3_MASK) {
						popupMenu.show( textField, e.getX(), e.getY() );
					}

				}
			});
		}
		private JPanel getPanel() { return panel; }
		private String getTopic() { return topicName; }
	}

	/**
	 * Time count down
	 * @author 
	 *
	 */
	private class TimeCountDown extends Thread {
		public TimeCountDown() {
			super();
		}
		
		@SuppressWarnings("deprecation")
		public void run () {
			while ( true ) {
				try {
					Thread.sleep( 1000 ); // Update value pre_second
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				if ( timeCount >= 0 ) {
					jlblTimeCount.setText( "" + timeCount );
					timeCount--;
				} else {
					JOptionPane.showMessageDialog( mainPanel, "Time Out!!!!");
					timeFrame.dispose(); // Dispose the time window
					timeThread.stop(); // Stop the time thread
				}
			}
		}
	}
}

