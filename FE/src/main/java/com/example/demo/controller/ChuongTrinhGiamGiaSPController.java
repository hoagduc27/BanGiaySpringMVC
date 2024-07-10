package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.repository.GiayDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class ChuongTrinhGiamGiaSPController {

    @Autowired
    ChuongTrinhGiamGiaSPRepo repo;

    @Autowired
    GiayRepo giayRepo;

    @Autowired
    ChuongTrinhGiamGiaChiTietSPRepo ctkmRepo;

    @Autowired
    ChuongTrinhGiamGiaChitietSanPhamDTO ctkmDTO;

    @Autowired
    GiayDTO giayDTO;

    @Autowired
    CTrinhKMSPDTO kmDTO;

    ///////////         CHUONG TRINH GIAM GIA SAN PHAM          ///////////////

    @RequestMapping("/admin/chuong-trinh-giam-gia-sp")
    public String voucher(@ModelAttribute("voucherForm") ChuongTrinhGiamGiaSP chuongTrinhGiamGiaSP,
                          @RequestParam("page") Optional<Integer> pageNumber, Model model,
                          @RequestParam("keyword") Optional<String> keyword) {
        PageDTO<ChuongTrinhGiamGiaSP> pageNo = repo.searchTrangThaiAndKeyword(pageNumber.orElse(0), "%" + keyword.orElse("") + "%");
        model.addAttribute("i", 0);
        model.addAttribute("voucherTable", pageNo);
        model.addAttribute("keyword", keyword.orElse(""));
        return "giamgiasanpham/chuong_trinh_giam_gia_san_pham";
    }

    @RequestMapping("/admin/chuong-trinh-giam-gia-sp/tim-theo-trang-thai")
    public String voucherbyTT(@ModelAttribute("voucherForm") ChuongTrinhGiamGiaSP chuongTrinhGiamGiaSP,
                              @RequestParam("page") Optional<Integer> pageNumber, Model model,
                              @RequestParam("trangThai") Integer trangThai
    ) {
        PageDTO<ChuongTrinhGiamGiaSP> pageNo = repo.searchTrangThai(pageNumber.orElse(0), trangThai);
        model.addAttribute("i", 0);
        model.addAttribute("voucherTable", pageNo);
        model.addAttribute("trangThaiDetail", trangThai);
        return "giamgiasanpham/chuong_trinh_giam_gia_san_pham";
    }

    @ModelAttribute("voucherTable")
    public List<ChuongTrinhGiamGiaSP> getListVoucher() {
        return repo.getListVoucher();
    }

    @PostMapping("/admin/chuong-trinh-giam-gia-sp/create")
    public String createVoucher(@ModelAttribute("voucherForm") ChuongTrinhGiamGiaSP voucherForm, Model model) {
        voucherForm.setMaKhuyenMai(kmDTO.generateNextMaKM());
        model.addAttribute("valMaKM", kmDTO.generateNextMaKM());
        repo.saveVoucher(voucherForm);
        return "redirect:/admin/chuong-trinh-giam-gia-sp";
    }

    @GetMapping("/admin/chuong-trinh-giam-gia-sp/viewupdate/{ma}")
    public String viewUpdate(@ModelAttribute("voucherForm") ChuongTrinhGiamGiaSP chuongTrinhGiamGiaSP,
                             @PathVariable("ma") String ma, Model model) {
        model.addAttribute("voucherForm", kmDTO.getOneByMa(ma));
        return "giamgiasanpham/update-khuyen-mai-san-pham";
    }

    @GetMapping("/admin/chuong-trinh-giam-gia-sp/viewadd")
    public String viewAdd(@ModelAttribute("voucherForm") ChuongTrinhGiamGiaSP chuongTrinhGiamGiaSP, Model model) {
        model.addAttribute("valMaKM", kmDTO.generateNextMaKM());
        return "giamgiasanpham/add-khuyen-mai-san-pham";
    }

    // update Giảm Giá Sản phẩm
    @PostMapping("/admin/chuong-trinh-giam-gia-sp/update")
    public String updateGGSP(@ModelAttribute("giamgiaSP") ChuongTrinhGiamGiaSP giamGiaSP) {
        repo.saveVoucher(giamGiaSP);
        if (giamGiaSP.getTrangThai() == 1) {
            List<ChuongTrinhGiamGiaChiTietSP> ggctsp = ctkmRepo.getListByGGSPid(giamGiaSP.getIdKhuyenMai());
            ggctsp.forEach(x ->
            {
                Giay giay = x.getGiay();
                BigDecimal giaBan = giay.getGiaban();
                Integer phanTramGiam = giamGiaSP.getPhanTramGiam();
                BigDecimal soTienGiam = giaBan.multiply(new BigDecimal(phanTramGiam)).divide(new BigDecimal(100));
                giay.setGia_sau_khuyen_mai(giaBan.subtract(soTienGiam));
                giayDTO.save(giay);
            });
        }
        else if(giamGiaSP.getTrangThai() == 0)
        {
            List<ChuongTrinhGiamGiaChiTietSP> ggctsp = ctkmRepo.getListByGGSPid(giamGiaSP.getIdKhuyenMai());
            ggctsp.forEach(x ->
            {
                Giay giay = x.getGiay();
                giay.setGia_sau_khuyen_mai(giay.getGiaban());
                giayDTO.save(giay);
                ChuongTrinhGiamGiaChiTietSP delCTKM = ctkmDTO.selectByTwoId(giamGiaSP.getIdKhuyenMai(), x.getGiay().getId());
                ctkmDTO.deleteById(delCTKM.getId());
            });
        }
        return "redirect:/admin/chuong-trinh-giam-gia-sp";
    }

    @PostMapping("/admin/chuong-trinh-giam-gia-sp/add")
    public String add(@ModelAttribute("voucherForm") ChuongTrinhGiamGiaSP voucherForm) {
        repo.saveVoucher(voucherForm);
        return "redirect:/admin/chuong-trinh-giam-gia-sp";
    }

    //////////      chi-tiet-khuyen-mai     ////

    @RequestMapping("/admin/chuong-trinh-giam-gia-sp/detail/{maVoucher}")
    public String detailVoucher(@ModelAttribute("ctkmForm") ChuongTrinhGiamGiaChiTietSP ctggctsp,
                                @PathVariable("maVoucher") String maKM, Model model) {
        ChuongTrinhGiamGiaSP getKM = kmDTO.getOneByMa(maKM);
        model.addAttribute("voucherForm", getKM);
        model.addAttribute("giayCheckBox", giayRepo.getListGiay());     //ListAllGiay
        List<ChuongTrinhGiamGiaChiTietSP> dsIdSanPham = ctkmDTO.listGiayByIdKM(getKM.getIdKhuyenMai());
        model.addAttribute("dsSPTable", dsIdSanPham);
        model.addAttribute("listGiay", giayDTO.listGiayChuaApDung());

        return "giamgiasanpham/chi_tiet_khuyen_mai";
    }

    @GetMapping("/admin/ctkm/delete/{idKhuyenMai}/{idGiay}")
    public String deleteby2Id(@PathVariable("idKhuyenMai") UUID idKM,
                              @PathVariable("idGiay") UUID idGiay,
                              Model model) {
        String maKM = repo.getOneById(idKM).getMaKhuyenMai();
        ChuongTrinhGiamGiaChiTietSP delCTKM = ctkmDTO.selectByTwoId(idKM, idGiay);
        ctkmDTO.deleteById(delCTKM.getId());
        List<ChuongTrinhGiamGiaChiTietSP> dsIdSanPham = ctkmDTO.listGiayByIdKM(idKM);
        model.addAttribute("dsSPTable", dsIdSanPham);
        Giay giay = giayDTO.getOneById(idGiay);
        giay.setGia_sau_khuyen_mai(giay.getGiaban());
        giayDTO.save(giay);

        return "redirect:/admin/chuong-trinh-giam-gia-sp/detail/" + maKM;
    }

    ////////////////////////////////////////////

    @ModelAttribute("giayCheckBox")
    public List<Giay> getListGiay() {
        return giayRepo.getListGiay();
    }

    @ModelAttribute("ctkmDetail")
    public List<ChuongTrinhGiamGiaChiTietSP> getListCtkm() {
        return ctkmRepo.getListCtkm();
    }

    @GetMapping("/view-ctkm")
    public String viewKhuyenMai(Model model) {
        model.addAttribute("ctkmTable", ctkmRepo.getListCtkm());
        //return "giamgiasanpham/chuong_tring_giam_gia_san_pham";
        return "giamgiasanpham/chi_tiet_khuyen_mai";
    }

    @Autowired
    NhanVienDAO nhanVienDAO;
    private Authentication authentication;
    @ModelAttribute("nhanVienLogin")
    public NhanVien nhanVienLogin() {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        NhanVien nv=nhanVienDAO.getNVByEmail(authentication.getName());
        return nv;
    }
}