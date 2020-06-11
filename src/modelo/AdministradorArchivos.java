package modelo;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Clase encargada del manejo de archivos binarios .dat
 * 
 * @author Esteban Guzmán R.
 */
public class AdministradorArchivos {

    /**
     * Guarta un arrayList de contratos en un archivo binario .dat
     * @param contratos 
     */
    public static void  guardarContratos(Contrato[] contratos) {
 
        try {
            
            ObjectOutputStream escribiendoFichero = new ObjectOutputStream( 
            new FileOutputStream("contratos.dat") );
            escribiendoFichero.writeObject(contratos);
            escribiendoFichero.close();
 
        } catch (FileNotFoundException fnfe) {
            System.out.println("Error: El fichero no existe " + fnfe);
        } catch (IOException ioe) {
            System.out.println("Error: Fallo en la escritura en el fichero. " + ioe);
        }
 
    }
    /**
     *Busca el archivo contratos.dat, lo lee y devuelve un arraylist de contratatos
     * @return un arraylist de contratatos
     */
    public static Contrato[] cargarContratos() {
        FileInputStream entradaArchivo;
        try {
            
            entradaArchivo = new FileInputStream(new File("contratos.dat"));
            ObjectInputStream objetoEntrante = new ObjectInputStream(entradaArchivo);
            Contrato[] array = (Contrato[]) objetoEntrante.readObject();
            return array;
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AdministradorArchivos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AdministradorArchivos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AdministradorArchivos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        
    }
    
    /**
     * Guarta un arrayList de mascotas en un archivo binario .dat
     * @param mascotas 
     */
    public static void  guardarMascotas(ArrayList<Mascota> mascotas) {
 
        try {
            
            ObjectOutputStream escribiendoFichero = new ObjectOutputStream( 
            new FileOutputStream("mascotas.dat") );
            escribiendoFichero.writeObject(mascotas);
            escribiendoFichero.close();
 
        } catch (FileNotFoundException fnfe) {
            System.out.println("Error: El fichero no existe " + fnfe);
        } catch (IOException ioe) {
            System.out.println("Error: Fallo en la escritura en el fichero. " + ioe);
        }
 
    }
    
    /**
     * Guarta un arrayList del inventario en un archivo binario .dat
     * @param inventario 
     */
    public static void  guardarInventario(ArrayList<Alimento> inventario) {
 
        try {
            
            ObjectOutputStream escribiendoFichero = new ObjectOutputStream( 
            new FileOutputStream("inventario.dat") );
            escribiendoFichero.writeObject(inventario);
            escribiendoFichero.close();
 
        } catch (FileNotFoundException fnfe) {
            System.out.println("Error: El fichero no existe " + fnfe);
        } catch (IOException ioe) {
            System.out.println("Error: Fallo en la escritura en el fichero. " + ioe);
        }
 
    }

    
    /**
     * Busca el archivo contratos.dat, lo lee y devuelve un arraylist del inventario
     * @return un arraylist del Inventario
     */
    public static ArrayList<Alimento> cargarInventario(){
        
        FileInputStream entradaArchivo;
        try {
            
            entradaArchivo = new FileInputStream(new File("inventario.dat"));
            ObjectInputStream objetoEntrante = new ObjectInputStream(entradaArchivo);
            ArrayList<Alimento> array = (ArrayList<Alimento>) objetoEntrante.readObject();
            return array;
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AdministradorArchivos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.out.println("No existe el archivo" + ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AdministradorArchivos.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
        
    }

    public static void guardarReporte(String resultado, String nombreRegistro) {
        try (FileWriter file = new FileWriter(nombreRegistro)) {
            file.write(resultado);
            file.flush();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    
}
