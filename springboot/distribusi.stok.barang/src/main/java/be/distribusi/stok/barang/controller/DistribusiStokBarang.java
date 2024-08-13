package be.distribusi.stok.barang.controller;

import be.distribusi.stok.barang.dto.add.ReqAddDTO;
import be.distribusi.stok.barang.dto.delete.ReqDeleteDTO;
import be.distribusi.stok.barang.dto.insert.ReqInsertDTO;
import be.distribusi.stok.barang.dto.order.ReqOrderDTO;
import be.distribusi.stok.barang.dto.select.ReqSelectDTO;
import be.distribusi.stok.barang.service.add.AddService;
import be.distribusi.stok.barang.service.delete.DeleteService;
import be.distribusi.stok.barang.service.insert.InsertService;
import be.distribusi.stok.barang.service.order.OrderService;
import be.distribusi.stok.barang.service.select.SelectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/distribusi")
@RequiredArgsConstructor
public class DistribusiStokBarang {

    private final InsertService insertService;
    private final AddService addService;
    private final OrderService orderService;
    private final DeleteService deleteService;
    private final SelectService selectService;

    @Validated
    @PostMapping("/insertBarang")
    public ResponseEntity<?> insertBarangController(@RequestBody @Valid ReqInsertDTO requestBody){
        return insertService.insertBarangService(requestBody);
    }

    @PostMapping("/addStokBarang")
    public  ResponseEntity<?> addBarangController(@RequestBody @Valid ReqAddDTO requestBody){
        return addService.addBarangService(requestBody);
    }

    @PostMapping("/orderBarang")
    public  ResponseEntity<?> orderBarangController(@RequestBody @Valid ReqOrderDTO requestBody){
        return orderService.orderBarangService(requestBody);
    }

    @PostMapping("/deleteBarang")
    public  ResponseEntity<?> deleteBarangController(@RequestBody @Valid ReqDeleteDTO requestBody){
        return deleteService.deleteBarangService(requestBody);
    }

    @PostMapping("/selectBarang")
    public  ResponseEntity<?> selectBarangController(@RequestBody @Valid ReqSelectDTO requestBody){
        return selectService.selectBarangService(requestBody);
    }
}
