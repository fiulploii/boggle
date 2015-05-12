import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Boggle 
{
	private List<String> 	dictionary 	= new ArrayList<String>();
	private List<Die>    	dice       	= new ArrayList<Die>();
	private Tree			tree	   	= new Tree();
	private	long			randomSeed	= 0x111111;
	private Random			random		= new Random( randomSeed );
	
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
				dice.add( new Die( string, id++, random ) );
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void solveRuslansBoard()
	{
		Board board = new Board( dice, tree, random );
		
		board.readFromString( "renotvsticieraldgnephtcdb" );
		System.out.println( board );
		
		board.solve();
		board.score();
		
		System.out.println( board.words );
		System.out.println( board.score );
	}
	
	public int tryRandomBoards( Board currentMasterBoard, int howMany, int currentDepth, int maxDepth, int currentMaxScore )
	{
		boolean generateMutations = false;
		
		if( currentDepth >= maxDepth )
		{
			return currentMaxScore;
		}
		
		if( currentMasterBoard != null )
		{
			generateMutations = true;
		}
		
		Board board = null;
		
		for( int idx = 0; idx < howMany; idx++ )
		{
			if( generateMutations )
			{
				board = new Board( currentMasterBoard );
				board.mutate();
			}
			else
			{
				board = new Board( dice, tree, random );
			}
			
			board.solve();
			board.score();

			if( board.score >= currentMaxScore * 0.8 || random.nextInt( 100 ) % 99 == 0 )
			{	
				System.out.println( "Score: " + board.score + ", Depth: " + currentDepth + ", Iteration: " + idx );
				System.out.println( "---------------------------------------" );

				board.print();
				
				System.out.println( board.words );
				System.out.println( "---------------------------------------\n" );

				int localScore = tryRandomBoards( board, howMany, currentDepth + 1, maxDepth, board.score );
				
				if( localScore > currentMaxScore )
				{
					currentMaxScore = localScore;
				}
			}
		}
		
		return currentMaxScore;
	}
		
	public void mutateRuslansBoard()
	{
		Board ruslansBoard = new Board( dice, tree, random );
		ruslansBoard.readFromString( "renotvsticieraldgnephtcdb" );
		tryRandomBoards( ruslansBoard, 10000, 0, 1000, 0 );
	}
	
	public static void main(String[] args) 
	{
		Boggle boggle = new Boggle();
		
		int score = boggle.tryRandomBoards( null, 300, 0, 4, 0);
		System.out.println( "Best score is: " + score );
	}
}
