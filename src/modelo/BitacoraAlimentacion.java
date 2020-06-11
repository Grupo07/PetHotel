
package modelo;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Bitácora de alimentación de una mascota
 * 
 * @author Luis Mariano Ramírez Segura
 */
public class BitacoraAlimentacion {
    
    private LocalDateTime fecha;
    private EstadoAlimentacion estado;
    private String horario;

    public BitacoraAlimentacion(LocalDateTime fecha, EstadoAlimentacion estado, String horario) {
        this.fecha = fecha;
        this.estado = estado;
        this.horario = horario;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public EstadoAlimentacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoAlimentacion estado) {
        this.estado = estado;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    @Override
    public String toString() {
        return "BitacoraAlimentacion{" + "fecha=" + fecha + ", estado=" + estado + ", horario=" + horario + '}';
    }
    
}
