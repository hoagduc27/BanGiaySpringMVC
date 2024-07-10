package com.example.demo.schedule;

import com.example.demo.entity.ChuongTrinhGiamGiaChiTietSP;
import com.example.demo.entity.ChuongTrinhGiamGiaSP;
import com.example.demo.entity.GiamGiaHoaDon;
import com.example.demo.entity.Giay;
import com.example.demo.repository.ChuongTrinhGiamGiaChiTietSanPhamRepo;
import com.example.demo.repository.ChuongTrinhGiamGiaSPRepository;
import com.example.demo.repository.GiayDAO;
import com.example.demo.repository.GiayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GiamGiaSpSchedule {
    @Autowired
    ChuongTrinhGiamGiaSPRepository ggspRepo;

    @Autowired
    ChuongTrinhGiamGiaChiTietSanPhamRepo ggctsprepo;

    @Autowired
    GiayDAO giayDAO;

    @Scheduled(fixedRate = 5000)
    public void updateExpiredGiamGiaHoaDon() {
        Date currentDate = new Date();
        List<ChuongTrinhGiamGiaSP> updateTT = ggspRepo
                .findAll()
                .stream()
                .filter(ggsp -> ggsp.getNgayKetThuc().before(currentDate))
                .collect(Collectors.toList());

        updateTT.forEach(ggsp -> {
            List<ChuongTrinhGiamGiaChiTietSP> chiTietCTSPList = ggctsprepo.findAllByChuongTrinhGiamGiaSPId(ggsp.getIdKhuyenMai());
            chiTietCTSPList.forEach(ctggsp ->
            {
                Giay giay = ctggsp.getGiay();
                giay.setGia_sau_khuyen_mai(giay.getGiaban());
                giayDAO.save(giay);
            });
            ggsp.setTrangThai(0);
        });

        ggspRepo.saveAll(updateTT);
    }
}
