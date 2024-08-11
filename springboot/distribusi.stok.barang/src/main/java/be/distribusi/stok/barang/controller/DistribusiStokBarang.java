package be.distribusi.stok.barang.controller;

import be.distribusi.stok.barang.dto.add.ReqAddDTO;
import be.distribusi.stok.barang.dto.insert.ReqInsertDTO;
import be.distribusi.stok.barang.dto.order.ReqOrderDTO;
import be.distribusi.stok.barang.service.DistribusiBarangService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/distribusi")
@RequiredArgsConstructor
public class DistribusiStokBarang {

    private final DistribusiBarangService distribusiBarangService;

    @Validated
    @PostMapping("/insertBarang")
    public ResponseEntity<?> insertBarangController(@RequestBody @Valid ReqInsertDTO requestBody) {
        return distribusiBarangService.insertBarangService(requestBody);
    }

    @PostMapping("/addStokBarang")
    public  ResponseEntity<?> addBarangController(@RequestBody @Valid ReqAddDTO requestBody){
        return distribusiBarangService.addBarangService(requestBody);
    }

    @PostMapping("/orderBarang")
    public  ResponseEntity<?> orderBarangController(@RequestBody @Valid ReqOrderDTO requestBody){
        return distribusiBarangService.orderBarangService(requestBody);
    }
}
