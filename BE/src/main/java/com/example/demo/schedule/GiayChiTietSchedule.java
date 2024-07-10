package com.example.demo.schedule;

import com.example.demo.entity.GiayChiTiet;
import com.example.demo.repository.GiayChiTietDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GiayChiTietSchedule {

    @Autowired
    private GiayChiTietDAO giayChiTietDAO;

    @Scheduled(fixedRate = 5000)
    public void kiemTraVaCapNhatTrangThai() {
        List<GiayChiTiet> danhSachGiayChiTiet = giayChiTietDAO.findAll();

        for (GiayChiTiet giayChiTiet : danhSachGiayChiTiet) {
            if (giayChiTiet.getSo_luong_ton() != null && giayChiTiet.getSo_luong_ton() == 0) {
                giayChiTiet.setTrangthai(0);
                giayChiTietDAO.save(giayChiTiet);
            }
        }
    }
}