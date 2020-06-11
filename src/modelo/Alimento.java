
package modelo;

/**
 * Alimento de una mascota.
 * 
 * @author Luis Mariano Ramírez Segura
 */
public class Alimento {
    
    private TipoAlimento tipo;
    private double existenciaKilos;
    private TipoAlimento tipo;

    public Alimento(TipoAlimento tipo, double existenciaKilos) {
        this.tipo = tipo;
        this.existenciaKilos = existenciaKilos;
    }

    public TipoAlimento getTipo() {
        return tipo;
    }

    public void setTipo(TipoAlimento tipo) {
        this.tipo = tipo;
    }

    public double getExistenciaKilos() {
        return existenciaKilos;
    }

    public void setExistenciaKilos(double existenciaKilos) {
        this.existenciaKilos = existenciaKilos;
    }

    public TipoAlimento getTipo() {
        return tipo;
    }

    public void setTipo(TipoAlimento tipo) {
        this.tipo = tipo;
    }
    
    

    @Override
    public String toString() {
        return "Alimento{" + "tipo=" + tipo + ", existenciaKilos=" + existenciaKilos + '}';
    }
    
}
