package com.example.demo.repository;

import com.example.demo.entity.ChuongTrinhGiamGiaChiTietSP;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Repository
public class ChuongTrinhGiamGiaChiTietSPRepo {

    RestTemplate restTemplate = new RestTemplate();

    String url = "http://localhost:2020/rest/chuong-trinh-giam-gia-chi-tiet-sp/hien-thi";
    String url2 = "http://localhost:2020/rest/chuong-trinh-giam-gia-chi-tiet-sp/detail-sp-by";
    String url1 = "http://localhost:2020/rest/chuong-trinh-giam-gia-chi-tiet-sp";

    private String getUrl(String ma) {
        return url + "/" + ma;
    }

    private String getUrl1(String ma) {
        return url1 + "/" + ma;
    }

    private String getUrlId(UUID idVoucher) {
        return url2 + "/" + idVoucher;
    }

    public List<ChuongTrinhGiamGiaChiTietSP> getListCtkm() {
        ResponseEntity<List<ChuongTrinhGiamGiaChiTietSP>> response =
                restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<ChuongTrinhGiamGiaChiTietSP>>() {
                });

        return response.getBody();
    }

    public ChuongTrinhGiamGiaChiTietSP getCTKMById(UUID idVoucher) {
        return restTemplate.getForObject(getUrlId(idVoucher), ChuongTrinhGiamGiaChiTietSP.class);
    }

    public List<ChuongTrinhGiamGiaChiTietSP> getListByGGSPid(UUID idggsp) {
        ResponseEntity<List<ChuongTrinhGiamGiaChiTietSP>> response =
                restTemplate.exchange(getUrl1("tim-ggctsp-by-ggsp/" + idggsp), HttpMethod.GET, null, new ParameterizedTypeReference<List<ChuongTrinhGiamGiaChiTietSP>>() {
                });

        return response.getBody();
    }

}
