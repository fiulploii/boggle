import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Boggle 
{
	static List<String> dictionary = new ArrayList<String>();
	static List<Die>    dice       = new ArrayList<Die>();
	static Tree			tree	   = new Tree();
	
	private static void readDictionary()
	{
		try 
		{
			List<String> dictionaryString = Files.readAllLines( Paths.get( "Dictionary.txt" ), Charset.defaultCharset() );
			
			for( String string : dictionaryString )
			{
				string.replaceAll( "qu", "q" );
				
				dictionary.addAll( Arrays.asList( string.toLowerCase().split( "\\s+" ) ) );
			}
			
			Collections.sort( dictionary );
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	private static void readDice()
	{
		List<String> diceString;
		try 
		{
			diceString = Files.readAllLines( Paths.get( "Dice.txt" ), Charset.defaultCharset() );
			
			int id = 0;
			for( String string : diceString )
			{
				dice.add( new Die( string, id++ ) );
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	private static void resetDiceWords()
	{
		for( Die die : dice )
		{
			die.words.clear();
		}
	}
	
	private static void readDiceFromString( String string )
	{
		string.trim();
		
		if( string.length() != 25 )
		{
			return;
		}
		
		dice.clear();
		
	}
	
	public static void main(String[] args) 
	{
		readDictionary();
		readDice();
		
		tree.loadDictionary( dictionary );
//		tree.print();
		
		for( int idx = 0; idx < 100; idx++ )
		{
			resetDiceWords();
			Board board = new Board( dice );
			System.out.println( board );
			
			board.solve( tree.root );
			board.printScore();
		}
	}
}
