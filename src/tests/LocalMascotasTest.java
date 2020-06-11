/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import controlador.LocalMascotas;
import modelo.AdministradorArchivos;
import modelo.TipoAlimento;

/**
 *
 * @author esteb
 */
public class LocalMascotasTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LocalMascotas local = new LocalMascotas();
        System.out.println(local.getInventario().toString()); 
    }
    
}
