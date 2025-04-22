/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package whitelotus_hotel;

import excepciones.MyException;
import gestor.Gestor;
import interfaz.MenuPrincipal;

/**
 *
 * @author paolaschicote
 */
public class WhiteLotus_Hotel {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            Gestor miConexion = new Gestor("root", "WhiteLotus_Hotel", "jdbc:mysql://localhost:3306/", "");

            miConexion.initDataBase();

            MenuPrincipal mp = new MenuPrincipal(miConexion);
            mp.setVisible(true);
            mp.setLocationRelativeTo(null);

        } catch (MyException ex) {

        }

    }

}
