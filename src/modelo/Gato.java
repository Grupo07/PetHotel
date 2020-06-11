
package modelo;

/**
 * Mascota de tipo gato.
 * 
 * @author Luis Mariano Ramírez Segura
 */
public class Gato extends Mascota {

    public Gato(int id, String nombre, boolean estaEnferma, String raza, Dueño elDueño) {
        super(id, nombre, estaEnferma, raza, elDueño);
    }

    
    @Override
    protected void definirAlimentacion() {
        super.tipoAlimento = TipoAlimento.GATO;
        super.vecesAlimentacion = 2;
        super.comidaKilos = 1.0;
    }
    
}
