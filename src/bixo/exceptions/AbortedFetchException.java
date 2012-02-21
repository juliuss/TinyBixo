package bixo.exceptions;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import bixo.datum.UrlStatus;

@SuppressWarnings({ "serial" })
public class AbortedFetchException extends BaseFetchException {
    private AbortedFetchReason _abortReason;
    
    public AbortedFetchException() {
        super();
    }
    
    public AbortedFetchException(String url, AbortedFetchReason abortReason) {
        super(url, "Aborted due to " + abortReason);
        
        _abortReason = abortReason;
    }
    
    public AbortedFetchException(String url, String msg, AbortedFetchReason abortReason) {
        super(url, msg);
        
        _abortReason = abortReason;
    }
    
    public AbortedFetchReason getAbortReason() {
        return _abortReason;
    }

    @Override
    public UrlStatus mapToUrlStatus() {
        switch (_abortReason) {
        case SLOW_RESPONSE_RATE:
            return UrlStatus.ABORTED_SLOW_RESPONSE;

        case INVALID_MIMETYPE:
            return UrlStatus.ABORTED_INVALID_MIMETYPE;
            
        case INTERRUPTED:
            return UrlStatus.SKIPPED_INTERRUPTED;
            
        default:
            throw new RuntimeException("Unknown abort reason: " + _abortReason);
        }
    }

    public void readFields(DataInput input) throws IOException {
        readBaseFields(input);
        
        _abortReason = AbortedFetchReason.valueOf(input.readUTF());
    }

    public void write(DataOutput output) throws IOException {
        writeBaseFields(output);
        output.writeUTF(_abortReason.name());
    }

    public int compareTo(AbortedFetchException e) {
        int result = compareToBase(e);
        if (result == 0) {
            result = _abortReason.compareTo(e._abortReason);
        }
        
        return result;
    }
}
