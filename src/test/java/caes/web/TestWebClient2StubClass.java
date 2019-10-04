package caes.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestWebClient2StubClass {
  @BeforeEach
  public void setUp() {
    TestWebClient2StubClass t = new TestWebClient2StubClass();
    URL.setURLStreamHandlerFactory(t.new StubStreamHandlerFactory());
  }

  @Test
  void testOk2() throws MalformedURLException {
    WebClient wc = new WebClient();
    String page = wc.getContent(new URL("http://localhost:9999/test"));
    assertEquals("It works", page);
  }

  private class StubStreamHandlerFactory implements URLStreamHandlerFactory {
    public URLStreamHandler createURLStreamHandler(String protocol) {
      return new StubURLStreamHandler();
    }
  }

  private class StubURLStreamHandler extends URLStreamHandler {
    protected URLConnection openConnection(URL url) throws IOException {
      return new StubURLConnection(url);
    }
  }

  public class StubURLConnection extends HttpURLConnection {
    private boolean isInput = true;

    protected StubURLConnection(URL url) {
      super(url);
    }

    public InputStream getInputStream() throws IOException {
      if (!isInput) {
        throw new ProtocolException("Cannot read from URLConnection"
            + " if doInput=false (call setDoInput(true))");
      }
      ByteArrayInputStream bais = new ByteArrayInputStream(
          new String("It works").getBytes());
      return bais;
    }

    public void disconnect() {}

    public void connect() throws IOException {}

    public boolean usingProxy() {
      return false;
    }
  }

}