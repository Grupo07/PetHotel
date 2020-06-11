
package modelo;

import java.io.Serializable;

/**
 * Dueño de una mascota.
 * 
 * @author Luis Mariano Ramírez Segura
 */
public class Dueño implements Serializable{
    
    private String nombre;
    private String celular;

    public Dueño(String nombre, String celular) {
        this.nombre = nombre;
        this.celular = celular;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    @Override
    public String toString() {
        return "Due\u00f1o{" + "nombre=" + nombre + ", celular=" + celular + '}';
    }
    
}
