/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkrbl;

/**
 *
 * @author utente
 */
public class Provider {

    /**
     * Initialize the class 
     * @param providerName The provider name
     * @param resultCode The result code
     * @param resultDesctiption  The result code description
     */
    public Provider(String providerName, String resultCode, String resultDesctiption) {
        this.providerName = providerName;
        this.resultCode = resultCode;
        this.resultDesctiption = resultDesctiption;
    }
    String providerName;
    String resultCode;
    String resultDesctiption;
}
