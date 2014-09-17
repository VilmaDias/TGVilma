
package tgvilma;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author Vilma
 */
public class PortaComm implements Runnable, SerialPortEventListener {

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
    protected int quantidade;

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public PortaComm(String p, int b, int t) {

        this.Porta = p;

        this.baudrate = b;

        this.timeout = t;

    }

    public void HabilitarEscrita() {

        Escrita = true;

        Leitura = false;

    }

    public void HabilitarLeitura() {

        Escrita = false;

        Leitura = true;

    }

//O método seguinte irá obter o ID da porta para ser utilizado na identificação da mesma:
    public void ObterIdDaPorta() {

        try {

            cp = CommPortIdentifier.getPortIdentifier(Porta);

            if (cp == null) {

                System.out.println("Erro na porta" );

                IDPortaOK = false;

                System.exit(1);

            }
            IDPortaOK = true;

        } catch (Exception e) {

            System.out.println("Erro obtendo ID da porta:" + e  );
            
            IDPortaOK = false;

            System.exit(1);

        }

    }

    
    public void AbrirPorta() {

        try {

            porta = (SerialPort) cp.open("SerialComLeitura", timeout);

            PortaOK = true;

            //configurar parâmetros
            porta.setSerialPortParams(baudrate,
                    porta.DATABITS_8,
                    porta.STOPBITS_1,
                    porta.PARITY_NONE);

            porta.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);

        } catch (Exception e) {
            PortaOK = false;
            System.out.println( "Erro de comunicação: " + e );
            System.exit(1);
        }
               IDPortaOK = true;
    }
        catch (Exception e) {

            System.out.println( "Erro obtendo ID da porta: " + e );

            IDPortaOK = false;

        System.exit(1);

    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void serialEvent(SerialPortEvent spe) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    }
    

    public void LerDados() {

        if (Escrita == false) {
            try {
                entrada = porta.getInputStream();

            } catch (Exception e) {

                System.out.println(" Erro de stream: "  + e);

                System.exit(1);

            }

            try {

                porta.addEventListener(this);

            } catch (Exception e) {

                System.out.println("Erro de listener: " + e );

                System.exit(1);

            }

            porta.notifyOnDataAvailable(true);

            try {

                threadLeitura = new Thread(this);

                threadLeitura.start();

                run();

            } catch (Exception e) {

                System.out.println("Erro de Thred: " + e );

            }

        } 
        
    }
    
    public void run() {

        try {

            Thread.sleep(5);

        } catch (Exception e) {
            System.out.println("Erro de Thred : "  + e );
        }
   }
public void serialEvent(SerialPortEvent ev) {

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
                while (novoDado != -1) {

                    try {

                        novoDado = entrada.read();

                        if (novoDado == -1) {

                            break;

                        }

                        if ('\r' == (char) novoDado) {

                            bufferLeitura.append('\n');

                        } else {

                            bufferLeitura.append((char) novoDado);

                        }

                    } catch (IOException ioe) {

                        System.out.println("Erro de leitura serial: " + ioe );

                    }

                }

                setQuantidade(new int(bufferLeitura ));

                System.out.println(getQuantidade());

                break;
        }

    }

    public void FecharCom() {

        try {

            porta.close();

        } catch (Exception e) {

            System.out.println("Erro fechando porta: " + e );

                System.exit(0);

        }

    }

    public String obterPorta() {

        return Porta;

    }

    public int obterBaudrate() {

        return baudrate;

    }

}
