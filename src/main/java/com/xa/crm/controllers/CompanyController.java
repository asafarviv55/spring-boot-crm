package com.xa.crm.controllers;

import com.xa.crm.models.Company;
import com.xa.crm.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @PostMapping
    public ResponseEntity<Company> createCompany(
            @RequestParam String name,
            @RequestParam(required = false) String industry,
            @RequestParam(required = false) Company.CompanyType type,
            @RequestParam(required = false) Long ownerId) {
        return ResponseEntity.ok(companyService.createCompany(name, industry, type, ownerId));
    }

    @PutMapping("/{id}/details")
    public ResponseEntity<Company> updateDetails(
            @PathVariable Long id,
            @RequestParam(required = false) String website,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String postalCode) {
        return ResponseEntity.ok(companyService.updateCompanyDetails(id, website, phone, address, city, state, country, postalCode));
    }

    @PutMapping("/{id}/size")
    public ResponseEntity<Company> updateSize(
            @PathVariable Long id,
            @RequestParam(required = false) Company.CompanySize size,
            @RequestParam(required = false) Integer employeeCount,
            @RequestParam(required = false) BigDecimal annualRevenue) {
        return ResponseEntity.ok(companyService.updateCompanySize(id, size, employeeCount, annualRevenue));
    }

    @PostMapping("/{id}/convert-to-customer")
    public ResponseEntity<Company> convertToCustomer(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.convertToCustomer(id));
    }

    @PostMapping("/{id}/assign")
    public ResponseEntity<Company> assignOwner(@PathVariable Long id, @RequestParam Long ownerId) {
        return ResponseEntity.ok(companyService.assignOwner(id, ownerId));
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<Company>> getByOwner(@PathVariable Long ownerId) {
        return ResponseEntity.ok(companyService.getCompaniesByOwner(ownerId));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Company>> getByType(@PathVariable Company.CompanyType type) {
        return ResponseEntity.ok(companyService.getCompaniesByType(type));
    }

    @GetMapping("/customers")
    public ResponseEntity<List<Company>> getCustomers() {
        return ResponseEntity.ok(companyService.getAllCustomers());
    }

    @GetMapping("/prospects")
    public ResponseEntity<List<Company>> getProspects() {
        return ResponseEntity.ok(companyService.getAllProspects());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Company>> search(@RequestParam String query) {
        return ResponseEntity.ok(companyService.searchCompanies(query));
    }

    @GetMapping("/industries")
    public ResponseEntity<List<String>> getIndustries() {
        return ResponseEntity.ok(companyService.getAllIndustries());
    }
}
