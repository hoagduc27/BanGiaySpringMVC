package com.example.demo.repository;

import com.example.demo.entity.ChuongTrinhGiamGiaChiTietSP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChuongTrinhGiamGiaChiTietSanPhamRepo extends JpaRepository<ChuongTrinhGiamGiaChiTietSP, UUID> {
    @Query(value = "select * from chuong_trinh_giam_gia_chi_tiet_san_pham ctkm join giay g on ctkm.id_giay = g.id \n" +
            "where id_chuong_trinh_giam_gia =?", nativeQuery = true)
    List<ChuongTrinhGiamGiaChiTietSP> findAllSPApdung(UUID id_chuong_trinh_giam_gia);

    @Query("SELECT ct FROM ChuongTrinhGiamGiaChiTietSP ct WHERE ct.chuongTrinhGiamGiaSP.idKhuyenMai = :chuongTrinhGiamGiaSPId")
    List<ChuongTrinhGiamGiaChiTietSP> findAllByChuongTrinhGiamGiaSPId(UUID chuongTrinhGiamGiaSPId);

}
