/*
 Copyright (c) 2010, Sungjin Han <meinside@gmail.com>
 All rights reserved.
 
 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:

  * Redistributions of source code must retain the above copyright notice,
    this list of conditions and the following disclaimer.
  * Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions and the following disclaimer in the
    documentation and/or other materials provided with the distribution.
  * Neither the name of meinside nor the names of its contributors may be
    used to endorse or promote products derived from this software without
    specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 POSSIBILITY OF SUCH DAMAGE.
 */

package kr.pe.meinside.android.ui;

import kr.pe.meinside.android.helper.LogHelper;
import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * 
 * @author meinside@gmail.com
 * @since 10.03.17.
 * 
 * last update 10.03.17.
 *
 */
public abstract class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Camera.PictureCallback, Camera.ShutterCallback
{
	public static final int IMAGE_MAX_WIDTH = 640;
	public static final int IMAGE_MAX_HEIGHT = 480;

	private static SurfaceHolder holder = null;
	private static Camera camera = null;

	/**
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public CameraSurfaceView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initialize(context);
	}

	/**
	 * 
	 * @param context
	 * @param attrs
	 */
	public CameraSurfaceView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initialize(context);
	}

	/**
	 * 
	 * @param context
	 */
	public CameraSurfaceView(Context context)
	{
		super(context);
		initialize(context);
	}

	/**
	 * 
	 * @param context
	 */
	private void initialize(Context context)
	{
		Log.v(LogHelper.where(), "initialize");
		
		holder = getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{
		Log.v(LogHelper.where(), "surfaceChanged");
		
		Camera.Parameters params = camera.getParameters();
		params.setPreviewSize(width, height);
		camera.setParameters(params);
		camera.startPreview();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		Log.v(LogHelper.where(), "surfaceCreated");

		try
		{
			camera = Camera.open();
			camera.setPreviewDisplay(holder);
		}
		catch(Exception e)
		{
			Log.e(LogHelper.where(), e.toString());
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		Log.v(LogHelper.where(), "surfaceDestroyed");
		
		if(camera != null)
		{
			camera.stopPreview();
			camera.release();
			camera = null;
		}
	}

	/**
	 * 
	 * @return
	 */
	public boolean capture()
	{
		Log.v(LogHelper.where(), "shutter clicked");

		if(camera != null)
		{
			camera.takePicture(this, this, this);
			return true;
		}
		return false;
	}

	/**
	 * implement this to do something with picture
	 */
	@Override
	abstract public void onPictureTaken(byte[] data, Camera camera);
}
