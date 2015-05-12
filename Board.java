import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import com.sun.xml.internal.ws.util.StringUtils;

public class Board 
{
	List<Die> dice;
	final long randomSeed = 0x99999;
	
	Random random = new Random( randomSeed );
	
	Board( List<Die> diceList )
	{
		dice = diceList;
		shuffle();
	}
	
	public void shuffle()
	{
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
	
	public void readFromString( String string )
	{
		string.trim();
		string.replaceAll( " \n", "" );
		
		if( string.length() != 25 )
		{
			System.out.println( string + " is not a valid board definition" );
			return;
		}
		
		dice.clear();
		char[] temp = new char[6];
		for( int idx = 0; idx < string.length(); idx++ )
		{
			Arrays.fill( temp, string.charAt( idx ) );
			String face = new String( temp );
			dice.add( new Die( face, idx ) );
		}
		
		for( int x = 0; x < 5; x++ )
		{
			for( int y = 0; y < 5; y++ )
			{
				get( x, y ).roll( 1 );
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
		
		resetUsedFlag();
	}
	
	public int printScore( boolean toConsole )
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
		
		if( toConsole )
		{
			System.out.println( wordSet );
			System.out.println( "Score: " + score );
		}
		
		return score;
	}
}