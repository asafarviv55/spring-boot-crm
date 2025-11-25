package com.xa.crm.services;

import com.xa.crm.models.Company;
import com.xa.crm.models.User;
import com.xa.crm.repositories.CompanyRepository;
import com.xa.crm.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    public Company createCompany(String name, String industry, Company.CompanyType type, Long ownerId) {
        Company company = new Company();
        company.setName(name);
        company.setIndustry(industry);
        company.setType(type != null ? type : Company.CompanyType.PROSPECT);

        if (ownerId != null) {
            User owner = userRepository.findById(ownerId).orElse(null);
            company.setOwner(owner);
        }

        return companyRepository.save(company);
    }

    public Company updateCompanyDetails(Long id, String website, String phone, String address,
                                        String city, String state, String country, String postalCode) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        if (website != null) company.setWebsite(website);
        if (phone != null) company.setPhone(phone);
        if (address != null) company.setAddress(address);
        if (city != null) company.setCity(city);
        if (state != null) company.setState(state);
        if (country != null) company.setCountry(country);
        if (postalCode != null) company.setPostalCode(postalCode);

        return companyRepository.save(company);
    }

    public Company updateCompanySize(Long id, Company.CompanySize size, Integer employeeCount, BigDecimal annualRevenue) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        company.setSize(size);
        company.setEmployeeCount(employeeCount);
        company.setAnnualRevenue(annualRevenue);

        return companyRepository.save(company);
    }

    public Company convertToCustomer(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        company.setType(Company.CompanyType.CUSTOMER);
        return companyRepository.save(company);
    }

    public Company assignOwner(Long companyId, Long ownerId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        company.setOwner(owner);
        return companyRepository.save(company);
    }

    public List<Company> getCompaniesByOwner(Long ownerId) {
        return companyRepository.findByOwnerId(ownerId);
    }

    public List<Company> getCompaniesByType(Company.CompanyType type) {
        return companyRepository.findByType(type);
    }

    public List<Company> getCompaniesByIndustry(String industry) {
        return companyRepository.findByIndustry(industry);
    }

    public List<Company> searchCompanies(String query) {
        return companyRepository.findByNameContainingIgnoreCase(query);
    }

    public List<Company> getAllCustomers() {
        return companyRepository.findAllCustomers();
    }

    public List<Company> getAllProspects() {
        return companyRepository.findAllProspects();
    }

    public List<String> getAllIndustries() {
        return companyRepository.findAllIndustries();
    }
}
