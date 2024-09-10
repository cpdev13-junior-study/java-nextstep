#### 1. Tomcat 서버를 시작할 때 웹 애플리케이션이 초기화하는 과정을 설명하라.
1. 서블릿 컨텍스트 생성
2. 서블릿 컨텍스트 초기화
   - ServletContextListener를 이용해 서블릿 컨텍스트 초기화, 소멸 과정을 담당
   - ServletContextListener를 구현한 ContextLoaderListener에서 datasbase와 커넥션을 맺고 주어진 jwp.sql 파일을 실행
3. loadOnStartup 기능을 이용해 서블릿 컨텍스트가 초기화될 때, DispatcherServlet을 생성(서블릿은 보통 호출될 때 생성함)
4. DispatcherServlet에서 요청한 path에 맞는 Controller를 requestMapping에서 조회
5. 조회된 Controller를 호출
6. 호출 결과를 클라이언트에게 적절한 처리를 한 후에 전달

#### 2. Tomcat 서버를 시작한 후 http://localhost:8080으로 접근시 호출 순서 및 흐름을 설명하라.
*

#### 7. next.web.qna package의 ShowController는 멀티 쓰레드 상황에서 문제가 발생하는 이유에 대해 설명하라.
* 
