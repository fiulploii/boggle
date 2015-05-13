import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class Board 
{
	private List<Die> 		dice;
	private Random 			random		= null;
	private Tree			tree		= null;
	private List<Integer>           scoreTable 	= Arrays.asList( 0, 0, 0, 0, 1, 2, 3, 5, 11 );
        private List<List<Die>>         neighbours      = new ArrayList<List<Die>>();
	
	public  HashSet<String>	words		= new HashSet<String>();
	public  int				score		= 0;
	
	Board( Board originalBoard )
	{
		this.dice = originalBoard.dice;
		this.tree = originalBoard.tree;
		this.random = originalBoard.random;
                
                createNeighboursCache();
	}
	
	Board( List<Die> diceList, Tree tree, Random random )
	{
		this.dice 	= diceList;
		this.tree 	= tree;
		this.random	= random;
		
                createNeighboursCache();
		shuffle();
		roll();
	}
	
        private void createNeighboursCache()
        {
                for( int idx = 0; idx < 25; idx++ )
                {
                    neighbours.add( new ArrayList<Die>() );
                }            
        }
        
        private void assignDicePositions()
	{
            for( int idx = 0; idx < 25; idx++ )
            {
                neighbours.get( idx ).clear();
            }
		
            for( int x = 0; x < 5; x++ )
            {
                    for( int y = 0; y < 5; y++ )
                    {
                            Die die = get( x, y );

                            die.x = x;
                            die.y = y;

                            neighbours.get( x * 5 + y ).addAll( getNeighboursSlow( x, y) );
                    }
            }
	}
	
	private String generateFacesFromChar( char c )
	{
		char[] temp = new char[6];
		Arrays.fill( temp, c );
		
		return new String( temp );
	}
	
	public void shuffle()
	{
		resetScore();
		
		Collections.shuffle( dice, random );
		assignDicePositions();
	}
		
	private void resetScore()
	{
		score = 0;
		words.clear();
	}
	
	public void roll()
	{
		resetScore();
		
		for( Die die: dice )
		{
			die.roll();
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
		
		for( int idx = 0; idx < string.length(); idx++ )
		{
			dice.add( new Die( generateFacesFromChar( string.charAt( idx ) ), idx, random ) );
		}

		assignDicePositions();
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
	
	public void print()
	{
		System.out.println( this );
	}
	
	public Die get( Integer x, Integer y )
	{
		return dice.get( x * 5 + y );
	}
	
        public List<Die> getNeighboursCached( int x, int y )
        {
            return neighbours.get( x * 5 + y );
        }
        
	public List<Die> getNeighboursSlow( int x, int y)
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
			die.usedInWord = false;
		}
	}
	
	public void solve()
	{
		resetScore();
		
		for( int x = 0; x < 5; x++ )
		{
			for( int y = 0; y < 5; y++ )
			{
				Die die = get( x, y );
				
				resetUsedFlag();
				innerSolve( die, tree.root.children.get( die.getChar() ), "" );
			}
		}
		
		resetUsedFlag();
	}
	
	private void innerSolve( Die die, TreeNode subtree, String prefix )
	{
		if( die.usedInWord || subtree == null )
		{
			return;
		}

		die.usedInWord = true;
		prefix += die.toString();

		if( subtree.isLastLetterOfWord == true )
		{
			words.add( prefix );
		}
		
		List<Die> neighbours = getNeighboursCached( die.x, die.y );
		
		for( Die neighbour : neighbours )
		{
			TreeNode nextTree = subtree.children.get( neighbour.getChar() );

			if( nextTree == null )
			{
				continue;
			}
			
			innerSolve( neighbour, nextTree, prefix );
		}
		
		die.usedInWord = false;
	}
	
	public int score()
	{
		score = 0;
		
		for( String word : words )
		{
			word.replaceAll( "q", "qu" );
			score += scoreTable.get( word.length() );
		}
		
		return score;
	}
	
	public void printWords()
	{
		System.out.println( words );
	}

	public void mutate() 
	{
		resetScore();
		
		int swapA = random.nextInt( 25 );
		int swapB = random.nextInt( 25 );
		
		Collections.swap( dice, swapA, swapB );
		
		dice.get( swapA ).roll();
		dice.get( swapB ).roll();
		
		assignDicePositions();
	}
}