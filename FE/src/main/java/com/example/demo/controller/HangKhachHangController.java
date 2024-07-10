package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.repository.HangKhachHangDAO;
import com.example.demo.repository.KhachHangDao;
import com.example.demo.repository.NhanVienDAO;
import com.example.demo.repository.QuyDoiDiemDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin/hkh")
public class HangKhachHangController {

    @Autowired
    private HangKhachHangDAO hangKhachHangDAO;
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
        model.addAttribute("list", hangKhachHangDAO.findAll());
        HangKhachHang hkh = new HangKhachHang();
        hkh.setTrangthai(1);
        model.addAttribute("hkh", hkh);
        return "hkh/index";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute HangKhachHang hangKhachHang) {
        hangKhachHang.setMa(hangKhachHangDAO.generateNextMaHkh());
        hangKhachHangDAO.save(hangKhachHang);
        return "redirect:/admin/hkh";
    }

    @GetMapping("/update/{id}")
    public String updateView(@PathVariable String id, Model model) {
        model.addAttribute("hkh", hangKhachHangDAO.findById(UUID.fromString(id)));
        return "hkh/update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute HangKhachHang hkh) {
        hangKhachHangDAO.save(hkh);
        return "redirect:/admin/hkh";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        hangKhachHangDAO.deleteById(UUID.fromString(id));
        return "redirect:/admin/hkh";
    }

    @Autowired
    KhachHangDao khachHangDao;

    @GetMapping("/kh-by-hkh/{ma}")
    public String khByHkh(@PathVariable String ma, Model model, @RequestParam(defaultValue = "0") String number) {
        Pageable pageable= PageRequest.of(Integer.valueOf(number),15);
        Page<KhachHang> page=khachHangDao.khInHkh(ma,pageable);
        PageDTO<KhachHang> pageDTO=new PageDTO<>(page);
        model.addAttribute("page",pageDTO);
        model.addAttribute("hkh",hangKhachHangDAO.getHangKhachHangByMa(ma));
        return "hkh/khach_hang";
    }

}
