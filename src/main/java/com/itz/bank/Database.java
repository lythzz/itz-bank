/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itz.bank;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
/**
 *
 * @author lucas
 */
public class Database {
    static String caminhoClientes = "src/main/database/clientes.csv";
    static String caminhoContas = "src/main/database/contas.csv";
    static String caminhoInvestimentos = "src/main/database/investimentos.csv";
    static String caminhoTransacoes = "src/main/database/transacoes.csv";
    
    /*
    Ordem CSV:
    Clientes: cpf, username, senha, nome;
    Conta C.: cpf, saldo, conta;
    Investimento: cpf, tipo(num), valor, data;
    Transacao: data, tipo, origem, destino, quantia;
    */
    
    public static int numContas;
    
    public static ArrayList<Cliente> getClientes() throws FileNotFoundException{
        Map<String, ContaCorrente> contasC = new HashMap<>(); // armazena temporiaramente as Contas antes de instanciar Clientes
        BufferedReader readerContasC = new BufferedReader(new FileReader(caminhoContas));
        String line;
     
        try {
            //ordem csv: cpf, saldo, conta (num de conta)
            readerContasC.readLine(); // pula cabeçalho da tabela
            while((line = readerContasC.readLine()) != null){
                String[] valores = line.split(";");
                String cpf = valores[0];
                float saldo = Float.parseFloat(valores[1]);
                String conta = valores[2];
                
                contasC.put(cpf, new ContaCorrente(saldo, conta));
                numContas++;
            }
            
        } catch (IOException e) {
            System.out.println("Ocorreu um erro: " + e);
        }
        
        Map<String, ArrayList<Investimento>> investimentos = new HashMap<>(); // armazena temporiaramente as Contas antes de instanciar Clientes
        BufferedReader readerContasI = new BufferedReader(new FileReader(caminhoInvestimentos));
     
        try {
            //ordem csv: cpf, tipo, valor, data
            readerContasI.readLine(); // pula cabeçalho da tabela
            while((line = readerContasI.readLine()) != null){
                String[] valores = line.split(";");
                String cpf = valores[0];
                String tipo = valores[1];
                float aporte = Float.parseFloat(valores[2]);
                LocalDateTime data = LocalDateTime.parse(valores[3]);
                if(investimentos.containsKey(cpf)){
                    investimentos.get(cpf).add(new Investimento(tipo, aporte, data));
                    investimentos.get(cpf).sort((i1, i2) -> i1.data.compareTo(i2.data));
                    continue;
                }
                
                investimentos.put(cpf, new ArrayList<Investimento>());
                investimentos.get(cpf).add(new Investimento(tipo, aporte, data));
                
            }
            
        } catch (IOException e) {
            System.out.println("Ocorreu um erro: " + e);
        }
        
        ArrayList<Cliente> clientes = new ArrayList<>();
        BufferedReader readerClientes = new BufferedReader(new FileReader(caminhoClientes));
        
        try {
            //ordem csv: cpf, username, senha, nome
            readerClientes.readLine(); // pula cabeçalho da tabela
            while((line = readerClientes.readLine()) != null){
                String[] valores = line.split(";");
                String cpf = valores[0];
                String username = valores[1];
                String senha = valores[2];
                String nome = valores[3];
                ContaCorrente corrente = contasC.get(cpf);
                ContaInvestimento investimento = investimentos.get(cpf) == null ? null : new ContaInvestimento(corrente.conta, investimentos.get(cpf));
                
                clientes.add(new Cliente(cpf, nome, username, senha, corrente, investimento));
            }
        } catch (IOException e) {
            System.out.println("Ocorreu um erro: " + e);
        }
        
        return clientes;
    }
    
    public static ArrayList<Transacao> getTransacoes() throws FileNotFoundException{
        ArrayList<Transacao> transacoes = new ArrayList<>();
        BufferedReader readerClientes = new BufferedReader(new FileReader(caminhoTransacoes));
        String line;
        try {
            //ordem csv: data, tipo, origem, destino, quantia
            readerClientes.readLine(); // pula cabeçalho da tabela
            while((line = readerClientes.readLine()) != null){
                String[] valores = line.split(";");
                LocalDateTime data = LocalDateTime.parse(valores[0]);
                String tipo = valores[1];
                String origem = valores[2];
                String destino = valores[3].equals("null") ? null : valores[3];
                float quantia = Float.parseFloat(valores[4]);
                transacoes.add(new Transacao(data, tipo, origem, destino, quantia));
            }
        } catch (IOException e) {
            System.out.println("Ocorreu um erro: " + e);
        }
        
      
        return transacoes;
    }
    
    public static void atualizarContaCorrente(ArrayList<Cliente> C){
        String[] contasStr = new String[C.size()];
        String cabecalho = "cpf;saldo;conta";
        for(int i = 0; i < C.size(); i++){
            if(C.get(i).contaCorrente == null) continue;
            ContaCorrente cc = C.get(i).contaCorrente;
            contasStr[i] = String.format("%s;%s;%s", C.get(i).cpf, cc.saldo, cc.conta);
        }
         try (BufferedWriter w = new BufferedWriter(new FileWriter(caminhoContas))){
             w.write(cabecalho);
             w.newLine();
             
             for(String s: contasStr){
                 w.write(s);
                 w.newLine();
             }
         } catch (IOException e){
             System.out.println("Ocorreu um erro ao salvar: " + e);
         }
    }
    
    public static void atualizarContaInvestimento(ArrayList<Cliente> C){
        ArrayList<String> investimentosStr = new ArrayList<String>();
        String cabecalho = "cpf;tipo;valor;data";
        for(int i = 0; i < C.size(); i++){//ordem csv: cpf, tipo, valor, data
            if(C.get(i).contaInvestimento == null) continue;
            ContaInvestimento ci = C.get(i).contaInvestimento;
            for(Investimento inv: ci.investimentos){
                investimentosStr.set(i, String.format("%s;%s;%s;%s;%s", C.get(i).cpf, inv.tipoNum, inv.aporte, inv.data));
            }
        }
         try (BufferedWriter w = new BufferedWriter(new FileWriter(caminhoInvestimentos))){
             w.write(cabecalho);
             w.newLine();
             
             for(String s: investimentosStr){
                 w.write(s);
                 w.newLine();
             }
         } catch (IOException e){
             System.out.println("Ocorreu um erro ao salvar: " + e);
         }
    }
    
    public static void atualizarTransacoes(ArrayList<Transacao> t){
        String[] transacaoStr = new String[t.size()];
        String cabecalho = "data;tipo;origem;destino;quantia";
        for(int i = 0; i < t.size(); i++){
            Transacao tr = t.get(i);
            transacaoStr[i] = String.format("%s;%s;%s;%s;%s", tr.data, tr.tipo, tr.origem, tr.destino, tr.quantia);
        }
         try (BufferedWriter w = new BufferedWriter(new FileWriter(caminhoTransacoes))){
             w.write(cabecalho);
             w.newLine();
             
             for(String s: transacaoStr){
                 w.write(s);
                 w.newLine();
             }
         } catch (IOException e){
             System.out.println("Ocorreu um erro ao salvar: " + e);
         }
    }
    
    public static void atualizarClientes(ArrayList<Cliente> C){
        String[] clientesStr = new String[C.size()];
        String cabecalho = "cpf;username;senha;nome";
        for(int i = 0; i < C.size(); i++){
            if(C.get(i).contaCorrente == null) continue;
            Cliente c = C.get(i);
            clientesStr[i] = String.format("%s;%s;%s;%s;%s", c.cpf, c.username, c.senha, c.nome);
        }
         try (BufferedWriter w = new BufferedWriter(new FileWriter(caminhoClientes))){
             w.write(cabecalho);
             w.newLine();
             
             for(String s: clientesStr){
                 w.write(s);
                 w.newLine();
             }
         } catch (IOException e){
             System.out.println("Ocorreu um erro ao salvar: " + e);
         }
    }
    
    public static int getNumContas(){
        return numContas;
    }
}
