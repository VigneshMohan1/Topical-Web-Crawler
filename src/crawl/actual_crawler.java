package crawl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class actual_crawler {
	Queue<String> url_queue=new LinkedList<String>();
	BufferedWriter bw;
	int m=0;
	public actual_crawler(Queue<String> url_queue) throws IOException  {
		this.url_queue=url_queue;
		
	}
	/*
	 * 
	 * Initialize a variable and have a bound on the url_queue else loops infinitely
	 * 
	 * 
	 */
	public Queue<String> crawl() throws IOException
	{
		Queue<String> temp_queue=new LinkedList<String>();
		while (!url_queue.isEmpty())
		{	
			/* dequeue seed URL*/
			String seedURL=url_queue.remove();
			System.out.println(seedURL);
			/* call function here to test the test the relevance of seed URL
			 * 
			 * if (relevant)
			 *  go head to extract anchor texts
			 * else
			 *  continue;
			 * */
			Document doc = Jsoup.connect(seedURL).timeout(0).ignoreContentType(true).ignoreHttpErrors(true).get();
			Elements links = doc.select("a[href]");
			Elements links1 = links.not("a[href~=(?i)\\.(png|jpe?g)]");
			/* extract anchor text from the seed URL*/
			String[] link_context=new String[links1.size()];
			String[] link_text=new String[links1.size()];
			ArrayList<String[]> siblings=new ArrayList<String[]>();
			int i=0;
			for (Element link : links1)
			{
				Element parent=link.parent();
				Elements sibling=link.siblingElements();
				while (parent!=null&&parent.text()==null&&parent.text().matches("^"))
				{
					parent=parent.parent();
				}
				int sibcount=0;
				String[] sibling_elem=new String[sibling.size()];
				for (Element s:sibling)
				{
					sibling_elem[sibcount]=s.text();
					sibcount++;
				}
				siblings.add(sibling_elem);
				link_context[i]=parent.text();
				link_text[i]=link.text();
				temp_queue.add(link.attr("abs:href"));
				i++;
			}
			if (m>=200)
			{
				return url_queue;
			}
			breadth_first(temp_queue,link_context,link_text,siblings);
		}
		return url_queue;
	}
	public void breadth_first(Queue<String> temp_queue,String[] link_context,String[] link_text,ArrayList<String[]> siblings) throws IOException
	{
		int i=0;
		while (!temp_queue.isEmpty())
		{
			/*
			 * if (relevant)
			 *   enqueue anchor text to url_queue and dequeue anchor text from temp_queue  
			 *  */
			System.out.println("---------------------h");
			System.out.println(temp_queue.element()+"  "+m);
			if (m>=200)
			{
				return;
			}
			m++;
			/*
			 *  link text
			 */
			System.out.println("---------------------");
			System.out.println(link_text[i]);
			/*
			 *  link context
			 */
			System.out.println("---------------------");
			System.out.println(link_context[i]);
			/*
			 * 
			 * siblings
			 */
			System.out.println("--------------------");
			for (String s : siblings.get(i))
			{
				System.out.println(s);
			}
			/*
			 * if relevant add to url_queue and dequeue anchor text from temp_queue 
			 * 
			 */
			/* condition to eliminate duplicate URLs in url queue*/
			if (!url_queue.contains(temp_queue))
				url_queue.add(temp_queue.element());
			temp_queue.remove();
			i++;
		}
	}
}
