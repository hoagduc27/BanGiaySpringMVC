package com.example.demo.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Table(name = "xuat_xu")
@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class XuatXu implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String ma;
    private String ten;
    private Integer trangthai;
}
