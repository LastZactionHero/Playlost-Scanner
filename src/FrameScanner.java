import javax.swing.*;
import java.awt.event.*;

public class FrameScanner extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = 7526472295622776147L;

	// UI Elements
	static FrameScanner jThisFrame = null;
	private JButton jButtonScan = null;
	private JTextField jFieldDir = null;
	private JButton	jButtonChoose = null;
	private JTextArea jTextScan = null;
	private JButton	jButtonStop = null;
	private Timer updateTimer = null;
	
	// Scanning
	private String mStringDir = null;
	private PlaylostScanner mScanner = null;
	
	
	/*
	 * Program main
	 */
	public static void main(String[] args)	
	{
		jThisFrame = new FrameScanner();
	}
	
	/*
	 * Constructor
	 */
	FrameScanner()
	{
		setLayout( null );
		setResizable( false );
		
		setTitle( "Playlost Library Scanner" );
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLocation( 100, 100 );
		
		jFieldDir = new JTextField();
		jFieldDir.setText( "Path to Music Directory" );
		jFieldDir.setBounds( 5, 0, 300, 35 );
		add( jFieldDir );
		
		jButtonChoose = new JButton( "Select Directory" );
		jButtonChoose.setBounds( 305, 0, 140, 35 );
		jButtonChoose.setActionCommand( "button_choose" );
		jButtonChoose.addActionListener( this );
		add( jButtonChoose );
		
		jTextScan = new JTextArea();
		jTextScan.setEditable( false );
		jTextScan.setBounds( 5, 45, 445, 200 );
		add( jTextScan );
		
		jButtonScan = new JButton( "Scan" );
		jButtonScan.setBounds( 5, 250, 340, 35 );
		jButtonScan.setActionCommand( "button_scan" );
		jButtonScan.addActionListener( this );
		add( jButtonScan );
		
		jButtonStop = new JButton( "Stop" );
		jButtonStop.setBounds( 350, 250, 100, 35 );
		jButtonStop.setActionCommand( "button_stop" );
		jButtonStop.addActionListener( this );
		jButtonStop.setEnabled( false );
		add( jButtonStop );
		
		setSize( 450, 315 );
		setVisible( true );
		
		setUIActivityState( false );
	}
	
	/*
	 * Event Handler (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
	{
		if( "button_choose".equals( e.getActionCommand() ) )
		{
			//Create a file chooser
			final JFileChooser jChooserDir = new JFileChooser();
			jChooserDir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int status = jChooserDir.showOpenDialog(this);
			
			if( status == JFileChooser.APPROVE_OPTION )
			{
				mStringDir = jChooserDir.getSelectedFile().getPath();
				jFieldDir.setText( mStringDir );
			}
		}
		else if( "button_scan".equals( e.getActionCommand() ) )
		{	
			setUIActivityState( true );
			
			mScanner = new PlaylostScanner( mStringDir );
			mScanner.start();
			
			updateTimer = new javax.swing.Timer( 1000, new ActionListener() {
		          public void actionPerformed(ActionEvent e) {
		              jThisFrame.updateOutputText();
		          }
		       } );
			updateTimer.setInitialDelay( 1000 );
			updateTimer.start();
			
		}
		else if( "button_stop".equals( e.getActionCommand() ) )
		{
			setUIActivityState( false );

			mScanner.kill();
			updateTimer.stop();
		}
		else if( "timer_update".equals( e.getActionCommand() ) )
		{
			updateOutputText();
			
			if( mScanner.getIsFinished() )
			{
				setUIActivityState( false );
			}
		}
	}
	
	/*
	 * Set UI Button state
	 */
	private void setUIActivityState( boolean inActive )
	{
		if( inActive )
		{
			jButtonScan.setEnabled( false );
			jFieldDir.setEnabled( false );
			jButtonChoose.setEnabled( false );
			jButtonStop.setEnabled( true );
		}
		else
		{
			jButtonScan.setEnabled( true );
			jFieldDir.setEnabled( true );
			jButtonChoose.setEnabled( true );
			jButtonStop.setEnabled( false );
		}
	}
	
	/*
	 * Update Scanner Text Field 
	 */
	private void updateOutputText()
	{
		jTextScan.setText( mScanner.getOutputText() );	
	}
	
}
