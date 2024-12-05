/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itz.bank;

/**
 *
 * @author lucas
 */
public class Cliente {
    String cpf;
    String nome;
    String username;
    String senha;
    
    ContaCorrente contaCorrente;
    ContaInvestimento contaInvestimento;
    
    public Cliente(String cpf, String nome, String username, String senha, ContaCorrente corrente, ContaInvestimento investimento){
        this.cpf = cpf;
        this.nome = nome;
        this.username = username;
        this.senha = senha;
        this.contaCorrente = corrente;
        this.contaInvestimento = investimento;
    }
}