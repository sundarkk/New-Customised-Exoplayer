# New-Customised-Exoplayer
Add Dependencies in rul HLS(m3u8).
 implementation 'com.google.android.exoplayer:exoplayer:2.10.4'
    implementation 'com.google.android.exoplayer:extension-okhttp:2.10.4'
    implementation 'com.google.android.exoplayer:extension-rtmp:2.10.4'
# Its project to add specific time in video pause and play.
# In project any life cycle in video play & pause
#Hls control code in below
  DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(
                this, "ExoPlayer"));
        HlsMediaSource hlsMediaSource1 = new HlsMediaSource.Factory(dataSourceFactory)
                .setAllowChunklessPreparation(true)
                .createMediaSource(videoUrl);
                
   # Add internet permission: <uses-permission android:name="android.permission.INTERNET" />             
