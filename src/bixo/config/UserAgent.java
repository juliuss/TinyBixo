package bixo.config;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UserAgent implements Serializable {
	public static final String DEFAULT_BROWSER_VERSION = "Mozilla/5.0";
	
	private final String _userAgent;
	
	public UserAgent(String userAgent) {
		_userAgent = userAgent;
	}

	public String getUserAgentString() {
		return _userAgent;
	}
}
