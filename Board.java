import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Board 
{
	private List<Die> 		dice;
        private List<Die>               availableDice   = new ArrayList<Die>();
	private Random 			random		= null;
	private Tree			tree		= null;
	private List<Integer>           scoreTable 	= Arrays.asList( 0, 0, 0, 0, 1, 2, 3, 5, 11 );
        private List<List<Die>>         neighbours      = new ArrayList<List<Die>>();
	
	public  HashSet<String>	words		= new HashSet<String>();
	public  int				score		= 0;
	
	Board( Board originalBoard )
	{
		this.dice = originalBoard.dice;
                for( int idx = 0; idx < this.dice.size(); idx++ )
                    this.availableDice.add( this.dice.get( idx ) );
                
		this.tree = originalBoard.tree;
		this.random = originalBoard.random;
                
                createNeighboursCache();
	}
	
	Board( List<Die> diceList, Tree tree, Random random )
	{
		this.dice 	= diceList;
                for( int idx = 0; idx < this.dice.size(); idx++ )
                    this.availableDice.add( this.dice.get( idx ) );
                
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
            resetScore();
            
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
                
                availableDice = dice;

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

        public void swap2RandomDice()
        {
		resetScore();
		
		int swapA = random.nextInt( 25 );
		int swapB = random.nextInt( 25 );
		
		Collections.swap( dice, swapA, swapB );
		
		assignDicePositions();
        }
        
	public void swapAndRoll2RandomDice() 
	{
		resetScore();
		
		int swapA = random.nextInt( 25 );
		int swapB = random.nextInt( 25 );
		
		Collections.swap( dice, swapA, swapB );
		
		dice.get( swapA ).roll();
		dice.get( swapB ).roll();
		
		assignDicePositions();
	}
        
        private void placeDie( Die die, List<Die> fromList, int x, int y )
        {
            dice.add( die );
            die.x = x;
            die.y = y;
            
            fromList.remove( die );
        }
        
        public void centerWeightedProbabilityBoard( int[][] dieCountMatrix )
        {
            List<Integer> ring1X = Arrays.asList( 1, 1, 1, 2, 3, 3, 3, 2 );
            List<Integer> ring1Y = Arrays.asList( 1, 2, 3, 3, 3, 2, 1, 1 );
            
            List<Integer> ring2X = Arrays.asList( 0, 0, 0, 0, 0, 1, 2, 3, 4, 4, 4, 4, 4, 3, 2, 1 );
            List<Integer> ring2Y = Arrays.asList( 0, 1, 2, 3, 4, 4, 4, 4, 4, 3, 2, 1, 0, 0, 0, 0 );
            
            List<Die> diceStillRemaining = new ArrayList<Die>();
            
            for( int idx = 0; idx < availableDice.size(); idx++ )
                    diceStillRemaining.add( availableDice.get( idx ) );
            
            dice.clear();
            
            // place center piece
            Die centerDie = diceStillRemaining.get( random.nextInt( diceStillRemaining.size() ) );
            
            placeDie( centerDie, diceStillRemaining, 2, 2 );
            
            // get next dice
            List<Die> ring1Dice = getNextBestDice( centerDie.id, 8, dieCountMatrix );
            Collections.shuffle( ring1Dice, random );
            
            // place ring 1
            for( int idx = 0; idx < ring1X.size(); idx++ )
            {
                int x = ring1X.get( idx );
                int y = ring1Y.get( idx );
                
                placeDie( ring1Dice.get( idx ), diceStillRemaining, x, y);
            }
            
            Collections.shuffle( diceStillRemaining, random );
            
            // place ring 2
            for( int idx = 0; idx < ring2X.size(); idx++ )
            {
                int x = ring2X.get( idx );
                int y = ring2Y.get( idx );
                
                placeDie( diceStillRemaining.get( 0 ), diceStillRemaining, x, y );
            }
        }
        
        private List<Die> getNextBestDice( int baseDie, int howMany, int[][] dieCountMatrix )
        {
            Integer[] returnValue = new Integer[ howMany ];
            Map<Integer, Integer> nextDiceMap = new HashMap<Integer, Integer>();
            
            for( int idx = 0; idx < 25; idx++ )
            {
                if( idx != baseDie )
                {
                    nextDiceMap.put( idx, dieCountMatrix[baseDie][idx] );
                }
            }
            
            Map<Integer,Integer> sortedNextDiceMap = sortByValue( nextDiceMap );
            
            int count = 0;
            for( Integer dieId : sortedNextDiceMap.keySet() )
            {
                returnValue[ count ] = dieId;
                count++;
                
                if( count >= howMany )
                    break;
            }
            
            List<Die> returnDice = new ArrayList<Die>();
            
            for( int idx = 0; idx < returnValue.length; idx++ )
            {
                for( Die die : availableDice )
                {
                    if( die.id == returnValue[idx] )
                    {
                        returnDice.add( die );
                    }
                }
            }
            
            return returnDice;
        }
        
    public static Map sortByValue(Map unsortMap) 
    {
        List list = new LinkedList(unsortMap.entrySet());

        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                        .compareTo(((Map.Entry) (o2)).getValue());
            }
        });

        Map sortedMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }
}