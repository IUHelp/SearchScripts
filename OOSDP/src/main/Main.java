package main;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.*;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;


import org.jsoup.Jsoup;  

import org.jsoup.nodes.Element;  

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main {

	 private static IndexWriter writer;		  // new index being built
	    private static ArrayList<String> indexed;
	    private static String beginDomain;
	   
	    
	    public static void main(String[] args) throws Exception{
	        String index = "/Users/nethra/Documents/workspace/OOSDP";
	        Directory dir = FSDirectory.open(Paths.get(index));
	        boolean create = true;
	        String link = "https://kb.iu.edu/";
	        beginDomain = Domain(link);
	        Analyzer analyzer=new StandardAnalyzer();
	    	IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
	    	iwc.setOpenMode(OpenMode.CREATE);
	    	writer = new IndexWriter(dir, iwc);
	        indexed = new ArrayList<String>();
	        PrintWriter printWriter = new PrintWriter("Corpus.txt", "UTF-8");;
	        indexDocs(link,printWriter);
	        printWriter.close();
	        writer.close();
	    }
	    private static void generateCorpus(String url,PrintWriter printWriter){
	    	// TODO Auto-generated method stub
			
	        System.out.println("Fetching "+url);
	       

	        try {
	        	
	        	String title = "Test";
	        	printWriter.println("<page>");
	            printWriter.println("<id>"+url+"</id>");
	            printWriter.println("<title>"+title+"</title>");
	         
	            //<title> title of the page </title>
	         
	            org.jsoup.nodes.Document doc = Jsoup.connect(url).get();
				Element body = doc.body();
				body.select("script, style, .hidden").remove();
				printWriter.println("<text>");
				printWriter.println(body.text());
				printWriter.println("</text>");
				printWriter.println("</page>");
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
	    	
	    }


	    private static void indexDocs(String url,PrintWriter printWriter) throws Exception {
	        //index page
	        Document doc = HTMLDocumen.Document(url);
	        //System.out.println("adding " + doc.get("path"));
	        
	        try {
	            indexed.add(doc.get("path"));
	            writer.addDocument(doc);		  // add docs unconditionally
	            //TODO: only add html docs
	            //and create other doc types
	            //get all links on the page then index them
	            LinkParser lp = new LinkParser(url);
	            URL[] links = lp.ExtractLinks();
	         
	            
	            for (URL l : links) {
	                //make sure the url hasnt already been indexed
	                //make sure the url contains the home domain
	                //ignore urls with a querystrings by excgluding "?" 
	                if ((!indexed.contains(l.toURI().toString())) && (l.toURI().toString().contains(beginDomain)) && (!l.toURI().toString().contains("?"))) {
	                    //don't index zip files
	                    if (!l.toURI().toString().endsWith(".zip") && !l.toURI().toString().endsWith(".pdf") && !l.toURI().toString().endsWith(".jpg")&&!l.toURI().toString().endsWith(".png"))
	                    {
//	                    System.out.print(l.toURI().toString());
	                    
	                    generateCorpus(url,printWriter);
	                    indexDocs(l.toURI().toString(),printWriter);
	                    }
	                }
	            }

	        } catch (Exception e) {
	           
	        }
	    }
	 
	    private static String Domain(String url)
		 {
		     int firstDot = url.indexOf(".");
		     int lastDot =  url.lastIndexOf(".");
		     return url.substring(0,lastDot);
		     //return url.substring(firstDot+1,lastDot);
		 }
	 
	 
}
