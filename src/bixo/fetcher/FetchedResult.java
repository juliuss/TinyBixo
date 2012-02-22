package bixo.fetcher;

import java.security.InvalidParameterException;

import bixo.datum.HttpHeaders;

public class FetchedResult {
    private final String _baseUrl;
    private final String _fetchedUrl;
    private final long _fetchTime;
    private final byte[] _content;
    private final String _contentType;
    private final int _responseRate;
    private final HttpHeaders _headers;
    private final String _newBaseUrl;
    private final int _numRedirects;
    private final String _hostAddress;
    
    public FetchedResult(   String baseUrl,
                            String redirectedUrl,
	                        long fetchTime,
	                        HttpHeaders headers, 
	                        byte[] content,
	                        String contentType,
	                        int responseRate,
	                        String newBaseUrl,
	                        int numRedirects,
	                        String hostAddress){
		
		if (baseUrl == null) {
        	throw new InvalidParameterException("baseUrl cannot be null");
        }
        
        if (redirectedUrl == null) {
        	throw new InvalidParameterException("redirectedUrl cannot be null");
        }
        
        if (headers == null) {
        	throw new InvalidParameterException("headers cannot be null");
        }
        
        if (content == null) {
        	throw new InvalidParameterException("content cannot be null");
        }
        
        if (contentType == null) {
            throw new InvalidParameterException("contentType cannot be null");
        }
        
        if (hostAddress == null) {
            throw new InvalidParameterException("hostAddress cannot be null");
        }
        
        _baseUrl = baseUrl;
        _fetchedUrl = redirectedUrl;
        _fetchTime = fetchTime;
        _content = content;
        _contentType = contentType;
        _responseRate = responseRate;
        _headers = headers;
        _newBaseUrl = newBaseUrl;
        _numRedirects = numRedirects;
        _hostAddress = hostAddress;
	}

	public String getBaseUrl() {
		return _baseUrl;
	}

	public String getFetchedUrl() {
		return _fetchedUrl;
	}

	public long getFetchTime() {
		return _fetchTime;
	}

	public byte[] getContent() {
		return _content;
	}

	public String getContentType() {
		return _contentType;
	}

	public int getResponseRate() {
		return _responseRate;
	}

	public HttpHeaders getHeaders() {
		return _headers;
	}

	public String getNewBaseUrl() {
		return _newBaseUrl;
	}

	public int getNumRedirects() {
		return _numRedirects;
	}

	public String getHostAddress() {
        return _hostAddress;
    }
}
