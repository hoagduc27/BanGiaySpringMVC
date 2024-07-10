package com.example.demo.repository;

import com.example.demo.entity.GiamGiaHoaDon;
import com.example.demo.entity.GiayChiTiet;
import com.example.demo.entity.PageDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Repository
public class GiayChiTietRepo {

    RestTemplate restTemplate = new RestTemplate();
    String url = "http://localhost:2020/rest/giaychitiet";

    //url theo id
    private String getUrl(UUID id) {
        return url + "/" + id;
    }

    //url + string
    private String getUrl1(String endpoint) {
        return url + "/" + endpoint;
    }

    // get all giày chi tiết
    public List<GiayChiTiet> getAll() {
        ResponseEntity<List<GiayChiTiet>> response =
                restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<GiayChiTiet>>() {
                });

        if (response.getBody() != null) {
            return response.getBody();
        } else {
            return Collections.emptyList();
        }
    }


    // phân trang
    public PageDTO<GiayChiTiet> getPageGiayCT(Integer page) {
        ResponseEntity<PageDTO<GiayChiTiet>> response = restTemplate.exchange(
                url + "/phantrang?page=" + page,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageDTO<GiayChiTiet>>() {
                }
        );
        return response.getBody();
    }

    // findGCT by id
    public GiayChiTiet getGiayChiTietById(UUID id) {
        return restTemplate.getForObject(getUrl(id), GiayChiTiet.class);
    }

}
