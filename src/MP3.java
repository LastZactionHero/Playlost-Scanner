import java.io.*;

/*
 * MP3 Scanner class
 */
public class MP3 {
	File mFile = null;
	String mArtist = "";
	String mTitle = "";
	String mDefault = "";
	boolean mValid = false;
	
	/*
	 * Constructor
	 */
	public MP3( File inFile )
	{
		mFile = inFile;
		mDefault = inFile.getPath();
		
		// Determine if valid, extract data
		mValid = isFileMP3();
		if( mValid ) extractData();
	}
	
	
	/*
	 * Getter, Valid File
	 */
	public boolean isValid()
	{
		return mValid;
	}
	
	
	/*
	 * Getter, Artist
	 */
	public String getArtist()
	{
		return mArtist;
	}
	
	
	/*
	 * Getter, Title
	 */
	public String getTitle()
	{
		return mTitle;
	}

	
	/*
	 * Getter, Default String
	 */
	public String getDefault()
	{
		return mDefault;
	}
	
	
	/* 
	 * Determine if the file is an MP3
	 */
	private boolean isFileMP3()
	{
		// Bail if file is bad
		if( mFile == null )
			return false;
		
		// Get position of last '.'
		String file_name = mFile.getName();
		int ext_posn = file_name.lastIndexOf('.');

		// Extract extension, see if it is MP3
		if( ext_posn > 0 )
		{
			String extension = file_name.substring(ext_posn);
			if( extension.compareToIgnoreCase( new String(".mp3") ) == 0 )
				return true;
		}	
		return false;
	}
	
	
	/*
	 * Extract tag data from MP3 file
	 */
	private void extractData()
	{
		// Bail if file is not readable
		if( !mFile.canRead() )
		{
			System.out.println("Read Failure!");
			return;
		}
		
		// Get file size, must be > 128
		long file_size = mFile.length();
		if( file_size <= 128 )
			return;

		byte[] read_bytes = new byte[ 129 ]; // Tag read buffer
		
		try
		{
			// Seek to last 128 byte of the file
			RandomAccessFile raf = new RandomAccessFile( mFile, "r" );
			raf.seek( file_size - 128 );
			
			// Read last 128 bytes of the file
			if( raf.read(read_bytes, 0, 128) == 128 )
			{
				// Tag starts with "TAG"
				String tag = new String( read_bytes );
				if( tag.indexOf("TAG") >= 0 )
				{
					// Extract title (3 - 32)
					mTitle = tag.substring(3, 32).replaceAll("[^\\p{ASCII}]", "");
					mTitle = mTitle.replaceAll( "[\"]", "" );
					int end_idx = mTitle.indexOf( 0 ); 
					if( end_idx >= 0 )	
					{
						mTitle = mTitle.substring(0, end_idx);
					}
					
					// Extract artist (33, 62)
					mArtist = tag.substring(33, 62).replaceAll("[^\\p{ASCII}]", "");
					end_idx = mArtist.indexOf( 0 );
					if( end_idx >= 0 )
					{
						mArtist = mArtist.substring(0, end_idx);
						mArtist = mArtist.replaceAll( "[\"]", "" );
					}
				}		
			}
			//reader.close();
			raf.close();
		
		}catch(IOException e)
		{
			System.out.println("Reader Failure!");
		}
	}

}
