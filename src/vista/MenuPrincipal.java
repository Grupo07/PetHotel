
package vista;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import controlador.LocalMascotas;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
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
 * Menu principal para acceder y manipular los datos del hotel de mascotas.
 * 
 * @author Luis Mariano Ramírez Segura
 */
public class MenuPrincipal extends javax.swing.JFrame {

    private LocalMascotas hotel;
    
    /**
     * Creates new form MenuPrincipal
     */
    public MenuPrincipal() {
        initComponents();
        setLocationRelativeTo(null);
        
        hotel = new LocalMascotas();
        
        this.formContratoDesde.setDate(new Date());
        this.formContratoHasta.setDate(formContratoDesde.getDate());
        
        this.fechaReportarAlimentacion.setDate(new Date());
        this.simularAlimentacionFecha.setDate(new Date());
        this.generarBitacoraFecha.setDate(new Date());
        
        configurarTablaContratos();
        configurarTablaInventario();
        
        actualizarTablaContratos();
        actualizarTablaInventario();
        
    }
    
    /**
     * Configura la columna para eliminar contratos.
     */
    private void configurarTablaContratos() {
        TableColumnModel modeloColumna = tablaContratos.getColumnModel();
        modeloColumna.getColumn(0).setPreferredWidth(20);
        modeloColumna.getColumn(2).setPreferredWidth(130);
        modeloColumna.getColumn(6).setPreferredWidth(50);
        
        tablaContratos.getColumn("Eliminar").setCellRenderer(new RenderizadorDeBotones());
        tablaContratos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                int fila = tablaContratos.rowAtPoint(evt.getPoint());  
                int columna = tablaContratos.columnAtPoint(evt.getPoint());
                
                System.out.println(String.valueOf(fila) + "," + String.valueOf(columna));
                
                // evitar bug de llamada inesperada con index -1
                if (fila == -1) return;
                
                boolean eliminarFila = (columna == 6);
                if (eliminarFila) {
                    int contratoId = (int) tablaContratos.getValueAt(fila, 0);
                    int eliminacionConfirmada = JOptionPane.showConfirmDialog(null, "Seguro que quieres eliminar el contrato con id " + String.valueOf(contratoId) + "?");
                    if (eliminacionConfirmada == 0) {
                        
                        // Eliminar contrato
                        hotel.retirarContrato(contratoId);
                        actualizarTablaContratos();
                       
                    }
                }
            }
        });
        
        tablaContratos.getModel().addTableModelListener(new TableModelListener() {
            
            public void tableChanged(TableModelEvent e) {
                int fila = e.getFirstRow();
                int columna = e.getColumn();
                
                // los valores son agregados con valor de columna -1
                if (columna == -1) return;
                
                String nuevoEstado = (String) tablaContratos.getValueAt(fila, columna);
                if (nuevoEstado.equals("Disponible") || nuevoEstado.equals("Extraviado")) {
                    int contratoId = (int) tablaContratos.getValueAt(fila, 0);
                    boolean disponible = (nuevoEstado.equals("Disponible"));
                    
                    // Actualizar disponibilidad de contrato
                    hotel.actualizarEstadoContrato(contratoId, disponible);
                    actualizarTablaContratos();
                  
                } else {
                    JOptionPane.showMessageDialog(null, "El estado del contrato puede ser solo Disponible o Extraviado");
                    tablaContratos.setValueAt("Disponible", fila, columna);
                }
            }
        });
    }

    /**
     * Configura la tabla inventario para actualizar la cantidad cuando se cambie en la tabla
     */
    private void configurarTablaInventario() {
        
        tablaInventario.getModel().addTableModelListener(new TableModelListener() {
            
            public void tableChanged(TableModelEvent e) {
                int fila = e.getFirstRow();
                int columna = e.getColumn();
                
                // los valores son agregados con valor de columna -1
                if (columna == -1) return;
                
                TipoAlimento tipo = TipoAlimento.valueOf((String) tablaInventario.getValueAt(fila, 0));
                
                String cantidadString = (String) tablaInventario.getValueAt(fila, 1);
                
                if (esUnNumeroDouble(cantidadString)) {
                    double cantidad = Double.parseDouble(cantidadString);
                    hotel.actualizarAlimento(tipo, cantidad);
                    actualizarTablaInventario();
                    
                } else {
                    JOptionPane.showMessageDialog(null, "La cantidad en kg debe ser un numero");
                    tablaInventario.setValueAt(1.0d, fila, 1);
                }
            }
        });
        tablaInventario.setRowHeight(54);
    }
    
    /**
     * Verifica que la informacion suministrada en el formulario sea valida.
     */
    private boolean formularioContratoEsValido() {
        
        String dueñoNombre = formDueñoNombre.getText().replace(" ", "");
        String mascotaNombre = formMascotaNombre.getText().replace(" ", "");
        String raza = formMascotaRaza.getText().replace(" ", "");
        if (dueñoNombre.isEmpty() || mascotaNombre.isEmpty() || raza.isEmpty()){
            JOptionPane.showMessageDialog(null, "Por favor llene los campos de nombre y raza");
            return false;
        }
        
        String telefono = this.formDueñoTelefono.getText().replace(" ", "");
        if(!esUnNumero(telefono)) {
            JOptionPane.showMessageDialog(null, "El telefono debe ser un valor numérico. Ej: 86542283");
            return false;
        }
        
        Date desde = this.formContratoDesde.getDate();
        Date hasta = this.formContratoHasta.getDate();
        if(desde == null || hasta == null){
            JOptionPane.showMessageDialog(null, "Por favor rellene las fechas de ingreso y salida");
            return false;
        }
        if(hasta.before(desde) || desde.equals(hasta)) {
            JOptionPane.showMessageDialog(null, "La fecha de salida debe ser posterior a la fecha de ingreso");
            return false;
        }
        return true;
        
    }
    
    /**
     * Verifica que un string contega un numero
     * 
     * @param numeroString string del numero a verificar
     * @return si numeroString es un numero 
     */
    private boolean esUnNumero(String numeroString) {
        if (numeroString == null)
            return false;
        
        try {
            int numero = Integer.parseInt(numeroString);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    
    /**
     * Verifica que un string contega un numero double
     * 
     * @param numeroString string del numero a verificar
     * @return si numeroString es un numero double
     */
    private boolean esUnNumeroDouble(String numeroString) {
        if (numeroString == null)
            return false;
 
        try {
            double numero = Double.parseDouble(numeroString);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    
    /**
     * Genera un contrato tomando la informacion del formulario.
     * @return contrato del formulario
     */
    private Contrato generarContratoDeFormulario() {
        
        Dueño dueño = new Dueño(formDueñoNombre.getText(), formDueñoTelefono.getText());
        Mascota mascota = null;
        
        int idMascota = (int) formMascotaId.getValue();
        String nombreMascota = formMascotaNombre.getText();
        String raza = this.formMascotaRaza.getText();
        boolean estaEnferma = formMascotaEnfermedad.isSelected();
        String tipoMascota = (String) formMascotaTipo.getSelectedItem();
        
        switch(tipoMascota) {
            case "Perro":
                mascota = new Perro(idMascota, nombreMascota, estaEnferma, raza, dueño);
                break;
            case "Gato":
                mascota = new Gato(idMascota, nombreMascota, estaEnferma, raza, dueño);
                break;
            case "Pajaro":
                mascota = new Pajaro(idMascota, nombreMascota, estaEnferma, raza, dueño);
                break;
            default:
                mascota = new Pez(idMascota, nombreMascota, estaEnferma, raza, dueño);
        }
        
        LocalDateTime desde = aLocalDateTime(formContratoDesde.getDate());
        LocalDateTime hasta = aLocalDateTime(formContratoHasta.getDate());
        double costo = (double) formCosto.getValue();
        
        return new Contrato(desde, hasta, mascota, costo);
                
    }
    
    /**
     * Convierte una variable Date a LocalDateTime
     * @param date variable tipo Date
     * @return variable tipo LocalDateTime
     */
    private LocalDateTime aLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).withHour(0).withMinute(0).withSecond(0).withNano(0);
    }
    
    /**
     * Actualiza los datos de la tabla de contratos
     */
    private void actualizarTablaContratos() {
        DefaultTableModel modelo = (DefaultTableModel) tablaContratos.getModel();
        modelo.setRowCount(0);
        Contrato[] contratos = hotel.getContratos();
        for(Contrato contrato : contratos) {
            int id = contrato.getNumero();
            if (id != -1) {
                String dueño = contrato.getMascota().getElDueño().getNombre();
                String mascota = contrato.getMascota().getNombre() + ", el " + contrato.getMascota().getCodigoAlimento().toString().toLowerCase();
            
                DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                String desde = contrato.getDesde().format(formatoFecha);
                String hasta = contrato.getHasta().format(formatoFecha);
            
                String estado = (contrato.isDisponible()) ? "Disponible" : "Extraviado";
                modelo.addRow(new Object[]{id, dueño, mascota, desde, hasta, estado, "X"});   
            }
        }
    }
    
    /**
     * Actualiza los datos de la tabla de inventario
     */
    private void actualizarTablaInventario() {
        DefaultTableModel modelo = (DefaultTableModel) tablaInventario.getModel();
        modelo.setRowCount(0);
        ArrayList<Alimento> inventario = hotel.getInventario();
        for(Alimento alimento : inventario) {
            String tipo = alimento.getTipo().toString();
            double cantidad = alimento.getExistenciaKilos();
            modelo.addRow(new Object[]{tipo, cantidad});   
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaContratos = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        formMascotaNombre = new javax.swing.JTextField();
        formMascotaEnfermedad = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        formContratoDesde = new com.toedter.calendar.JDateChooser();
        formCosto = new javax.swing.JSpinner();
        registrarContratoBtn = new javax.swing.JButton();
        formMascotaId = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        formMascotaRaza = new javax.swing.JTextField();
        formDueñoNombre = new javax.swing.JTextField();
        formDueñoTelefono = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        formContratoHasta = new com.toedter.calendar.JDateChooser();
        jLabel8 = new javax.swing.JLabel();
        formMascotaTipo = new javax.swing.JComboBox<>();
        simularIngresosBtn = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaInventario = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        estadoReportarAlimentacion = new javax.swing.JComboBox<>();
        idReportarAlimentacion = new javax.swing.JComboBox<>();
        fechaReportarAlimentacion = new com.toedter.calendar.JDateChooser();
        reportarAlimentacionBtn = new javax.swing.JButton();
        horarioReportarAlimentacion = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        mostrarDetallesMascotasBtn = new javax.swing.JButton();
        planearAlimentacionBtn = new javax.swing.JButton();
        generarBitacoraFecha = new com.toedter.calendar.JDateChooser();
        simularAlimentacionFecha = new com.toedter.calendar.JDateChooser();
        simularAlimentacionBtn = new javax.swing.JButton();
        generarBitacoraBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1000, 800));
        setSize(new java.awt.Dimension(1000, 800));

        jTabbedPane1.setPreferredSize(new java.awt.Dimension(1200, 776));

        jPanel6.setBackground(new java.awt.Color(204, 204, 204));

        tablaContratos.setFont(new java.awt.Font("Dialog", 0, 13)); // NOI18N
        tablaContratos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Dueño", "Mascota", "Desde", "Hasta", "Estado", "Eliminar"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tablaContratos);

        jLabel5.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(51, 51, 51));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Registrar Contrato");

        jPanel7.setBackground(new java.awt.Color(204, 204, 204));

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 51));
        jLabel1.setText("Mascota");

        formMascotaNombre.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        formMascotaNombre.setText("Nombre");

        formMascotaEnfermedad.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        formMascotaEnfermedad.setText("Enfermedad");

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 51, 51));
        jLabel2.setText("Dueño");

        formCosto.setModel(new javax.swing.SpinnerNumberModel(1.0d, 0.0d, null, 1.0d));

        registrarContratoBtn.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        registrarContratoBtn.setText("Registrar");
        registrarContratoBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registrarContrato(evt);
            }
        });

        formMascotaId.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        formMascotaId.setMinimumSize(new java.awt.Dimension(30, 100));

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 51, 51));
        jLabel3.setText("ID");

        formMascotaRaza.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        formMascotaRaza.setText("Raza");

        formDueñoNombre.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        formDueñoNombre.setText("Nombre");

        formDueñoTelefono.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        formDueñoTelefono.setText("Teléfono");

        jLabel4.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 51, 51));
        jLabel4.setText("Desde");

        jLabel7.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(51, 51, 51));
        jLabel7.setText("Hasta");

        jLabel8.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(51, 51, 51));
        jLabel8.setText("Costo Diario");

        formMascotaTipo.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        formMascotaTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Perro", "Gato", "Pez", "Pajaro" }));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(formCosto)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(formMascotaEnfermedad, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
                                    .addComponent(formMascotaRaza, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(formMascotaNombre, javax.swing.GroupLayout.Alignment.LEADING))
                                .addComponent(jLabel4))
                            .addComponent(formMascotaId, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(formContratoDesde, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(51, 51, 51)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(formMascotaTipo, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(formContratoHasta, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
                                    .addComponent(formDueñoTelefono, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(formDueñoNombre, javax.swing.GroupLayout.Alignment.LEADING))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(118, 118, 118)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel8))
                        .addContainerGap(80, Short.MAX_VALUE))))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(79, 79, 79)
                .addComponent(registrarContratoBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(formMascotaId, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(formDueñoNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(formMascotaNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(formDueñoTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(formMascotaRaza, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(formMascotaEnfermedad)
                .addGap(13, 13, 13)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(formContratoDesde, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(formContratoHasta, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(9, 9, 9)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(formCosto, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(formMascotaTipo))
                .addGap(80, 80, 80)
                .addComponent(registrarContratoBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        simularIngresosBtn.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        simularIngresosBtn.setText("Simular Ingresos");
        simularIngresosBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simularIngresos(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(simularIngresosBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 786, Short.MAX_VALUE))
                .addGap(21, 21, 21))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 594, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(simularIngresosBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Contratos", jPanel6);

        tablaInventario.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        tablaInventario.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Comida", "Kg"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tablaInventario);

        jPanel10.setBackground(new java.awt.Color(204, 204, 204));

        jLabel12.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(51, 51, 51));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Reportar Alimentación");

        jLabel13.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(51, 51, 51));
        jLabel13.setText("Número ");

        estadoReportarAlimentacion.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        estadoReportarAlimentacion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ALIMENTADO", "NO_QUISO", "NO_HAY_ALIMENTO", "SIN_INFORMACION" }));

        idReportarAlimentacion.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        idReportarAlimentacion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" }));

        reportarAlimentacionBtn.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        reportarAlimentacionBtn.setText("Reportar");
        reportarAlimentacionBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reportarAlimentacion(evt);
            }
        });

        horarioReportarAlimentacion.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        horarioReportarAlimentacion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "6am", "12md", "6pm" }));

        jLabel14.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(51, 51, 51));
        jLabel14.setText("de Contrato");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel13)
                            .addComponent(estadoReportarAlimentacion, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(50, 50, 50)
                        .addComponent(fechaReportarAlimentacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(idReportarAlimentacion, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addComponent(horarioReportarAlimentacion, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(23, Short.MAX_VALUE))
            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(reportarAlimentacionBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(78, 78, 78))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(idReportarAlimentacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(estadoReportarAlimentacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(horarioReportarAlimentacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(fechaReportarAlimentacion, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(29, 29, 29)
                .addComponent(reportarAlimentacionBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        mostrarDetallesMascotasBtn.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        mostrarDetallesMascotasBtn.setText("Mostrar Detalles de Mascotas");
        mostrarDetallesMascotasBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mostrarDetallesMascotasBtnActionPerformed(evt);
            }
        });

        planearAlimentacionBtn.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        planearAlimentacionBtn.setText("Planear Alimentación");
        planearAlimentacionBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                planearAlimentacionBtnActionPerformed(evt);
            }
        });

        simularAlimentacionBtn.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        simularAlimentacionBtn.setText("Simular Alimentacion");
        simularAlimentacionBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simularAlimentacionBtnActionPerformed(evt);
            }
        });

        generarBitacoraBtn.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        generarBitacoraBtn.setText("Generar Bitácora");
        generarBitacoraBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generarBitacoraBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(80, 80, 80)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(mostrarDetallesMascotasBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(planearAlimentacionBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 81, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 244, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addComponent(simularAlimentacionFecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGap(6, 6, 6)
                            .addComponent(simularAlimentacionBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 535, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(generarBitacoraFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(generarBitacoraBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 255, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 135, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(mostrarDetallesMascotasBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(87, 87, 87))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(simularAlimentacionFecha, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                            .addComponent(simularAlimentacionBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(96, 96, 96)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(generarBitacoraFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(planearAlimentacionBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(generarBitacoraBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(102, 102, 102))
        );

        jTabbedPane1.addTab("Alimentacion", jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void generarBitacoraBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generarBitacoraBtnActionPerformed
        Date fecha = generarBitacoraFecha.getDate();
        if (fecha != null) {
            hotel.generarBitacoraDeFecha(aLocalDateTime(fecha));
        } else {
            JOptionPane.showMessageDialog(null, "Por favor ingrese una fecha para generar la bitácora");
    }//GEN-LAST:event_generarBitacoraBtnActionPerformed

    private void simularAlimentacionBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simularAlimentacionBtnActionPerformed
        Date fecha = simularAlimentacionFecha.getDate();
        if (fecha != null) {
            JOptionPane.showMessageDialog(null, hotel.simularAlimentacionMascotas(aLocalDateTime(fecha)));
            actualizarTablaInventario();
        } else {
            JOptionPane.showMessageDialog(null, "Por favor ingrese una fecha para la simulación de alimentos");
        }
    }//GEN-LAST:event_simularAlimentacionBtnActionPerformed

    private void planearAlimentacionBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_planearAlimentacionBtnActionPerformed
        JOptionPane.showMessageDialog(null, hotel.planearAlimentacion());
    }//GEN-LAST:event_planearAlimentacionBtnActionPerformed

    private void mostrarDetallesMascotasBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mostrarDetallesMascotasBtnActionPerformed
        JOptionPane.showMessageDialog(null, hotel.mostrarDetalleMascotas());
    }//GEN-LAST:event_mostrarDetallesMascotasBtnActionPerformed

    private void reportarAlimentacion(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reportarAlimentacion
        Date fechaOriginal = this.fechaReportarAlimentacion.getDate();
        if (fechaOriginal != null) {
            LocalDateTime fecha = aLocalDateTime(fechaOriginal);
            EstadoAlimentacion estado = EstadoAlimentacion.valueOf((String) estadoReportarAlimentacion.getSelectedItem());
            String horario = (String) horarioReportarAlimentacion.getSelectedItem();
            BitacoraAlimentacion bitacora = new BitacoraAlimentacion(fecha, estado, horario);

            int idContrato = Integer.parseInt((String) idReportarAlimentacion.getSelectedItem());
            String respuesta = hotel.reportarAlimentacion(bitacora, idContrato);
            JOptionPane.showMessageDialog(null, respuesta);
            actualizarTablaInventario();

        } else {
            JOptionPane.showMessageDialog(null, "Por favor ingresar una fecha para reportar alimentación");
        }
    }//GEN-LAST:event_reportarAlimentacion

    private void simularIngresos(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simularIngresos
        String numeroDeIngresos = JOptionPane.showInputDialog("Ingrese el número de ingresos");

        if (esUnNumero(numeroDeIngresos)) {
            int cantidadIngresos = Integer.parseInt(numeroDeIngresos);
            hotel.simularIngresos(cantidadIngresos);
            actualizarTablaContratos();

        } else {
            JOptionPane.showMessageDialog(null, "Por favor ingresar un número válido");
        }

    }//GEN-LAST:event_simularIngresos

    private void registrarContrato(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registrarContrato
        if(formularioContratoEsValido()){
            int indexDisponible = hotel.cupoDisponible();
            boolean hayEspacio = (indexDisponible != -1);
            if (hayEspacio) {
                Contrato contrato = generarContratoDeFormulario();
                contrato.setNumero(indexDisponible);

                hotel.registratContrator(contrato);
                actualizarTablaContratos();

            } else {
                JOptionPane.showMessageDialog(null, "El hotel está lleno");
            }
        }
    }//GEN-LAST:event_registrarContrato

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MenuPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MenuPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MenuPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MenuPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MenuPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> estadoReportarAlimentacion;
    private com.toedter.calendar.JDateChooser fechaReportarAlimentacion;
    private com.toedter.calendar.JDateChooser formContratoDesde;
    private com.toedter.calendar.JDateChooser formContratoHasta;
    private javax.swing.JSpinner formCosto;
    private javax.swing.JTextField formDueñoNombre;
    private javax.swing.JTextField formDueñoTelefono;
    private javax.swing.JRadioButton formMascotaEnfermedad;
    private javax.swing.JSpinner formMascotaId;
    private javax.swing.JTextField formMascotaNombre;
    private javax.swing.JTextField formMascotaRaza;
    private javax.swing.JComboBox<String> formMascotaTipo;
    private javax.swing.JButton generarBitacoraBtn;
    private com.toedter.calendar.JDateChooser generarBitacoraFecha;
    private javax.swing.JComboBox<String> horarioReportarAlimentacion;
    private javax.swing.JComboBox<String> idReportarAlimentacion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton mostrarDetallesMascotasBtn;
    private javax.swing.JButton planearAlimentacionBtn;
    private javax.swing.JButton registrarContratoBtn;
    private javax.swing.JButton reportarAlimentacionBtn;
    private javax.swing.JButton simularAlimentacionBtn;
    private com.toedter.calendar.JDateChooser simularAlimentacionFecha;
    private javax.swing.JButton simularIngresosBtn;
    private javax.swing.JTable tablaContratos;
    private javax.swing.JTable tablaInventario;
    // End of variables declaration//GEN-END:variables
}
