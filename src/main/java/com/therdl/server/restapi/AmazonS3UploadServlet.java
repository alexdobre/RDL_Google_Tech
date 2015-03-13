package com.therdl.server.restapi;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.google.inject.Singleton;
import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;
import com.therdl.server.api.UserService;
import com.therdl.server.apiimpl.CredentialsService;
import com.therdl.server.data.AwsS3Credentials;
import com.therdl.server.validator.impl.AvatarFileValidatorImpl;
import com.therdl.shared.beans.Beanery;
import com.therdl.shared.exceptions.AvatarInvalidException;

/**
 * Uploads an image to the amazon S3 service
 */
@Singleton
public class AmazonS3UploadServlet extends HttpServlet {
	final Logger log = LoggerFactory.getLogger(AmazonS3UploadServlet.class);
	/**
	 * Upload servlet to AWS S3 bucket
	 */
	private static final long serialVersionUID = -7720246048637220075L;
	private static final int THRESHOLD_SIZE = 1024 * 10;  // 10kB
	private static final int MAX_FILE_SIZE = 1024 * 10; // 10kB
	private static final int MAX_REQUEST_SIZE = 1024 * 50; // 50kb
	private static String AMAZON_ACCESS_KEY;
	private static String AMAZON_SECRET_KEY;
	private static final String S3_BUCKET_NAME = "RDL_Avatars";

	private final Provider<HttpSession> session;
	private CredentialsService credentialsService;
	private Beanery beanery;
	private String avatarText;
	private UserService userService;


	@Inject
	public AmazonS3UploadServlet(Provider<HttpSession> session, UserService userService, CredentialsService credentialsService) {
		super();
		this.session = session;
		this.userService = userService;
		this.credentialsService = credentialsService;
		beanery = AutoBeanFactorySource.create(Beanery.class);
		AwsS3Credentials cred = credentialsService.getAwsS3Credentials();
		AMAZON_ACCESS_KEY = cred.getAccesKey();
		AMAZON_SECRET_KEY = cred.getSecretKey();
	}

	/**
	 * handles file upload via HTTP POST method.
	 */
	protected void doPost(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException, IOException {
		String userName = (String)session.get().getAttribute("username");
		if (userName == null || userName.equals("")) return;
		log.info("Amazon S3 servlet doPost BEGIN for username " + userName);

		ServletFileUpload upload = setupFileUpload(request, response);
		if (upload == null)
			return;

		String uuidValue = userName;

		try {
			FileItem itemFile = getFileItem(request, upload);
			log.info("Retrieved item file of size: " + itemFile.getSize() + " and type: " + itemFile.getContentType());
			if (itemFile != null) {
				//validate file item
				if (!AvatarFileValidatorImpl.isImageValid(itemFile)) {
					throw new AvatarInvalidException();
				}
				itemFile.setFieldName(userName);

				uploadFile(uuidValue, itemFile);

			} else {
				log.info(uuidValue + ":error:" + "No Upload file");
			}

		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw new ServletException();
		}
		log.info(uuidValue + ":Upload done");
	}

	private void uploadFile(String uuidValue, FileItem itemFile) throws IOException {
		log.info("S3 servlet uploadFile BEGIN");
		// get item input stream to upload file into s3 aws
		BasicAWSCredentials awsCredentials = new BasicAWSCredentials(AMAZON_ACCESS_KEY, AMAZON_SECRET_KEY);

		AmazonS3 s3client = new AmazonS3Client(awsCredentials);
		try {

			ObjectMetadata om = new ObjectMetadata();
			om.setContentLength(itemFile.getSize());
			String keyName = uuidValue;

			s3client.putObject(new PutObjectRequest(S3_BUCKET_NAME, keyName, itemFile.getInputStream(), om));
			s3client.setObjectAcl(S3_BUCKET_NAME, keyName, CannedAccessControlList.PublicRead);

		} catch (AmazonServiceException ase) {
			log.error(ase.getMessage(), ase);

		} catch (AmazonClientException ace) {
			log.error(ace.getMessage(), ace);
		}

		log.info("S3 servlet uploadFile END");
	}

	private ServletFileUpload setupFileUpload(HttpServletRequest request, HttpServletResponse response) throws IOException {
		log.info("Setup file upload BEGIN");
		// needed for cross-domain communication
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		response.setHeader("Access-Control-Max-Age", "86400");

		// checks if the request actually contains upload file
		if (!ServletFileUpload.isMultipartContent(request)) {
			PrintWriter writer = response.getWriter();
			writer.println("Request does not contain upload data");
			writer.flush();
			return null;
		}
		// configures upload settings
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(THRESHOLD_SIZE);

		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setFileSizeMax(MAX_FILE_SIZE);
		upload.setSizeMax(MAX_REQUEST_SIZE);
		log.info("Setup file upload END");
		return upload;
	}

	private FileItem getFileItem(HttpServletRequest request, ServletFileUpload upload) throws FileUploadException {
		log.info("Get file item BEGIN");
		// parses the request's content to extract file data
		FileItem result = null;
		List formItems = upload.parseRequest(request);
		Iterator iter = formItems.iterator();

		// iterates over form's fields to get UUID Value
		while (iter.hasNext()) {
			FileItem item = (FileItem)iter.next();
			// processes only fields that are not form fields
			if (item.getFieldName().equals("fileElement")) {
				result = item;
				break;
			}
		}
		return result;
	}
}
