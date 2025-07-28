package com.apidepartamentos.api_departamentos.dto;

import java.util.List;

public class SearchDepartamentoResponse {

    private int currentPage;
    private int totalPages;
    private int totalItems;
    private Long size;
    List<DepartamentoResponse> departamentos;
    public int getCurrentPage() {
        return currentPage;
    }
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    public int getTotalPages() {
        return totalPages;
    }
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
    public int getTotalItems() {
        return totalItems;
    }
    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }
    public Long getSize() {
        return size;
    }
    public void setSize(Long size) {
        this.size = size;
    }
    public List<DepartamentoResponse> getDepartamentos() {
        return departamentos;
    }
    public void setDepartamentos(List<DepartamentoResponse> departamentos) {
        this.departamentos = departamentos;
    }

    



}
