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
		
		for( Die die : dice )
		{
			die.roll( random.nextInt( 6 ) );
		}
		
		Collections.shuffle( dice, random );
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
}