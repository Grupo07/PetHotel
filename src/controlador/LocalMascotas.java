
package controlador;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import modelo.Alimento;
import modelo.Contrato;

/**
 *
 * @author 
 */
public class LocalMascotas {
 
    private static int MAXIMA_CAPACIDAD = 10;
    private static int HORA_ALIMENTACION = 12;
    private Contrato[] contratos;
    private ArrayList<Alimento> inventario;
    
    public LocalMascotas() {
        this.contratos = new Contrato[10];
        this.inventario = new ArrayList<>();
    }
    
    
    public void simularIngresos(int n) {
        
    }
    
    public static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
        Random random = new Random();
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }
    
    public boolean registratContrator(Contrato contrato) {
        int index = cupoDisponible();
        if(index >= 0){
            contrato.setNumero(index);
            contratos[index] = contrato;
        }
        return false;
    }
    
    public void actualizarAlimento(modelo.TipoAlimento tipo, double existenciaKilos) {
        for(int i = 0; i < inventario.size(); i++){
            if(inventario.get(i).getTipo() == tipo){
                inventario.get(i).setExistenciaKilos(existenciaKilos);
            }
        }
    }
    
    public String mostrarDetalleMascotas() {
        String resultado = "";
        for(int i = 0; i < 10; i++){
            if(contratos[i] != null){
                resultado += contratos[i].getMascota().toString() + "\n";
            }
        }
        return resultado;
    }
    
    public ArrayList<String> planearAlimentacion() {
        return new ArrayList<String>();
    }
    
    public String alimentarMascota(Date fecha, int idMascota) {
        return "";
    }
    
    public void simularAlimentacionMascotas(Date fecha) {
        
    }
    
    public void generarBitacoraDeFecha(Date fecha) {
        
    }
    
    public int cupoDisponible() {
        for(int i = 0; i < 10; i++){
            if(contratos[i] == null){
                return i;
            }
        }
        return -1;
    }
    
}
