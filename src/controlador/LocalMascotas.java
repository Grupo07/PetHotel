package controlador;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import modelo.Alimento;
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
    private static int HORA_ALIMENTACION = 12;
    private Contrato[] contratos;
    private ArrayList<Alimento> inventario;

    public LocalMascotas() {
        this.contratos = new Contrato[10];
        this.inventario = new ArrayList<>();
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
        } else {
            contrato.setNumero(-1);
            System.out.println("No hay campo para:" + contrato.toString());
        }
        return false;
    }

    public void actualizarAlimento(modelo.TipoAlimento tipo, double existenciaKilos) {
        for (int i = 0; i < inventario.size(); i++) {
            if (inventario.get(i).getTipo() == tipo) {
                inventario.get(i).setExistenciaKilos(existenciaKilos);
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
        for (int i = 0; i < 10; i++) {
            if (contratos[i] == null) {
                return i;
            }
        }
        return -1;
    }

}
