package be.distribusi.stok.barang.controller;

import be.distribusi.stok.barang.dto.insert.ReqInsertDTO;
import be.distribusi.stok.barang.service.DistribusiBarangService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/distribusi")
@RequiredArgsConstructor
public class DistribusiStokBarang {

    private final DistribusiBarangService distribusiBarangService;

    @PostMapping("/insert/barang")
    public ResponseEntity<?> insertBarangController(@RequestBody ReqInsertDTO requestBody) {
        return distribusiBarangService.insertBarangService(requestBody);
    }
}
