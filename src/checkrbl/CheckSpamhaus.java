/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkrbl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author utente
 */
public class CheckSpamhaus extends CheckResource {

    /**
     * Initialize the class with the provider name
     *
     * @param providerName
     */
    public CheckSpamhaus(String providerName) {
        super(providerName);
    }

    private static final String SPAMHAUS_IP_SERVICE = "zen.spamhaus.org";
    private static final String SPAMHAUS_DOMAIN_SERVICE = ".dbl.spamhaus.org";

    /**
     * Check domain against the dbl list of spamhaus spammer's domain
     *
     * @param domain - The domain to be checked
     * @return A result code in spamhaus form or an empty string if not resolved
     */
    @Override
    public String checkDomain(String domain) {
        return checkIpOrDomain(domain, CheckType.domainResource);
    }

    @Override
    public String checkIp(String ip) {
        return checkIpOrDomain(ip, CheckType.ipResource);
    }

    private String checkIpOrDomain(String resource, CheckType checkType) {
        StringBuilder sb = new StringBuilder();

        InetAddress inetAddress = null;
        String result = null;

        switch (checkType) {
            case domainResource:
                // Just append spamhaus domain
                sb.append(resource);
                sb.append(SPAMHAUS_DOMAIN_SERVICE);
                break;
            case ipResource:
                // Reverse ip
                String[] ipTemp = resource.split("\\.");

                for (int i = ipTemp.length - 1; i >= 0; i--) {
                    sb.append(ipTemp[i]);
                    sb.append(".");
                }

                sb.append(SPAMHAUS_IP_SERVICE);
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

    /**
     * Return the description of spamhaus code or an empty string if not found
     *
     * @param resultCode
     * @return Description of the code or an empty string if the code is unknown
     */
    @Override
    public String convertResultCode(String resultCode) {
        if (RETURN_CODES.containsKey(resultCode.trim())) {
            return RETURN_CODES.get(resultCode.trim());
        } else {
            return "";
        }
    }

    private static final Map<String, String> RETURN_CODES;

    static {
        Map<String, String> tempMap = new HashMap<>();

        tempMap.put("127.0.1.2", "spam domain");
        tempMap.put("127.0.1.4", "phish domain");
        tempMap.put("127.0.1.5", "malware domain");
        tempMap.put("127.0.1.6", "botnet C&C domain");
        tempMap.put("127.0.1.102", "abused legit spam");
        tempMap.put("127.0.1.103", "abused spammed redirector domain");
        tempMap.put("127.0.1.104", "abused legit phish");
        tempMap.put("127.0.1.105", "abused legit malware");
        tempMap.put("127.0.1.106", "abused legit botnet C&C");
        tempMap.put("127.0.1.255", "IP queries prohibited!");
        tempMap.put("127.0.0.2", "Spamhaus SBL Data");
        tempMap.put("127.0.0.3", "Spamhaus SBL CSS Data");
        tempMap.put("127.0.0.4", "CBL Data");
        tempMap.put("127.0.0.9", "Spamhaus DROP/EDROP Data (in addition to 127.0.0.2, since 01-Jun-2016)");
        tempMap.put("127.0.0.10", "ISP Maintained");
        tempMap.put("127.0.0.11", "Spamhaus Maintained");
        RETURN_CODES = Collections.unmodifiableMap(tempMap);
    }

    /*
        Spamhaus result code policy
        ---------------------------
        127.0.0.0/24 	Spamhaus IP Blocklists
        127.0.1.0/24 	Spamhaus Domain Blocklists
        127.0.2.0/24 	Spamhaus Whitelists

        Spamhaus Domain Blocklists result codes
        ---------------------------------------
        127.0.1.2 		spam domain
        127.0.1.4 		phish domain
        127.0.1.5 		malware domain
        127.0.1.6	 	botnet C&C domain
        127.0.1.102 	abused legit spam
        127.0.1.103 	abused spammed redirector domain
        127.0.1.104 	abused legit phish
        127.0.1.105 	abused legit malware
        127.0.1.106 	abused legit botnet C&C
        127.0.1.255 	IP queries prohibited!

        Spamhaus IP Zone
        ----------------
        127.0.0.2 	SBL 	Spamhaus SBL Data
        127.0.0.3 	SBL 	Spamhaus SBL CSS Data
        127.0.0.4 	XBL 	CBL Data
        127.0.0.9 	SBL 	Spamhaus DROP/EDROP Data (in addition to 127.0.0.2, since 01-Jun-2016)
        127.0.0.10 	PBL 	ISP Maintained
        127.0.0.11 	PBL 	Spamhaus Maintained
     */
}
