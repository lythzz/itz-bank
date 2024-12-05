/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itz.bank;

import com.itz.bank.Database;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.time.LocalDateTime;

/**
 *
 * @author lucas
 */
public class Banco {

    String nome = "ITZ Bank";
    String cnpj = "73.353.925/0001-90";
    int numContas;
    Scanner s = new Scanner(System.in);
    ArrayList<Cliente> clientes;
    ArrayList<Transacao> transacoes = new ArrayList();

    public void adicionarCliente(Cliente c) {
        clientes.add(c);
        Database.atualizarClientes(this.clientes);
    }

    public Banco() {
        try {
            this.clientes = Database.getClientes();
            this.numContas = Database.getNumContas();
            this.transacoes = Database.getTransacoes();
        } catch (FileNotFoundException e) {
            System.out.println("Ocorreu um erro: " + e);
        }
    }

    public void registrarTransacao(Transacao t) {
        this.transacoes.add(t);
        Database.atualizarTransacoes(this.transacoes);
    }

    public ArrayList<Transacao> getTransacoes() {
        return this.transacoes;
    }
    
    public void salvar(int n){
        switch(n){
            case 0:
                Database.atualizarContaCorrente(this.clientes);
                break;
            case 1:
                Database.atualizarContaInvestimento(this.clientes);
                break;
            case 2:
                Database.atualizarClientes(this.clientes);
                break;
        }
    }
    
    public Cliente login(Scanner s, ArrayList<Cliente> clientes) {
        Cliente usuario = null;
        boolean credenciaisInvalidas = false;
        while (true) {
            Utils.clear();

            if (usuario != null) {
                break;
            }
            if (credenciaisInvalidas) {
                System.out.println("Username ou senha incorretos. Tente novamente.");
            }

            System.out.println("Login");
            System.out.print("Username: ");
            String username = s.next();
            System.out.print("Senha: ");
            String senha = s.next();
            for (Cliente cliente : clientes) {
                if (cliente.username.equals(username) && cliente.senha.equals(senha)) {
                    usuario = cliente;
                    break;
                }
                credenciaisInvalidas = true;
            }

        }
        return usuario;
    }

    public Cliente registrar(Scanner s, Banco b) {
        ArrayList<String> listaCpfs = new ArrayList<>();
        ArrayList<String> listaUsernames = new ArrayList<>();
        for (Cliente c : b.clientes) {
            listaCpfs.add(c.cpf);
            listaUsernames.add(c.username.toLowerCase());
        }

        Utils.clear();
        System.out.println("Registro");
        System.out.print("Nome: ");
        String nome = s.next();
        String cpf;
        String username;
        while (true) {
            System.out.print("CPF (apenas numeros): ");
            cpf = s.next();
            if (cpf.matches("\\d{11}") && !listaCpfs.contains(cpf)) {
                break;
            }
            if (listaCpfs.contains(cpf)) {
                System.out.println("Ja existe uma conta cadastrada com esse CPF.");
            } else {
                System.out.println("CPF invalido");
            }
        }
        while (true) {
            System.out.print("Username: ");
            username = s.next();
            if (!listaUsernames.contains(username.toLowerCase())) {
                break;
            }
            System.out.println("Esse username ja esta em uso.");
        }
        System.out.print("Senha: ");
        String senha = s.next();
        Cliente novoCliente = new Cliente(cpf, nome, username, senha, null, null);
        b.adicionarCliente(novoCliente);
        return b.clientes.get(b.clientes.size() - 1);
    }

    public Cliente authMenu(Banco b) {
        Scanner s = b.s;
        while (true) {
            Utils.clear();
            System.out.println("1. Fazer login");
            System.out.println("2. Criar conta");
            System.out.println("3. Sair");
            System.out.print("Escolha uma opcao: ");

            String option = s.next();
            switch (option) {
                case "1":
                    Cliente u = login(s, b.clientes);
                    return u;
                case "2":
                    Cliente user = registrar(s, b);
                    b.adicionarCliente(user);
                    return user;
                case "3":
                    System.exit(0);
                    break;
                default:
                    break;
            }
        }
    }

    public void menuContaCorrente(String tipoConta, Cliente u, Banco b) {
        Scanner s = b.s;
        String operacao = null;
        while (true) {
            if (tipoConta.equals("")) {
                break;
            }
            Utils.clear();
            if (operacao == null) {
                System.out.println("Ola " + u.nome);
                System.out.println("Agencia/Conta: " + u.contaCorrente.AGENCIA + "/" + u.contaCorrente.conta);
                System.out.println("Saldo R$ " + u.contaCorrente.saldo);
                System.out.println("O que voce deseja fazer?");
                System.out.println("1. Ver extrato");
                System.out.println("2. Realizar deposito");
                System.out.println("3. Realizar saque");
                System.out.println("4. Realizar transferencia");
                System.out.println("5. Voltar");
                System.out.print("Escolha uma opcao: ");
                operacao = s.next();
            }

            switch (operacao) {
                case "1":
                    String periodo = "";
                    String tipoTransacao = "";
                    boolean periodoValido = false;
                    boolean tipoValido = false;

                    while (true) {
                        if (!(periodo.equals("1") || periodo.equals("2") || periodo.equals("3") || periodo.equals("4"))) {
                            Utils.clear();
                            System.out.println("Qual o periodo?");
                            System.out.println("1. Ultimas 24h");
                            System.out.println("2. Ultimos 7 dias");
                            System.out.println("3. Ultimos 30 dias");
                            System.out.println("4. Todo o periodo");
                            System.out.print("Escolha uma opcao: ");
                            periodo = s.next();
                        } else {
                            periodoValido = true;
                        }

                        if (periodoValido && !(tipoTransacao.equals("1") || tipoTransacao.equals("2") || tipoTransacao.equals("3") || tipoTransacao.equals("4") || tipoTransacao.equals("5"))) {
                            Utils.clear();
                            System.out.println("Qual o tipo de transacao?");
                            System.out.println("1. Deposito");
                            System.out.println("2. Saque");
                            System.out.println("3. Transferencia");
                            System.out.println("4. Resgate Investimento/Aportes");
                            System.out.println("5. Todos");
                            System.out.print("Escolha uma opcao: ");
                            tipoTransacao = s.next();
                        } else {
                            tipoValido = true;
                        }

                        if (periodoValido && tipoValido) {
                            s.nextLine();
                            u.contaCorrente.verExtrato(b, periodo, tipoTransacao);
                            operacao = null;
                            break;
                        }
                    }
                    break;
                case "2":
                    Utils.clear();
                    System.out.print("Qual o valor a ser depositado? R$ ");
                    while (!s.hasNextFloat()) {
                        System.out.println("Valor invalido, tente novamente.");
                        s.next();
                    }
                    float valor = s.nextFloat();
                    s.nextLine();
                    operacao = null;
                    u.contaCorrente.depositar(b, valor);
                    break;
                case "3":
                    Utils.clear();
                    System.out.print("Qual o valor a ser sacado? R$ ");
                    while (!s.hasNextFloat()) {
                        System.out.println("Valor invalido, tente novamente.");
                        s.next();
                    }
                    valor = s.nextFloat();
                    s.nextLine();
                    u.contaCorrente.sacar(b, valor);
                    operacao = null;
                    break;
                case "4":
                    Utils.clear();
                    System.out.print("Qual o valor a ser transferido? R$ ");
                    while (!s.hasNextFloat()) {
                        System.out.println("Valor invalido, tente novamente.");
                        s.next();
                    }
                    valor = s.nextFloat();
                    s.nextLine();
                    Map<String, Cliente> contas = new HashMap<>();

                    for (Cliente c : this.clientes) {
                        if (c.contaCorrente == null) {
                            continue;
                        }
                        contas.put(c.contaCorrente.conta, c);
                    }
                    String contaDestino = "";
                    while (true) {
                        System.out.print("Qual a conta de destino? ");
                        contaDestino = s.nextLine();
                        if (contas.get(contaDestino) == null) {
                            System.out.println("Conta não encontrada.");
                            continue;
                        }
                        String nomeDestinatario = contas.get(contaDestino).nome;
                        System.out.printf("O destinatario e %s ? %n", nomeDestinatario);
                        System.out.println("1. Sim");
                        System.out.println("2. Nao");
                        System.out.print("Escolha uma opcao: ");
                        String opcao = s.nextLine();
                        if (opcao.equals("1")) {
                            break;
                        }
                    }
                    u.contaCorrente.transferir(b, valor, contaDestino);
                    Cliente destinatario = contas.get(contaDestino);
                    destinatario.contaCorrente.saldo += valor;
                    operacao = null;
                    break;
                case "5":
                    tipoConta = "";
                    return;
            }
        }
    }
    
    public void menuContaInvestimento(String tipoConta, Cliente u, Banco b){
        Scanner s = b.s;
        String operacao = null;
        ArrayList<Investimento> invs = u.contaInvestimento.investimentos;
        while (true) {
            if (tipoConta.equals("")) {
                break;
            }
            Utils.clear();
            if (operacao == null) {
                System.out.println("Ola " + u.nome);
                System.out.println("Saldo C. Corrente R$ " + u.contaCorrente.saldo);
                System.out.println("O que voce deseja fazer?");
                System.out.println("1. Realizar aporte");
                System.out.println("2. Ver investimentos");
                System.out.println("3. Voltar");
                System.out.print("Escolha uma opcao: ");
                operacao = s.next();
            }

            switch (operacao) {
                case "1":
                    float quantia = 0;
                    String tipoInvestimento = "";
                    boolean valorValido = false;
                    boolean tipoValido = false;

                    while (true) {
                        if (!(tipoInvestimento.equals("1") || tipoInvestimento.equals("2") || tipoInvestimento.equals("3"))) {
                            Utils.clear();
                            System.out.println("Qual o tipo de investimento?");
                            System.out.println("1. LCI | Liquidez: 275 dias | Rentabilidade: 0,83%");
                            System.out.println("2. LCA | Liquidez: 180 dias | Rentabilidade: 0,66%");
                            System.out.println("3. CDB | Liquidez: diaria | Rentabilidade: 0,90%");
                            System.out.print("Escolha uma opcao: ");
                            tipoInvestimento = s.next();
                        } else {
                            tipoValido = true;
                        }

                        if (tipoValido && quantia <= 0) {
                            Utils.clear();
                            System.out.print("Quanto vc deseja aportar? R$ ");
                            if(s.hasNextFloat()) quantia = s.nextFloat();
                        } else {
                            valorValido = true;
                        }

                        if (valorValido && tipoValido) {
                            s.nextLine();
                            u.contaInvestimento.aportar(b, u, tipoInvestimento, quantia);
                            operacao = null;
                            break;
                        }
                    }
                    break;
                case "2":
                    u.contaInvestimento.verInvestimentos(b, u);
                    operacao = null;
                    break;
                case "3":
                    tipoConta = "";
                    return;
            }
        }
    }
    
    public void menuPrincipal(Banco b, Cliente u) {
        String tipoConta = "";
        boolean deveExecutar = true;
        while (deveExecutar) {
            Scanner s = b.s;
            switch (tipoConta) {
                case "":
                    Utils.clear();
                    System.out.println("Ola " + u.nome);
                    System.out.println("Voce deseja acessar qual conta? ");
                    System.out.println("1. Conta corrente");
                    System.out.println("2. Conta de investimentos");
                    System.out.println("3. Sair");
                    System.out.print("Selecione uma opcao: ");
                    tipoConta = s.next();
                    break;
                case "1":
                    Utils.clear();
                    System.out.println("Ola " + u.nome);
                    if (u.contaCorrente == null) {
                        System.out.println("Atualmenta voce nao possui uma conta corrente.");
                        System.out.println("Voce deseja iniciar uma conta corrente?");
                        System.out.println("1. Sim");
                        System.out.println("2. Nao (voltar ao menu)");
                        System.out.print("Selecione uma opcao: ");
                        String deveCriarConta = s.next();
                        if (deveCriarConta.equals("1")) {
                            String numeroConta = String.format("%04d", b.numContas + 1);
                            u.contaCorrente = new ContaCorrente(0.00f, numeroConta);
                            b.salvar(0);
                        }

                        if (deveCriarConta.equals("2")) {
                            tipoConta = "";
                            break;
                        }
                    }

                    menuContaCorrente(tipoConta, u, b);
                    tipoConta = "";
                    break;
                case "2":
                    Utils.clear();
                    System.out.println("Ola " + u.nome);
                    if (u.contaInvestimento == null) {
                        System.out.println("Atualmenta voce nao possui uma conta de investimentos.");
                        System.out.println("Voce deseja iniciar uma conta de investimentos?");
                        System.out.println("1. Sim");
                        System.out.println("2. Nao (voltar ao menu)");
                        System.out.print("Selecione uma opcao: ");
                        String deveCriarConta = s.next();
                        if (deveCriarConta.equals("1")) {
                            if(u.contaCorrente == null){
                                System.out.println("Voce nao pode criar uma conta de investimentos sem possuir uma conta corrente.");
                            } else {
                                String numeroConta = u.contaCorrente.conta;
                                u.contaInvestimento = new ContaInvestimento(numeroConta, new ArrayList<Investimento>());
                                b.salvar(1);
                            } 
                        }

                        if (deveCriarConta.equals("2")) {
                            tipoConta = "";
                            break;
                        }
                    }

                    menuContaInvestimento(tipoConta, u, b);
                    tipoConta = "";
                    break;
                case "3":
                    deveExecutar = false;
                    break;
            }

        }
    }

    public static void main(String[] args) {
        
        Banco b = new Banco();
        Cliente usuario = null;
        
        while (true) {
            usuario = b.authMenu(b);
            b.menuPrincipal(b, usuario);
        }
    }
}
