package org.andlib.helpers;

import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * 
 * @author meinside@gmail.com
 * @since 10.02.25.
 * 
 * last update 10.11.03.
 *
 */
final public class ResourceHelper
{
	/**
	 * get resource id from given name and type
	 * 
	 * @param context
	 * @param name resource's name (without extension)
	 * @param type type of resource (ex: "drawable", "raw", ...)
	 * @return 0 if fails
	 */
	public static int getResourceId(Context context, String name, String type)
	{
		return context.getResources().getIdentifier(name, type, context.getPackageName());
	}
	
	/**
	 * 
	 * @param context
	 * @param resourceId
	 * @return null if fails
	 */
	public static InputStream getResourceAsInputStream(Context context, int resourceId)
	{
		try
		{
			return context.getResources().openRawResource(resourceId);
		}
		catch(Exception e)
		{
			Logger.e(e.toString());
		}
		return null;
	}
	
	/**
	 * 
	 * @param context
	 * @param name of resource (without extension)
	 * @param type of resource (ex: "drawable", "raw", ...)
	 * @return null if fails
	 */
	public static InputStream getResourceAsInputStream(Context context, String name, String type)
	{
		try
		{
			return context.getResources().openRawResource(getResourceId(context, name, type));
		}
		catch(Exception e)
		{
			Logger.e(e.toString());
		}
		return null;
	}
	
	/**
	 * 
	 * @param context
	 * @param resourceId
	 * @return null if fails
	 */
	public static AssetFileDescriptor getResourceAsFd(Context context, int resourceId)
	{
		try
		{
			return context.getResources().openRawResourceFd(resourceId);
		}
		catch(Exception e)
		{
			Logger.e(e.toString());
		}
		return null;
	}
	
	/**
	 * 
	 * @param context
	 * @param name of resource (without extension)
	 * @param type of resource (ex: "drawable", "raw", ...)
	 * @return null if fails
	 */
	public static AssetFileDescriptor getResourceAsFd(Context context, String name, String type)
	{
		try
		{
			return context.getResources().openRawResourceFd(getResourceId(context, name, type));
		}
		catch(Exception e)
		{
			Logger.e(e.toString());
		}
		return null;
	}

	/**
	 * get filepath of given content uri
	 * 
	 * @param activity
	 * @param contentUri   
	 * @return null if fails
	 */
	public static String getFilepathOfUri(Activity activity, Uri contentUri)
	{
		Cursor cursor = activity.managedQuery(contentUri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
		try
		{
			int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(index);
		}
		catch(Exception e)
		{
			Logger.e(e.toString());
		}
		return null;
	}
}
