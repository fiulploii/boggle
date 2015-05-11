public class Die
{
	String faces;
	int rolledFace = 0;
	int id = 0;
	
	Die( String face, Integer id )
	{
		this.faces = face;
		this.id = id;
	}

	public String toString()
	{
		return faces.substring( rolledFace, rolledFace + 1 );
	}
	
	char getChar()
	{
		return faces.charAt( rolledFace );
	}
	
	void roll( int face )
	{
		this.rolledFace = face;
	}
}
