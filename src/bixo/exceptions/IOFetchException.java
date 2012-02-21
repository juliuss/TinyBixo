package bixo.exceptions;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import bixo.datum.UrlStatus;

@SuppressWarnings({ "serial" })
public class IOFetchException extends BaseFetchException {
    
    public IOFetchException() {
        super();
    }
    
    public IOFetchException(String url, IOException e) {
        super(url, e);
    }

    @Override
    public UrlStatus mapToUrlStatus() {
        return UrlStatus.ERROR_IOEXCEPTION;
    }
    
    public void readFields(DataInput input) throws IOException {
        readBaseFields(input);
    }

    public void write(DataOutput output) throws IOException {
        writeBaseFields(output);
    }

    public int compareTo(IOFetchException e) {
        return compareToBase(e);
    }

}
