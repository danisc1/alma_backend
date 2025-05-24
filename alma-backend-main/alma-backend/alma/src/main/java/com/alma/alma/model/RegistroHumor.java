package com.alma.alma.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "registro_humor")
public class RegistroHumor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int avaliacao; // ex: 1 a 5

    private String anotacao;

    private LocalDateTime data;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    // getters e setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getAvaliacao() { return avaliacao; }
    public void setAvaliacao(int avaliacao) { this.avaliacao = avaliacao; }

    public String getAnotacao() { return anotacao; }
    public void setAnotacao(String anotacao) { this.anotacao = anotacao; }

    public LocalDateTime getData() { return data; }
    public void setData(LocalDateTime data) { this.data = data; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
