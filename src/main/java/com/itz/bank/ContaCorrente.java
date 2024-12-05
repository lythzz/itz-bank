/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itz.bank;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
/**
 *
 * @author lucas
 */
public class ContaCorrente extends Conta {
    
    public ContaCorrente(float saldo, String conta){
        super(conta);
        this.saldo = saldo;
   }
    
    void depositar(Banco b, float quantia){
        Scanner s = b.s;
        this.saldo += quantia;
        Transacao t = new Transacao(LocalDateTime.now(), "Deposito", this.conta, null, quantia);
        b.registrarTransacao(t);
        b.salvar(0);
        System.out.println("Deposito realizado com sucesso!");
        System.out.println("Saldo atual: R$ " + String.format("%.2f", saldo));
        System.out.print("Aperte enter para continuar...");
        s.nextLine();
    }
    
    void sacar(Banco b, float quantia){
        Scanner s = b.s;
        if(quantia > this.saldo){
            System.out.println("Saldo insuficiente!");
            System.out.print("Aperte enter para continuar...");
            s.nextLine();
            return;
        }
        this.saldo -= quantia;
        Transacao t = new Transacao(LocalDateTime.now(), "Saque", this.conta, null, quantia);
        b.registrarTransacao(t);
        b.salvar(0);
        System.out.println("Saque realizado com sucesso!");
        System.out.println("Saldo atual: R$ " + String.format("%.2f", saldo));
        System.out.print("Aperte enter para continuar...");
        s.nextLine();
    }
    
    void transferir(Banco b, float quantia, String destino){
        Scanner s = b.s;
        if(quantia > this.saldo){
            System.out.println("Saldo insuficiente!");
            System.out.print("Aperte enter para continuar...");
            s.nextLine();
            return;
        }
        this.saldo -= quantia;
        Transacao t = new Transacao(LocalDateTime.now(), "Transferencia", this.conta, destino, quantia);
        b.registrarTransacao(t);
        b.salvar(0);
        System.out.println("Transferencia realizada com sucesso!");
        System.out.println("Saldo atual: R$ " + String.format("%.2f", saldo));
        System.out.print("Aperte enter para continuar...");
        s.nextLine();
    }
    
    public void verExtrato(Banco b, String periodoNum, String tipoNum){
        Scanner s = b.s;
        ArrayList<Transacao> transacoes = b.getTransacoes();
        LocalDateTime periodo;
        String tipo;
        
        ArrayList<Transacao> transacoesFiltradas = new ArrayList();
        
        switch(periodoNum){
            case "1":
                periodo = LocalDateTime.now().minusHours(24);
                break;
            case "2":
                periodo = LocalDateTime.now().minusDays(7);
                break;
            case "3":
                periodo = LocalDateTime.now().minusDays(30);
                break;
            case "4":
                periodo = null;
                break;
            default:
                periodo = null;
                break;
        }
        
        switch(tipoNum){
            case "1":
                tipo = "Deposito";
                break;
            case "2":
                tipo = "Saque";
                break;
            case "3":
                tipo = "Transferencia";
                break;
            case "4":
                tipo = "Aporte";
                break;
            case "5":
                tipo = null;
            default:
                tipo = null;
                break;
        }
                
                    
        for(Transacao t: transacoes){
            if(!t.origem.equals(this.conta) && (t.destino == null || !t.destino.equals(this.conta))) continue;
            
            boolean tipoCorreto = (tipo == null || t.tipo.equals(tipo) || t.tipo.contains(tipo));
            boolean periodoCorreto = (periodo == null || t.data.isAfter(periodo));
            
            if(tipoCorreto && periodoCorreto){
                Transacao cpy = t;
                if(cpy.destino.equals(this.conta)){
                     cpy.tipo = "Transf. Recebida"; 
                     cpy.destino = cpy.origem;
                }
                transacoesFiltradas.add(cpy);
            }
        }
        
        if(transacoesFiltradas.size() == 0){
            Utils.clear();
            System.out.println("Nao foram encontradas transacoes para essa conta com os filtros atuais.");
            System.out.print("Aperte enter para continuar...");
            s.nextLine();
            return;
        }
        
        Utils.clear();
        DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        System.out.printf("%-17s %-18s %-15s %-12s%n", "Data e Hora", "Tipo", "Origem/destino", "Quantia");
        
        
        for(Transacao t: transacoesFiltradas){
            String data = t.data.format(formatoData);
            System.out.printf("%-17s %-18s %-15s R$ %,12.2f%n", data, t.tipo, (t.destino == null ? "------" : t.destino), t.quantia);
        }
        System.out.println("--------------------------------------------------------------------");
        System.out.printf("%-52s R$ %12.2f ", "Saldo atual ", this.saldo);
        
        System.out.print("\nAperte enter para continuar");
        s.nextLine();
    }
}
