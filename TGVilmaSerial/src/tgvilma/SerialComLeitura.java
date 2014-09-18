/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package tgvilma;


import gnu.io.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import static sun.misc.PostVMInitHook.run;
/**
 *
 * @author Vilma
 */
public class SerialComLeitura {
  public String Dadoslidos;
  public int nodeBytes;
  private int baudrate;
  private int timeout;
  private CommPortIdentifier cp;
  private SerialPort porta;
  private OutputStream saida;
  private InputStream entrada;
  private Thread threadLeitura;
  private boolean IDPortaOK;
  private boolean PortaOK;
  private boolean Leitura;
  private boolean Escrita;
  private String Porta;
  protected String peso;
   
  public SerialComLeitura( String p , int b , int t ){

        this.Porta = p;
        this.baudrate = b;
        this.timeout = t;     
    
    }
  public void setPeso(String peso){
      this.peso = peso;
  }
  public String getPeso(){
      return peso;
}
  public void HabilitarEscrita(){
      Escrita = false;
      Leitura = true;
  }
  
  public void ObterIdDaPorta(){
      try{
          
          cp = CommPortIdentifier.getPortIdentifier(Porta);

            if ( cp == null ) {       
          
                 System.out.println("Erro na porta");
                 IDPortaOK = false;
                 System.exit(1);
      }
      
      IDPortaOK =true;
  
  } catch (Exception e){
        System.out.println (" ID não localizado da porta: " + e);
        IDPortaOK = false;
        System.exit(1);
     }
  }
  public void AbrirPorta(){

        try {

            porta = (SerialPort)cp.open("SerialComLeitura", timeout);
            PortaOK = true;

            //configurar parâmetros

            porta.setSerialPortParams(baudrate,
            porta.DATABITS_8,
            porta.STOPBITS_1,
            porta.PARITY_NONE);
            porta.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);

        }catch(Exception e){

            PortaOK = false;
            System.out.println("Erro abrindo comunicação: " + e);
            System.exit(1);
            }
        }
            
  public void LerDados(){
        if (Escrita == false){

            try {

                entrada = porta.getInputStream();

            } catch (Exception e) {
                System.out.println("Erro de stream: " + e);
                System.exit(1);

            }

            try {                   
                porta.addEventListener(null); //mudei era só () this);

            } catch (Exception e) {

                System.out.println("Erro de listener: " + e);
                System.exit(1);

            }

            porta.notifyOnDataAvailable(true);

            try {
                threadLeitura = new Thread();  //mudei era só (this) no lugar de runnable;
                threadLeitura.start();
              run();

            } catch (Exception e) {
                System.out.println("Erro de Thred: "+ e);

            }

        }
  }

        public void EnviarUmaString(String msg){

        if (Escrita==true) {

            try {

                saida = porta.getOutputStream();
                System.out.println("FLUXO OK!");

            } catch (Exception e) {
                System.out.println("Erro.STATUS: " + e );

            }

            try {

                System.out.println("Enviando um byte para " + Porta );
                System.out.println("Enviando : " + msg );
                saida.write(msg.getBytes());
                Thread.sleep(100);
                saida.flush();

            } catch (Exception e) {
                System.out.println("Houve um erro durante o envio. ");
                System.out.println("STATUS: " + e );
                System.exit(1);
            }

        } else {
            System.exit(1);

        }

}
public void run(){

        try {
            Thread.sleep(5);
        } catch (Exception e) {
            System.out.println("Erro de Thred: " + e);

        }

}
public void serialEvent(SerialPortEvent ev){   
        StringBuffer bufferLeitura = new StringBuffer();
        int novoDado = 0;

        switch (ev.getEventType()) {

            case SerialPortEvent.BI:
            case SerialPortEvent.OE:
            case SerialPortEvent.FE:
            case SerialPortEvent.PE:
            case SerialPortEvent.CD:
            case SerialPortEvent.CTS:
            case SerialPortEvent.DSR:
            case SerialPortEvent.RI:
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
            break;
                
            case SerialPortEvent.DATA_AVAILABLE:
                //Novo algoritmo de leitura.
                while(novoDado != -1){

                    try{
                        novoDado = entrada.read();
                        if(novoDado == -1){
                            break;
                        }

                        if('\r' == (char)novoDado){
                            bufferLeitura.append('\n');

                        }else{
                            bufferLeitura.append((char)novoDado);

                        }

                    }catch(IOException ioe){
                        System.out.println("Erro de leitura serial: " + ioe);
                    }

                }

                setPeso(new String(bufferLeitura));
                System.out.println(getPeso());
            break;

        }
}

//Agora somente os métodos para fechar a serial, obter a porta e obter o baurate do seu equipamento e assim essa classe está finalizada.

public void FecharCom(){
            try {
                porta.close();
            } catch (Exception e) {
                System.out.println("Erro fechando porta: " + e);
                System.exit(0);
            }
}   

public String obterPorta(){
        return Porta;

}   

public int obterBaudrate(){
        return baudrate;

    }

}

 





