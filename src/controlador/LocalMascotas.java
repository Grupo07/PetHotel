
package controlador;

import java.util.ArrayList;
import java.util.Date;
import modelo.Alimento;
import modelo.Contrato;

/**
 *
 * @author 
 */
public class LocalMascotas {
 
    private static int MAXIMA_CAPACIDAD = 10;
    private static int HORA_ALIMENTACION = 12;
    private ArrayList<Contrato> contratos;
    private ArrayList<Alimento> inventario;
    
    public LocalMascotas() {
        this.contratos = new ArrayList<>();
        this.inventario = new ArrayList<>();
    }
    
    private void cargarContratos() {
        //
    }
    
    public void simularIngresos(int n) {
        
    }
    
    public boolean registratContrator(Contrato contrato) {
        return true;
    }
    
    public void registrarAlimento(Alimento alimento) {
        
    }
    
    public String mostrarDetalleMascotas() {
        return "";
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
    
    public boolean hayCupo() {
        return true;
    }
    
}
