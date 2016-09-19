package main.java.SaslTest;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;

/**
 * Created by wso2 on 8/31/16.
 */
public class SASLClient implements SaslClient {
    @Override
    public String getMechanismName() {

        return null;
    }

    @Override
    public boolean hasInitialResponse() {
        return false;
    }

    @Override
    public byte[] evaluateChallenge(byte[] challenge) throws SaslException {
        return new byte[0];
    }

    @Override
    public boolean isComplete() {
        return false;
    }

    @Override
    public byte[] unwrap(byte[] incoming, int offset, int len) throws SaslException {
        return new byte[0];
    }

    @Override
    public byte[] wrap(byte[] outgoing, int offset, int len) throws SaslException {
        return new byte[0];
    }

    @Override
    public Object getNegotiatedProperty(String propName) {
        return null;
    }

    @Override
    public void dispose() throws SaslException {

    }
}
