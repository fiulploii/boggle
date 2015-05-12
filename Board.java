import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class Board 
{
	List<Die> dice;
	
	Board( List<Die> diceList )
	{
		dice = diceList;
		final long randomSeed = 0x111123;
		
		Random random = new Random( randomSeed );
		
		Collections.shuffle( dice, random );
		
		for( int x = 0; x < 5; x++ )
		{
			for( int y = 0; y < 5; y++ )
			{
				get( x, y ).roll( random.nextInt( 6 ) );
				get( x, y ).x = x;
				get( x, y ).y = y;
			}
		}
	}
	
	public String toString()
	{
		String string = new String();
		
		for( int x = 0; x < 5; x++ )
		{
			for( int y = 0; y < 5; y++ )
			{
				string += String.valueOf( get( x, y ) );
			}
			
			string += "\n";
		}
		
		return string;
	}
	
	public Die get( Integer x, Integer y )
	{
		return dice.get( x * 5 + y );
	}
	
	public List<Die> getNeighbours( Integer x, Integer y)
	{
		List<Die> neighbours = new ArrayList<Die>();
		
		List<Integer> deltaXList = Arrays.asList( -1, -1, -1, 0, +1, +1, +1, 0 );
		List<Integer> deltaYList = Arrays.asList( -1, 0, +1, +1, +1, 0, -1, -1 );
		
		for( int idx = 0; idx < deltaXList.size(); idx++ )
		{
			int deltaX = deltaXList.get( idx );
			int deltaY = deltaYList.get( idx );
			
			int neighbourX = x + deltaX;
			int neighbourY = y + deltaY;
			
			if( neighbourX < 5 && neighbourX >=0 && neighbourY < 5 && neighbourY >=0 )
			{
				neighbours.add( get( neighbourX, neighbourY ) );
			}
		}
		
		return neighbours;
	}
	
	public void resetUsedFlag()
	{
		for( Die die : dice )
		{
			die.used = false;
		}
	}
	
	public void solve( TreeNode tree )
	{
		for( int x = 0; x < 5; x++ )
		{
			for( int y = 0; y < 5; y++ )
			{
				resetUsedFlag();
				Die die = get( x, y );
				die.solve( tree.children.get( die.getChar() ), this, "" );
			}
		}
	}
	
	public void printScore()
	{
		int score = 0;
		List<String> allWords = new ArrayList<String>();
		List<Integer> scoreTable = Arrays.asList( 0, 0, 0, 0, 1, 2, 3, 5, 11 );
		
		for( int x = 0; x < 5; x++ )
		{
			for( int y = 0; y < 5; y++ )
			{
				List<String> words = get( x, y ).getWords();
				allWords.addAll( words );
			}
		}
		
		Collections.sort( allWords );
		Set<String> wordSet = new TreeSet<String>( allWords );
		
		for( String word : wordSet )
		{
			word.replaceAll( "q", "qu" );
			score += scoreTable.get( word.length() );
		}
		
		System.out.println( wordSet );
		
		System.out.println( "Score: " + score );
	}
}