package main.java.Authentication;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * Created by wso2 on 9/12/16.
 */
public class LdapWriteManager {


    public void addEntry(DirContext ctx,String name_cn, String name_sn, String name_title,String role) {
        try {
            // Create a new set of attributes
            BasicAttributes attrs = new BasicAttributes();


            // The item is an organizationalPerson, which is a subclass of person.
            // Person is a subclass of top. Store the class hierarchy in the
            // objectClass attribute
            Attribute classes = new BasicAttribute("objectclass");
            classes.add("top");
            classes.add("person");
            classes.add("organizationalPerson");

            // Add the objectClass attribute to the attribute set
            attrs.put(classes);

            // Store the other attributes in the attribute set
            attrs.put("sn", name_sn);
            attrs.put("title", name_title);
            // attrs.put("mail", "samantha@wutka.com");

            // Add the new entry to the directory server
            String entry="ldap://ldap.wso2.com/cn="+name_cn+","+role+",dc=wso2,dc=com";
            System.out.println(entry);
            ctx.createSubcontext(entry, attrs);

            // Create another set of attributes
            attrs = new BasicAttributes();

            // Use the same objectClass attribute as before
            attrs.put(classes);

        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    public void changeAttributeValue(DirContext ctx,String dn,String attr,String value) throws NamingException {

        // Attribute orig=ctx.getAttributes("cn=Janitha,ou=manager,ou=people,dc=wso2,dc=com"){};
        String name="ldap://localhost:389/"+dn;
        Attributes orig = ctx.getAttributes(name, new String[] {attr});


        ModificationItem[] mods = new ModificationItem[1];

        // Add additional value to "telephonenumber"
        mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                new BasicAttribute(attr, value));


        // Perform the requested modifications on the named object
        ctx.modifyAttributes(name, mods);

    }


    public void addAttribute(DirContext ctx,String dn,String attr,String value) throws NamingException {


        String name="ldap://localhost:389/"+dn;

        Attributes add=ctx.getAttributes(name,new String[] {attr});

        ModificationItem[] mods=new ModificationItem[1];

        mods[0]=new ModificationItem(ctx.ADD_ATTRIBUTE,new BasicAttribute(attr,value));

        ctx.modifyAttributes(name,mods);

    }





    public void changePassword(DirContext ctx,String name,String password) throws NamingException {

        String entry="ldap://localhost:389/"+name;

        Attributes pass = ctx.getAttributes(entry, new String[] {"userPassword"
                });


        ModificationItem[] mods=new ModificationItem[1];
        mods[0]=new ModificationItem(ctx.REPLACE_ATTRIBUTE,new BasicAttribute("userPassword",password));

        ctx.modifyAttributes(entry,mods);

    }


    public void removeAttribute(DirContext ctx,String dn,String attr) throws NamingException {

        String name= "ldap://localhost:389/"+dn;

        Attributes add=ctx.getAttributes(name,new String[] {attr});


        ModificationItem[] mods=new ModificationItem[1];
        mods[0]=new ModificationItem(ctx.REMOVE_ATTRIBUTE,new BasicAttribute(attr));
        ctx.modifyAttributes(name,mods);
    }





    public void deleteEntry(DirContext context) {
        try {
            String dn="ldap://ldap.wso2.com/uid=indika,ou=manager,ou=people,dc=wso2,dc=com";

            Attributes matchingAttr = new BasicAttributes(); //search for the existance of dn
            matchingAttr.put(new BasicAttribute("dn"));
            NamingEnumeration<SearchResult> searchResult = context.search(dn, matchingAttr);



            try {

                context.destroySubcontext(dn);

            } catch (NamingException e) {
                e.printStackTrace();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}




