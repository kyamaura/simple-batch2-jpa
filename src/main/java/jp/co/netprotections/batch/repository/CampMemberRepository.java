package jp.co.netprotections.batch.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import jp.co.netprotections.batch.entity.CampMember;

/**
 * CAMPメンバーのリポジトリ（DAO）
 * 
 * @author v.le
 *
 */
@Component
public interface CampMemberRepository extends CrudRepository<CampMember, Integer> {

}
