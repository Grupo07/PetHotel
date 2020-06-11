
package modelo;

import java.util.ArrayList;
import java.util.Date;

/**
 * Contrato de estancia de una mascota en el hotel.
 * 
 * @author Luis Mariano Ramírez Segura
 */
public class Contrato {
    
    private int numero;
    private Date desde;
    private Date hasta;
    private Mascota Mascota;
    private double costoXDia;
    private ArrayList<BitacoraAlimentacion> registros;

    public Contrato( Date desde, Date hasta, Mascota Mascota, double costoXDia) {
        this.desde = desde;
        this.hasta = hasta;
        this.Mascota = Mascota;
        this.costoXDia = costoXDia;
        this.registros = new ArrayList<>();
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public Date getDesde() {
        return desde;
    }

    public void setDesde(Date desde) {
        this.desde = desde;
    }

    public Date getHasta() {
        return hasta;
    }

    public void setHasta(Date hasta) {
        this.hasta = hasta;
    }

    public Mascota getMascota() {
        return Mascota;
    }

    public void setMascota(Mascota Mascota) {
        this.Mascota = Mascota;
    }

    public double getCostoXDia() {
        return costoXDia;
    }

    public void setCostoXDia(double costoXDia) {
        this.costoXDia = costoXDia;
    }

    public ArrayList<BitacoraAlimentacion> getRegistros() {
        return registros;
    }

    public void setRegistros(ArrayList<BitacoraAlimentacion> registros) {
        this.registros = registros;
    }

    @Override
    public String toString() {
        return "Contrato{" + "numero=" + numero + ", desde=" + desde + ", hasta=" + hasta + ", idMascota=" + Mascota + ", costoXDia=" + costoXDia + ", registros=" + registros + '}';
    }
    
}
