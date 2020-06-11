
package modelo;

/**
 * Mascota de tipo pajaro.
 * 
 * @author Luis Mariano Ramírez Segura
 */
public class Pajaro extends Mascota {

    public Pajaro(int id, String nombre, boolean estaEnferma, String raza, Dueño elDueño) {
        super(id, nombre, estaEnferma, raza, elDueño);
    }
    
    @Override
    protected void definirAlimentacion() {
        super.tipoAlimento = TipoAlimento.PAJARO;
        super.vecesAlimentacion = 1;
        super.comidaKilos = 0.3;
    }
    
}
