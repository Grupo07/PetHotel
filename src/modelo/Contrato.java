
package modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

/**
 * Contrato de estancia de una mascota en el hotel.
 * 
 * @author Luis Mariano Ramírez Segura
 */
public class Contrato implements Serializable{
    
    private int numero;
    private boolean disponible;
    private LocalDateTime desde;
    private LocalDateTime hasta;
    private Mascota Mascota;
    private double costoXDia;
    private ArrayList<BitacoraAlimentacion> registros;

    public Contrato( LocalDateTime desde, LocalDateTime hasta, Mascota Mascota, double costoXDia) {
        this.desde = desde;
        this.hasta = hasta;
        this.Mascota = Mascota;
        this.costoXDia = costoXDia;
        this.registros = new ArrayList<>();
        this.disponible = true;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean Disponible) {
        this.disponible = Disponible;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public LocalDateTime getDesde() {
        return desde;
    }

    public void setDesde(LocalDateTime desde) {
        this.desde = desde;
    }

    public LocalDateTime getHasta() {
        return hasta;
    }

    public void setHasta(LocalDateTime hasta) {
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
    
    public void agregarRegistro(BitacoraAlimentacion registro){
        registros.add(registro);
    }

    @Override
    public String toString() {
        return "Contrato{" + "numero=" + numero + ", desde=" + desde + ", hasta=" + hasta + ", idMascota=" + Mascota + ", costoXDia=" + costoXDia + ", registros=" + registros + '}';
    }
    
}
