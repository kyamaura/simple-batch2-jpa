package jp.co.netprotections.batch;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
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

		// 既存のデータを全部クリアする
		campMemberService.deleteAll();

		List<CampMember> campMemberAddList = new ArrayList<CampMember>();

		try {
			//入隊データのCSVファイルを読み取りを開始する
			Reader in = new FileReader("data/members.csv");
			Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
			
			//行ずつを取り出す
			for (CSVRecord record : records) {
				//行の属性を取得する
				String memberName = record.get("memberName");
				int eventPlanning = Integer.parseInt(record.get("eventPlanning"));
				int cogitation = Integer.parseInt(record.get("cogitation"));
				int coodination = Integer.parseInt(record.get("coodination"));
				int programmingAbility = Integer.parseInt(record.get("programmingAbility"));
				int infrastructureKnowledge = Integer.parseInt(record.get("infrastructureKnowledge"));

				//メンバーオブジェクトを作成する
				CampMember newCampMember = new CampMember(memberName, eventPlanning, cogitation, coodination,
						programmingAbility, infrastructureKnowledge);

				//バリデーション実施（詳細のバリデーションはCAMPメンバーのEntityクラスをご確認）
				Set<ConstraintViolation<CampMember>> constraintViolations = validator.validate(newCampMember);
				if (constraintViolations.size() == 0) {
					//入隊基準を満たすかどうかをチェックする
					if( campMemberService.isValidToBeMember(newCampMember)) {
						//満たせばDBにインサートするリストに追加する
						newCampMember.setEnlistedPropriety(true);
						campMemberAddList.add(newCampMember);
					} else {
						logger.info("invalid user: " + newCampMember.getMemberName());
					}
				} else {
					//バリデーション失敗の場合はログに詳細内容を書き込む
					constraintViolations.stream()
							.map(constraintViolation -> String.format("user %s: %s value '%s' %s",
									newCampMember.getMemberName(), constraintViolation.getPropertyPath(),
									constraintViolation.getInvalidValue(), constraintViolation.getMessage()))
							.collect(Collectors.toList()).forEach(message -> logger.error(message));
				}

				//バルクインサートでDBに新会員データを入り込む（５件ごとですが、実際は数千件ごと）
				if (campMemberAddList.size() >= 5) {
					campMemberService.addAll(campMemberAddList);
					campMemberAddList.clear();
				}
			}

			//残りのデータをバルクインサートでDBに入り込む
			if (campMemberAddList.size() >= 0) {
				campMemberService.addAll(campMemberAddList);
				campMemberAddList.clear();
			}

		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
		
		ctx.close();
	}

}
