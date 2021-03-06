package controlador;

import java.text.DecimalFormat;
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
 * Controlador central
 * 
 * @author Esteban Guzmán R.
 */
public class LocalMascotas {

    private static int MAXIMA_CAPACIDAD = 10;
    private static String[] HORAS_ALIMENTACION = {"6am", "12md", "6pm"};
    private Contrato[] contratos;
    private ArrayList<Alimento> inventario;
    
    /**
     * Crea una instancia del controlador con los contratos cargados y el inventario
     */
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

    /**
     * Ingresa con datos aleatorios un cantidad de mascotas al hotel
     * @param n cantidad de mascotas por ingresar
     */
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

    /**
     * Devuelve un valor random de un Enum
     * @param <T> generico
     * @param clazz enums
     * @return Valor random de un Enum
     */
    public static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
        Random random = new Random();
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

    /**
     * Registra un contrato al sistema
     * @param contrato contrato a registrar
     * @return true si hay espacio y se registró el contrato
     */
    public boolean registratContrator(Contrato contrato) {
        int index = cupoDisponible();
        if (index >= 0) {
            contrato.setNumero(index);
            contratos[index] = contrato;
            AdministradorArchivos.guardarContratos(contratos);
            AdministradorArchivos.guardarMascotas(obtenerListaMascota());
            return true;

        } else {
            contrato.setNumero(-1);
            System.out.println("No hay campo para:" + contrato.toString());
        }
        return false;
    }

    /**
     * Obtiene la lista de mascotas en el hotel
     * @return lista de mascotas en el hotel
     */
    public ArrayList<Mascota> obtenerListaMascota() {
        ArrayList<Mascota> lista = new ArrayList<Mascota>();
        for (int i = 0; i < 10; i++) {
            if (contratos[i].getMascota() != null) {
                lista.add(contratos[i].getMascota());
            }
        }
        return lista;
    }

    /**
     * Un setter de la cantidad de kilos de un tipo de alimento
     * @param tipo tipo de alimento
     * @param existenciaKilos nueva cantidad de kilos 
     */
    public void actualizarAlimento(modelo.TipoAlimento tipo, double existenciaKilos) {
        for (int i = 0; i < inventario.size(); i++) {
            if (inventario.get(i).getTipo() == tipo) {
                inventario.get(i).setExistenciaKilos(existenciaKilos);
                AdministradorArchivos.guardarInventario(inventario);
            }
        }
    }

    /**
     * toString de todas las mascotas registradas
     * @return toString de todas las mascotas registradas
     */
    public String mostrarDetalleMascotas() {
        String resultado = "";
        for (int i = 0; i < 10; i++) {
            if (contratos[i].getMascota() != null) {
                resultado += contratos[i].getMascota().toString() + "\n";
            }
        }
        return resultado;
    }

    /**
     * Retira un contrato del registro
     * @param index El id del contrato a retirrar
     */
    public void retirarContrato(int index) {
        contratos[index] = new Contrato();
        AdministradorArchivos.guardarContratos(contratos);
        AdministradorArchivos.guardarMascotas(obtenerListaMascota());
    }

    /**
     * Calcula la cantidad de alimento necesario para hoy
     * @return un string con el detalle de cuanto alimento se necesita de cada tipo
     */
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

    /**
     * Guarda el reporte de una alimencion en la bitacora, revisa si ya ha sido registrada,
     * @param bitacora nuevo registro sobre la alimentación
     * @param idContrato numero del contrato del animal a alimentar
     * @return Un string con el resultado registrado en la bitacora
     */
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
                            return "Ya hay un registro este día y horario";
                        }
                    }
                }
            }
        }
        contratos[idContrato].agregarRegistro(bitacora);
        AdministradorArchivos.guardarContratos(contratos);
        //Rebajando la cantidad de comida en inventario
        if (bitacora.getEstado() == EstadoAlimentacion.ALIMENTADO) {
            if(cantidadAlimento(contratos[idContrato].getMascota().getCodigoAlimento()) 
                    < contratos[idContrato].getMascota().getComidaKilos()){
                bitacora.setEstado(EstadoAlimentacion.NO_HAY_ALIMENTO);
                return contratos[idContrato].getMascota().getCodigoAlimento() + ":"
                    + contratos[idContrato].getMascota().getNombre()
                    + " no fue alimentado, no hay suficiente alimento";
            }
            double nuevaCantidadKilos = -1;
            for (int i = 0; i < inventario.size(); i++) {
                if (inventario.get(i).getTipo()
                        == contratos[idContrato].getMascota().getCodigoAlimento()) {
                    nuevaCantidadKilos = inventario.get(i).getExistenciaKilos()
                            - contratos[idContrato].getMascota().getComidaKilos();
                    DecimalFormat df = new DecimalFormat("#.0");
                    String troncador = df.format(nuevaCantidadKilos);
                    nuevaCantidadKilos = Double.parseDouble(troncador);
                    actualizarAlimento(contratos[idContrato].getMascota()
                            .getCodigoAlimento(), nuevaCantidadKilos);
                    return contratos[idContrato].getMascota().getCodigoAlimento() + ":"
                            + contratos[idContrato].getMascota().getNombre()
                            + " ha sido alimentado";
                }
            }
        } else if (bitacora.getEstado() == EstadoAlimentacion.NO_HAY_ALIMENTO) {
            return contratos[idContrato].getMascota().getCodigoAlimento() + ":"
                    + contratos[idContrato].getMascota().getNombre()
                    + " no fue alimentado, no hay suficiente alimento";
        } else if (bitacora.getEstado() == EstadoAlimentacion.NO_QUISO) {
            return contratos[idContrato].getMascota().getCodigoAlimento() + ":"
                    + contratos[idContrato].getMascota().getNombre()
                    + " no fue alimentado, porque estaba enfermo";
        } else {
            return contratos[idContrato].getMascota().getCodigoAlimento() + ":"
                    + contratos[idContrato].getMascota().getNombre()
                    + " no fue alimentado, porque se perdío su información";
        }
        return null;
    }

    /**
     * Genera un random de registros para todas las mascotas. También pierde 
     * y encuentra contratos.
     * @param fecha Fecha cuando simulan los registros
     * @return Un resumen de cada registro de alimentacion
     */
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

    /**
     * Get de la cantidad de alimento en kilos de un tipo de alimento
     * @param tipo tipo de alimento
     * @return cantidad en kilos del alimento
     */
    public double cantidadAlimento(TipoAlimento tipo) {
        for (int i = 0; i < inventario.size(); i++) {
            if (inventario.get(i).getTipo() == tipo) {
                return inventario.get(i).getExistenciaKilos();
            }
        }
        return -1;
    }

    /**
     * Gerena un archivo txt con las bitacora del día con todas las mascotas
     * @param fecha 
     */
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

    /**
     * Saca los registros de un contrato
     * @param fecha fecha de los registros a sacar
     * @param lista lista con todos los registros
     * @return registros filtrados
     */
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

    /**
     * Actualiza el estado de un contrato
     * @param id id del contrato
     * @param disponible  nuevo estado del contrato
     */
    public void actualizarEstadoContrato(int id, boolean disponible) {
        contratos[id].setDisponible(disponible);
        AdministradorArchivos.guardarContratos(contratos);
    }

    /**
     * En caso de haber un cupo disponible, devuelve el valor 
     * @return index disponible para registrar un contrato
     */
    public int cupoDisponible() {
        for (int i = 0; i < 10; i++) {
            if (contratos[i].getMascota() == null) {
                return i;
            }
        }
        return -1;
    }

}
