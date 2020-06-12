
package vista;

import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

/**
 * Renderizador de celdas utilizado para darle el aspecto de botones a una columna de un JTable.
 * 
 * @author Luis Mariano Ramírez Segura
*/
class RenderizadorDeBotones extends JButton implements TableCellRenderer {
    

        public RenderizadorDeBotones() {
            setOpaque(true);
        }
        
        /**
         * Establece el estilo de boton para la celda
         * 
         * @param table tabla padre
         * @param value valor de celda
         * @param isSelected esta la celda seleccionada
         * @param hasFocus esta la celda enfocada
         * @param row fila de celda
         * @param column columna de celda
         * @return 
         */
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, 
                                                       boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(UIManager.getColor("Button.background"));
            }
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }
