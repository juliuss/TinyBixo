package bixo.exceptions;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import bixo.datum.UrlStatus;

@SuppressWarnings({ "serial" })
public class RedirectFetchException extends BaseFetchException {
    
    // Possible redirect exception types.
    
    public enum RedirectExceptionReason {
        TOO_MANY_REDIRECTS,         // Request for original URL tried too many hops.
        PERM_REDIRECT_DISALLOWED,   // RedirectMode disallows a permanent redirect.
        TEMP_REDIRECT_DISALLOWED    // RedirectMode disallows a temp redirect.
    }

    private String _redirectedUrl;
    private RedirectExceptionReason _reason;
    
    public RedirectFetchException() {
        super();
    }
    
    public RedirectFetchException(String url, String redirectedUrl, RedirectExceptionReason reason) {
        super(url, "Too many redirects");
        _redirectedUrl = redirectedUrl;
        _reason = reason;
    }
    
    public String getRedirectedUrl() {
        return _redirectedUrl;
    }
    
    public RedirectExceptionReason getReason() {
        return _reason;
    }
    
    @Override
    public UrlStatus mapToUrlStatus() {
        if (_reason == RedirectExceptionReason.TOO_MANY_REDIRECTS) {
            return UrlStatus.HTTP_TOO_MANY_REDIRECTS;
        } else if (_reason == RedirectExceptionReason.TEMP_REDIRECT_DISALLOWED) {
            return UrlStatus.HTTP_REDIRECTION_ERROR;
        } else if (_reason == RedirectExceptionReason.PERM_REDIRECT_DISALLOWED) {
            return UrlStatus.HTTP_MOVED_PERMANENTLY;
        } else {
            throw new RuntimeException("Unknown redirection exception reason: " + _reason);
        }
    }

    public void readFields(DataInput input) throws IOException {
        readBaseFields(input);
        
        _redirectedUrl = input.readUTF();
        _reason = RedirectExceptionReason.valueOf(input.readUTF());
    }

    public void write(DataOutput output) throws IOException {
        writeBaseFields(output);
        output.writeUTF(_redirectedUrl);
        output.writeUTF(_reason.name());
    }

    public int compareTo(RedirectFetchException e) {
        int result = compareToBase(e);
        if (result == 0) {
            result = _redirectedUrl.compareTo(e._redirectedUrl);
        }
        if (result == 0) {
            result = _reason.compareTo(e._reason);
        }
        
        return result;
    }

}
