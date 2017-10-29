/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkrbl;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author utente
 */
public class CheckSpameatingmonkey extends CheckResource {

    /**
     * Initialize the class with the provider name
     * @param providerName 
     */
    public CheckSpameatingmonkey(String providerName) {
        super(providerName);
    }

    private static final String CHECKING_URL = ".fresh30.spameatingmonkey.net";

    /**
     * Check a domain
     * @param domain
     * @return The Spameatingmonkey code if the domain was found or an empty string
     */
    @Override
    public String checkDomain(String domain) {
        return (checkIpOrDomain(domain, CheckType.domainResource));
    }

    /**
     * Check an ip address
     * @param ip
     * @return The Spameatingmonkey code if the ip address was found or an empty string
     */
    @Override
    public String checkIp(String ip) {
        return (checkIpOrDomain(ip, CheckType.ipResource));
    }

    /**
     * Convert the Spameatingmonkey code into a descriptive string or null if not known
     * @param resultCode
     * @return The Spameatingmonkey code desciption or an empty string if the code was unknown
     */
    @Override
    public String convertResultCode(String resultCode) {
        if (resultCode.trim().equals("127.0.0.2")) {
            return "There is a registered entry";
        } else {
            return "";
        }
    }

    /**
     * Do the job
     * @param resource The resource to check
     * @param checkType The resource type
     * @return The string with the code 
     */
    private String checkIpOrDomain(String resource, CheckType checkType) {

        StringBuilder sb = new StringBuilder();

        InetAddress inetAddress ;
        String result ;

        switch (checkType) {
            case domainResource:
                // Just append spamhaus domain
                sb.append(resource);
                sb.append(CHECKING_URL);
                break;
            case ipResource:
                // Reverse ip
                String[] ipTemp = resource.split("\\.");

                for (int i = ipTemp.length - 1; i >= 0; i--) {
                    sb.append(ipTemp[i]);
                    sb.append(".");
                }

                sb.append(CHECKING_URL);
                break;
            default:               
                return "";
        }

        try {
            inetAddress = InetAddress.getByName(sb.toString());
            result = inetAddress.getHostAddress();
        } catch (UnknownHostException ex) {
            result = "";
        }

        return result;
    }
}
