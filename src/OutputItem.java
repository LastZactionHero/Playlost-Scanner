import java.io.File;

/*
 * Lightweight container for song title, artist, and filename
 */
public class OutputItem {
	String mTitle;
	String mArtist;
	String mDefault;
	
	/*
	 * Constructor
	 */
	public OutputItem( MP3 inMP3 )
	{
		// Get data from MP3
		mTitle = inMP3.getTitle();
		mArtist = inMP3.getArtist();
		mDefault = inMP3.getDefault();
		
		// If no title was found, use the filename
		if( mTitle.length() == 0 )
		{
			//int startPosn = mDefault.lastIndexOf( '/' );
			int startPosn = mDefault.lastIndexOf( File.separator );
			mTitle = mDefault.substring( startPosn + 1 );
		}
		
		mDefault = replaceWindowsSlahses( mDefault );
	}
	
	
	/*
	 * Print MP3 data in Javascript format
	 */
	public String print( int inIndex )
	{
		String ret = "this.list[" + inIndex + "] = new Song( \"" + mTitle + "\", \"" + mArtist + "\", \"" + mDefault +"\" );\n";
		return ret;
	}
	
	/*
	 * Replace Windows \ with \\
	 */
	private String replaceWindowsSlahses( String inFilename )
	{
		return inFilename.replaceAll( "\\\\", "\\\\\\\\" );
	}
}
