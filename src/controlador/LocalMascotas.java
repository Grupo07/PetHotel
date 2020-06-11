package controlador;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import modelo.AdministradorArchivos;
import modelo.Alimento;
import modelo.BitacoraAlimentacion;
import modelo.Contrato;
import modelo.Dueño;
import modelo.Gato;
import modelo.Mascota;
import modelo.Pajaro;
import modelo.Perro;
import modelo.Pez;
import modelo.TipoAlimento;

/**
 *
 * @author Esteban Guzmán R.
 */
public class LocalMascotas {

    private static int MAXIMA_CAPACIDAD = 10;
    private static String[] HORAS_ALIMENTACION = {"6am", "12md", "6pm"};
    private Contrato[] contratos;
    private ArrayList<Alimento> inventario;

    public LocalMascotas() {
        this.contratos = new Contrato[10];
        this.inventario = new ArrayList<>();
        this.inventario = AdministradorArchivos.cargarInventario();
        this.contratos = AdministradorArchivos.cargarContratos();
    }

    public Contrato[] getContratos() {
        return contratos;
    }

    public ArrayList<Alimento> getInventario() {
        return inventario;
    }
    
    

    public void simularIngresos(int n) {
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            //Generando un dueño
            String[] nombres = {"Eduardo", "Manolo", "Pepe", "Maria"};
            String[] razas = {"Callejero", "Mestizo", "Ni idea", "Original"};
            String nombreDueño = nombres[random.nextInt(3)];
            String celular = random.nextInt(9999999) + "";
            Dueño dueño = new Dueño(nombreDueño, celular);
            //Generando datos de una mascota
            int id = random.nextInt(Integer.MAX_VALUE);
            String nombre = nombres[random.nextInt(3)];
            Boolean estaEnferma = random.nextBoolean();
            String raza = razas[random.nextInt(3)];
            Mascota mascota = null;
            TipoAlimento tipo = randomEnum(TipoAlimento.class);
            if (tipo == TipoAlimento.GATO) {
                mascota = new Gato(id, nombre, estaEnferma, raza, dueño);
            } else if (tipo == TipoAlimento.PERRO) {
                mascota = new Perro(id, nombre, estaEnferma, raza, dueño);
            } else if (tipo == TipoAlimento.PEZ) {
                mascota = new Pez(id, nombre, estaEnferma, raza, dueño);
            } else if (tipo == TipoAlimento.PAJARO) {
                mascota = new Pajaro(id, nombre, estaEnferma, raza, dueño);
            }
            //Generando un contrato
            LocalDateTime today = LocalDateTime.now();
            LocalDateTime desde = today.minusDays(random.nextInt(10) + 0L);
            LocalDateTime hasta = today.plusDays(random.nextInt(10) + 0L);
            double costoXDia = random.nextInt(20000) + random.nextDouble();
            //registrar contrato
            registratContrator(new Contrato(desde, hasta, mascota, costoXDia));
        }
    }

    public static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
        Random random = new Random();
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

    public boolean registratContrator(Contrato contrato) {
        int index = cupoDisponible();
        if (index >= 0) {
            contrato.setNumero(index);
            contratos[index] = contrato;
            AdministradorArchivos.guardarContratos(contratos);
            AdministradorArchivos.guardarMascotas(obtenerListaMascota());
            
        } else {
            contrato.setNumero(-1);
            System.out.println("No hay campo para:" + contrato.toString());
        }
        return false;
    }
    public ArrayList<Mascota> obtenerListaMascota(){
        ArrayList<Mascota> lista = new ArrayList<Mascota>();
        for(int i = 0; i < 10; i++){
            if(contratos[i] != null){
                lista.add(contratos[i].getMascota());
            }
        }
        return lista;
    }
    public void actualizarAlimento(modelo.TipoAlimento tipo, double existenciaKilos) {
        for (int i = 0; i < inventario.size(); i++) {
            if (inventario.get(i).getTipo() == tipo) {
                inventario.get(i).setExistenciaKilos(existenciaKilos);
                AdministradorArchivos.guardarInventario(inventario);
            }
        }
    }

    public String mostrarDetalleMascotas() {
        String resultado = "";
        for (int i = 0; i < 10; i++) {
            if (contratos[i] != null) {
                resultado += contratos[i].getMascota().toString() + "\n";
            }
        }
        return resultado;
    }
    public void retirarContrato(int index){
        contratos[index] = null;
        AdministradorArchivos.guardarContratos(contratos);
        AdministradorArchivos.guardarMascotas(obtenerListaMascota());
    }

    public String planearAlimentacion() {
        String resultado = "";
        ArrayList<Mascota> mascotas = obtenerListaMascota();
        double alimentoPerro = 0;
        double alimentoGato = 0;
        double alimentoPez = 0;
        double alimentoPajaro = 0;
        for(int i = 0; i < mascotas.size(); i++){
            if(mascotas.get(i).getCodigoAlimento() == TipoAlimento.GATO){
                alimentoGato += mascotas.get(i).getComidaKilos();
            } else if (mascotas.get(i).getCodigoAlimento() == TipoAlimento.PERRO){
                alimentoPerro += mascotas.get(i).getComidaKilos();
            } else if (mascotas.get(i).getCodigoAlimento() == TipoAlimento.PAJARO){
                alimentoPajaro += mascotas.get(i).getComidaKilos();
            } else if (mascotas.get(i).getCodigoAlimento() == TipoAlimento.PEZ) {
                alimentoPez += mascotas.get(i).getComidaKilos();
            } 
        }
        resultado = "Gato : " + alimentoGato + "\nPerro : " + alimentoPerro +
                "\nPez : " + alimentoPez + "\nPajaro : " + alimentoPajaro; 
        return resultado;
    }

    public String reportarAlimentacion(BitacoraAlimentacion bitacora, int idMascota) {
        int indexContrato = -1;
        for(int i = 0; i < 10; i++){
            if(contratos[i].getMascota().getId() == idMascota){
                indexContrato = i;
            }
        }
        if(indexContrato == -1){
            return "No existe esa mascota";
        } else {
            for(int j = 0; j < contratos[indexContrato].getRegistros().size(); j++) {
                if(contratos[indexContrato].getRegistros().get(j).getFecha()
                        == bitacora.getFecha()){
                    if(contratos[indexContrato].getRegistros().get(j).getHorario()
                            == bitacora.getHorario()){
                        return "Mascota ya fue alimentada este día y horario";
                    }
                }
            }
            contratos[indexContrato].agregarRegistro(bitacora);
            return contratos[indexContrato].getMascota().getNombre() 
                    + "ha sido alimentado";
        }
    }
    public void simularAlimentacionMascotas(Date fecha) {

    }

    public void generarBitacoraDeFecha(Date fecha) {

    }
    
    public void actualizarEstadoContrato(int id, boolean disponible){
        contratos[id].setDisponible(disponible);
    }

    public int cupoDisponible() {
        for (int i = 0; i < 10; i++) {
            if (contratos[i] == null) {
                return i;
            }
        }
        return -1;
    }

}
