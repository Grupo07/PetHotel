
package vista;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import controlador.LocalMascotas;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import modelo.Contrato;
import modelo.Dueño;
import modelo.Gato;
import modelo.Mascota;
import modelo.Pajaro;
import modelo.Perro;
import modelo.Pez;

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
        
        // valores de prueba
        Contrato[] contratos = new Contrato[10];
        Contrato contrato1 = new Contrato(LocalDateTime.now(), LocalDateTime.now(), new Perro(22, "Tob", false, "Chihuahua", new Dueño("John", "88665412")), 2.0);
        contrato1.setNumero(0);
        contrato1.setDisponible(false);
        Contrato contrato2 = new Contrato(LocalDateTime.now(), LocalDateTime.now(), new Perro(55, "Sam", true, "Labrador", new Dueño("Mason", "87666419")), 5.0);
        contrato2.setNumero(1);
        contratos[0] = contrato1;
        contratos[1] = contrato2;
        hotel.setContratos(contratos);
        actualizarTablaContratos();
        
        configurarTablaContratos();
    }
    
    /**
     * Configura la columna para eliminar contratos.
     */
    private void configurarTablaContratos() {
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
                        Contrato[] contratos = hotel.getContratos();
                        contratos[contratoId] = null;
                        hotel.setContratos(contratos);
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
                    Contrato[] contratos = hotel.getContratos();
                    Contrato contrato = contratos[contratoId];
                    contrato.setDisponible(disponible);
                    contratos[contratoId] = contrato;
                    hotel.setContratos(contratos);
                    actualizarTablaContratos();
                  
                } else {
                    JOptionPane.showMessageDialog(null, "El estado del contrato puede ser solo Disponible o Extraviado");
                    tablaContratos.setValueAt("Disponible", fila, columna);
                }
            }
        });
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
     * Verifica que un string contega un numero.
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
            if (contrato != null) {
             int id = contrato.getNumero();
            String dueño = contrato.getMascota().getElDueño().getNombre();
            String mascota = contrato.getMascota().getNombre();
            
            DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String desde = contrato.getDesde().format(formatoFecha);
            String hasta = contrato.getHasta().format(formatoFecha);
            
            String estado = (contrato.isDisponible()) ? "Disponible" : "Extraviado";
            modelo.addRow(new Object[]{id, dueño, mascota, desde, hasta, estado, "X"});   
            }
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
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1000, 800));
        setSize(new java.awt.Dimension(1000, 800));

        jTabbedPane1.setPreferredSize(new java.awt.Dimension(1200, 776));

        jPanel6.setBackground(new java.awt.Color(204, 204, 204));

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
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(formCosto, javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(formMascotaEnfermedad, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                                        .addComponent(formMascotaRaza, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(formMascotaNombre, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(formMascotaId, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING))
                                    .addComponent(jLabel4)))
                            .addComponent(formContratoDesde, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(71, 71, 71)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(formMascotaTipo, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(formContratoHasta, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(formDueñoNombre, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                                    .addComponent(formDueñoTelefono, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(60, 60, 60))))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(118, 118, 118)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel8))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 783, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 594, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(131, 131, 131)))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Contratos", jPanel6);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1189, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 749, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("tab1", jPanel1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1189, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 749, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("tab2", jPanel2);

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

    private void registrarContrato(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registrarContrato
        if(formularioContratoEsValido()){
            int indexDisponible = 2; // hardcode valor !!!!!
            boolean hayEspacio = (indexDisponible != -1);
            if (hayEspacio) {
                Contrato contrato = generarContratoDeFormulario();
                contrato.setNumero(indexDisponible);
                
                // Registrando el contrato
                Contrato[] contratos = hotel.getContratos();
                contratos[indexDisponible] = contrato;
                hotel.setContratos(contratos);
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton registrarContratoBtn;
    private javax.swing.JTable tablaContratos;
    // End of variables declaration//GEN-END:variables
}
