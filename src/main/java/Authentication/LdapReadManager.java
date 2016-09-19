package main.java.Authentication;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.naming.ldap.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by wso2 on 9/12/16.
 */



    /**
     * Demonstrates how to retrieve all attributes of a named object.
     *
     * usage: java StartTLS
     */
    class LdapReadManager {

        void printAttrs(Attributes attrs) {
            if (attrs == null) {
                System.out.println("No attributes");
            } else {
      /* Print each attribute */
                try {
                    for (NamingEnumeration ae = attrs.getAll(); ae.hasMore(); ) {
                        Attribute attr = (Attribute) ae.next();
                        System.out.println("attribute: " + attr.getID());

          /* print each value */
                        for (NamingEnumeration e = attr.getAll(); e.hasMore(); System.out
                                .println("value: " + e.next()))
                            ;
                    }
                } catch (NamingException e) {
                    e.printStackTrace();
                }
            }
        }


        public void getNameList(String cn, DirContext ctx) {


            NamingEnumeration results = null;
            try {
                SearchControls controls = new SearchControls();
                controls.setSearchScope(SearchControls.SUBTREE_SCOPE);

                if (cn != null && !cn.equalsIgnoreCase("")) {


                    results = ctx.search("", "(cn=" + cn + ")", controls);
                    System.out.println(cn + " is cn");
                } else {
                    System.out.println("No results found");
                }
                while (results.hasMore()) {

                    SearchResult searchResult = (SearchResult) results.next();
                    Attributes attributes = searchResult.getAttributes();
                    System.out.println("Full Name:  " + attributes.get("cn").get());
                    if (attributes.get("givenName") != null)
                        System.out.println("First Name:  " + attributes.get("givenName").get());
                    System.out.println("Last Name: " + attributes.get("sn").get());

                }


            } catch (Exception e) {
                System.out.println("Error : " + e);
            } finally {
                if (results != null) {
                    try {
                        results.close();
                    } catch (Exception e) {
                        System.out.println("Error : " + e);
                    }
                }
            }
        }



        public void getRoleList(String ou, DirContext ctx) {


            NamingEnumeration results = null;
            try {
                SearchControls controls = new SearchControls();
                controls.setSearchScope(SearchControls.SUBTREE_SCOPE);

                if (ou != null && !ou.equalsIgnoreCase("")) {


                    results = ctx.search("", "(ou=" + ou + ")", controls);
                    System.out.println(ou + " is ou");
                } else {
                    System.out.println("No results found");
                }
                while (results.hasMore()) {

                    SearchResult searchResult = (SearchResult) results.next();

                    Attributes attributes = searchResult.getAttributes();
                    System.out.println(attributes.get("dc=wso2,dc=com").get());

                }

            } catch (Exception e) {
                System.out.println("Error : " + e);
            } finally {
                if (results != null) {
                    try {
                        results.close();
                    } catch (Exception e) {
                        System.out.println("Error : " + e);
                    }
                }
            }
        }


        private String getUserDN(String username) {
            return new StringBuffer()
                    .append("uid=")
                    .append(username)
                    .append(",")
                    .append(ConnectorConstants.USERS_OU)
                    .toString();
        }


        private String getUserUID(String userDN) {
            int start = userDN.indexOf("=");
            int end = userDN.indexOf(",");

            if (end == -1) {
                end = userDN.length();
            }

            return userDN.substring(start + 1, end);
        }

        private String getGroupDN(String name) {
            return new StringBuffer()
                    .append("ou=")
                    .append(name)
                    .append(",")
                    .append(ConnectorConstants.GROUPS_OU)
                    .toString();
        }

        public boolean userInGroup(String username, String groupName, DirContext ctx)
                throws NamingException {

            // Set up attributes to search for
            String[] searchAttributes = new String[1];
            searchAttributes[0] = "uniqueMember";

            Attributes attributes =
                    ctx.getAttributes(getGroupDN(groupName),
                            searchAttributes);
            if (attributes != null) {
                Attribute memberAtts = attributes.get("uniqueMember");
                if (memberAtts != null) {
                    for (NamingEnumeration vals = memberAtts.getAll();
                         vals.hasMoreElements();
                            ) {
                        if (username.equalsIgnoreCase(
                                getUserUID((String) vals.nextElement()))) {
                            return true;
                        }
                    }
                }
            }

            return false;
        }


    }

