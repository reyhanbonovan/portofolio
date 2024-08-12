//Request Json Schemas
/*
    POST Header :
    http://localhost:8080/distribusi/insertBarang
    request body:
    {
        "kode_barang": "A001",
        "nama_barang": "LEAIR",
        "harga_beli": "5000",
        "harga_jual": "7500",
        "stok_masuk": "20"
    }

    POST Header :
    http://localhost:8080/distribusi/addStokBarang
    request body:
    {
        "kode_barang": "A001",
        "nama_barang": "LEAIR",
        "stok_masuk": "4"
    }

    POST Header :
    http://localhost:8080/distribusi/orderBarang
    request body:
    {
        "kode_barang": "A001",
        "nama_barang": "LEAIR",
        "banyak_pesanan": "5"
    }

    POST Header :
    http://localhost:8080/distribusi/deleteBarang
    request body:
    {
        "kode_barang": "A001",
        "nama_barang": "LEAIR"
    }

    POST Header :
    http://localhost:8080/distribusi/selectBarang
    request body:
    {
        "kode_barang": "A001",
        "nama_barang": "LEAIR"
    }
*/

