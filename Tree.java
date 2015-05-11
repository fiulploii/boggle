import java.util.List;

public class Tree 
{
	TreeNode root = new TreeNode( '\0' );
	
	public void loadDictionary( List<String> dictionary )
	{
		for( String word : dictionary )
		{
			root.addWord( word );
		}
	}
}
