package crawl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class topical_crawler {
	public static void main(String[] args) throws IOException,InterruptedException {
		Queue<String> main_queue=new LinkedList<String>();
		/* list of seed URLs*/
		main_queue.add("https://en.wikipedia.org/wiki/Agriculture");
		main_queue.add("https://en.wikipedia.org/wiki/Wheat");
		main_queue.add("https://en.wikipedia.org/wiki/Rice");
		actual_crawler as=new actual_crawler(main_queue);
		as.crawl();
		System.out.println(as.url_queue.size());
		File file = new File("/home/vignesh/workspace1/url.txt");


		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		for (String s : as.url_queue)
		{
			bw.write(s);
			bw.write('\n');
		}
		bw.close();
	}
}
