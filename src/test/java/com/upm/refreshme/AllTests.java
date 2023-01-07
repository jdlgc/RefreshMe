package com.upm.refreshme;

import com.google.firebase.FirebaseApp;
import com.upm.refreshme.backend.Servlet;
import com.upm.refreshme.backend.WebPage;
import com.upm.refreshme.launch.Main;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


public class AllTests {

    private Tomcat tomcat;

    @Before
    public void setUp() throws Exception {
        tomcat = new Tomcat();
    }

    @After
    public void tearDown() throws LifecycleException {
        tomcat.stop();
    }

    @Test
    public void testTomcatStart() throws LifecycleException {
        String webPort = "8080";
        tomcat.setPort(Integer.valueOf(webPort));
        tomcat.start();
        Assertions.assertNotNull(tomcat.getServer().getState());
    }

    @Test
    public void testTomcatStop() throws LifecycleException {
        String webPort = "8080";
        tomcat.setPort(Integer.valueOf(webPort));
        tomcat.start();
        tomcat.stop();
        Assertions.assertNotNull(tomcat.getServer().getState());
    }

    @Test
    public void testWebappDirLocation() {
        String webappDirLocation = "src/main/webapp/";
        StandardContext ctx = (StandardContext) tomcat.addWebapp("", new File(webappDirLocation).getAbsolutePath());
        Assertions.assertNotNull(ctx);
    }

    @Test
    public void testWebPort() {
        String webPort = "8080";
        tomcat.setPort(Integer.valueOf(webPort));
        Assertions.assertEquals(tomcat.getConnector().getPort(), 8080);
    }

    @Test
    public void testWebResourceRoot() {
        String webappDirLocation = "src/main/webapp/";
        StandardContext ctx = (StandardContext) tomcat.addWebapp("", new File(webappDirLocation).getAbsolutePath());
        File additionWebInfClasses = new File("target/classes");
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
                additionWebInfClasses.getAbsolutePath(), "/"));
        ctx.setResources(resources);
        Assertions.assertNotNull(ctx.getResources());
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();
    @Test(timeout = 1000)
    public void MainTest() throws Exception{
        exceptionRule.expect(Exception.class);
        CountDownLatch latch = new CountDownLatch(1);
        latch.await(500, TimeUnit.MILLISECONDS);
        Main.main(new String[0]);
    }

    @Test
    public void WebPageTest(){
        WebPage wp = new WebPage("Nombre","URL","Categoria","UltimosCambios");
        Assertions.assertEquals("Nombre",wp.getNombre());
        Assertions.assertEquals("URL",wp.getUrl());
        Assertions.assertEquals("Categoria",wp.getCategoria());
        Assertions.assertEquals("UltimosCambios",wp.getUltimosCambios());
    }

    @Test
    public void testAddNewWebPage() {
        Servlet servlet = new Servlet();
        WebPage webPage =  new WebPage("Nombre","URL","Categoria","UltimosCambios");
        Assertions.assertTrue(servlet.addNewWebPage(webPage));
    }


    @Test(expected = Exception.class)
    public void initiateFirebaseException() {
        Servlet servlet = new Servlet();
        Mockito.when(FirebaseApp.initializeApp(Mockito.anyString())).thenThrow(new Exception());

    }

    @Test
    public void testGetWebPages() {
        Servlet servlet = new Servlet();

        ArrayList<WebPage> result = servlet.getWebPagesList("");
        Assertions.assertNotNull(result);
    }
}
