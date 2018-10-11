package jp.co.netprotections.batch.service;

import java.util.Collection;
import java.util.List;
import jp.co.netprotections.batch.entity.CampMember;

/**
 * @author v.le CAMPメンバーサービス
 */
public interface CampMemberService {
	/**
	 * DBにメンバー追加する
	 * 
	 * @param campMember
	 */
	public void add(CampMember campMember);

	/**
	 * DBにメンバーリスト追加する
	 * 
	 * @param campMembers
	 */
	public void addAll(Collection<CampMember> campMembers);

	/**
	 * DBからメンバーリスト取得する
	 * 
	 * @return
	 */
	public List<CampMember> listAll();

	/**
	 * DBのメンバーデータ全部削除する
	 */
	public void deleteAll();

	/**
	 * 人の入隊基準を満たせるかどうかチェックする
	 * 
	 * @param newCampMember
	 * @return
	 */
	public boolean isValidToBeMember(CampMember newCampMember);
}
