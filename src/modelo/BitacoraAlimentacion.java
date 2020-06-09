
package modelo;

import java.util.Date;

/**
 * Bitácora de alimentación de una mascota
 * 
 * @author Luis Mariano Ramírez Segura
 */
public class BitacoraAlimentacion {
    
    private Date fecha;
    private EstadoAlimentacion estado;

    public BitacoraAlimentacion(Date fecha, EstadoAlimentacion estado) {
        this.fecha = fecha;
        this.estado = estado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public EstadoAlimentacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoAlimentacion estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "BitacoraAlimentacion{" + "fecha=" + fecha + ", estado=" + estado + '}';
    }
    
}
