package com.vnpt_technology.loadtest;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by huyvv
 * Date: 18/08/2021
 * Time: 5:09 PM
 * for all issues, contact me: huyvv@vnpt-technology.vn
 **/
public class LdapPerformance {
    private final int totalThread;
    private final String url;
    private final String filter;
    private final long totalTimeRun;

    public LdapPerformance(int totalThread, String url, String filter, long totalTimeRun) {
        this.totalThread = totalThread;
        this.url = url;
        this.filter = filter;
        this.totalTimeRun = totalTimeRun;
    }

    public void loadTest() {
        List<Future<?>> resultInFutures = new ArrayList<>();
        ExecutorService service = Executors.newFixedThreadPool(totalThread);
        List<LdapRequestThread> ldapRequestThreads = new LinkedList<>();
        for (int i = 0; i < totalThread; i++) {
            LdapRequestThread ldapRequestThread = new LdapRequestThread(filter, totalTimeRun, url);
            ldapRequestThreads.add(ldapRequestThread);
            resultInFutures.add(service.submit(ldapRequestThread));
        }

        // wait cho den khi cac task finish
        for (Future<?> future : resultInFutures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        //TODO: bắt đầu thống kê, in ra file chẳng hạn
        int totalResult = 0;
        for (LdapRequestThread ldapRequestThread : ldapRequestThreads) {
            totalResult+= ldapRequestThread.getResultCSV().size();
        }
        System.out.println("totalResult: " + totalResult);

        service.shutdown();
    }

    public static void main(String[] args) {
        /*int totalThead = 16;
        String url = "ldap://10.3.20.34:389";
        String filter = "(sid=OP=Q;Tbl_Name=SIMRTDB;Tbl_Key=84896190016;Field_Name=Balance_Transfer_Allowed,Class_of_Service_ID,SIM_State,SIM_PIN)";
        long totalTimeRun = 60 * 1000L;*/

        int totalThead;
        String url;
        String filter;
        long totalTimeRun;

        totalThead = Integer.parseInt(args[0]);
        url = args[1];
        filter = args[2];
        totalTimeRun = Long.parseLong(args[3]);

        LdapPerformance ldapPerformance = new LdapPerformance(totalThead, url, filter, totalTimeRun);
        ldapPerformance.loadTest();
    }
}
