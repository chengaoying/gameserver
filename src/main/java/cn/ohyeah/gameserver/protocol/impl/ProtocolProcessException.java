package cn.ohyeah.gameserver.protocol.impl;

public class ProtocolProcessException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = -159637313303701419L;

	public ProtocolProcessException() {}

    public ProtocolProcessException(String message) {
        super(message);
    }

    public ProtocolProcessException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProtocolProcessException(Throwable cause) {
        super(cause);
    }
}
