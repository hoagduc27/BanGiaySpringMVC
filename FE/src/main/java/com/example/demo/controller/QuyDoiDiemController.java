package com.example.demo.controller;

import com.example.demo.entity.NhanVien;
import com.example.demo.entity.QuyDoiDiem;
import com.example.demo.repository.NhanVienDAO;
import com.example.demo.repository.QuyDoiDiemDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin/quydoidiem")
public class QuyDoiDiemController {

    @Autowired
    private QuyDoiDiemDAO quyDoiDiemDAO;
    @Autowired
    private NhanVienDAO nhanVienDAO;


    private Authentication authentication;

    @ModelAttribute("nhanVienLogin")
    public NhanVien nhanVienLogin() {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        NhanVien nv = nhanVienDAO.getNVByEmail(authentication.getName());
        return nv;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("list", quyDoiDiemDAO.findAll());
        QuyDoiDiem quyDoiDiem = new QuyDoiDiem();
        quyDoiDiem.setTrangthai(1);
        model.addAttribute("quyDoiDiem", quyDoiDiem);
        return "quydoidiem/index";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute QuyDoiDiem quyDoiDiem) {
        switch (quyDoiDiem.getTrangthai()) {
            case 0:
                quyDoiDiemDAO.save(quyDoiDiem);
                break;
            case 1: {
                List<QuyDoiDiem> list = quyDoiDiemDAO.findAll();
                list.stream().forEach(item -> {
                    item.setTrangthai(0);
                    quyDoiDiemDAO.save(item);
                });
                quyDoiDiem.setTrangthai(1);
                quyDoiDiemDAO.save(quyDoiDiem);
                break;
            }
        }
        return "redirect:/admin/quydoidiem";
    }

    @GetMapping("/update/{id}")
    public String updateView(@PathVariable String id, Model model) {
        model.addAttribute("quyDoiDiem", quyDoiDiemDAO.findById(UUID.fromString(id)));
        return "quydoidiem/update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute QuyDoiDiem quyDoiDiem) {
        switch (quyDoiDiem.getTrangthai()) {
            case 0:
                quyDoiDiemDAO.save(quyDoiDiem);
                break;
            case 1: {
                List<QuyDoiDiem> list = quyDoiDiemDAO.findAll();
                list.stream().forEach(item -> {
                    item.setTrangthai(0);
                    quyDoiDiemDAO.save(item);
                });
                quyDoiDiem.setTrangthai(1);
                quyDoiDiemDAO.save(quyDoiDiem);
                break;
            }
        }
        return "redirect:/admin/quydoidiem";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        quyDoiDiemDAO.deleteById(UUID.fromString(id));
        return "redirect:/admin/quydoidiem";
    }

}
