
package modelo;

/**
 * Mascota de tipo perro.
 * 
 * @author Luis Mariano Ramírez Segura
 */
public class Perro extends Mascota {

    public Perro(int id, String nombre, boolean estaEnferma, String raza, Dueño elDueño) {
        super(id, nombre, estaEnferma, raza, elDueño);
    }
    
    @Override
    protected void definirAlimentacion() {
        super.tipoAlimento = TipoAlimento.PERRO;
        super.vecesAlimentacion = 3;
        super.comidaKilos = 1.5;
    }
    
}
