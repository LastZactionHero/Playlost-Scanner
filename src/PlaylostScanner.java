import java.io.*;
import java.util.*;

/*
 * Scans a music library and constructs output
 * Javascript file for use with the viewer
 */
public class PlaylostScanner extends Thread {
	
	Vector<OutputItem> output_list = new Vector<OutputItem>(); // Output list of songs
	String mPath = ""; // Base path
	String mOutput = ""; // Output display text
	boolean mKill = false; // Stop scan flag
	
	/*
	 * Constructor
	 */
	public PlaylostScanner( String inBasePath )
	{
		mPath = inBasePath;
	}
	
	
	/* 
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run()
	{
		mKill = false;
		scanLibrary();	
	}
	
	
	/*
	 * Perform library scan
	 */
	void scanLibrary()
	{
		// Get the base directory
		File dir_base = new File( mPath );
		
		// Scan library recursively
		scanDirectory( dir_base );
		
		randomize();
		
		// Write output to javascript file
		writeOutput();
		mOutput = "Scan Complete!\n" + mOutput;
	}
	
	
	/*
	 * Music directory recursive scan
	 */
	boolean scanDirectory( File inDir )
	{
		if( inDir.isDirectory() )
		{
			String pathBase = inDir.getPath();
			
			// Update output string
			String scanString = pathBase + "\n";
			mOutput = scanString + mOutput;
			System.out.println( scanString );

			// Get all sub files
			String[] fileList = inDir.list();
			
			// Scan each file as directory or MP3 
			for(int i = 0; i < fileList.length; i++ )
			{
				// If the kill flag has been set, call everything off
				if( mKill ) return false;
				
				// Construct path
				//File sub_file = new File( pathBase + "/" + fileList[i] );
				File sub_file = new File( pathBase + File.separator + fileList[i] );
				
				// Scan as directory or mp3
				if( !scanDirectory(sub_file) )
				{
					scanMP3(sub_file);
				}
			}
			return true;
		}
		return false;
	}
	
	
	/*
	 * Scan MP3 File
	 */
	void scanMP3(File in_file)
	{
		MP3 mp3 = new MP3(in_file);
		if( mp3.isValid() )
		{
			// Add to output list
			output_list.add( new OutputItem(mp3) );
		}
	}
	
	
	/*
	 * Write songs to Javascript output file
	 */
	void writeOutput()
	{
		// Open output file
		File out_file = new File("./scripts/autogen_playlist.js");
		try{
			out_file.createNewFile();
			FileWriter writer = new FileWriter(out_file);
			
			// Header
			writer.write( "function Setup_Autogen_Song_Database()\n");
			writer.write( "{\n" );
			writer.write( "this.list_length = " + output_list.size() + ";\n" );
			writer.write( "this.list = new Array( this.list_length );\n" );
			
			// Write individual tracks
			for( int i = 0; i < output_list.size(); i++ )
			{
				writer.write( output_list.get(i).print(i) );
			}
			
			// Footer
			writer.write("}\n");
			writer.close();
		}catch(IOException e){
		}	
	}
	
	
	/*
	 * Stop scanner
	 */
	void kill()
	{
		mKill = true;	
	}
	
	
	/*
	 * Randomize Playlist
	 */
	void randomize()
	{
		int length = output_list.size();
		Random generator = new Random();
		
		for( int i = 0; i < length; i++ )
		{
			int random_idx = generator.nextInt( length - 1 );
			
			OutputItem temp = output_list.get( i );
			output_list.set( i, output_list.get( random_idx ) );
			output_list.set( random_idx, temp );
		}
	}
	
	
}
