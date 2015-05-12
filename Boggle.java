import java.io.IOException;
import java.util.Date;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Boggle 
{
	private List<String> 	dictionary = new ArrayList<String>();
	private List<Die>    	dice       = new ArrayList<Die>();
	private Tree			tree	   = new Tree();
	
	Boggle()
	{
		readDictionary();
		readDice();
	}
	
	private void readDictionary()
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
			
			tree.loadDictionary( dictionary );
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	private void readDice()
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
	
	private void resetDiceWords()
	{
		for( Die die : dice )
		{
			die.wordsStartingHere.clear();
		}
	}
	
	private void rollOneSearch()
	{
		Board board = new Board( dice );
		int maxScore = 0;
		
		for( int idx = 0; idx < 10000; idx++ )
		{
			board.solve( tree.root );
			int score = board.printScore( false );
			
			if( score > maxScore )
			{
				//board.rollRandomDie();
			}
			else
			{
				board.shuffle();
			}
		}
	}

	public void solveRuslansBoard()
	{
		Board board = new Board( dice );
		
		board.readFromString( "renotvsticieraldgnephtcdb" );
		System.out.println( board );
		
		board.solve( tree.root );
		board.printScore( true );
	}
	
	public void tryRandomBoards( int howMany )
	{
		int maxScore = 0;

		for( int idx = 0; idx < howMany; idx++ )
		{
			resetDiceWords();
			Board board = new Board( dice );
			board.print();
			
			board.solve( tree.root );
			int score = board.printScore( true );
			
			if( score > maxScore )
			{
				maxScore = score;
			}
		}
		
		System.out.println( "\nMax Score: " + maxScore );
	}
	
	public static void main(String[] args) 
	{
		Boggle boggle = new Boggle();
		
		boggle.solveRuslansBoard();
		boggle.tryRandomBoards( 10 );
	}
}
