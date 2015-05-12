import java.util.Random;

public class Die
{
	public 			int 				id 					= 0;
	public 			int 				x 					= 0;
	public 			int 				y 					= 0;
	public 			boolean 			usedInWord 			= false;
	public 			char 				rolledFace			= '\0';

	private 		String				rolledFaceString	= "";
	private 		Random 				random 				= null;
	private			String 				faces				= null;
	
	
	Die( String faces, int id, Random random )
	{
		this.faces 	= faces;
		this.id 	= id;
		this.random = random;
		
		roll();
	}

	public String toString()
	{
		return rolledFaceString;
	}
	
	public char getChar()
	{
		return rolledFace;
	}
	
	public void roll()
	{
		rolledFace 			= faces.charAt( random.nextInt( 6 ) );
		rolledFaceString 	= String.valueOf( rolledFace );
	}
}
