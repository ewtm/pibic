/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.pibic.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mergulhao
 */
public class Teste_Conexao {

    public static void main(String[] args) {
        try {
            Connection conexao = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/pibic_final", "root", "");
            System.out.println("Conectado!");
            conexao.close();
        } catch (SQLException ex) {
            Logger.getLogger(Teste_Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
