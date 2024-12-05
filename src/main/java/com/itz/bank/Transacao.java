/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itz.bank;
import java.time.LocalDateTime;
/**
 *
 * @author lucas
 */
public class Transacao {
    LocalDateTime data;
    String tipo;
    String origem;
    String destino;
    float quantia;
    
    public Transacao(LocalDateTime data, String tipo, String origem, String destino, float quantia){
        this.data = data;
        this.tipo = tipo;
        this.origem = origem;
        this.destino = destino;
        this.quantia = quantia;
    }
}
