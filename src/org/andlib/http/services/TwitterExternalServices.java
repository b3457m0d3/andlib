package org.andlib.http.services;

import java.io.File;
import java.util.HashMap;

import org.andlib.helpers.Logger;
import org.andlib.http.ApacheHttpUtility;

import android.os.Handler;

/**
 * 
 * @author meinside@gmail.com
 * @since 10.01.14.
 * 
 * last update 10.11.08.
 *
 */
public class TwitterExternalServices extends TwitterServices
{
	public static final String TWITTER_ECHO_VERIFY_CREDENTIALS_URL = "https://api.twitter.com/1/account/verify_credentials.json";

	public static final String YFROG_UPLOAD_URL = "https://yfrog.com/api/xauth_upload";
	public static final String TWITPIC_UPLOAD_URL = "http://api.twitpic.com/2/upload.json";
	public static final String IMGLY_UPLOAD_URL = "http://img.ly/api/2/upload.json";
	public static final String TWITVID_UPLOAD_URL = "http://im.twitvid.com/api/upload";

	/**
	 * @param consumerKey
	 * @param consumerSecret
	 * @param accessToken
	 * @param accessTokenSecret
	 * @param screenName
	 */
	public TwitterExternalServices(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret, String screenName)
	{
		super(consumerKey, consumerSecret, accessToken, accessTokenSecret, screenName);
	}


	/* ---------------------------------------------------------------- */
	/* YFrog service */

	/**
	 * upload a image to yfrog service
	 * 
	 * - http://code.google.com/p/imageshackapi/wiki/TwitterAuthentication
	 * - http://code.google.com/p/imageshackapi/wiki/YFROGupload
	 * 
	 * @param resultHandler
	 * @param devKey
	 * @param media
	 * @return id of AsyncHttpTask that is assigned to this POST job
	 */
	public String asyncUploadMediaToYfrog(Handler resultHandler, String devKey, File media)
	{
		if(!isAuthorized)
		{
			Logger.i("not authorized yet");
			return null;
		}

		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("X-Auth-Service-Provider", TWITTER_ECHO_VERIFY_CREDENTIALS_URL);
		headers.put("X-Verify-Credentials-Authorization", generateTwitterOAuthEchoCredentials());
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("key", devKey);
		params.put("media", media);
		
		return ApacheHttpUtility.getInstance().postAsync(resultHandler, YFROG_UPLOAD_URL, headers, params);
	}

	
	/* ---------------------------------------------------------------- */
	/* TwitPic service */

	/**
	 * upload an image file to twitpic service
	 * 
	 * http://dev.twitpic.com/docs/2/upload/
	 * 
	 * @param resultHandler
	 * @param devKey
	 * @param message
	 * @param media
	 * @return id of AsyncHttpTask that is assigned to this POST job
	 */
	public String asyncUploadMediaToTwitpic(Handler resultHandler, String devKey, String message, File media)
	{
		if(!isAuthorized)
		{
			Logger.i("not authorized yet");
			return null;
		}

		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("X-Auth-Service-Provider", TWITTER_ECHO_VERIFY_CREDENTIALS_URL);
		headers.put("X-Verify-Credentials-Authorization", generateTwitterOAuthEchoCredentials());
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("key", devKey);
		params.put("message", message);
		params.put("media", media);
		
		return ApacheHttpUtility.getInstance().postAsync(resultHandler, TWITPIC_UPLOAD_URL, headers, params);
	}

	
	/* ---------------------------------------------------------------- */
	/* img.ly service */

	/**
	 * upload an image file to img.ly service
	 * 
	 * http://img.ly/api/docs
	 * 
	 * @param resultHandler
	 * @param message
	 * @param media
	 * @return id of AsyncHttpTask that is assigned to this POST job
	 */
	public String asyncUploadMediaToImgly(Handler resultHandler, String message, File media)
	{
		if(!isAuthorized)
		{
			Logger.i("not authorized yet");
			return null;
		}

		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("X-Auth-Service-Provider", TWITTER_ECHO_VERIFY_CREDENTIALS_URL);
		headers.put("X-Verify-Credentials-Authorization", generateTwitterOAuthEchoCredentials());
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("message", message);
		params.put("media", media);
		
		return ApacheHttpUtility.getInstance().postAsync(resultHandler, IMGLY_UPLOAD_URL, headers, params);
	}


	/* ---------------------------------------------------------------- */
	/* TwitVid service */

	/**
	 * upload a video file to twitvid service
	 * 
	 * http://twitvid.pbworks.com/Twitvid%C2%A0API%C2%A0Method%3A%C2%A0upload
	 * 
	 * @param resultHandler
	 * @param message
	 * @param title
	 * @param description
	 * @param video
	 * @return id of AsyncHttpTask that is assigned to this POST job
	 */
	public String asyncUploadVideoToTwitvid(Handler resultHandler, String message, String title, String description, File video)
	{
		if(!isAuthorized)
		{
			Logger.i("not authorized yet");
			return null;
		}

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("message", message);
		params.put("title", title);
		params.put("description", description);
		params.put("media", video);
		params.put("x_auth_service_provider", TWITTER_ECHO_VERIFY_CREDENTIALS_URL);
		params.put("x_verify_credentials_authorization", generateTwitterOAuthEchoCredentials());
		
		return ApacheHttpUtility.getInstance().postAsync(resultHandler, TWITVID_UPLOAD_URL, null, params);
	}

	/**
	 * generate credentials string for OAuth echo authorization needed by each service
	 * 
	 * @return generated credentials string
	 */
	private String generateTwitterOAuthEchoCredentials()
	{
		HashMap<String, String> requestTokenHash = new HashMap<String, String>();
		requestTokenHash.put("oauth_consumer_key", getConsumerKey());
		requestTokenHash.put("oauth_token", getAccessToken());
		requestTokenHash.put("oauth_signature_method", "HMAC-SHA1");
		requestTokenHash.put("oauth_timestamp", getTimestamp());
		requestTokenHash.put("oauth_nonce", getNonce());
		requestTokenHash.put("oauth_version", "1.0");
		requestTokenHash.put("oauth_signature", generateAccessSignature(generateSignatureBaseString("GET", TWITTER_ECHO_VERIFY_CREDENTIALS_URL, requestTokenHash, null)));
		
		return generateAuthHeader(requestTokenHash);
	}
}
