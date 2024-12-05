/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itz.bank;
import java.util.ArrayList;
import java.time.LocalDateTime;
/**
 *
 * @author lucas
 */
public class Utils {
    
public static void clear() {
    for (int i = 0; i < 20; i++) {
        System.out.println("");
    }
    System.out.println("====================================================================\n");
    System.out.println("                              ITZ Bank\n");
    System.out.println("====================================================================\n");
    }

public static ArrayList<Transacao> getTransacoesExemplo(){
    ArrayList<Transacao> transacoes = new ArrayList();
    transacoes.add(new Transacao(LocalDateTime.now(), "Deposito", "1402", null, 500.0f));
    transacoes.add(new Transacao(LocalDateTime.now().minusDays(1), "Saque", "1402", null, 200.0f));
    transacoes.add(new Transacao(LocalDateTime.now().minusHours(2), "Transferencia", "1402", "0701", 150.5f));
    transacoes.add(new Transacao(LocalDateTime.now().minusDays(3), "Deposito", "1402", null, 1000.0f));
    transacoes.add(new Transacao(LocalDateTime.now().minusHours(10), "Saque", "1402", null, 50.0f));
    transacoes.add(new Transacao(LocalDateTime.now().minusDays(7), "Transferencia", "1402", "0701", 750000.0f));
    transacoes.add(new Transacao(LocalDateTime.now().minusMinutes(30), "Deposito", "1402", null, 120.0f));
    transacoes.add(new Transacao(LocalDateTime.now().minusDays(10), "Saque", "1402", null, 400.0f));
    transacoes.add(new Transacao(LocalDateTime.now().minusHours(1), "Transferencia", "1402", "0109", 600.0f));
    transacoes.add(new Transacao(LocalDateTime.now().minusMonths(1), "Deposito", "1402", null, 900.0f));
    return transacoes;
}
}

