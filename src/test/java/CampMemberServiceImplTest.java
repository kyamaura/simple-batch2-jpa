import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import jp.co.netprotections.batch.entity.CampMember;
import jp.co.netprotections.batch.service.CampMemberService;

public class CampMemberServiceImplTest {

	  /**
	   * 正常系(アウトプットがtrueになるべきインプットを受け取るテストをします).
	   */
	  @Test
	  public void testCampMemberServiceImpl011() {
		  ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:/spring.xml");
		  CampMemberService campMemberService = ctx.getBean(CampMemberService.class);
		  CampMember newCampMember = new CampMember("hiko", 3, 3, 3, 3, 3);
		  boolean x = campMemberService.isValidToBeMember(newCampMember);
		  assertThat(x, is(true));
	  }

	  /**
	   * 正常系(checkAllSkillPointGreater0ExceptPlaning()が正常に機能してfalseを返すかテストします).
	   */
	  @Test
	  public void testCampMemberServiceImpl012() {
		  ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:/spring.xml");
		  CampMemberService campMemberService = ctx.getBean(CampMemberService.class);
		  CampMember newCampMember = new CampMember("hama", 5, 0, 0, 0, 0);
		  boolean x = campMemberService.isValidToBeMember(newCampMember);
		  assertThat(x, is(false));
	  }

	  /**
	   * 正常系(checkTotalPointLessThan10()が正常に機能してfalseを返すかテストします).
	   */
	  @Test
	  public void testCampMemberServiceImpl013() {
		  ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:/spring.xml");
		  CampMemberService campMemberService = ctx.getBean(CampMemberService.class);
		  CampMember newCampMember = new CampMember("hama", 2, 2, 2, 2, 2);
		  boolean x = campMemberService.isValidToBeMember(newCampMember);
		  assertThat(x, is(false));
	  }

	  /**
	   * 正常系(イベント企画力が1点以下である条件が正常に機能してfalseを返すかテストします).
	   */
	  @Test
	  public void testCampMemberServiceImpl014() {
		  ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:/spring.xml");
		  CampMemberService campMemberService = ctx.getBean(CampMemberService.class);
		  CampMember newCampMember = new CampMember("hama", 1, 5, 5, 5, 5);
		  boolean x = campMemberService.isValidToBeMember(newCampMember);
		  assertThat(x, is(false));
	  }

	  /**
	   * 正常系(調整力が1点以下である条件が正常に機能してfalseを返すかテストします).
	   */
	  @Test
	  public void testCampMemberServiceImpl015() {
		  ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:/spring.xml");
		  CampMemberService campMemberService = ctx.getBean(CampMemberService.class);
		  CampMember newCampMember = new CampMember("hama", 5, 5, 1, 5, 5);
		  boolean x = campMemberService.isValidToBeMember(newCampMember);
		  assertThat(x, is(false));
	  }

	  /**
	   * 異常系(数値の一つがnullであるときどのような結果を返すかテストします).
	   */
	  @Test
	  public void testCampMemberServiceImpl016() {
		  ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:/spring.xml");
		  CampMemberService campMemberService = ctx.getBean(CampMemberService.class);
		  CampMember newCampMember = new CampMember("hama", 5, 5, 1, 5, 5);
		  boolean x = campMemberService.isValidToBeMember(newCampMember);
		  assertThat(x, is(false));
	  }

}
