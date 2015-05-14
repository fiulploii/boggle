import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Boggle 
{
	private List<String> 	dictionary 	= new ArrayList<String>();
	private List<Die>    	dice       	= new ArrayList<Die>();
	private Tree			tree	   	= new Tree();
	private	long			randomSeed	= 0x111111;
	private Random			random		= new Random( randomSeed );
        private String          maxBoard        = "";
        private int             maxScore        = 0;
        private int             boardsExplored  = 0;
        private int[][] dieCountMatrix = new int[25][25];
        private int[][] charCountMatrix = new int[255][255];	
        
	Boggle()
	{
            readDice();
            readDictionary();
            computeDieProbabilityMatrix();
	}
	
        public void newRandomWithSeed( long seed )
        {
            this.randomSeed = seed;
            random = new Random( randomSeed );
        }
        
        private void computeDieProbabilityMatrix()
        {
            for( int x = 0; x < 25; x++ )
            {
                for( int y = 0; y < 25; y++ )
                {
                    dieCountMatrix[x][y] = 0;
                }
            }
            
            for( int x = 0; x < 255; x++ )
            {
                for( int y = 0; y < 255; y++ )
                {
                    charCountMatrix[x][y] = 0;
                }
            }

            for( String word : dictionary )
            {
                for( int idx = 0; idx < word.length() - 1; idx++ )
                {
                    charCountMatrix[ word.charAt( idx ) ][ word.charAt( idx + 1 ) ]++;
                }
            }
            
            for( char firstChar = 0; firstChar < 255; firstChar++ )
            {
                for( char nextChar = 0; nextChar < 255; nextChar++ )
                {
                    List<Die> firstDice = getDiceContainingLetter( firstChar );
                    List<Die> nextDice = getDiceContainingLetter( nextChar );

                    for( Die firstDie : firstDice )
                    {
                        for( Die nextDie : nextDice )
                        {
                            if( firstDie.id != nextDie.id )
                                dieCountMatrix[ firstDie.id ][ nextDie.id ] += charCountMatrix[ firstChar ][ nextChar ];
                        }
                    }
                }
            }
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
	
        public void centerWeightedProbabilityBoard()
        {
            Board board = new Board( dice, tree, random );
            board.centerWeightedProbabilityBoard( dieCountMatrix );
            
            board.print();
            board.solve();
            board.score();
            
            System.out.println( board.score );
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
	
	public void tryRandomBoards( Board currentMasterBoard, int howMany, int currentDepth, int maxDepth )
	{
		boolean generateMutations = false;
                
		if( currentDepth > maxDepth )
		{
			return;
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
				board.swapAndRoll2RandomDice();
			}
			else
			{
				board = new Board( dice, tree, random );
			}
			
			board.solve();
			board.score();
                        boardsExplored++;
                        
                        if( boardsExplored % 600000 == 0 ) // about every 5 minutes on my machine
                        {
                            System.out.println( new Date().toString() + "\nAfter " + boardsExplored + " boards the max score is " + maxScore + " on board:\n" + maxBoard.toString() + "\n--------------------------------------------" );
                        }

			if( board.score >= maxScore )
			{
                                maxScore = board.score;
                                maxBoard = board.toString();
                                
				tryRandomBoards( board, howMany, currentDepth + 1, maxDepth );
			}
		}
	}
		
	public void mutateRuslansBoard()
	{
		Board ruslansBoard = new Board( dice, tree, random );
		ruslansBoard.readFromString( "renotvsticieraldgnephtcdb" );
		tryRandomBoards( ruslansBoard, 10000, 0, 1000 );
	}
	
	public static void main(String[] args) 
	{
            
		Boggle boggle = new Boggle();

                if( args.length > 0 )
                {
                    System.out.println( "Using random seed " + args[0] );
                    boggle.newRandomWithSeed( Long.parseLong( args[0] ) );
                }
                
                Date start = new Date();
		//boggle.tryRandomBoards( null, 1000000, 0, 10 );
                for( int idx = 0; idx < 10; idx++ )
                    boggle.centerWeightedProbabilityBoard();
                
                Date end = new Date();
                
                System.out.println( "Processing started " + start );
                System.out.println( "Processing ended " + end );
                System.out.println( "Best score out of " + boggle.boardsExplored + " is: " + boggle.maxScore );
                System.out.println( "Best board is:\n" + boggle.maxBoard );
	}

    private List<Die> getDiceContainingLetter( char letter ) 
    {
        List<Die> returnList = new ArrayList<Die>();
        
        for( Die die : dice )
        {
            if( die.toString().indexOf( letter ) >-1 )
            {
                returnList.add( die );
            }
        }
        
        return returnList;
    }
}
