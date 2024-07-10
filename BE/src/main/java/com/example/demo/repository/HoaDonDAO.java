package com.example.demo.repository;

import com.example.demo.entity.HoaDon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface HoaDonDAO extends JpaRepository<HoaDon, UUID> {

    // Query tìm hóa đơn theo mã
    @Query("select hd from HoaDon hd where hd.ma=?1")
    HoaDon findHoaDonByMa(String ma);

    // Query tìm hóa đơn với điều kiện hóa đơn chưa được áp mã và trạng thái phải là chưa thanh toán
    @Query("SELECT hd FROM HoaDon hd WHERE hd.trangthai = 0 AND hd.id NOT IN (SELECT ct.hd.id FROM GiamGiaChiTietHoaDon ct WHERE ct.hd.trangthai = 0)")
    List<HoaDon> findHoaDonChuaApDungChuongTrinhGiamGia();

    @Query("SELECT hd FROM HoaDon hd " +
            "WHERE (hd.trangthai = 0 OR hd.trangthai = 1) " +
            "AND hd.tong_tien >= (SELECT MIN(gghd.dieu_kien) FROM GiamGiaHoaDon gghd WHERE gghd.trangthai = 1) " +
            "AND hd.id NOT IN (SELECT ct.hd.id FROM GiamGiaChiTietHoaDon ct)")
    Page<HoaDon> findHoaDonChuaApDungChuongTrinhGiamGiaPage(Pageable pageable);

    // Query getall hóa đơn chờ xác nhận và là tại quầy
    @Query("SELECT hd FROM HoaDon hd WHERE hd.trangthai = 0 and hd.hinh_thuc_mua = 0 ORDER BY CAST(SUBSTRING(hd.ma, 3, LENGTH(hd.ma) - 2) AS int) DESC")
    Page<HoaDon> findHoaDonChoXacNhan(Pageable pageable);

    // Query getall hóa đơn theo trạng thái
    @Query("SELECT hd FROM HoaDon hd WHERE hd.trangthai = ?1 order by hd.ma desc ")
    Page<HoaDon> findHoaDonbyTrangThai(Integer trangthai, Pageable pageable);

    @Query("SELECT hd FROM HoaDon hd WHERE hd.trangthai = 0 and hd.hinh_thuc_mua = 0 " +
            "AND hd.ma LIKE %:keyword% ORDER BY CAST(SUBSTRING(hd.ma, 3, LENGTH(hd.ma) - 2) AS int) DESC")
    Page<HoaDon> searchHoaDonByKeyword(@Param("keyword") String keyword, Pageable pageable);


    @Query("SELECT hd FROM HoaDon hd LEFT JOIN hd.khachHang kh LEFT JOIN hd.nhanVien nv WHERE " +
            "((hd.ma LIKE %:keyword% and hd.ma like :timTheo ) " +
            "or (COALESCE(kh.hoten, '') LIKE %:keyword% and hd.khachHang.ma like :timTheo) " +
            "or (COALESCE(nv.hoTen, '') LIKE %:keyword% and hd.nhanVien.ma like :timTheo) " +
            "or (COALESCE(hd.tong_tien, '') LIKE %:keyword%)) " +
            "AND hd.trangthai = :trangThai")
    Page<HoaDon> searchHoaDon(@Param("keyword") String keyword, @Param("timTheo") String timtheo, @Param("trangThai") Integer trangThai, Pageable pageable);

}
