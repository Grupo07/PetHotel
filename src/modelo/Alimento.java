
package modelo;

/**
 * Alimento de una mascota.
 * 
 * @author Luis Mariano Ramírez Segura
 */
public class Alimento {
    
    private String codigo;
    private String nombre;
    private String marca;
    private double existenciaKilos;
    private TipoAlimento tipo;

    public Alimento(String codigo, String nombre, String marca, double existenciaKilos) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.marca = marca;
        this.existenciaKilos = existenciaKilos;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
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
        return "Alimento{" + "codigo=" + codigo + ", nombre=" + nombre + ", marca=" + marca + ", existenciaKilos=" + existenciaKilos + '}';
    }
    
}
