/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tgvilma;

/**
 *
 * @author Vilma
 */
public class MainPorta  extends SerialCom{
 
    /*Implementando a leitura/escrita serial

Podemos finalmente dizer que o pior já passou, vamos trabalhar com a classe Main
para aproveitarmos os recursos das outras duas classes e efetuarmos a leitura e escrita na porta serial.
O primeiro passo é cuidar dos imports:*/

public static void Main(String[] args){     

        //Iniciando leitura serial

        SerialComLeitura leitura = new SerialComLeitura("COM1", 9600, 0);

        //leitura.HabilitarLeitura();

        leitura.ObterIdDaPorta();
        leitura.AbrirPorta();
        leitura.LerDados();

        //Controle de tempo da leitura aberta na serial

        try {
            Thread.sleep(1000);
            
        } catch (InterruptedException ex) {
            System.out.println("Erro na Thread: " + ex);

        }

        leitura.FecharCom();
    }

}
