package bixo.fetcher;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Tuple2<T1, T2> {
  public final T1 _1;
  public final T2 _2;

  public Tuple2(T1 o1, T2 o2) {
    _1 = o1;
    _2 = o2;
  }
  
  public T1 getKey() {
    return _1;
  }
  
  public T2 getValue() {
    return _2;
  }
  
  @SuppressWarnings("deprecation")
  @Override
  public String toString() {
    try {
      return  URLEncoder.encode(_1.toString(),"utf-8") + "=" + URLEncoder.encode(_2.toString(),"utf-8");
    } catch (UnsupportedEncodingException e) {
      return  URLEncoder.encode(_1.toString()) + "=" + URLEncoder.encode(_2.toString());
    }
  }
}
