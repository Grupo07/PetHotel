
package modelo;

import java.io.Serializable;

/**
 * Mascota que se puede ser ingresada en el hotel de mascotas.
 * 
 * @author Luis Mariano Ramírez Segura
 */
public abstract class Mascota implements Serializable{
    
    private int id;
    private String nombre;
    private boolean estaEnferma;
    private String raza;
    private Dueño elDueño;
    protected TipoAlimento tipoAlimento;
    protected int vecesAlimentacion;
    protected double comidaKilos;

    public Mascota(int id, String nombre, boolean estaEnferma, String raza, Dueño elDueño) {
        this.id = id;
        this.nombre = nombre;
        this.estaEnferma = estaEnferma;
        this.raza = raza;
        this.elDueño = elDueño;
        this.tipoAlimento = TipoAlimento.PERRO;
        this.vecesAlimentacion = 0;
        this.comidaKilos= 0;
    }
    
    protected abstract void definirAlimentacion();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isEstaEnferma() {
        return estaEnferma;
    }

    public void setEstaEnferma(boolean estaEnferma) {
        this.estaEnferma = estaEnferma;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public Dueño getElDueño() {
        return elDueño;
    }

    public void setElDueño(Dueño elDueño) {
        this.elDueño = elDueño;
    }

    public TipoAlimento getCodigoAlimento() {
        return tipoAlimento;
    }

    public int getVecesAlimentacion() {
        return vecesAlimentacion;
    }
    
    public double getComidaKilos() {
        return comidaKilos;
    }

    //    private String nombre;
    //    protected TipoAlimento tipoAlimento;
    //    private int id;
    //    private String raza;
    
//    private boolean estaEnferma;
//    private Dueño elDueño;
    
//    protected int vecesAlimentacion;
//    protected double comidaKilos;
    
    @Override
    public String toString() {
        String toString = nombre + ", el " + tipoAlimento.toString().toLowerCase() + ", id: " + String.valueOf(id) + ", raza: " + raza + ". " ;
        
        toString += "Enferma: ";
        String enferma = (estaEnferma) ? "Sí" : "No";
        toString += enferma + ". ";
        
        toString += "Come " + String.valueOf(comidaKilos) + " kg, " + String.valueOf(vecesAlimentacion) + " ";
        String vezOVeces = (vecesAlimentacion == 1) ? "vez" : "veces";
        toString += vezOVeces + " al día. ";
        
        toString +=  "Dueño: " + elDueño.getNombre() + " .";        
        return toString;
    }
     
}
