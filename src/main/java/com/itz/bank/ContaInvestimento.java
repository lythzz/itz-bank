/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itz.bank;
import java.util.ArrayList;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
/**
 *
 * @author lucas
 */
public class ContaInvestimento extends Conta{
    ArrayList<Investimento> investimentos;
    
    public ContaInvestimento(String conta, ArrayList<Investimento> investimentos){
        super(conta);
        this.investimentos = investimentos;
    }
    
    void aportar(Banco b, Cliente u, String tipo, float quantia){
        Scanner s = b.s;
        Investimento i = new Investimento(tipo, quantia, LocalDateTime.now());
        
        if(u.contaCorrente.saldo < quantia){
            while(true){
                Utils.clear();
                System.out.println("Voce nao possui saldo suficiente na sua conta corrente para realizar essa operacao.");
                System.out.printf("Deseja depositar R$ %,.2f para realizar o aporte? (s/n)", quantia - u.contaCorrente.saldo);
                String op = b.s.nextLine();
                
                if(op.equals("n")) return;
                if(op.equals("s")){
                    u.contaCorrente.depositar(b, quantia - u.contaCorrente.saldo);
                    Utils.clear();
                    break;
                }
            }
        }
        
        this.investimentos.add(i);
        Transacao t = new Transacao(LocalDateTime.now(), "Aporte", this.conta, null, quantia);
        b.registrarTransacao(t);
        u.contaCorrente.saldo -= quantia;
        b.salvar(1);
        System.out.println("Aporte realizado com sucesso!");
        System.out.print("Aperte enter para continuar...");
        s.nextLine();
    }
    
    void resgatarInvestimento(Banco b, Cliente u, Long id){
        Scanner s = b.s;
        float total = 0;
        Investimento i = null;
        for(Investimento inv: this.investimentos){
            if(id.equals(inv.id)){
                i = inv;
                break;
            }
        }
        LocalDateTime fim = LocalDateTime.now();
        long minutos = Duration.between(i.data, fim).toMinutes();
        total = (float) (i.aporte * Math.pow((1 + i.rentabilidadeMinuto), minutos));
        Transacao t = new Transacao(LocalDateTime.now(), "Resg. Aporte", this.conta, null, total);
        b.registrarTransacao(t);
        u.contaCorrente.saldo += total;
        b.salvar(1);
        System.out.println("Resgate realizado com sucesso!");
        System.out.print("Aperte enter para continuar...");
        s.nextLine();
    }
    
    void verInvestimentos(Banco b, Cliente u){
        while(true){
            Utils.clear();
            
            if(this.investimentos.size() == 0){
                System.out.println("Voce nao possui nenhum investimento.");
                System.out.print("Aperte enter para continuar...");
                b.s.nextLine();
                return;
            }
            
            DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yy");
            DateTimeFormatter dataCompleta = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            System.out.printf("%2s %-12s %-5s %-12s%n", "ID", "Data", "Tipo", "Quantia");
            
            int index = 1;
            for(Investimento i: this.investimentos){
                 String data = i.data.format(formatoData);
                 System.out.printf("%2s %-12s %-5s R$ %,12.2f%n", index, data, i.tipo, i.aporte);
                 index++;
            }
            System.out.print("Escolha um investimento ou digite 'q' para voltar: ");
            String opcao = b.s.nextLine(); 
            if(opcao.equals("q")) return;
            
            try{
                int opcaoNum = Integer.parseInt(opcao);
                if(opcaoNum > 0 && opcaoNum <= this.investimentos.size()){
                    while(true){
                        Utils.clear();
                        Investimento i = this.investimentos.get(opcaoNum-1);
                        String tipo = "";
                        String inicio = i.data.format(dataCompleta);
                        String prazo = i.prazo.format(dataCompleta);
                        LocalDateTime fim = LocalDateTime.now();
                        long minutos = Duration.between(i.data, fim).toMinutes();
                        float vAtual = (float) (i.aporte * Math.pow((1 + i.rentabilidadeMinuto), minutos));
                        switch(i.tipoNum){
                            case 1:
                                tipo = "Letra de Credito Imobiliario";
                                break;
                            case 2:
                                tipo = "Letra de Credito do Agronegocio";
                                break;
                            case 3:
                                tipo = "Certificado de Deposito Bancario";
                                break;
                        }
                        System.out.printf("%-35s %32s%n", "ID:", i.id);
                        System.out.printf("%-35s %32s%n", "Tipo:", tipo);
                        System.out.printf("%-35s %32s%n", "Data:", inicio);
                        System.out.printf("%-35s %32s%n", "Aporte:", String.format("R$ %,.2f", i.aporte));
                        System.out.printf("%-35s %32s%n", "Prazo minimo:", i.tipoNum == 3 ? "----------" : prazo);
                        System.out.printf("%-35s %32s%n", "Valor atual:", String.format("R$ %,.2f", vAtual));
                        System.out.println("--------------------------------------------------------------------");
                        System.out.println("*ATENCAO! Ao resgatar um investimento antes do prazo minimo voce re-\ncebe-ra apenas o aporte inicial.");
                        System.out.println("Voce deseja:");
                        System.out.println("!. Resgatar investimento");
                        System.out.println("2. Voltar");
                        System.out.print("Escolha uma opcao: ");
                        String op = b.s.nextLine();
                        if(op.equals("1")){
                            this.resgatarInvestimento(b, u, i.id);
                            return;
                        }
                        if(op.equals("2")) break;
                    }
                }  
            } catch (NumberFormatException e){
                //evita que  o programa crashe caso o usuario de um input invalido
            }
        }   
    }
}
