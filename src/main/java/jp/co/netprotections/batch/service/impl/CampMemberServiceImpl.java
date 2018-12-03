package jp.co.netprotections.batch.service.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.netprotections.batch.entity.CampMember;
import jp.co.netprotections.batch.repository.CampMemberRepository;
import jp.co.netprotections.batch.service.CampMemberService;

/**
 * CAMPメンバーサービスの実装
 * @author v.le
 *
 */
@Service
public class CampMemberServiceImpl implements CampMemberService {
	@Autowired
	private CampMemberRepository campMemberRepository;

	@Transactional
	public void add(CampMember campMember) {
		campMemberRepository.save(campMember);
	}

	@Transactional
	public void addAll(Collection<CampMember> campMembers) {
		campMemberRepository.saveAll(campMembers);
	}

	@Transactional(readOnly = true)
	public List<CampMember> listAll() {
		return (List<CampMember>) campMemberRepository.findAll();

	}

	@Transactional
	public void deleteAll() {
		campMemberRepository.deleteAll();
	}

	@Transactional
	public void delete(CampMember campMember) {
		campMemberRepository.delete(campMember);
	}

	@Override
	public boolean isValidToBeMember(CampMember newCampMember) {
		// ・リクエストのイベント企画力が1以下の場合、入隊可否はfalseとする
		// ・以下の条件を満たす場合、全て結果レスポンスの入隊可否をfalseとする：
		// - イベント企画力が1点以下である
		// - 調整力が1点以下である
		// - イベント企画力以外が全て0点である
		// - 合計が10点以下である
		if (newCampMember.getEventPlanning() <= 1 || newCampMember.getCoodination() <= 1
				|| !newCampMember.checkAllSkillPointGreater0ExceptPlaning() || !newCampMember.checkTotalPointLessThan10())
			return false;

		// ・上記条件以外の場合は全て入隊可否をtrue、つまり入隊可能とする
		return true;
	}
}
