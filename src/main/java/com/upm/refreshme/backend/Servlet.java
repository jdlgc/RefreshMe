package com.upm.refreshme.backend;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.UserAuthorizer;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.EmailIdentifier;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetUsersResult;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import com.google.firebase.auth.internal.GetAccountInfoResponse.User;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.internal.FirebaseService;
import com.google.firebase.auth.UserInfo;

public class Servlet {

	private static UserRecord userRecord;
	private static FirebaseAuth defaultAuth;
	private static FirebaseDatabase defaultDatabase;
	private static User userAuth;
	private List<String> WebPages;

	final static String rmEmail = "refreshmeapp@gmail.com";
	final static String token = "jprafjwywgsijwci";

	public static void main(String[] args) throws IOException{

		FileInputStream serviceAccount = new FileInputStream("./ServiceAccountKey.json");

		FirebaseOptions options = FirebaseOptions.builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.setDatabaseUrl("https://refreshme-8db78-default-rtdb.europe-west1.firebasedatabase.app/")
				.build();

		// Initialize the default app
		FirebaseApp defaultApp = FirebaseApp.initializeApp(options);

		
		System.out.println(defaultApp.getName());  // "[DEFAULT]"

		// Retrieve services by passing the defaultApp variable...
		defaultAuth = FirebaseAuth.getInstance(defaultApp);
		defaultDatabase = FirebaseDatabase.getInstance(defaultApp);
		
		String password = "123456";
		try {
			getClientPages();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (password.length() < 6) {
			System.out.println("Por favor, introduzca  una contraseña de al menos 6 caracteres.");
		} else {
			try {
				createUser("John Doe", "jorcordd@gmail.com", password);
			} catch (FirebaseAuthException e) {
				switch(e.getMessage()) {
				case "The user with the provided email already exists (EMAIL_EXISTS).":
					System.out.println("Correo ya registrado, por favor, introduzca un correo no existente en RefreshMe.");
					break;
				default:
					System.out.println(e.getMessage());
				}
			}
		}
	}

	private static void createUser(String userName, String email, String password) throws FirebaseAuthException {
		CreateRequest request = new CreateRequest()
				.setEmail(email)
				.setEmailVerified(false)
				.setPassword(password)
				.setDisplayName(userName)
				.setDisabled(false);

		userRecord = FirebaseAuth.getInstance().createUser(request);
		System.out.println("Successfully created new user: " + userRecord.getUid());
		sendEmailVerification(email, userName);
	}

	private static void sendEmailVerification(String email, String userName) {
		String link = "";
		try {

			link = FirebaseAuth.getInstance()
					.generateEmailVerificationLink(
							email);
			// Construct email verification template, embed the link and send
			// using custom SMTP server.
			//		  sendCustomEmail(email, userName, link);
			//		  sendCustomEmail(email, password);
			System.out.println("Este es el link: " + link);
		} catch (FirebaseAuthException e) {
			System.out.println("Error generating email link: " + e.getMessage());
		}



		Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "587");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true"); //TLS

		Session session = Session.getInstance(prop,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(rmEmail, token);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("refreshmeapp@gmail.com"));
			message.setRecipients(
					Message.RecipientType.TO,
					InternetAddress.parse(email)
					);
			message.setSubject("RefreshMe Authentication");
			message.setText("Hola " + userName + ",\r\n"
					+ "\r\n"
					+ "¡Bienvenido a RefreshMe!\r\n"
					+ "\r\n"
					+ "Para poder validar tu cuenta necesitamos que hagas click en el siguiente enlace:\r\n"
					+ link
					+ "\r\n"
					+ "\r\n"
					+ "Si no has solicitado crearte una cuenta, puedes ignorar este mensaje.\r\n"
					+ "\r\n"
					+ "Gracias,\r\n"
					+ "\r\n"
					+ "Tu equipo de RefreshMe");

			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	private static void logIn(String email, String password) throws FirebaseAuthException {
		//Check if user already signed from previous session		
		defaultAuth = FirebaseAuth.getInstance();
		
		UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmail(email);
		// See the UserRecord reference doc for the contents of userRecord.
		System.out.println("Successfully fetched user data: " + userRecord.getEmail());

	}
	private static FirebaseAuth firebaseAuth;
	
	private static void logOut(String email, String password) throws FirebaseAuthException {
		//FirebaseAuth.getInstance().signOut();

	}
	protected static List <WebPage> UserWebPages = new ArrayList<WebPage>();
	public List<WebPage> getWebPageList(){
		return UserWebPages;
	}
	
	protected static void getClientPages() throws InterruptedException, ExecutionException {
		
		CollectionReference WebPages = FirestoreClient.getFirestore().collection("users/1/web-pages/") ;
		System.out.println("Hay paginas:");
		ApiFuture<QuerySnapshot> querysnapshot =WebPages.get();	
		
		for (DocumentSnapshot web : querysnapshot.get().getDocuments()) {
			WebPage webpage = new WebPage(
					web.getId(),
					web.getString("categoria"),
					web.getString("nombre"),
					web.getString("ultimosCambios"),
					web.getString("url")
				);
			System.out.println("id: "+ web.getId());
			System.out.println("categoria: "+ web.getString("categoria"));
			System.out.println("name: "+ web.getString("nombre"));
			System.out.println("ultimosCambios: "+ web.getString("ultimosCambios"));
			System.out.println("url: "+web.getString("url"));
			UserWebPages.add(webpage);
			System.out.println("---------------------");
		}
	
	}
	protected static void  deleteClientPages(int index) throws InterruptedException, ExecutionException{
		
		ApiFuture<WriteResult> writeResult = FirestoreClient.getFirestore().collection("users/1/web-pages").document(UserWebPages.get(index).getId()).delete();

		System.out.println("Update time : " + writeResult.get().getUpdateTime());
	}

}
