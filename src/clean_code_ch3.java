// clean code , 3장 함수


// 이 코드는 이해하기 쉬운가?
public static String testableHtml (PageData pageData, boolean includeSuiteSetup) throws Exception {
	WikiPage wikiPage = pageData.getWikiPage();
	StringBuffer buffer = new StringBuffer();
	if (pageData. hasAttribute("Test")) {
		if (includeSuiteSetup) {
			WikiPage suiteSetup =
			PageCrawlerImpl. getInheritedPagel(SuiteResponder.SUITE_SETUP_NAME, wikiPage);
			if (suiteSetup != null) {
				WikiPagePath pagePath = suiteSetup.getPageCrawler().getFullPath(suiteSetup);
				String pagePathName = PathParser.render(pagePath);
				buffer.append(" ! include -setup .").append (pagePathName).append ("\n"):
			}
		}

		WikiPage setup = PageCrawlerImpl.get InheritedPage ("SetUp", wikipage);
		if (setup != null) {
			WikiPagePath setupPath = wikiPage.getPageCrawler(). getFullPath(setup);
			String setupPathName = PathParser. render (setupPath);
			buffer append("'! include -setup.").aappend (setupPathName).append ("\n");
		}
	}

	buffer.append(pageData.getContent());
	if (pageData. hasAttribute("Test")) {
		WikiPage teardown = PageCrawler Impl. get InheritedPage("TearDown", wikiPage);
		if (teardown != null) {
			WikiPagePath tearDownPath = wikiPage.getPageCrawler().getFullPath(teardown);

			String tearDownPathName = PathParser.render(tearDownPath);
			buffer.append ("\n" ).append(" ! include -teardown .").append (tearDownPathName ).append ("\n");
		}
		if (includeSuiteSetup) {
			WikiPage suiteTeardown = PageCrawlerImpl. getInheritedPage(
				SuiteResponder. SUITE_TEARDOWN_NAME,WiKiPage );

			if (suiteTeardown != null) {
				WikiPagePath pagePath =
					suiteTeardown.getPageCrawler().getFullPath (suiteTeardown);
				String pagePathName = PathParser. render (pagePath) ;
				buffer. append ("! include -teardown .").append (pagePathName ).append ("\n" );
			}
		}
	}

	pageData. setContent (buffer.toString());
	return pageData.getHtml()
}





















// 이 코드는 어떤가?
public static String renderPageWithSetupsAndTeardowns( PageData pageData, boolean isSuite) throws Exception {
	boolean isTestPage = pageData.hasAttribute("Test");
	if (isTestPage) {
		WikiPage testPage = pageData.getWikiPage();
		StringBuffer newPageContent = new StringBuffer();
		includeSetupPages(testPage, newPageContent, isSuite);
		newPageContent.append(pageData.getContent());
		includeTeardownPages(testPage, newPageContent, isSuite);
		pageData.setContent(newPageContent.toString());
	}
	return pageData.getHtml();
}

















/************************************* 
 * 작게 만들어라 (함수만드는 첫번째 규칙) 
**************************************/

// 작다고 좋다는 근거는 없다. 경험상 작은 함수가 좋았다.
// 최소한 위에 있는 함수정도 사이즈이고 그것보다 더 작아야 한다.

// 이정도 사이즈라야 한다.
public static String renderPageWithSetupsAndTeardowns2( PageData pageData, boolean isSuite) throws Exception {
   if (isTestPage(pageData))
   	includeSetupAndTeardownPages(pageData, isSuite);
   return pageData.getHtml();
}

// 함수에서 들여쓰기 수준은 1단이나 2단을 넘지 않아야 한다.
// if else block 안에서 호출하는 코드는 한줄이라야 한다.


















/********************************
 * 한 가지만 해라
 *******************************/

// renderPageWithSetupsAndTeardowns2 는 한가지만 한다.
// testableHtml 는 안에서 너무 많은 일을 하고 있다.

///////////////////
// 함수는 한 가지를 해야 한다. 그 한 가지를 잘 해야 한다. 그 한 가지만을 해야 한다.
///////////////////


// renderPageWithSetupsAndTeardowns2 는 진짜 한가지만 하는가?
// 1. 페이지가 테스트 페이지 인지 판단하고
// 2. 테스트 페이지라면 설정, 해제 페이지를 추가하고
// 3. 페이지를 html 로 렌더링 한다.


// 추상화 수준이 하나인 단계만 수행해야한다.

// TO renderPageWithSetupsAndTeardowns2, 
// 페이지가 테스트 페이지인지 확인한 후 테스트 페이지라면 설정 페이지와 해제 페이지를 넣는다.테스트 페이지든 아니든 페이지를 html 로 렌더링 한다.

// 함수에서 의미 있는 다른 함수 이름을 추출할 수 있다면 그 함수는 여러가지 일을 하는 것이다.

// 거의 동일한 내용으로 추상화가 되는 수준이면 한가지 일을 하는 것이라고 볼 수 있다.

// 혹은 함수안에서 섹션을 나눌수 있다? 그럼 여러가지 일을 하는것이다.












/********************************
 * 함수 당 추상화 수준(level)은 하나로!
 ********************************/


// 추상화 *수준* 이 동일해야 한다.

//  getHtml() 추상화 수준이 높다.
//  String tearDownPathName = PathParser.render(tearDownPath); 중간정도 수준
//  .append ("\n" ); 너무 낮은 수준
// 이런 다양한 수준이 함수에 섞여 있으면 안된다. 비슷한 수준으로 맞춰야 한다. 근본개념인지 세부사항인지 헷갈리게 된다.
















/********************************
 * 위에서 아래로 코드 읽기 : 내려가기 규칙
 ******************************/


// 코드는 이야기처럼 위에서 아래로 읽혀야 한다.
// 위에서는 추상화 수준이 높은 코드가, 그 다음에는 한단계 낮은 코드가 나온다.


/*
T○ 설정 페이지와 해제 페이지를 포함하려면, 설정 페이지를 포함하고. 테스트 페이지 내용을 포함하고 해제 페이지를 포함한다.
	T○설정페이지를포함하려면,슈트이면슈트설정페이지를포함한후일반 설정페이지률포함한다.
	T○슈트설정페이지를포함하려면.부모계층에서“SuiteSelIIp”페이지를찾 아include문과페이지경로를추가한다.
	TO부모계층을검색하려면,......
*/



/****************************
 * switch 사용할때 주의점
 ****************************/


// switch 는 함수가 커지기 쉽다.
// 다형적 객체 생성시에만 사용하는것을 권장한다....



/******************************
 * 서술적인 이름을 사용하라
 ********************************/


// “코드를 읽으면서 짐작했던 기 능을각루틴이그대로수행한다면깨끗한코드라불러도되겠다.”

// 이름이 길어도 괜찮다. 겁먹을 필요없다. 길고 서술적인 이름이 짧고 어려운 이름보다 좋다. 길고 서술적인 이름이 길고 서술적인 주석보다 좋다. 
//함수 이름 을 정할 때는 여러 단어가 쉽게 읽히는 명명법을 사용한다. 그런 다음. 여러 단어 를 사용해 함수 기능을 잘 표현하는 이름을 선택한다.


// 이름 바꾸기 쉽다. (툴이 자동으로 해준다.) 자주 바꿔보자.




/*******************************
 * 함수 인수
 * ***************************/


 //함수에서 이상적인 인수 개수는 0개(무항)다. 
 //다음은 1개(단항)고, 다음은 2개(이항)다 3개 (삼항)는 가능한 피하는 편이 좋다. 
 //4개 이상 (다항)은 특별한 이유가 필요하다. 특별한 이유 가있어도사용하면안된다.
 

// 인수가 3개를 넘어가면 인수 마다유효한값으로모든조합을구성해테스트하기가상당히부담스러워진다.


// 출력 인수는 입력 인수보다 이해하기 어렵다!!! (인수로 넘겨준 값을 출력으로 받는 경우)

// out = doSomething(in) 
// doSomething(in, out)


// 플래그 인수도 안된다!
// render(true); 이런거.


 // 2항, 3항 모두 한번씩 고민해야한다.

 // 인수가 여러개가 되어야한다면 class 변수를 생각해봐야한다.


 // 이름
 // write(name) .. 좋다.
 // assertEquals -> assertExpectedEqualsActual(expected, actual)
















 /***************************************************
  * 부수효과
  * ********************************************/



public class UserValidator {
	private Cryptographer cryptographer;
	public boolean checkPassword(String userName, String password) { 
		User user = UserGateway.findByName(userName);
		if (user != User.NULL) {
			String codedPhrase = user.getPhraseEncodedByPassword(); 
			String phrase = cryptographer.decrypt(codedPhrase, password); 
			if ("Valid Password".equals(phrase)) {
				Session.initialize();
				return true; 
			}
		}
		return false; 
	}
}




// Session.initialize();
// 시간적 결합 : 이 함수는 세션 초기화때만 호출할 수 있게 된다.


// 이름에 session initialize 가 들어가야한다.



/*********************************************************
 * 출력인수
 * ********************************************/



appendFooter(report);


report.appendFooter();







/************************************
 * 명령과 조회를 분리하라
 * *********************************/

// 함수는 수행하거나 응답하거나 둘 중 하나만 해야한다. (앞에서도 나왔떤)



public boolean set(String attribute, String value);

if (set("username", "unclebob")) {
	//,....
}


// set 이 여기서는 설정되어있다는 소리인지, 설정한다는것인지 모호해진다.




if (attributeExists("username")) { 
	setAttribute("username" , "unclebob");
}



/************************************
 * 오류 코드 보다는 예외
 * ***********************************/

// 이건 특별한건 없다고 봄. 오류 코드는 잘 안쓰고 있으니..


/************************************
 * 반복하지 말라
 * ********************************/

// 중복은 제거해야 한다.



/*******************
 * 어떻게 짤 것인가?
 ********************/

// 함수를 짜고
// 테스트를 만들고
// 코드와 테스트를 고치고 정리한다.
// 반복

