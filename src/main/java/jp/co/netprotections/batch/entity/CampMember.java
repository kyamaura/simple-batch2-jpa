package jp.co.netprotections.batch.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author v.le Entity: CAMPメンバー
 */
@Entity
@Table(name = "CampMembers")
@Data
@AllArgsConstructor
public class CampMember {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	/** 隊員氏名 */
	@Column
	@NotNull
	private String memberName;

	/** イベント企画力 */
	@Column
	@NotNull
	@Min(0)
	@Max(5)
	private int eventPlanning;

	/** 思考力 */
	@Column
	@NotNull
	@Min(0)
	@Max(5)
	private int cogitation;

	/** 調整力 */
	@Column
	@NotNull
	@Min(0)
	@Max(5)
	private int coodination;

	/** プログラム製造力 */
	@Column
	@NotNull
	@Min(0)
	@Max(5)
	private int programmingAbility;

	/** 基盤理解 */
	@Column
	@NotNull
	@Min(0)
	@Max(5)
	private int infrastructureKnowledge;

	/** 隊員氏名 */
	@Column
	private Boolean enlistedPropriety = null;

	/**
	 * Default Constructor
	 */
	public CampMember() {
		// nothing to do
	}

	/**
	 * 基本属性でCAMPメンバーエンティティー生成
	 *
	 * @param memberName2
	 * @param eventPlanning2
	 * @param cogitation2
	 * @param coodination2
	 * @param programmingAbility2
	 * @param infrastructureKnowledge2
	 */
	public CampMember(String memberName2, int eventPlanning2, int cogitation2, int coodination2,
			int programmingAbility2, int infrastructureKnowledge2) {
		// TODO Auto-generated constructor stub
		this.memberName = memberName2;
		this.eventPlanning = eventPlanning2;
		this.cogitation = cogitation2;
		this.coodination = coodination2;
		this.programmingAbility = programmingAbility2;
		this.infrastructureKnowledge = infrastructureKnowledge2;
	}

	/**
	 * イベント企画力以外が全て0点であるかどうかの確認
	 *
	 * @return
	 */
	public boolean checkAllSkillPointGreater0ExceptPlaning() {
		// TODO Auto-generated method stub
		if (this.cogitation == 0 && this.coodination == 0 && this.infrastructureKnowledge == 0
				&& this.programmingAbility == 0)
			return false;

		return true;
	}

	/**
	 * 合計が10点以下であるかどうかの確認
	 *
	 * @return
	 */
	public boolean checkTotalPointLessThan10() {
		// TODO Auto-generated method stub
		if (this.totalPoint() <= 10)
			return false;
		return true;
	}

	/**
	 * 合計ポイント計算
	 *
	 * @return
	 */
	private int totalPoint() {
		// TODO Auto-generated method stub
		return this.eventPlanning + this.cogitation + this.coodination + this.infrastructureKnowledge
				+ this.programmingAbility;
	}
}
