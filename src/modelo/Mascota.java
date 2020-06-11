
package modelo;

/**
 * Mascota que se puede ser ingresada en el hotel de mascotas.
 * 
 * @author Luis Mariano Ramírez Segura
 */
public abstract class Mascota {
    
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
    
    public abstract void definirAlimentacion();

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

    @Override
    public String toString() {
        return "Mascota{" + "id=" + id + ", nombre=" + nombre + ", estaEnferma=" + estaEnferma + ", raza=" + raza + ", elDue\u00f1o=" + elDueño + ", tipoAlimento=" + tipoAlimento + ", vecesAlimentacion=" + vecesAlimentacion + ", comidaKilos=" + comidaKilos + '}';
    }
     
}
