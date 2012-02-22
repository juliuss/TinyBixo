import bixo.config.UserAgent;
import bixo.exceptions.BaseFetchException;
import bixo.fetcher.FetchedResult;
import bixo.fetcher.SimpleHttpFetcher;

public class Test {
  
  public static UserAgent userAgent = new UserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_0) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.220 Safari/535.1");
  
  public static void main(String args[]) {
    SimpleHttpFetcher fetcher = new SimpleHttpFetcher(50, userAgent);
    try {
      FetchedResult result = fetcher.fetch("http://www.google.com");
      String html = new String(result.getContent());
      System.out.println(html);
      System.out.println(result.getNumRedirects());
      System.out.println(result.getFetchedUrl());
      
      result = fetcher.fetch("http://www.bhphotovideo.com/c/buy/Camcorders/ci/1871/N/4294548093");
      html = new String(result.getContent());
      System.out.println(html);
      System.out.println(result.getNumRedirects());
      System.out.println(result.getFetchedUrl());
      
      result = fetcher.fetch("https://www.google.com/");
      html = new String(result.getContent());
      System.out.println(html);
      System.out.println(result.getNumRedirects());
      System.out.println(result.getFetchedUrl());
     
    } catch (BaseFetchException e) {
      e.printStackTrace();
    }
  }
}
