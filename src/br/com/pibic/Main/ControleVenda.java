/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.pibic.Main;

/**
 *
 * @author Mergulhao
 */
import br.com.pibic.util.Conexao;
import br.com.pibic.util.PortaSerial;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Said
 */
public class ControleVenda extends Thread {

    String Tags, Tagstemp;
    String[] TagsArray;
    Connection conn;
    JTextArea AreaProdutos;
    JLabel LabelPrecoTotal;
    Timer timer = new Timer(4000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ReceberTagsSerial();
            try {
                GetProdutos();
            } catch (SQLException ex) {
                Logger.getLogger(ControleVenda.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    });

    // construtor
    public ControleVenda(JTextArea AreaProdutos, JLabel LabelPrecoTotal) {
        this.AreaProdutos = AreaProdutos;
        this.LabelPrecoTotal = LabelPrecoTotal;
    }

    public void run() {
        try {
//            new PortaSerial().connect("COM5");

            timer.setRepeats(true);
            timer.start();
        } catch (Exception ex) {
            Logger.getLogger(ControleVenda.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void ReceberTagsSerial() {
        if (Tagstemp != "null") {
            Tags = PortaSerial.SerialReader.tags;

            Tagstemp = "";
            System.out.print(Tags);
        }
    }

    public void GetProdutos() throws SQLException {
        if (Tags.length() != 0) {
            TagsArray = Tags.split("\r\n");

            String[] Produtos = new String[TagsArray.length];
            Float[] Valor = new Float[TagsArray.length];
            int[] id_produtos = new int[TagsArray.length];
            conn = Conexao.createConnection();

            PreparedStatement ps;
            String sql = null;
            ResultSet resultado;

            for (int i = 0; i < TagsArray.length; i++) {
                sql = "SELECT Id_Produto from tags where Tag = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, TagsArray[i]);
                resultado = ps.executeQuery();

                if (resultado.next()) {

                    id_produtos[i] = resultado.getInt("Id_Produto");

                }//fim if
            } // fim for

            // pegar nome dos produtos de acordo com id 
            for (int i = 0; i < id_produtos.length; i++) {
                sql = "Select Produto,Valor from Produtos where id = ?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, id_produtos[i]);
                resultado = ps.executeQuery();

                if (resultado.next()) {
                    Produtos[i] = resultado.getString("Produto");
                    Valor[i] = resultado.getFloat("Valor");
                }
            }

            String temp = "";
            Float precototal = 0f;
            for (int i = 0; i < id_produtos.length; i++) {
                temp += Produtos[i] + " " + "R$" + Valor[i] + "\n";
                precototal += Valor[i];

            }
            AreaProdutos.setText(temp);
            LabelPrecoTotal.setText("R$ " + String.valueOf(precototal));

        }
    }

}
