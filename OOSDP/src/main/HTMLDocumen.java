package main;
import java.io.*;
import org.apache.lucene.document.*;
import org.htmlparser.beans.StringBean;
import org.htmlparser.Parser;
import org.htmlparser.NodeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.ParserException;
public class HTMLDocumen {
	public static Document Document(String url)throws IOException, InterruptedException  {
		    // make a new, empty document
		  Document doc = new Document();
		  String title = new String();
		  String summary = new String();

		  // Add the url as a field named "path".e. searchable), but don't  Use a field that is 
		  // indexed (i. tokenize the field into words.
		  doc.add(new StringField("path", url, Field.Store.YES));
		  // Add the tag-stripped contents as a Reader-valued Text field so it will
		  // get tokenized and indexed.
		  StringBean sb = new StringBean();
		    sb.setLinks (false);
		    sb.setURL (url);
		    StringReader sr = new StringReader(sb.getStrings());
		    doc.add(new TextField("contents", sr));
		    Parser bParser;
		    NodeFilter bFilter;

		            try
		            {
		                bParser = new Parser ();
		                bFilter = new TagNameFilter ("TITLE");
		                bParser.setResource (url);
		                title = bParser.parse( bFilter).asString();
		       
		            }
		            catch (ParserException e)
		            {
		                e.printStackTrace ();
		            }
		    
		            try
		            {
		                bParser = new Parser ();
		                bFilter = new TagNameFilter ("BODY");
		                bParser.setResource (url);
		                try
		                {
		                summary = bParser.parse( bFilter).asString().substring(0, 1000);
		                }
		                catch(StringIndexOutOfBoundsException e)
		                {
		                   summary = "";
		                }
		                
		            }
		            catch (ParserException e)
		            {
		                e.printStackTrace ();
		            }
		    //System.out.print(title);
		    System.out.print(summary);
		    //Add the title as a field that it can be searched and that is stored.
		    doc.add(new TextField("title", title, Field.Store.YES));
		    doc.add(new TextField("summary",summary, Field.Store.YES));
		    return doc;
		  }

		  private HTMLDocumen() {}
}
