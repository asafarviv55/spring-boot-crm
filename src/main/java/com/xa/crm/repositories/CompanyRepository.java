package com.xa.crm.repositories;

import com.xa.crm.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    List<Company> findByOwnerId(Long ownerId);
    List<Company> findByType(Company.CompanyType type);
    List<Company> findByIndustry(String industry);
    List<Company> findBySize(Company.CompanySize size);
    List<Company> findByNameContainingIgnoreCase(String name);

    @Query("SELECT DISTINCT c.industry FROM Company c WHERE c.industry IS NOT NULL")
    List<String> findAllIndustries();

    @Query("SELECT c FROM Company c WHERE c.type = 'CUSTOMER'")
    List<Company> findAllCustomers();

    @Query("SELECT c FROM Company c WHERE c.type = 'PROSPECT'")
    List<Company> findAllProspects();
}
