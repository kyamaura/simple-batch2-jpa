import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import jp.co.netprotections.batch.CampMemberCheckBatch;
import jp.co.netprotections.batch.entity.CampMember;
import jp.co.netprotections.batch.service.CampMemberService;



@RunWith(JUnit4.class)
public class CampMemberCheckBatchTest {

	  /*
	   * 正常系（campmembersテーブルからデータが適切に取り出せているかテストします）.
	   */
	  @Test
	  public void testBatch001() {
		  ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:/spring.xml");
		  CampMemberService campMemberService = ctx.getBean(CampMemberService.class);
		  List<CampMember> records = campMemberService.listAll();
		  CampMember record0 = records.get(0);
		  assertThat(record0.getMemberName(), is("uchida"));
	  }

	  /*
	   * 正常系（取り出したデータを適切に処理できているかテストします（基準に満たない場合））.
	   */
	  @Test
	  public void testBatch002() {
		  ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:/spring.xml");
		  CampMemberService campMemberService = ctx.getBean(CampMemberService.class);
		  List<CampMember> records = campMemberService.listAll();
		  CampMember member = records.get(0);
				if( campMemberService.isValidToBeMember(member)) {
					member.setEnlistedPropriety(true);
					campMemberService.add(member);
				} else {
					member.setEnlistedPropriety(false);
					campMemberService.add(member);
				}

		  assertThat(records.get(0).getEnlistedPropriety(), is(false));
	  }


	  /*
	   * 正常系（取り出したデータを適切に処理できているかテストします（基準を満たす場合））.
	   */
	  @Test
	  public void testBatch003() {
		  ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:/spring.xml");
		  CampMemberService campMemberService = ctx.getBean(CampMemberService.class);
		  List<CampMember> records = campMemberService.listAll();
		  CampMember member = records.get(1);
				if( campMemberService.isValidToBeMember(member)) {
					member.setEnlistedPropriety(true);
					campMemberService.add(member);
				} else {
					member.setEnlistedPropriety(false);
					campMemberService.add(member);
				}

		  assertThat(records.get(1).getEnlistedPropriety(), is(true));
	  }

	  /*
	   * 正常系（validationにかかったときに処理がされずに値が返されるかテストします））
	   * 空のcampmemberをvalidationにかける..
	   */
	  @Test
	  public void testBatch004() {
		  Logger logger = LoggerFactory.getLogger(CampMemberCheckBatch.class);
		  logger.info("Start CAMP Member Check!!");
		  ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:/spring.xml");
		  CampMemberService campMemberService = ctx.getBean(CampMemberService.class);
    	  ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		  Validator validator = factory.getValidator();
		  CampMember member = new CampMember();

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

		  assertNull(member.getEnlistedPropriety());
	  }


  /**
   *異常系（classpath:/spring.xmlが存在しないときに、正しいエラーがコンソールに出力されるかテストします）.
   */
  @Test
  public void testA() {
	  try {
		  CampMemberCheckBatch.main(null);
		  assertThat(1, is(4));
	  } catch (Exception e) {
		  assertThat(e, is(not(nullValue())));
		  System.out.println(e.getMessage());
	  }
  }

  /**
  *異常系（application.propertiesのjdbc.driverClassNameがorg.postgresql.Driverでないときに、
  *正しくエラーがコンソールに出力されるかテストします）.
  */
 @Test
 public void testB() {
	  try {
		  CampMemberCheckBatch.main(null);
		  assertThat(1, is(4));
	  } catch (Exception e) {
		  assertThat(e, is(not(nullValue())));
		  System.out.println(e.getMessage());
	  }
 }



}
