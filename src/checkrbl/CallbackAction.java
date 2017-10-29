/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkrbl;

/**
 * Callback interface to be able to show working operations
 * @author utente
 */
public interface CallbackAction {
    /**
     * The callback 
     * @param value 
     */
    public void doAction(String value);
}
