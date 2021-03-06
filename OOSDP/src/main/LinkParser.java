package main;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import java.util.Vector;
import java.net.URL;
import java.net.MalformedURLException;

public class LinkParser {
    
    String url;
    Parser parser;
    NodeFilter filter;
    NodeList list;
    LinkTag link;
    URL[] linkArray;
    Vector vector;
    
    @SuppressWarnings("rawtypes")
	public LinkParser(String Url)
    {
        url = Url;
        vector=new Vector(1,4);
    }
    
    @SuppressWarnings("unchecked")
	public URL[] ExtractLinks()
    {
      filter = new NodeClassFilter(LinkTag.class);        
            
        try
        {
            parser = new Parser (url);
            list = parser.extractAllNodesThatMatch (filter);
                for (int i = 0; i < list.size (); i++)
                       try
                        {
                            link = (LinkTag)list.elementAt (i);
                            vector.add(new URL (link.getLink ()));
                        }
                        catch (MalformedURLException murle)
                        {
                        }
                linkArray = new URL[vector.size ()];
                vector.copyInto (linkArray);
        }
        catch (ParserException e)
        {
            e.printStackTrace ();
        }
      
        return (linkArray);
    }
 }

