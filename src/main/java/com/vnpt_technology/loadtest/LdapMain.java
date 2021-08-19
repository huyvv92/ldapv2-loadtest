package com.vnpt_technology.loadtest;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.util.Hashtable;

/**
 * Created by huyvv
 * Date: 17/08/2021
 * Time: 2:30 PM
 * for all issues, contact me: huyvv@vnpt-technology.vn
 **/
public class LdapMain {

    public DirContext ctx;

    public void connect() throws NamingException {
        String url = "ldap://10.3.20.34:389";
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, url);
        env.put("java.naming.ldap.version", "2");
        env.put("java.naming.ldap.derefAliases", "never");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, "dview=HNOCS5_request_data");
        env.put(Context.SECURITY_CREDENTIALS, "BIND");
        ctx = new InitialDirContext(env);
    }

    public void test(String[] params) throws NamingException {
        connect();
        SearchControls ctrl = new SearchControls();
        ctrl.setSearchScope(SearchControls.ONELEVEL_SCOPE);

        for (String filter : params) {
            NamingEnumeration<SearchResult> answer = ctx.search("dview=HNOCS5_request_data", filter, ctrl);
            while (answer.hasMore()) {
                SearchResult entry = answer.next();
                NamingEnumeration<String> attrs = entry.getAttributes().getIDs();
                while (attrs.hasMore()) {
                    String key = attrs.next();
                    System.out.println(key + "|" + entry.getAttributes().get(key).get());
                }
            }
            answer.close();
        }
        ctx.close();
    }

    public static void main(String[] args) throws NamingException {
        String[] filters = new String[] {
                "(sid=op=A;ACTION=IMOM#ADJ:BALANCE,MSISDN=84896190016,AMOUNT=47wQbNPTDJp9hMYdvogK2hAUiHsGeiybwaWe36bwtRQ3UTpYV7YuZ8FV5j9nauFCWwcjM6dTzpL5s2N79Rp5unwdMvc8ZKU=0)",
                "(sid=op=A;ACTION=IMOM#ADJ:BALANCE,MSISDN=84896190016,AMOUNT=4000000000,ADJ=INCR,BAL=P,NO_LC=N,RECHARGE=N,UCL=N,Rchg_Type=0,Neg_Credit=N,USERID=Trans_id,CV_Delta=0)",
                "(sid=op=A;ACTION=IMOM#ADJ:BALANCE,MSISDN=84896190016,AMOUNT=40000000000,ADJ=DECR,BAL=P,NO_LC=N,RECHARGE=N,UCL=N,Rchg_Type=0,Neg_Credit=N,USERID=Trans_id,CV_Delta=0)",
                "(sid=op=A;ACTION=IMOM#ADJ:BALANCE,MSISDN=84896190016,AMOUNT=40000000,ADJ=INCR,BAL=P,NO_LC=N,RECHARGE=N,UCL=N,Rchg_Type=0,Neg_Credit=N,USERID=Trans_id,CV_Delta=0)",
                "(sid=op=A;ACTION=IMOM#ADJ:BALANCE,MSISDN=84896190016,AMOUNT=1000,ADJ=SET,BAL=P,NO_LC=N,RECHARGE=Y,UCL=N,Rchg_Type=0,TRANS_ID=truongcccc)",
                "(sid=op=A;ACTION=IMOM#ADJ:EBUCKET,MSISDN=84896190016,BUCKETSOURCEID=100,ADJUSTMENT=INCR,AMOUNT=2000,IDTYPE=S,TRANS_ID=truongvx12345)",
                "(sid=op=A;ACTION=IMOM#ADJ:BALANCE,MSISDN=84896190016,AMOUNT=10000,ADJ=INCR,BAL=P,NO_LC=N,RECHARGE=N,CV_Delta=30,UCL=N,Rchg_Type=0)",
                "(sid=op=A;ACTION=IMOM#SUB:DTPBUNDLE,MSISDN=84896190016,PTP_ID=SOGTB2_M,START_DATE=2021/09/19,TRANS_ID=truongvx,BDL_UPD=Y,PERIOD_SUB=30,TriggerRAR=Y)",
                "(sid=op=A;ACTION=IMOM#RMV:BUNDLE,MSISDN=84896190016,PTP_ID=SOGTB2_M)",
                "(sid=op=A;ACTION=IMOM#QRY:BUNDLE,MSISDN=84896190016,IDTYPE=S,SUMTYPE=0,IDX=1)",
                "(sid=op=A;ACTION=IMOM#UPD:BUNDLE,MSISDN=84896190016,PTP_ID=GPRS,END_DATE=2022/09/01)",
                "(sid=op=A;ACTION=IMOM#UPD:BUNDLE,MSISDN=84896190016,PTP_ID=GPRS,END_DATE=2022/09/09)",
                "(sid=op=A;ACTION=IMOM#UPD:SIMCOSP,MSISDN=84896190016,COSP=CS_PRE_DT2)",
                "(sid=op=A;ACTION=IMOM#UPD:CD,a=84764229263,e=QT2)",
                "(sid=op=A;ACTION=IMOM#UPD:CD,a=84896190016,NC1=Y)",
                "(sid=OP=U;Tbl_Name=SIMRTDB;Tbl_Key=84896190016#Balance_Transfer_Allowed=Y)",
                "(sid=OP=U;Tbl_Name=SIMRTDB;Tbl_Key=84896190016#IMSI_1=54636728282029)",
                "(sid=op=A;ACTION=IMOM#UPD:CD,a=84764229263,d=VALID)",
                "(sid=OP=U;Tbl_Name=SIMRTDB;Tbl_Key=84896190016#LANGUAGE=English)",
                "(sid=OP=U;Tbl_Name=SIMRTDB;Tbl_Key=84896190016#SIM_PIN=1111)",
                "(sid=OP=U;Tbl_Name=SIMRTDB;Tbl_Key=84896190016#Error_Indicator=A)",
                "(sid=OP=U;Tbl_Name=SIMRTDB;Tbl_Key=84896190016#Error_Indicator=0)",
                "(sid=OP=U;Tbl_Name=SIMRTDB;Tbl_Key=84896190016#SIM_State=VALID)",
                "(sid=OP=Q;Tbl_Name=SIMRTDB;Tbl_Key=84896190016;Field_Name=Balance_Transfer_Allowed,Class_of_Service_ID,SIM_State,SIM_PIN)",
        };

        LdapMain ldapMain = new LdapMain();
        ldapMain.test(filters);
    }
}