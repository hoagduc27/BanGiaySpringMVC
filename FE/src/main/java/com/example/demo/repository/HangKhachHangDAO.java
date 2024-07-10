package com.example.demo.repository;

import com.example.demo.entity.HangKhachHang;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface HangKhachHangDAO extends JpaRepository<HangKhachHang, UUID> {
    @Query("select p from HangKhachHang p where p.ma=?1")
    HangKhachHang getHangKhachHangByMa(String ma);

    @Query("SELECT MAX(CAST(SUBSTRING(hd.ma, 4, LENGTH(hd.ma) - 3) AS int)) FROM HangKhachHang hd")
    Integer findMaxMaHkhNumber();

    default String generateNextMaHkh() {
        Integer maxMaNumber = findMaxMaHkhNumber();
        int nextNumber;

        if (maxMaNumber != null) {
            nextNumber = maxMaNumber + 1;
        } else {
            nextNumber = 1;
        }
        return "HKH" + nextNumber;
    }
}
