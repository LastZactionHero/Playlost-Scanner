import javax.swing.*;
import java.awt.event.*;

public class FrameScanner extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = 7526472295622776147L;

	static FrameScanner jThisFrame = null;
	JButton 	jButtonScan = null;
	JTextField 	jFieldDir = null;
	JButton		jButtonChoose = null;
	JTextArea	jTextScan = null;
	String		mStringDir = null;
	PlaylostScanner mScanner = null;
	JButton		jButtonStop = null;
	Timer		updateTimer = null;

	
	public static void main(String[] args)	
	{
		jThisFrame = new FrameScanner();
	}
	
	FrameScanner()
	{
		setLayout( null );
		
		setTitle( "Playlost Library Scanner" );
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
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
	}
	
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
			jButtonScan.setEnabled( false );
			jFieldDir.setEnabled( false );
			jButtonChoose.setEnabled( false );
			jButtonStop.setEnabled( true );
			
			mScanner = new PlaylostScanner( mStringDir );
			mScanner.start();
			
			//updateTimer = new Timer( 1000, this );
			//updateTimer.setInitialDelay( 1000 );
			//updateTimer.setActionCommand( "timer_update" );
			//updateTimer.start();
			
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
			jButtonScan.setEnabled( true );
			jFieldDir.setEnabled( true );
			jButtonChoose.setEnabled( true );
			jButtonStop.setEnabled( false );
			
			mScanner.kill();
			updateTimer.stop();
		}
		else if( "timer_update".equals( e.getActionCommand() ) )
		{
			updateOutputText();
		}
	}
	
	public void updateOutputText()
	{
		jTextScan.setText( mScanner.mOutput );	
	}
	
}
