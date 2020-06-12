package controlador;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import modelo.AdministradorArchivos;
import modelo.Alimento;
import modelo.BitacoraAlimentacion;
import modelo.Contrato;
import modelo.Dueño;
import modelo.EstadoAlimentacion;
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

    public void setContratos(Contrato[] contratos) {
        this.contratos = contratos;
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

    public ArrayList<Mascota> obtenerListaMascota() {
        ArrayList<Mascota> lista = new ArrayList<Mascota>();
        for (int i = 0; i < 10; i++) {
            if (contratos[i].getMascota() != null) {
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
            if (contratos[i].getMascota() != null) {
                resultado += contratos[i].getMascota().toString() + "\n";
            }
        }
        return resultado;
    }

    public void retirarContrato(int index) {
        contratos[index] = new Contrato();
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
        for (int i = 0; i < mascotas.size(); i++) {
            if (mascotas.get(i).getCodigoAlimento() == TipoAlimento.GATO) {
                alimentoGato += mascotas.get(i).getComidaKilos();
            } else if (mascotas.get(i).getCodigoAlimento() == TipoAlimento.PERRO) {
                alimentoPerro += mascotas.get(i).getComidaKilos();
            } else if (mascotas.get(i).getCodigoAlimento() == TipoAlimento.PAJARO) {
                alimentoPajaro += mascotas.get(i).getComidaKilos();
            } else if (mascotas.get(i).getCodigoAlimento() == TipoAlimento.PEZ) {
                alimentoPez += mascotas.get(i).getComidaKilos();
            }
        }
        resultado = "Gato : " + alimentoGato + "\nPerro : " + alimentoPerro
                + "\nPez : " + alimentoPez + "\nPajaro : " + alimentoPajaro;
        return resultado;
    }

    public String reportarAlimentacion(BitacoraAlimentacion bitacora, int idContrato) {
        
        //Buscar si ya se alimento
        for (int j = 0; j < contratos[idContrato].getRegistros().size(); j++) {
            if (contratos[idContrato].getRegistros().get(j).getFecha().getYear()
                    == bitacora.getFecha().getYear()) {
                if (contratos[idContrato].getRegistros().get(j).getFecha().getMonth()
                        == bitacora.getFecha().getMonth()) {
                    if (contratos[idContrato].getRegistros().get(j).getFecha().getDayOfMonth()
                            == bitacora.getFecha().getDayOfMonth()) {
                        if (contratos[idContrato].getRegistros().get(j).getHorario()
                                == bitacora.getHorario()) {
                            return "Mascota ya fue alimentada este día y horario";
                        }
                    }
                }
            }
        }
        contratos[idContrato].agregarRegistro(bitacora);
        AdministradorArchivos.guardarContratos(contratos);
        //Rebajando la cantidad de comida en inventario
        if (bitacora.getEstado() == EstadoAlimentacion.ALIMENTADO) {
            double nuevaCantidadKilos = -1;
            for (int i = 0; i < inventario.size(); i++) {
                if (inventario.get(i).getTipo()
                        == contratos[idContrato].getMascota().getCodigoAlimento()) {
                    nuevaCantidadKilos = inventario.get(i).getExistenciaKilos()
                            - contratos[idContrato].getMascota().getComidaKilos();
                    actualizarAlimento(contratos[idContrato].getMascota()
                            .getCodigoAlimento(), nuevaCantidadKilos);
                    return contratos[idContrato].getMascota().getCodigoAlimento() + ":"
                            + contratos[idContrato].getMascota().getNombre()
                            + " ha sido alimentado";
                }
            }
        } else if (bitacora.getEstado() == EstadoAlimentacion.NO_HAY_ALIMENTO){
            return contratos[idContrato].getMascota().getCodigoAlimento() + ":"
                            + contratos[idContrato].getMascota().getNombre()
                            + " no fue alimentado, no hay alimento";
        } else if (bitacora.getEstado() == EstadoAlimentacion.NO_QUISO){
            return  contratos[idContrato].getMascota().getCodigoAlimento() + ":"
                            + contratos[idContrato].getMascota().getNombre()
                            + " no fue alimentado, porque estaba enfermo";
        } else {
            return contratos[idContrato].getMascota().getCodigoAlimento() + ":"
                            + contratos[idContrato].getMascota().getNombre()
                            + " no fue alimentado, porque se perdío su información";
        }
        return null;
    }

    public String simularAlimentacionMascotas(LocalDateTime fecha) {
        String resultado = "";
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            if (contratos[i].getMascota() != null) {
                contratos[i].setDisponible(random.nextBoolean());
                EstadoAlimentacion estado = EstadoAlimentacion.ALIMENTADO;
                if (contratos[i].getMascota().isEstaEnferma()) {
                    estado = EstadoAlimentacion.NO_QUISO;
                } else if (contratos[i].isDisponible() == false) {
                    estado = EstadoAlimentacion.SIN_INFORMACION;
                } else if (contratos[i].getMascota().getComidaKilos()
                        > cantidadAlimento(contratos[i].getMascota().getCodigoAlimento())) {
                    estado = EstadoAlimentacion.NO_HAY_ALIMENTO;
                }
                for (int j = 0; j < contratos[i].getMascota().getVecesAlimentacion(); j++) {
                    BitacoraAlimentacion registro = new BitacoraAlimentacion(fecha, estado, HORAS_ALIMENTACION[j]);
                    resultado += reportarAlimentacion(registro, i) + "\n";
                }

            }
        }
        return resultado;
    }

    public double cantidadAlimento(TipoAlimento tipo) {
        for (int i = 0; i < inventario.size(); i++) {
            if (inventario.get(i).getTipo() == tipo) {
                return inventario.get(i).getExistenciaKilos();
            }
        }
        return -1;
    }

    public void generarBitacoraDeFecha(LocalDateTime fecha) {
        String resultado = "";
        for (int i = 0; i < 10; i++) {
            if (contratos[i].getMascota() != null) {
                resultado += contratos[i].getMascota().getCodigoAlimento() + "/"
                        + contratos[i].getMascota().getId() + "/"
                        + contratos[i].getMascota().getNombre() + ": "
                        + obtenerRegistros(fecha, contratos[i].getRegistros())
                        + "\n";
            }
        }
        AdministradorArchivos.guardarReporte(resultado,
                "Reporte" + fecha.getDayOfMonth() + "_" + fecha.getMonth()
                        + "_" + fecha.getYear() + ".txt");
    }

    public String obtenerRegistros(LocalDateTime fecha,
            ArrayList<BitacoraAlimentacion> lista) {

        String resultado = "";
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getFecha().getYear() == fecha.getYear()) {
                if (lista.get(i).getFecha().getMonth() == fecha.getMonth()) {
                    if (lista.get(i).getFecha().getDayOfMonth() == fecha.getDayOfMonth()) {
                        resultado += lista.get(i).toString();
                    }
                }
            }
        }
        return resultado;
    }

    public void actualizarEstadoContrato(int id, boolean disponible) {
        contratos[id].setDisponible(disponible);
        AdministradorArchivos.guardarContratos(contratos);
    }

    public int cupoDisponible() {
        for (int i = 0; i < 10; i++) {
            if (contratos[i].getMascota() == null) {
                return i;
            }
        }
        return -1;
    }

    public void llenarNulls() {
        for (int i = 0; i < 10; i++) {
            contratos[i] = new Contrato();
        }
        AdministradorArchivos.guardarContratos(contratos);
        AdministradorArchivos.guardarMascotas(obtenerListaMascota());
    }

}
