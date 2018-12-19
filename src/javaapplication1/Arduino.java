/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

import com.panamahitek.ArduinoException;
import com.panamahitek.PanamaHitek_Arduino;
import com.panamahitek.PanamaHitek_MultiMessage;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import org.jpl7.Query;

/**
 *
 * @author PCLR
 */
public class Arduino {

    public static PanamaHitek_Arduino ino = new PanamaHitek_Arduino();
    private static PanamaHitek_MultiMessage multi = new PanamaHitek_MultiMessage(2, ino);
    private static final SerialPortEventListener l = new SerialPortEventListener() {
        @Override
        public void serialEvent(SerialPortEvent spe) {
            try {
                if (multi.dataReceptionCompleted()) {
                    System.out.println("Humedad --> " + multi.getMessage(0));
                    System.out.println("Grados C --> " + multi.getMessage(1));
                    // System.out.println("Valor de c --> " + multi.getMessage(2));
                    String temperatura = multi.getMessage(1);
                    String humedad = multi.getMessage(0);
                    String hora = "11";

                    String tl = "consult('final.pl')";
                    Query ql = new Query(tl);
                    System.out.println(tl + "" + (ql.hasSolution() ? "satisfactoria" : "Fallo"));

                    String Conec = ("abrirConexion.");
                    Query ConexionBd = new Query(Conec);
                    System.out.println(tl + "" + (ConexionBd.hasSolution() ? "Conectado a la bd" : "Fallo la conexion a bd.."));

                    String optimizar = ("optimizar(" + temperatura + "," + humedad + ")");
                    Query Opt = new Query(optimizar);
                    System.out.println("optimizar=" + Opt.hasSolution());
                    if (!Opt.hasSolution()) {
                        ///Abrir techo
                        String techo = ("abrirTecho(" + hora + "," + humedad + ").");
                        Query Abrir = new Query(techo);
                        if (Abrir.hasSolution()) {
                            ino.sendData("1");
                            System.out.println("AbrirTecho=" + Abrir.hasSolution());
                        } else {
                            ino.sendData("0");
                            System.out.println("AbrirTecho=" + Abrir.hasSolution());
                        }
                        //abrir mediotecho
                        String tm = ("abrirMedioTecho(" + temperatura + ")");
                        Query Tmedio = new Query(tm);
                        if (Tmedio.hasSolution()) {
                            ino.sendData("2");
                            System.out.println("AbrirMedioTecho=" + Tmedio.hasSolution());
                        } else {
                            ino.sendData("0");
                            System.out.println("AbrirMedioTecho=" + Tmedio.hasSolution());
                        }

                        ///Encender focos
                        String foco = ("encenderFocos(" + temperatura + "," + hora + "," + humedad + ").");
                        Query Foco = new Query(foco);

                        if (Foco.hasSolution()) {
                            ino.sendData("3");
                            System.out.println("encenderFoco=" + Foco.hasSolution());
                        } else {
                            ino.sendData("4");
                            System.out.println("encenderFoco=" + "true");
                        }
                        ///Regar
                        String r = ("regar(" + humedad + "," + temperatura + ").");
                        Query regar = new Query(r);
                       // System.out.println("regar=" + regar.hasSolution());
                        if (regar.hasSolution()) {
                            ino.sendData("7");
                            System.out.println("regar=" + regar.hasSolution());
                        } else {
                            ino.sendData("8");
                            System.out.println("regar=" + regar.hasSolution());
                        }
                        //encender ventiador
                        String v = ("encenderVentilador(" + temperatura + ")");
                        Query ventilador = new Query(v);

                        if (ventilador.hasSolution()) {
                            ino.sendData("5");
                            System.out.println("Ventilador=" + ventilador.hasSolution());
                        } else {
                            ino.sendData("6");
                            System.out.println("Ventilador=" + ventilador.hasSolution());
                        }
                        //cerrar conexion
                        String cerrar = ("cerrarConexion.");
                        Query cbd = new Query(cerrar);
                        System.out.println("ConexionCerrarda=" + cbd.hasSolution());

                    }else
                    {  
                        ino.sendData("8");
                             ino.sendData("0");
                          ino.sendData("4");
                        
                         ino.sendData("6");
                          
                    }

                    System.out.println("-----------------------------------");

                    multi.flushBuffer();
                }
            } catch (ArduinoException | SerialPortException ex) {
                System.out.println("Error:" + ex);
            }
        }
    };

    public Arduino() {
        try {

            ino.arduinoRXTX("COM5", 9600, l);
        } catch (ArduinoException ex) {
            System.out.println("Error:" + ex);
        }
    }

    public void prolog(String H, String T, String Hr) {
        String temperatura = T;
        String humedad = H;
        String hora = Hr;

        String tl = "consult('final.pl')";
        Query ql = new Query(tl);
        System.out.println(tl + "" + (ql.hasSolution() ? "satisfactoria" : "Fallo"));

        String Conec = ("abrirConexion.");
        Query ConexionBd = new Query(Conec);
        System.out.println(tl + "" + (ConexionBd.hasSolution() ? "Conectado a la bd" : "Fallo la conexion a bd.."));

        String optimizar = ("abrirMedioTecho(" + temperatura + ")");
        Query Opt = new Query(optimizar);
        System.out.println(Opt.hasSolution());

        ///Abrir techo
        String techo = ("abrirTecho(" + hora + "," + humedad + ").");
        Query Abrir = new Query(techo);
        System.out.println("AbrirTecho=" + Abrir.hasSolution());

        ///Encender focos
        String foco = ("encenderFocos(" + temperatura + "," + hora + "," + humedad + ").");
        Query Foco = new Query(foco);
        System.out.println("encenderFoco=" + Foco.hasSolution());
        ///Regar
        String r = ("regar(" + hora + "," + temperatura + ").");
        Query regar = new Query(r);
        System.out.println("regar=" + regar.hasSolution());
        //encender ventiador
        String v = ("encenderVentilador(" + temperatura + ")");
        Query ventilador = new Query(v);
        System.out.println("Ventilador=" + ventilador.hasSolution());
        //abrir mediotecho
        String tm = ("abrirMedioTecho(" + temperatura + ")");
        Query Tmedio = new Query(tm);
        System.out.println("abrirMedioTecho=" + Tmedio.hasSolution());
    }
}
