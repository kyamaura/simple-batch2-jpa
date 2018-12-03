package jp.co.netprotections.batch;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import jp.co.netprotections.batch.entity.CampMember;
import jp.co.netprotections.batch.service.CampMemberService;

/**
 * @author v.le
 *　入隊基準チェックバッチ
 */
public class CampMemberCheckBatch {

	public static void main(String[] args) {
		// ロガーを用意する
		Logger logger = LoggerFactory.getLogger(CampMemberCheckBatch.class);
		logger.info("Start CAMP Member Check!!");

		// Create Spring application context
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:/spring.xml");

		// Get service from context. (service's dependency (CampMember) is autowired in
		// CampMemberService)
		CampMemberService campMemberService = ctx.getBean(CampMemberService.class);

		// Validatorを用意する
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();

		List<CampMember> records = campMemberService.listAll();
		System.out.println(records.size());
		//行ずつを取り出す
		for (CampMember member: records) {

			//バリデーション実施（詳細のバリデーションはCAMPメンバーのEntityクラスをご確認）
			Set<ConstraintViolation<CampMember>> constraintViolations = validator.validate(member);
			if (constraintViolations.size() == 0) {
				//入隊基準を満たすかどうかをチェックする
				if( campMemberService.isValidToBeMember(member)) {
					//満たせばDBにインサートするリストに追加する
					member.setEnlistedPropriety(true);

					campMemberService.add(member);

				} else {
					member.setEnlistedPropriety(false);
					campMemberService.add(member);
					logger.info("invalid user: " + member.getMemberName());
				}
			} else {
				//バリデーション失敗の場合はログに詳細内容を書き込む
				constraintViolations.stream()
						.map(constraintViolation -> String.format("user %s: %s value '%s' %s",
								member.getMemberName(), constraintViolation.getPropertyPath(),
								constraintViolation.getInvalidValue(), constraintViolation.getMessage()))
						.collect(Collectors.toList()).forEach(message -> logger.error(message));
			}

		}

		ctx.close();
	}

}
