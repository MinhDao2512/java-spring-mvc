package vn.hoidanit.laptopshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.hoidanit.laptopshop.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findById(long id);

    void deleteById(long id);

    Boolean existsUserByEmail(String email);

    User findByEmail(String email);
}