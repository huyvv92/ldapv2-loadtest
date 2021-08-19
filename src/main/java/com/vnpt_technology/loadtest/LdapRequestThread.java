package com.vnpt_technology.loadtest;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by huyvv
 * Date: 18/08/2021
 * Time: 5:39 PM
 * for all issues, contact me: huyvv@vnpt-technology.vn
 **/
public class LdapRequestThread extends Thread {

    private final String filter;
    private final long totalRunTime;
    private final String urlLdap;
    private final Set<StringBuffer> resultCSV = new LinkedHashSet<>();
    private DirContext ctx;

    public LdapRequestThread(String filter, long totalRunTime, String urlLdap) {
        this.filter = filter;
        this.totalRunTime = totalRunTime;
        this.urlLdap = urlLdap;
        this.connect();
    }

    public Set<StringBuffer> getResultCSV() {
        return resultCSV;
    }

    public void connect() {
        try {
            Hashtable env = new Hashtable();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, this.urlLdap);
            env.put("java.naming.ldap.version", "2");
            env.put("java.naming.ldap.derefAliases", "never");
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put(Context.SECURITY_PRINCIPAL, "dview=HNOCS5_request_data");
            env.put(Context.SECURITY_CREDENTIALS, "BIND");
            this.ctx = new InitialDirContext(env);
        } catch (NamingException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            SearchControls ctrl = new SearchControls();
            ctrl.setSearchScope(SearchControls.ONELEVEL_SCOPE);

            long startTime = System.currentTimeMillis();
            long currentTime = startTime;
            while (currentTime - startTime < totalRunTime) {
                //StringBuffer result = new StringBuffer();
                //result.append(this.getName()).append(";").append(currentTime);
                NamingEnumeration<SearchResult> answer = ctx.search("dview=HNOCS5_request_data", filter, ctrl);
                long responseTime = System.currentTimeMillis();
                //result.append(";").append(responseTime);
//                while (answer.hasMore()) {
//                    SearchResult entry = answer.next();
//                    NamingEnumeration<String> attrs = entry.getAttributes().getIDs();
//                    while (attrs.hasMore()) {
//                        String key = attrs.next();
//                        result.append(";").append(key).append("=").append(entry.getAttributes().get(key).get());
//                    }
//                }
                answer.close();
                //resultCSV.add(result);
                currentTime = System.currentTimeMillis();
            }

            ctx.close();
        }catch (NamingException ex) {
            ex.printStackTrace();
        }
    }
}
