/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkrbl;

/**
 * The type of resources that can be checked: domain, ip or an unknown resource
 *
 * @author utente
 */
public enum CheckType {
    /**
     * The resource is a domain name
     */
    domainResource,
    /**
     * The resource is an ip address
     */
    ipResource,
    /**
     * The resource is unknown or a syntax error specifying domain or ip
     */
    unknownResource;
}
