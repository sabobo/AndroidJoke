package com.joke;

import java.io.File;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import com.joke.info.GlobalInfo;
import com.joke.util.FrameConfigure;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
/**
 * 数据共享中心
 * @author liubo
 *
 */
public class Appcontents extends Application {
	/***/
	public static Appcontents appcontents = null;
	 /** 上下文 */
    private static Context context;
    /**会员id*/
    public static String user_id = "";
	/**服务器app下载地址 http://cyd.tmallshop.cn/fore.micro.apk*/
	public static String downloadapkurl= "";
	
	public Appcontents() {

	};

	public synchronized Appcontents getIntstances() {
		if (appcontents == null) {
			appcontents = (Appcontents) getIntstances().getApplicationContext();
		}
		return appcontents;
	}

	 	@Override
    public void onCreate() {
	        // TODO Auto-generated method stub
	        super.onCreate();
	        context = this;
	        initImageLoader(getApplicationContext());
	        initGlobal();
	 }
	 /**
	  * 异步下载初始化 initImageLoader:(这里用一句话描述这个方法的作用). <br/>
	  * @author chenhao
	  * @param context
	  * @since JDK 1.6
	  */
	    public static void initImageLoader(Context context) {
	        File cacheDir = new File(FrameConfigure.NORMAL_IMG_DRC);
	        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).memoryCacheExtraOptions(480, 800).threadPoolSize(3) // default
            .threadPriority(Thread.NORM_PRIORITY - 1) // default
            .tasksProcessingOrder(QueueProcessingType.FIFO) // default
            .denyCacheImageMultipleSizesInMemory().memoryCache(new LruMemoryCache(2 * 1024 * 1024)) // 内存缓存
            .memoryCacheSize(2 * 1024 * 1024).memoryCacheSizePercentage(13) // default
            .diskCache(new UnlimitedDiscCache(cacheDir)) // default
            .diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(100).diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
            .imageDownloader(new BaseImageDownloader(context)) // default
            .imageDecoder(new BaseImageDecoder(true)) // default
            .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
            .build();
	        // .writeDebugLogs() 打印日志
	        ImageLoader.getInstance().init(config);
	    }
	    /**
		 * 初始化全局变量 实际工作中这个方法中serverVersion从服务器端获取，最好在启动画面的activity中执行
		 */
		public void initGlobal() {
			try {
				// 获取本地版本号
				GlobalInfo.localVersion = getPackageManager().getPackageInfo(
						getPackageName(), 0).versionCode;
				// 假设服务端版本号为2，这个应该是要获取服务器端的版本号的，这里只是假设服务端版本号2
				//GlobalInfo.serverVersion = 2;
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		}
}
