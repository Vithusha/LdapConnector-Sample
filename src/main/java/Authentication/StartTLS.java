package main.java.Authentication;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.naming.ldap.StartTlsRequest;
import javax.naming.ldap.StartTlsResponse;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.util.Hashtable;

/**
 * Created by wso2 on 9/12/16.
 */
public class StartTLS {

   // public void initConnection() {
    public static void main(String[] args){

        // Set up environment for creating initial context
        Hashtable env = new Hashtable(11);
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://localhost:389/dc=wso2,dc=com");
        env.put(Context.SECURITY_AUTHENTICATION,"simple");
        env.put(Context.SECURITY_PRINCIPAL,"cn=admin,dc=wso2,dc=com");
        env.put(Context.SECURITY_CREDENTIALS,"admin");


       ;



        try
        {
            // Create initial context
            LdapContext ctx = new InitialLdapContext(env, null);
            System.out.println("Connection established");


            Attributes attrs = ctx.getAttributes("ldap://localhost:389", new String[]{"supportedextension"});
            new LdapReadManager().printAttrs(attrs);
            System.out.println(ctx.getAttributes(""));

            // Start TLS

           // new LdapWriteManager().addEntry(ctx,"Shantha","Gunasena","Marketing Manager","ou=manager,ou=people");
            StartTlsResponse tls =
                    (StartTlsResponse) ctx.extendedOperation(new StartTlsRequest());


            tls.setHostnameVerifier( new HostnameVerifier()
            {
                public boolean verify( String hostname, SSLSession session )
                {
                    return true;
                }
            } );

             tls.negotiate();







           new LdapReadManager().getNameList("cn=Priya,dc=wso2,dc=com",ctx);


           // new LdapWriteManager().addAttribute(ctx,"cn=Janitha,ou=manager,ou=people,dc=wso2,dc=com","postalCode","30000");


            // Stop TLS
            tls.close();

            // Close the context when we're done
            ctx.close();

        } catch (NamingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
