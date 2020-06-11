
package modelo;

/**
 * Mascota de tipo pez.
 * 
 * @author Luis Mariano Ramírez Segura
 */
public class Pez extends Mascota {

    public Pez(int id, String nombre, boolean estaEnferma, String raza, Dueño elDueño) {
        super(id, nombre, estaEnferma, raza, elDueño);
        definirAlimentacion();
    }
    
    @Override
    protected void definirAlimentacion() {
        super.tipoAlimento = TipoAlimento.PEZ;
        super.vecesAlimentacion = 1;
        super.comidaKilos = 0.3;
    }
    
}
