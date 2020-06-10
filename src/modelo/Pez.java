
package modelo;

/**
 * Mascota de tipo pez.
 * 
 * @author Luis Mariano Ramírez Segura
 */
public class Pez extends Mascota {

    public Pez(int id, String nombre, boolean estaEnferma, String raza, Dueño elDueño) {
        super(id, nombre, estaEnferma, raza, elDueño);
    }
    
    @Override
    protected void definirAlimentacion() {
        super.codigoAlimento = "pez";
        super.comidaKilos = 0.5;
    }
    
}