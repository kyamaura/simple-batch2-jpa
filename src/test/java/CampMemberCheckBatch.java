import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import jp.co.netprotections.batch.entity.CampMember;
import jp.co.netprotections.batch.service.CampMemberService;



@RunWith(JUnit4.class)
public class CampMemberCheckBatch {

	  /*
	   * campmembersテーブルからデータが適切に取り出せているかテストします.
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
	   * 取り出したデータを適切に処理できているかテストします（基準に満たない場合）.
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
	   * 取り出したデータを適切に処理できているかテストします（基準を満たす場合）.
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



}
