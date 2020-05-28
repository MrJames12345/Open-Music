import java.util.*;
import java.io.*;
import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;

class OpenMusic
{

    public static void main( String args[] )
    {
    
        //Input file variables
        FileInputStream fileInStrm = null;
        InputStreamReader fileSR = null;
        BufferedReader fileBR = null;
        String line = null;
        //Output file variables
        FileOutputStream fileOutStrm = null;
        PrintWriter filePW = null;
        String finalOutput = "";
        //Number of iterations
        int iterations = 3;
        //title's separator
        String separator = " - - - - - - - - - - - -";
        //title's array
        String[] usedTitles = new String[ iterations ];
        //URL
        String ytmUrl = null;
        //Output dialog box
        OutputBox outputBox = null;
        //Other
        int i;
        String tempString = null;
        
        try
        {
        
            //Open 'NewMusic.txt'
            fileInStrm = new FileInputStream( "C:\\Users\\James\\Google Drive\\#Coding\\Java\\NewMusic.txt" );
            fileSR = new InputStreamReader ( fileInStrm );
            fileBR = new BufferedReader( fileSR );

            //Go through old titles until get separator line
            do
            {
                line = fileBR.readLine();
            }
            while ( !line.equals(separator) );
            //Get first title
            line = fileBR.readLine();

            //If no new titles, output box "No new music mate."
            if ( line == null )
            {
                outputBox = new OutputBox( "Open Music", false );
                outputBox.update( "No new music mate!" );
            }
            else
            {

                //Get rest of titles until num of iterations, or until none left, and add to usedtitles[]
                for ( i = 0; i < iterations && line != null && !line.equals(""); i++ )
                {
                    usedTitles[i] = line;
                    line = fileBR.readLine();
                }
                
                //Begin creating finalOutput by adding usedTitles then separator
                for ( i = 0; i < iterations && usedTitles[i] != null; i++ ) //(Doesn't keep going if there weren't as many titles as there are set iterations)
                {
                    finalOutput = finalOutput + usedTitles[i] + "\n";
                }
                finalOutput = finalOutput + separator;

                //Get rest of lines and add to finalOutput string
                while ( line != null )
                {
                    finalOutput = finalOutput + "\n" + line; //('/n' done before so doesn't cause empty lines at end)
                    line = fileBR.readLine();
                }
                
                //Close input reader
                fileBR.close();
                
                //Write finalOutput to new 'NewMusic.txt' file
                fileOutStrm = new FileOutputStream( "NewMusic.txt" );
                filePW = new PrintWriter( fileOutStrm );
                filePW.println( finalOutput );

                //Close output writer
                filePW.close();

                //Open each new title is YTM search
                for ( i = 0; i < iterations && usedTitles[i] != null; i++ )
                {
                    tempString = toggleUrlSuitable( usedTitles[i] );
                    ytmUrl = "https://music.youtube.com/search?q=" + tempString;
                    Desktop.getDesktop().browse( new URI(ytmUrl) );
                }

            }

        }
        catch ( URISyntaxException e )
        {
            outputBox = new OutputBox( "Open Music", false );
            outputBox.update( "Error with opening URL's!" );
        }
        catch ( IOException e )
        {
            outputBox = new OutputBox( "Open Music", false );
            outputBox.update( "Error with I/O!" );
        }
    
    }
    
    //Make given text suitable to add onto URL
    private static String toggleUrlSuitable(String inSearch)
    {
        String outSearch = inSearch;
        
        //Get rid of unwanted search terms
        outSearch = outSearch.replace("Official Music Video", "");
        outSearch = outSearch.replace("Music Video", "");
        outSearch = outSearch.replace("Official Video", "");
        outSearch = outSearch.replace("Official Audio", "");
        outSearch = outSearch.replace("Lyrics", "");
        
        outSearch = outSearch.replaceAll("&", "%26");
        outSearch = outSearch.replaceAll(",", "%2C");
        outSearch = outSearch.replaceAll("\\+", "%2B");
        outSearch = outSearch.replace(' ', '+');
        outSearch = outSearch.replaceAll("\\|", "");
        
        return outSearch;
    }

}