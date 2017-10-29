/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkrbl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Common class for checkers
 *
 * @author utente
 */
public abstract class CheckResource {

    private final String providerName;
    
    /**
     * Initialize the provider checker
     * @param providerName The provider name used to check the resource
     */
    public CheckResource(String providerName) {
        this.providerName = providerName;
    }
   
    /**
     * Check the domain against a provider
     * @param domain Domain name to be checked
     * @return The exit code of the provider service
     */
    public abstract String checkDomain(String domain);

    /**
     * Check the domain against a provider
     * @param ip Ip address to be checked
     * @return The exit code of the provider service
     */
    public abstract String checkIp(String ip);

    /**
     * Convert the provuder's exit code into a descriptive string
     * @param resultCode
     * @return The description of the provider's exit code
     */
    public abstract String convertResultCode(String resultCode);

    /**
     * Check a generic resource list (ip address or domain name) against the provider
     * @param resources Resourcet to be checked
     * @param callback A callback that is called for every list item processed
     * @return A list of exits codes
     */
    public List<String> checkResources(List<String> resources, CallbackAction callback) {
        List<String> resultCodes = new ArrayList<>();
        String result = null;
        for (String resource : resources) {
            switch (resourceType(resource)) {
                case domainResource:
                    result = checkDomain(resource);
                    resultCodes.add(result);
                    break;
                case ipResource:
                    result = checkIp(resource);
                    resultCodes.add(result);
                    break;
                default:
                    result=null;
                    break;
            }
            if (callback != null && result!=null) {
                callback.doAction(resource+" - "+result);
            }
        }

        return resultCodes;
    }

    /**
     * Check if a resource is a domain or an ip address
     * @param resource Resource to be checked
     * @return The corrisponding resource type of unknown in case of a unknown resource type
     */
    private CheckType resourceType(String resource) {
        Pattern ipParser = Pattern.compile("^(?:(?:\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])\\.){3}(?:\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])$");
        
        Matcher m = ipParser.matcher(resource);
        if (m.find()) {
            return CheckType.ipResource;
        }

        if (resource.matches("^([a-zA-Z0-9\\.\\-_]+)$")) {
            return CheckType.domainResource;
        }

        return CheckType.unknownResource;
    }

    /**
     * Return the provider name
     * @return String containing the provider name
     */
    public String getProviderName() {
        return providerName;
    }
}
