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

package org.andlib.ui;

import org.andlib.helpers.Logger;
import org.andlib.helpers.MediaRecorderBase;
import org.andlib.helpers.image.ImageUtility;
import org.andlib.helpers.video.VideoRecorder;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.media.MediaRecorder;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * surface view for capturing videos through camera
 * 
 * @author meinside@gmail.com
 * @since 10.11.04.
 * 
 * last update 11.02.08.
 *
 */
public class CamcorderSurfaceView extends SurfaceView implements SurfaceHolder.Callback
{
	private static SurfaceHolder holder = null;
	private static Camera camera = null;
	private VideoRecorder recorder = null;

	/**
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public CamcorderSurfaceView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initialize(context);
	}

	/**
	 * 
	 * @param context
	 * @param attrs
	 */
	public CamcorderSurfaceView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initialize(context);
	}

	/**
	 * 
	 * @param context
	 */
	public CamcorderSurfaceView(Context context)
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
		if(isInEditMode())
			return;

		Logger.v("initialize");
		
		holder = getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		recorder = new VideoRecorder(getPreferredOutputFormat(), getPreferredAudioSource(), getPreferredAudioEncoder(), MediaRecorderBase.DEFAULT_SAMPLING_RATE, MediaRecorderBase.DEFAULT_ENCODING_BITRATE, MediaRecorderBase.DEFAULT_NUM_CHANNELS, getPreferredVideoSource(), getPreferredVideoEncoder(), MediaRecorderBase.DEFAULT_FRAME_RATE, holder.getSurface(), getPrefferedMaxDurationMillis(), getPreferredMaxFileSize());
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{
		Logger.v("surfaceChanged");
		
		Camera.Parameters params = camera.getParameters();
		Size optimalSize = ImageUtility.getOptimalPreviewSize(params.getSupportedPreviewSizes(), width, height);
		params.setPreviewSize(optimalSize.width, optimalSize.height);
		camera.setParameters(params);
		camera.startPreview();

		recorder.setVideoSize(optimalSize.width, optimalSize.height);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		Logger.v("surfaceCreated");

		try
		{
			camera = Camera.open();
			camera.setPreviewDisplay(holder);
		}
		catch(Exception e)
		{
			Logger.e(e.toString());
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		Logger.v("surfaceDestroyed");
		
		if(camera != null)
		{
			camera.stopPreview();
			camera.release();
			camera = null;
		}
	}

	/**
	 * 
	 * @param handler
	 * @param listener
	 */
	public void setListener(Handler handler, MediaRecorderBase.MediaRecorderListener listener)
	{
		recorder.setListener(handler, listener);
	}

	/**
	 * 
	 * @param filepath
	 */
	public void record(String filepath)
	{
		camera.unlock();
		recorder.startRecording(filepath, camera);
	}

	/**
	 * 
	 */
	public void stop()
	{
		recorder.stopRecording();
	}

	/**
	 * override this function to change output format
	 * 
	 * @return preferred output format (default: DEFAULT)
	 */
	protected int getPreferredOutputFormat()
	{
		return MediaRecorder.OutputFormat.DEFAULT;
	}

	/**
	 * override this function to change audio source
	 * 
	 * @return preferred audio source (default: DEFAULT)
	 */
	protected int getPreferredAudioSource()
	{
		return MediaRecorder.AudioSource.DEFAULT;
	}

	/**
	 * override this function to change audio encoder
	 * 
	 * @return preferred audio encoder (default: DEFAULT)
	 */
	protected int getPreferredAudioEncoder()
	{
		return MediaRecorder.AudioEncoder.DEFAULT;
	}

	/**
	 * override this function to change video source
	 * 
	 * @return preferred video source (default: DEFAULT)
	 */
	protected int getPreferredVideoSource()
	{
		return MediaRecorder.VideoSource.DEFAULT;
	}

	/**
	 * override this function to change video encoder
	 * 
	 * @return preferred video encoder (default: DEFAULT)
	 */
	protected int getPreferredVideoEncoder()
	{
		return MediaRecorder.VideoEncoder.DEFAULT;
	}

	/**
	 * override this function to change max duration of video
	 * 
	 * @return preferred max duration of video in millis (default: infinite)
	 */
	protected int getPrefferedMaxDurationMillis()
	{
		return MediaRecorderBase.DEFAULT_MAX_DURATION;
	}

	/**
	 * override this function to change max file size of video
	 * 
	 * @return preferred max file size of video (default: infinite)
	 */
	protected long getPreferredMaxFileSize()
	{
		return MediaRecorderBase.DEFAULT_MAX_FILESIZE;
	}
}
