package core.nxg.repository;
import core.nxg.entity.TechTalentUser;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.Collections;

import core.nxg.entity.User;

public class TechTalentRepositoryImpl implements TechTalentRepository {

  @Override
  public Optional<User> findByEmail(String email) {
    return null;
    // implementation goes here
  }

@Override
public void flush() {
    throw new UnsupportedOperationException("Unimplemented method 'flush'");
}

@Override
public <S extends TechTalentUser> S saveAndFlush(S entity) {
    throw new UnsupportedOperationException("Unimplemented method 'saveAndFlush'");
}

@Override
public <S extends TechTalentUser> List<S> saveAllAndFlush(Iterable<S> entities) {
    throw new UnsupportedOperationException("Unimplemented method 'saveAllAndFlush'");
}

@Override
public void deleteAllInBatch(Iterable<TechTalentUser> entities) {
    throw new UnsupportedOperationException("Unimplemented method 'deleteAllInBatch'");
}

@Override
public void deleteAllByIdInBatch(Iterable<Long> ids) {
    throw new UnsupportedOperationException("Unimplemented method 'deleteAllByIdInBatch'");
}

@Override
public void deleteAllInBatch() {
    throw new UnsupportedOperationException("Unimplemented method 'deleteAllInBatch'");
}

@Override
public TechTalentUser getOne(Long id) {
    throw new UnsupportedOperationException("Unimplemented method 'getOne'");
}

@Override
public TechTalentUser getById(Long id) {
    throw new UnsupportedOperationException("Unimplemented method 'getById'");
}

@Override
public TechTalentUser getReferenceById(Long id) {
    throw new UnsupportedOperationException("Unimplemented method 'getReferenceById'");
}

@Override
public <S extends TechTalentUser> List<S> findAll(Example<S> example) {
    throw new UnsupportedOperationException("Unimplemented method 'findAll'");
}

@Override
public <S extends TechTalentUser> List<S> findAll(Example<S> example, Sort sort) {
    throw new UnsupportedOperationException("Unimplemented method 'findAll'");
}

@Override
public <S extends TechTalentUser> List<S> saveAll(Iterable<S> entities) {
    throw new UnsupportedOperationException("Unimplemented method 'saveAll'");
}

@Override
public List<TechTalentUser> findAllById(Iterable<Long> ids) {
    throw new UnsupportedOperationException("Unimplemented method 'findAllById'");
}

@Override
public <S extends TechTalentUser> S save(S entity) {
    throw new UnsupportedOperationException("Unimplemented method 'save'");
}

@Override
public Optional<TechTalentUser> findById(Long id) {
    throw new UnsupportedOperationException("Unimplemented method 'findById'");
}

@Override
public boolean existsById(Long id) {
    throw new UnsupportedOperationException("Unimplemented method 'existsById'");
}

@Override
public long count() {
    throw new UnsupportedOperationException("Unimplemented method 'count'");
}

@Override
public void deleteById(Long id) {
    throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
}

@Override
public void delete(TechTalentUser entity) {
    throw new UnsupportedOperationException("Unimplemented method 'delete'");
}

@Override
public void deleteAllById(Iterable<? extends Long> ids) {
    throw new UnsupportedOperationException("Unimplemented method 'deleteAllById'");
}

@Override
public void deleteAll(Iterable<? extends TechTalentUser> entities) {
    throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
}

@Override
public void deleteAll() {
    throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
}

@Override
public List<TechTalentUser> findAll(Sort sort) {
    String sql = "SELECT * FROM tech_talent_users ORDER BY " + sort.toString();
    JdbcTemplate jdbcTemplate = new JdbcTemplate();
    List<TechTalentUser> users = jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(TechTalentUser.class) );

    if (users.isEmpty()) {
        return Collections.emptyList();
    }

    return users;
}


@Override
public Page<TechTalentUser> findAll(Pageable pageable) {
    String sql = "SELECT * FROM tech_talent_users ORDER BY " + pageable.toString();
    PageImpl<TechTalentUser> page = new PageImpl<>(null, pageable, 0);
    JdbcTemplate jdbcTemplate = new JdbcTemplate();
    List<TechTalentUser> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TechTalentUser.class));

    if (users.isEmpty()) {
        return page.getPageable().isPaged() ? page : new PageImpl<>(users);
    }

    return new PageImpl<>(users, pageable, users.size());
}

@Override
public <S extends TechTalentUser> Optional<S> findOne(Example<S> example) {
    throw new UnsupportedOperationException("Unimplemented method 'findOne'");
}

@Override
public <S extends TechTalentUser> Page<S> findAll(Example<S> example, Pageable pageable) {
    throw new UnsupportedOperationException("Unimplemented method 'findAll'");
}

@Override
public <S extends TechTalentUser> long count(Example<S> example) {
    throw new UnsupportedOperationException("Unimplemented method 'count'");
}

@Override
public <S extends TechTalentUser> boolean exists(Example<S> example) {
    throw new UnsupportedOperationException("Unimplemented method 'exists'");
}

@Override
public <S extends TechTalentUser, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
    throw new UnsupportedOperationException("Unimplemented method 'findBy'");
}



@Override
public List<TechTalentUser> findAll() {
    throw new UnsupportedOperationException("Unimplemented method 'findAll'");
}
}