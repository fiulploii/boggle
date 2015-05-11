import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Board 
{
	List<Die> dice;
	
	Board( List<Die> diceList )
	{
		dice = diceList;
		final long randomSeed = 0x987123;
		
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
		Integer myOffset = x * 5 + y;
		
		List<Integer> deltaList = Arrays.asList( -6, -5, -4, -1, +1, +4, +5, +6 );
		
		for( Integer delta : deltaList )
		{
			if( myOffset - delta >= 0 )
			{
				neighbours.add( dice.get( myOffset - delta ) );
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
				get( x, y ).solve( tree, getNeighbours( x, y ), "" );
			}
		}
	}
	
	public void printScore()
	{
		for( int x = 0; x < 5; x++ )
		{
			for( int y = 0; y < 5; y++ )
			{
				List<String> words = get( x, y ).getWords();
				
				for( String word : words )
				{
					System.out.println( x + "," + y + ": " + word + "\n" );
				}
			}
		}
	}
}