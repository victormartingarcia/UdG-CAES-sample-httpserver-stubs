package caes.web;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TestWebClient1StubHTTPServer {

  static Server server;

  @BeforeAll
  static void setup() throws Exception {
    server = new Server(8080);
    ContextHandler ctx = new ContextHandler("/testok");
    ctx.setHandler(new TestGetContentOkHandler());
    ContextHandler ctxErr = new ContextHandler("/testerror");
    ctxErr.setHandler(new TestGetContentError());
    ContextHandlerCollection coll = new ContextHandlerCollection();
    coll.setHandlers(new Handler[] { ctx, ctxErr});
    server.setHandler(coll);
    server.start();
  }

  @AfterAll
  static void teardown() throws Exception {
    server.stop();
  }

  @Test
  void testOk() throws MalformedURLException {
    WebClient wc = new WebClient();
    String page = wc.getContent(new URL("http://localhost:8080/testok"));
    assertEquals(page, "It works");
  }

  @Test
  void testError() throws MalformedURLException {
    WebClient wc = new WebClient();
    String page = wc.getContent(new URL("http://localhost:8080/testerror"));
    assertNull(page);
  }



  private static class TestGetContentOkHandler extends AbstractHandler {
    @Override
    public void handle(String s, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
      httpServletResponse.setContentType("text/html;charset=utf-8");
      httpServletResponse.setStatus(HttpServletResponse.SC_OK);
      request.setHandled(true);
      httpServletResponse.getWriter().println("It works");

    }
  }

  private static class TestGetContentError extends AbstractHandler {
    @Override
    public void handle(String s, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
      httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
      request.setHandled(true);
    }
  }

}
